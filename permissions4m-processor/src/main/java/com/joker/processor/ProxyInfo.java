package com.joker.processor;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by joker on 2017/7/26.
 */

class ProxyInfo {
    private static final String PERMISSIONS_PROXY = "PermissionsProxy";
    private static final String CONCAT = "$$";
    private final String packageName;
    private final String className;
    private final TypeElement element;
    private final String proxyName;
    // methodName -> requestCodes
    Map<String, int[]> grantedMap = new HashMap<>();
    Map<String, int[]> deniedMap = new HashMap<>();
    Map<String, int[]> rationaleMap = new HashMap<>();
    // requestCode -> methodName
    Map<Integer, String> singleGrantMap = new HashMap<>();
    Map<Integer, String> singleDeniedMap = new HashMap<>();
    Map<Integer, String> singleRationaleMap = new HashMap<>();

    ProxyInfo(Elements util, TypeElement element) {
        this.element = element;
        packageName = util.getPackageOf(element).getQualifiedName().toString();
        className = getClassName(element, packageName);
        proxyName = className + CONCAT + PERMISSIONS_PROXY;
    }

    String getProxyName() {
        return proxyName;
    }

    private String getClassName(TypeElement element, String packageName) {
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
                .append("public class ").append(proxyName).append(" implements ").append
                (PERMISSIONS_PROXY).append
                ("<").append(element.getSimpleName()).append("> {\n");

        generateMethodCode(builder);

        builder.append("}\n");

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
                .append("switch(code) {");
        for (String methodName : rationaleMap.keySet()) {
            int[] ints = rationaleMap.get(methodName);
            for (int requestCode : ints) {
                builder.append("case ").append(requestCode).append(":\n");
                if (singleRationaleMap.containsKey(requestCode)) {
                    singleRationaleMap.remove(requestCode);
                }
            }
            builder.append("{\n").append("object.").append(methodName).append("(code);\nbreak;}\n");
        }

        for (Integer requestCode : singleRationaleMap.keySet()) {
            builder.append("case ").append(requestCode).append(": {\n")
                    .append("object.").append(singleRationaleMap.get(requestCode)).append("();\nbreak;\n}");
        }


        builder.append("default:\nbreak;\n").append("}\n}\n\n");
    }

    private void generateDeniedMethod(StringBuilder builder) {
        checkBuilderNonNull(builder);
        builder.append("@Override\n").append("public void denied(").append(element.getSimpleName())
                .append(" object, int code) {\n")
                .append("switch(code) {");
        for (String methodName : deniedMap.keySet()) {
            int[] ints = deniedMap.get(methodName);
            for (int requestCode : ints) {
                builder.append("case ").append(requestCode).append(":\n");
                if (singleDeniedMap.containsKey(requestCode)) {
                    singleDeniedMap.remove(requestCode);
                }
            }
            builder.append("{\n").append("object.").append(methodName).append("(code);\nbreak;}\n");
        }

        for (Integer requestCode : singleDeniedMap.keySet()) {
            builder.append("case ").append(requestCode).append(": {\n")
                    .append("object.").append(singleDeniedMap.get(requestCode)).append("();\nbreak;\n}");
        }

        builder.append("default:\nbreak;\n").append("}\n}\n\n");
    }

    private void generateGrantedMethod(StringBuilder builder) {
        checkBuilderNonNull(builder);
        builder.append("@Override\n").append("public void granted(").append(element.getSimpleName())
                .append(" object, int code) {\n")
                .append("switch(code) {");
        for (String methodName : grantedMap.keySet()) {
            int[] ints = grantedMap.get(methodName);
            for (int requestCode : ints) {
                builder.append("case ").append(requestCode).append(":\n");
                if (singleGrantMap.containsKey(requestCode)) {
                    singleGrantMap.remove(requestCode);
                }
            }
            builder.append("{\n").append("object.").append(methodName).append("(code);\nbreak;}\n");
        }

        for (Integer requestCode : singleGrantMap.keySet()) {
            builder.append("case ").append(requestCode).append(": {\n")
                    .append("object.").append(singleGrantMap.get(requestCode)).append("();\nbreak;\n}");
        }

        builder.append("default:\nbreak;\n").append("}\n}\n\n");
    }

    private void checkBuilderNonNull(StringBuilder builder) {
        if (builder == null) return;
    }
}
