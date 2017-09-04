package com.joker.api.wrapper;

import android.annotation.SuppressLint;

/**
 * Created by joker on 2017/8/17.
 */

abstract class FragmentBaseWrapper extends AbstractWrapper implements Wrapper {
    public FragmentBaseWrapper() {

    }

    @SuppressWarnings("unchecked")
    @SuppressLint("NewApi")
    void tryRequestWithAnnotation() {
        Object context = getContext();
        int requestCode = getRequestCode();
        String requestPermission = getRequestPermission();
        if (context instanceof android.support.v4.app.Fragment) {
            if (((android.support.v4.app.Fragment) context).shouldShowRequestPermissionRationale
                    (requestPermission)) {
                if (!getProxy(context.getClass().getName()).customRationale(context, requestCode)) {
                    getProxy(context.getClass().getName()).rationale(context, requestCode);
                    originalRequest();
                }
            } else {
                originalRequest();
            }
        } else {
            if (((android.app.Fragment) context).shouldShowRequestPermissionRationale
                    (requestPermission)) {
                if (!getProxy(context.getClass().getName()).customRationale(context,
                        requestCode)) {
                    getProxy(context.getClass().getName()).rationale(context, requestCode);
                    originalRequest();
                }
            } else {
                originalRequest();
            }
        }
    }

    @SuppressLint("NewApi")
    void tryRequestWithListener() {
        PermissionCustomRationaleListener customRationaleListener = getPermissionCustomRationaleListener();
        Object context = getContext();
        int requestCode = getRequestCode();
        String requestPermission = getRequestPermission();
        PermissionRequestListener requestListener = getPermissionRequestListener();

        if (context instanceof android.app.Fragment) {
            if (((android.app.Fragment) context).shouldShowRequestPermissionRationale
                    (requestPermission)) {
                if (customRationaleListener != null) {
                    customRationaleListener.permissionCustomRationale(getRequestCode());
                } else {
                    requestListener.permissionRationale(requestCode);
                    originalRequest();
                }
            } else {
                originalRequest();
            }
        } else {
            if (((android.support.v4.app.Fragment) context).shouldShowRequestPermissionRationale
                    (requestPermission)) {
                if (customRationaleListener != null) {
                    customRationaleListener.permissionCustomRationale(getRequestCode());
                } else {
                    requestListener.permissionRationale(requestCode);
                    originalRequest();
                }
            } else {
                originalRequest();
            }
        }
    }

    @Override
    @SuppressLint("NewApi")
    void originalRequest() {
        Object context = getContext();
        String permission = getRequestPermission();
        int code = getRequestCode();
        if (context instanceof android.app.Fragment) {
            ((android.app.Fragment) context).requestPermissions(new String[]{permission}, code);
        } else {
            ((android.support.v4.app.Fragment) context).requestPermissions(new
                    String[]{permission}, code);
        }
    }
}
