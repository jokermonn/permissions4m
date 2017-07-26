package com.joker.processor;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by joker on 2017/7/26.
 */

class ProxyInfo {
    private static final String SUFFIX = "PermissionsProxy";
    private final String packageName;
    private final String className;
    private final TypeElement element;
    private final String proxyName;
    // code -> method name
    Map<Integer, String> grantedMap = new HashMap<>();
    Map<Integer, String> deniedMap = new HashMap<>();
    Map<Integer, String> rationaleMap = new HashMap<>();

    ProxyInfo(Elements util, TypeElement element) {
        this.element = element;
        packageName = util.getPackageOf(element).getQualifiedName().toString();
        className = getClassName(element, packageName);
        proxyName = className + "$$" + SUFFIX;
    }

    String getProxyName() {
        return proxyName;
    }

    String getClassName(TypeElement element, String packageName) {
        int packageLen = packageName.length() + 1;
        return element.getQualifiedName().toString().substring(packageLen)
                .replace('.', '$');
    }

    TypeElement getElement() {
        return element;
    }

    String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(packageName).append(";\n\n")
                .append("import com.joker.api.*;\n\n")
                .append("public class ").append(proxyName).append(" implements ").append(SUFFIX).append
                ("<").append(element.getSimpleName()).append("> {\n");

        generateMethodCode(builder);

        builder.append("\n}\n");

        return builder.toString();
    }

    private void generateMethodCode(StringBuilder builder) {
        generateGrantedMethod(builder);
        generateDeniedMethod(builder);
        generateRationaleMethod(builder);
    }

    private void generateRationaleMethod(StringBuilder builder) {
        checkBuilderNonNull(builder);
        builder.append("@Override\n").append("public void rationale(").append(element.getSimpleName())
                .append(" object, int code) {\n")
                .append("switch(code) {\n");
        for (Integer id : rationaleMap.keySet()) {
            builder.append("case ").append(id).append(": {")
                    .append("object.").append(rationaleMap.get(id)).append("();\nbreak;\n")
                    .append("}\n");
        }
        builder.append("default:{\nbreak;\n}").append("}\n}\n\n");
    }

    private void generateDeniedMethod(StringBuilder builder) {
        checkBuilderNonNull(builder);
        builder.append("@Override\n").append("public void denied(").append(element.getSimpleName())
                .append(" object, int code) {\n")
                .append("switch(code) {\n");
        for (Integer id : deniedMap.keySet()) {
            builder.append("case ").append(id).append(": {")
                    .append("object.").append(deniedMap.get(id)).append("();\nbreak;\n")
                    .append("}\n");
        }
        builder.append("default:{\nbreak;\n}").append("}\n}\n\n");
    }

    private void generateGrantedMethod(StringBuilder builder) {
        checkBuilderNonNull(builder);
        builder.append("@Override\n").append("public void granted(").append(element.getSimpleName())
                .append(" object, int code) {\n")
                .append("switch(code) {\n");
        for (Integer id : grantedMap.keySet()) {
            builder.append("            case ").append(id).append(": {")
                    .append("               object.").append(grantedMap.get(id)).append("();\nbreak;\n")
                    .append("}\n");
        }
        builder.append("default:{\nbreak;\n}").append("}\n}\n\n");
    }

    private void checkBuilderNonNull(StringBuilder builder) {
        if (builder == null) return;
    }
}
