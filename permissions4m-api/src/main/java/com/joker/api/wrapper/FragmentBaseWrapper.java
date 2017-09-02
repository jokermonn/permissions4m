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
                    ((android.support.v4.app.Fragment) context).requestPermissions(new
                            String[]{requestPermission}, requestCode);
                }
            } else {
                ((android.support.v4.app.Fragment) context).requestPermissions(new
                        String[]{requestPermission}, requestCode);
            }
        } else {
            if (((android.app.Fragment) context).shouldShowRequestPermissionRationale
                    (requestPermission)) {
                if (!getProxy(context.getClass().getName()).customRationale(context,
                        requestCode)) {
                    getProxy(context.getClass().getName()).rationale(context, requestCode);
                    ((android.app.Fragment) context).requestPermissions(new String[]{requestPermission
                    }, requestCode);
                }
            } else {
                ((android.app.Fragment) context).requestPermissions(new String[]{requestPermission},
                        requestCode);
            }
        }
    }

    @SuppressLint("NewApi")
    void tryRequestWithListener() {
        Object context = getContext();
        int requestCode = getRequestCode();
        String requestPermission = getRequestPermission();
        PermissionRequestListener requestListener = getPermissionRequestListener();
        if (context instanceof android.app.Fragment) {
            if (((android.app.Fragment) context).shouldShowRequestPermissionRationale
                    (requestPermission)) {
                if (requestListener != null) {
                    requestListener.permissionRationale(requestCode);
                }
            }
            ((android.app.Fragment) context).requestPermissions(new String[]{requestPermission},
                    requestCode);
        } else {
            if (((android.support.v4.app.Fragment) context).shouldShowRequestPermissionRationale
                    (requestPermission)) {
                if (requestListener != null) {
                    requestListener.permissionRationale(requestCode);
                }
            }
            ((android.support.v4.app.Fragment) context).requestPermissions(new
                    String[]{requestPermission}, requestCode);
        }
    }

    @Override
    @SuppressLint("NewApi")
    void originalRequest() {
        String requestPermission = getRequestPermission();
        int requestCode = getRequestCode();
        Object context = getContext();
        if (context instanceof android.app.Fragment) {
            ((android.app.Fragment) context).requestPermissions(new String[]{requestPermission},
                    requestCode);
        } else if (context instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) context).requestPermissions(new
                    String[]{requestPermission}, requestCode);
        } else {
            throw new IllegalArgumentException(getClass().getName() + "is not Fragment");
        }
    }
}
