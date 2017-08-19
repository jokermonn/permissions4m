package com.joker.processor;

import java.util.Arrays;
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
    // non rationale request
    Map<String, NonRationalePermissionBean> nonRationaleMap = new HashMap<>();
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
                .append("import com.joker.api.*;\n")
                .append("import com.joker.api.support.*;\n")
                .append("import android.content.Intent;\n\n")
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
        generatePageIntent(builder);
        generateSyncRequestPermissionsMethod(builder);
    }

    private void generateSyncRequestPermissionsMethod(StringBuilder builder) {
        if (checkBuilderNonNull(builder)) return;

        builder.append("@Override\n").append("public void startSyncRequestPermissionsMethod(").append
                (element.getSimpleName()).append(" object) {\n")
                .append("Permissions4M.requestPermission(object, \"").append(firstRequestPermission)
                .append("\", ").append(firstRequestCode).append(");\n")
                .append("}");
    }

    private void generateCustomRationaleMethod(StringBuilder builder) {
        if (checkBuilderNonNull(builder)) return;

        builder.append("@Override\n").append("public boolean customRationale(").append(element
                .getSimpleName())
                .append(" object, int code) {\n")
                .append("switch(code) {");
        for (Map.Entry<String, int[]> entry : customRationaleMap.entrySet()) {
            String methodName = entry.getKey();
            int[] ints = entry.getValue();
            for (int requestCode : ints) {
                builder.append("case ").append(requestCode).append(":\n");
                if (singleCustomRationaleMap.containsKey(requestCode)) {
                    singleCustomRationaleMap.remove(requestCode);
                }
            }
            builder.append("{\n").append("object.").append(methodName).append("(code);\nreturn true;\n}\n");
        }

        for (Map.Entry<Integer, String> entry : singleCustomRationaleMap.entrySet()) {
            int requestCode = entry.getKey();
            builder.append("case ").append(requestCode).append(": {\n")
                    .append("object.").append(entry.getValue()).append("();" +
                    "\nreturn true;\n}");
        }

        builder.append("default:\nreturn false;\n").append("}\n}\n\n");
    }

    private void generatePageIntent(StringBuilder builder) {
        if (checkBuilderNonNull(builder)) return;

        generatePageType2Intent(builder);
        builder.append("@Override\n").append("public void intent(").append(element.getSimpleName())
                .append(" object, int code) {\n")
                .append("switch(code) {");

        for (Map.Entry<String, NonRationalePermissionBean> entry : nonRationaleMap.entrySet()) {
            String methodName = entry.getKey();
            NonRationalePermissionBean bean = entry.getValue();
            int[] values = bean.getValue();
            int[] pageType = bean.getPageType();
            for (int i = 0; i < values.length; i++) {
                builder.append("case ").append(values[i]).append(":\n{")
                        .append("Intent intent = pageType2Intent(object, ").append(pageType[i]).append(");")
                        .append("object.").append(methodName).append("(").append(values[i]).append(", " +
                        "intent);break;}\n");
            }
        }

        builder.append("default:\nbreak;\n").append("}\n}\n\n");
    }

    private void generatePageType2Intent(StringBuilder builder) {
        builder.append("public Intent pageType2Intent(Object object, int pageType) {\n")
                .append("Intent intent;")
                .append("switch(pageType) {\n")
                .append("case 1: {android.app.Activity activity; if (object instanceof android.app" +
                        ".Fragment) {activity = ((android.app.Fragment)object).getActivity();}else if" +
                        "(object instanceof android" +
                        ".support.v4.app.Fragment){activity = ((android.support.v4.app.Fragment)object)" +
                        ".getActivity();}else{activity = " +
                        "(android.app.Activity)object;} return PermissionsPageManager.getIntent" +
                        "(activity);}")
                .append("default: return PermissionsPageManager.getIntent();}}");
    }

    private void generateRationaleMethod(StringBuilder builder) {
        if (checkBuilderNonNull(builder)) return;

        builder.append("@Override\n").append("public void rationale(").append(element.getSimpleName())
                .append(" object, int code) {\n")
                .append("switch(code) {");
        for (Map.Entry<String, int[]> entry : rationaleMap.entrySet()) {
            String methodName = entry.getKey();
            int[] ints = entry.getValue();
            for (int requestCode : ints) {
                builder.append("case ").append(requestCode).append(":\n{");
                builder.append("object.").append(methodName).append("(").append(requestCode).append(");\n");
                builder.append("break;}\n");
                if (singleRationaleMap.containsKey(requestCode)) {
                    singleRationaleMap.remove(requestCode);
                }
            }
        }

        for (Map.Entry<Integer, String> entry : singleRationaleMap.entrySet()) {
            int requestCode = entry.getKey();
            builder.append("case ").append(requestCode).append(": {\n")
                    .append("object.").append(entry.getValue()).append("();\nbreak;\n}");
        }

        builder.append("default:\nbreak;\n").append("}\n}\n\n");
    }

    private void generateDeniedMethod(StringBuilder builder) {
        if (checkBuilderNonNull(builder)) return;

        builder.append("@Override\n").append("public void denied(").append(element.getSimpleName())
                .append(" object, int code) {\n")
                .append("switch(code) {");
        for (Map.Entry<String, int[]> entry : deniedMap.entrySet()) {
            String methodName = entry.getKey();
            int[] ints = entry.getValue();
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

        for (Map.Entry<Integer, String> entry : singleDeniedMap.entrySet()) {
            int requestCode = entry.getKey();
            builder.append("case ").append(requestCode).append(": {\n")
                    .append("object.").append(entry.getValue()).append("();\nbreak;\n}");
        }

        builder.append("default:\nbreak;\n").append("}\n}\n\n");
    }

    private void generateGrantedMethod(StringBuilder builder) {
        if (checkBuilderNonNull(builder)) return;

        builder.append("@Override\n").append("public void granted(").append(element.getSimpleName())
                .append(" object, int code) {\n")
                .append("switch(code) {");
        for (Map.Entry<String, int[]> entry : grantedMap.entrySet()) {
            String methodName = entry.getKey();
            int[] ints = entry.getValue();
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

        for (Map.Entry<Integer, String> entry : singleGrantMap.entrySet()) {
            int requestCode = entry.getKey();
            builder.append("case ").append(requestCode).append(": {\n")
                    .append("object.").append(entry.getValue()).append("();\nbreak;\n}");
        }

        builder.append("default:\nbreak;\n").append("}\n}\n\n");
    }

    private void addSyncRequestPermissionMethod(StringBuilder builder, int targetRequestCode) {
        // syncPermissions size is 1
        for (Map.Entry<int[], String[]> entry : syncPermissions.entrySet()) {
            int[] requestCodes = entry.getKey();
            String[] permissions = entry.getValue();
            int length = requestCodes.length;
            // when syncRequestPermission size is 1
            if (length == 1) {
                firstRequestCode = requestCodes[0];
                firstRequestPermission = permissions[0];
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

    private boolean checkBuilderNonNull(StringBuilder builder) {
        return builder == null;
    }

    private String getClassName(TypeElement element, String packageName) {
        int packageLen = packageName.length() + 1;
        return element.getQualifiedName().toString().substring(packageLen)
                .replace('.', '$');
    }

    static class NonRationalePermissionBean {
        private int[] value;
        private int[] pageType;

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            NonRationalePermissionBean that = (NonRationalePermissionBean) object;

            return Arrays.equals(value, that.value) && Arrays.equals(pageType, that.pageType);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(value);
            result = 31 * result + Arrays.hashCode(pageType);
            return result;
        }

        @Override
        public String toString() {
            return "NonRationalePermissionBean{" +
                    "value=" + Arrays.toString(value) +
                    ", pageType=" + Arrays.toString(pageType) +
                    '}';
        }

        int[] getValue() {
            return value;
        }

        void setValue(int[] value) {
            this.value = value;
        }

        int[] getPageType() {
            return pageType;
        }

        void setPageType(int[] pageType) {
            this.pageType = pageType;
        }
    }
}
