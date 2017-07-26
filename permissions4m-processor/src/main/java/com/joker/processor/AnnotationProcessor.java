package com.joker.processor;

import com.google.auto.service.AutoService;
import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsRationale;

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
import javax.lang.model.element.ElementKind;
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
        if (isIllegal(roundEnv, PermissionsGranted.class)) return false;
        if (isIllegal(roundEnv, PermissionsDenied.class)) return false;
        if (isIllegal(roundEnv, PermissionsRationale.class)) return false;

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
     * @return true if illegal
     */
    private boolean isIllegal(RoundEnvironment roundEnv, Class<? extends Annotation> clazz) {
        for (Element element : roundEnv.getElementsAnnotatedWith(clazz)) {
            if (isValid(element)) return true;
            ExecutableElement method = (ExecutableElement) element;
            TypeElement typeElement = (TypeElement) method.getEnclosingElement();
            String typeName = typeElement.getQualifiedName().toString();
            ProxyInfo info = map.get(typeName);
            if (info == null) {
                info = new ProxyInfo(mUtils, typeElement);
                map.put(typeName, info);
            }

            Annotation annotation = method.getAnnotation(clazz);
            String methodName = method.getSimpleName().toString();
            if (annotation instanceof PermissionsGranted) {
                int value = ((PermissionsGranted) annotation).value();
                info.grantedMap.put(value, methodName);
            } else if (annotation instanceof PermissionsDenied) {
                int value = ((PermissionsDenied) annotation).value();
                info.deniedMap.put(value, methodName);
            } else if (annotation instanceof PermissionsRationale) {
                int value = ((PermissionsRationale) annotation).value();
                info.rationaleMap.put(value, methodName);
            } else {
                error(method, "%s not support.", method);
                return true;
            }
        }

        return false;
    }

    /**
     * check the annotation is annotated with method
     *
     * @param element the annotation annotated
     * @return true if element not method
     */
    private boolean isValid(Element element) {
        if (element.getKind() != ElementKind.METHOD) {
            error(element, "%s must be annotated with method", element.getSimpleName());
            return true;
        }
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
        Set<String> set = new HashSet<>(3);
        set.add(PermissionsGranted.class.getCanonicalName());
        set.add(PermissionsDenied.class.getCanonicalName());
        set.add(PermissionsRationale.class.getCanonicalName());

        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
