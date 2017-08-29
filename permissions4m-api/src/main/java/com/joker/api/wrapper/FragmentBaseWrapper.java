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
        if (getContext() instanceof android.support.v4.app.Fragment) {
            if (((android.support.v4.app.Fragment) getContext()).shouldShowRequestPermissionRationale
                    (getPermission())) {
                if (!getProxy(getContext().getClass().getName()).customRationale(getContext(),
                        getRequestCode())) {
                    getProxy(getContext().getClass().getName()).rationale(getContext(), getRequestCode());
                    ((android.support.v4.app.Fragment) getContext()).requestPermissions(new
                            String[]{getPermission()}, getRequestCode());
                }
            } else {
                ((android.support.v4.app.Fragment) getContext()).requestPermissions(new
                        String[]{getPermission()}, getRequestCode());
            }
        } else {
            if (((android.app.Fragment) getContext()).shouldShowRequestPermissionRationale
                    (getPermission())) {
                if (!getProxy(getContext().getClass().getName()).customRationale(getContext(),
                        getRequestCode())) {
                    getProxy(getContext().getClass().getName()).rationale(getContext(), getRequestCode());
                    ((android.app.Fragment) getContext()).requestPermissions(new String[]{getPermission()
                    }, getRequestCode());
                }
            } else {
                ((android.app.Fragment) getContext()).requestPermissions(new String[]{getPermission()},
                        getRequestCode());
            }
        }
    }

    @SuppressLint("NewApi")
    void tryRequestWithListener() {
        if (getContext() instanceof android.app.Fragment) {
            if (((android.app.Fragment) getContext()).shouldShowRequestPermissionRationale(getPermission
                    ())) {
                PermissionRequestListener requestListener = getPermissionRequestListener();
                if (requestListener != null) {
                    requestListener.permissionRationale();
                }
            }
            ((android.app.Fragment) getContext()).requestPermissions(new String[]{getPermission()},
                    getRequestCode());
        } else {
            if (((android.support.v4.app.Fragment) getContext()).shouldShowRequestPermissionRationale
                    (getPermission())) {
                PermissionRequestListener requestListener = getPermissionRequestListener();
                if (requestListener != null) {
                    requestListener.permissionRationale();
                }
            }
            ((android.support.v4.app.Fragment) getContext()).requestPermissions(new
                    String[]{getPermission()}, getRequestCode());
        }
    }

    @Override
    @SuppressLint("NewApi")
    void originalRequest() {
        if (getContext() instanceof android.app.Fragment) {
            ((android.app.Fragment) getContext()).requestPermissions(new String[]{getPermission()},
                    getRequestCode());
        } else if (getContext() instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) getContext()).requestPermissions(new
                    String[]{getPermission()}, getRequestCode());
        } else {
            throw new IllegalArgumentException(getClass().getName() + "is not Fragment");
        }
    }
}
