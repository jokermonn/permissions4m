package com.joker.annotation;

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
    private final TypeElement element;
    private final String proxyName;
    // methodName -> requestCodes
    Map<String, int[]> grantedMap = new HashMap<>();
    Map<String, int[]> deniedMap = new HashMap<>();
    Map<String, int[]> rationaleMap = new HashMap<>();
    Map<String, int[]> customRationaleMap = new HashMap<>();
    // requestCode -> methodName
    Map<Integer, String> singleGrantMap = new HashMap<>();
    Map<Integer, String> singleDeniedMap = new HashMap<>();
    Map<Integer, String> singleRationaleMap = new HashMap<>();
    Map<Integer, String> singleCustomRationaleMap = new HashMap<>();
    // sync request
    Map<int[], String[]> syncPermissions = new HashMap<>(1);
    private int firstRequestCode;
    private String firstRequestPermission;

    ProxyInfo(Elements util, TypeElement element) {
        this.element = element;
        packageName = util.getPackageOf(element).getQualifiedName().toString();
        String className = getClassName(element, packageName);
        proxyName = className + CONCAT + PERMISSIONS_PROXY;
    }

    String getProxyName() {
        return proxyName;
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
        generateCustomRationaleMethod(builder);
        generateSyncRequestPermissionsMethod(builder);
    }

    private void generateSyncRequestPermissionsMethod(StringBuilder builder) {
        checkBuilderNonNull(builder);
        builder.append("@Override\n").append("public void startSyncRequestPermissionsMethod(").append
                (element.getSimpleName()).append(" object) {\n")
                .append("Permissions4M.requestPermission(object, \"").append(firstRequestPermission)
                .append("\", ").append(firstRequestCode).append(");\n")
//                .append("Permissions4M.requestPermission(object, \"").append(syncRequestPermissions[0])
//                .append("\", ").append(syncRequestCode[0]).append(");\n")
                .append("}");
    }

    private void generateCustomRationaleMethod(StringBuilder builder) {
        checkBuilderNonNull(builder);
        builder.append("@Override\n").append("public boolean customRationale(").append(element
                .getSimpleName())
                .append(" object, int code) {\n")
                .append("switch(code) {");
        for (String methodName : customRationaleMap.keySet()) {
            int[] ints = customRationaleMap.get(methodName);
            for (int requestCode : ints) {
                builder.append("case ").append(requestCode).append(":\n");
                if (singleCustomRationaleMap.containsKey(requestCode)) {
                    singleCustomRationaleMap.remove(requestCode);
                }
            }
            builder.append("{\n").append("object.").append(methodName).append("(code);\nreturn true;\n}\n");
        }

        for (Integer requestCode : singleCustomRationaleMap.keySet()) {
            builder.append("case ").append(requestCode).append(": {\n")
                    .append("object.").append(singleCustomRationaleMap.get(requestCode)).append("();" +
                    "\nreturn true;\n}");
        }

        builder.append("default:\nreturn false;\n").append("}\n}\n\n");
    }

    private void generateRationaleMethod(StringBuilder builder) {
        checkBuilderNonNull(builder);
        builder.append("@Override\n").append("public void rationale(").append(element.getSimpleName())
                .append(" object, int code) {\n")
                .append("switch(code) {");
        for (String methodName : rationaleMap.keySet()) {
            int[] ints = rationaleMap.get(methodName);
            for (int requestCode : ints) {
                builder.append("case ").append(requestCode).append(":\n{");
                builder.append("object.").append(methodName).append("(").append(requestCode).append(");\n");
                builder.append("break;}\n");
                if (singleRationaleMap.containsKey(requestCode)) {
                    singleRationaleMap.remove(requestCode);
                }
            }
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
                builder.append("case ").append(requestCode).append(":\n{");
                builder.append("object.").append(methodName).append("(").append(requestCode).append(");\n");
                // judge whether need write request permission method
                addSyncRequestPermissionMethod(builder, requestCode);
                builder.append("break;}\n");
                if (singleDeniedMap.containsKey(requestCode)) {
                    singleDeniedMap.remove(requestCode);
                }
            }
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
                builder.append("case ").append(requestCode).append(":\n{");
                builder.append("object.").append(methodName).append("(").append(requestCode).append(");\n");
                // judge whether need write request permission method
                addSyncRequestPermissionMethod(builder, requestCode);
                builder.append("break;}\n");
                if (singleGrantMap.containsKey(requestCode)) {
                    singleGrantMap.remove(requestCode);
                }
            }
        }

        for (Integer requestCode : singleGrantMap.keySet()) {
            builder.append("case ").append(requestCode).append(": {\n")
                    .append("object.").append(singleGrantMap.get(requestCode)).append("();\nbreak;\n}");
        }

        builder.append("default:\nbreak;\n").append("}\n}\n\n");
    }

    private void addSyncRequestPermissionMethod(StringBuilder builder, int targetRequestCode) {
        // syncPermissions size is 1
        for (int[] requestCodes : syncPermissions.keySet()) {
            int length = requestCodes.length;
            String[] permissions = syncPermissions.get(requestCodes);
            // when syncRequestPermission size is 1
            if (length == 1) {
                firstRequestCode = requestCodes[0];
                firstRequestPermission = permissions[0];
//                builder.append("Permissions4M.requestPermission(object,\"").append(firstRequestPermission).append("\",").append(firstRequestCode).append(");\n");
            } else {
                // when syncRequestPermission size bigger than 1
                for (int i = 0; i < length - 1; i++) {
                    if (i == 0) {
                        firstRequestCode = requestCodes[0];
                        firstRequestPermission = permissions[0];
                    }
                    if (requestCodes[i] == targetRequestCode) {
                        builder.append("Permissions4M.requestPermission(object,\"").append(permissions[i +
                                1]).append("\",").append(requestCodes[i + 1]).append(");\n");
                    }
                }
            }
        }
    }

    private void checkBuilderNonNull(StringBuilder builder) {
        if (builder == null) return;
    }

    private String getClassName(TypeElement element, String packageName) {
        int packageLen = packageName.length() + 1;
        return element.getQualifiedName().toString().substring(packageLen)
                .replace('.', '$');
    }
}
