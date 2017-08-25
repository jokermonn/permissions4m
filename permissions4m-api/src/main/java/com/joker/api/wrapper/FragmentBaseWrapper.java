package com.joker.api.wrapper;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.joker.api.apply.ForceApplyPermissions;
import com.joker.api.apply.NormalApplyPermissions;

/**
 * Created by joker on 2017/8/17.
 */

abstract class FragmentBaseWrapper extends AbstractWrapper implements Wrapper {
    public FragmentBaseWrapper() {

    }

    @Override
    void requestPermissionWithAnnotation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (getPermissionRequestListener() != null) {
                getPermissionRequestListener().permissionGranted();
            }
        } else {
            // Fragment、SupportFragment
            fragmentRequestWithAnnotation();
        }
    }

    @Override
    void requestPermissionWithListener() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (getPermissionRequestListener() != null) {
                getPermissionRequestListener().permissionGranted();
            }
        } else {
            // Fragment、SupportFragment
            fragmentRequestWithListener();
        }
    }

    @SuppressWarnings("unchecked")
    private void fragmentRequestWithAnnotation() {
        String permission = getPermission();
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager
                .PERMISSION_GRANTED) {
            tryRequestWithAnnotation();
        } else {
            // may granted
            mayGrantedWithAnnotation();
        }
    }

    private void fragmentRequestWithListener() {
        String permission = getPermission();
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager
                .PERMISSION_GRANTED) {
            tryRequestWithListener();
        } else {
            // may granted
            mayGrantedWithListener();
        }
    }

    @SuppressWarnings("unchecked")
    private void tryRequestWithAnnotation() {
        if (getContext() instanceof android.support.v4.app.Fragment) {
            if (((android.support.v4.app.Fragment) getContext()).shouldShowRequestPermissionRationale
                    (getPermission())) {
                if (!getProxy(getContext().getClass().getName()).customRationale(getContext(), getRequestCode())) {
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
                if (!getProxy(getContext().getClass().getName()).customRationale(getContext(), getRequestCode())) {
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

    private void mayGrantedWithListener() {
        if (isRequestForce()) {
            ForceApplyPermissions.grantedOnResultWithListener(this);
        } else {
            NormalApplyPermissions.grantedWithListener(this);
        }
    }

    private void tryRequestWithListener() {
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

    @SuppressWarnings("unchecked")
    private void mayGrantedWithAnnotation() {
        if (isRequestForce()) {
            ForceApplyPermissions.grantedOnResultWithAnnotation(this);
        } else {
            NormalApplyPermissions.grantedWithAnnotation(this);
        }
    }

    @Override
    void normalRequest() {
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
