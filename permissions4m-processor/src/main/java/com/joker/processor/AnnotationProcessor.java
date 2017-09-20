package com.joker.processor;

import com.google.auto.service.AutoService;
import com.joker.annotation.PermissionsCustomRationale;
import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsNonRationale;
import com.joker.annotation.PermissionsRationale;
import com.joker.annotation.PermissionsRequestSync;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {
    private Map<String, ProxyInfo> map = new HashMap<>();
    private Elements mUtils;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        map.clear();
        if (!isAnnotatedWithClass(roundEnv, PermissionsRequestSync.class)) return false;
        if (!isAnnotatedWithMethod(roundEnv, PermissionsNonRationale.class)) return false;
        if (!isAnnotatedWithMethod(roundEnv, PermissionsGranted.class)) return false;
        if (!isAnnotatedWithMethod(roundEnv, PermissionsDenied.class)) return false;
        if (!isAnnotatedWithMethod(roundEnv, PermissionsRationale.class)) return false;
        if (!isAnnotatedWithMethod(roundEnv, PermissionsCustomRationale.class)) return false;

        Writer writer = null;
        for (ProxyInfo info : map.values()) {
            try {
                JavaFileObject file = mFiler.createSourceFile(info.getProxyName(), info.getElement());
                writer = file.openWriter();
                writer.write(info.generateJavaCode());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return true;
    }

    /**
     * check the annotation is illegal or not
     *
     * @param roundEnv
     * @param clazz    the annotation type
     * @return false if illegal
     */
    private boolean isAnnotatedWithClass(RoundEnvironment roundEnv, Class<? extends Annotation> clazz) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(clazz);
        for (Element element : elements) {
            if (isValid(element)) {
                return false;
            }
            TypeElement typeElement = (TypeElement) element;
            String typeName = typeElement.getQualifiedName().toString();
            ProxyInfo info = map.get(typeName);
            if (info == null) {
                info = new ProxyInfo(mUtils, typeElement);
                map.put(typeName, info);
            }

            Annotation annotation = element.getAnnotation(clazz);
            if (annotation instanceof PermissionsRequestSync) {
                String[] permissions = ((PermissionsRequestSync) annotation).permission();
                int[] value = ((PermissionsRequestSync) annotation).value();
                if (permissions.length != value.length) {
                    error(element, "permissions's length not equals value's length");
                    return false;
                }
                info.syncPermissions.put(value, permissions);
            } else {
                error(element, "%s not support.", element);
                return false;
            }
        }

        return true;
    }

    /**
     * check the annotation is illegal or not
     *
     * @param roundEnv
     * @param clazz    the annotation type
     * @return false if illegal
     */
    private boolean isAnnotatedWithMethod(RoundEnvironment roundEnv, Class<? extends Annotation> clazz) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(clazz);
        for (Element element : elements) {
            if (isValid(element)) {
                return false;
            }
            ExecutableElement method = (ExecutableElement) element;
            TypeElement typeElement = (TypeElement) method.getEnclosingElement();
            String typeName = typeElement.getQualifiedName().toString();
            ProxyInfo info = map.get(typeName);
            if (info == null) {
                info = new ProxyInfo(mUtils, typeElement);
                map.put(typeName, info);
            }

            int size = method.getParameters().size();
            Annotation annotation = method.getAnnotation(clazz);
            String methodName = method.getSimpleName().toString();
            if (annotation instanceof PermissionsGranted) {
                int[] value = ((PermissionsGranted) annotation).value();
                if (value.length > 1 || size == 1) {
                    info.grantedMap.put(methodName, value);
                } else {
                    info.singleGrantMap.put(value[0], methodName);
                }
            } else if (annotation instanceof PermissionsDenied) {
                int[] value = ((PermissionsDenied) annotation).value();
                if (value.length > 1 || size == 1) {
                    info.deniedMap.put(methodName, value);
                } else {
                    info.singleDeniedMap.put(value[0], methodName);
                }
            } else if (annotation instanceof PermissionsRationale) {
                int[] value = ((PermissionsRationale) annotation).value();
                if (value.length > 1 || size == 1) {
                    info.rationaleMap.put(methodName, value);
                } else {
                    info.singleRationaleMap.put(value[0], methodName);
                }
            } else if (annotation instanceof PermissionsCustomRationale) {
                int[] value = ((PermissionsCustomRationale) annotation).value();
                if (value.length > 1 || size == 1) {
                    info.customRationaleMap.put(methodName, value);
                } else {
                    info.singleCustomRationaleMap.put(value[0], methodName);
                }
            } else if (annotation instanceof PermissionsNonRationale) {
                int[] value = ((PermissionsNonRationale) annotation).value();
                if (value.length > 1 || size == 2) {
                    info.nonRationaleMap.put(methodName, value);
                } else {
                    info.singleNonRationaleMap.put(value[0], methodName);
                }
            } else {
                error(method, "%s not support.", method);
                return false;
            }
        }

        return true;
    }

    /**
     * check the annotation is public or not
     *
     * @param element the annotation annotated
     * @return true if not public
     */
    private boolean isValid(Element element) {
        if (element.getModifiers().contains(Modifier.ABSTRACT) || element.getModifiers().contains
                (Modifier.PRIVATE)) {
            error(element, "%s must could not be abstract or private");
            return true;
        }

        return false;
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>(16);
        set.add(PermissionsGranted.class.getCanonicalName());
        set.add(PermissionsDenied.class.getCanonicalName());
        set.add(PermissionsRationale.class.getCanonicalName());
        set.add(PermissionsCustomRationale.class.getCanonicalName());
        set.add(PermissionsRequestSync.class.getCanonicalName());
        set.add(PermissionsNonRationale.class.getName());

        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
