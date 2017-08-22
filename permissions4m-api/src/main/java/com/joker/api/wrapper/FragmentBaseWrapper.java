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

    @SuppressWarnings("unchecked")
    @Override
    void requestPermissionWithAnnotation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            proxy.granted(getContext(), getRequestCode());
        } else {
            initProxy(getContext());
            String permission = getPermission();
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager
                    .PERMISSION_GRANTED) {
                requestWhetherNeedRationaleWithAnnotation();
            } else {
//             granted
                if (isRequestForce()) {
                    ForceApplyPermissions.grantedOnResultWithAnnotation(this);
                } else {
                    proxy.granted(getContext(), getRequestCode());
                }
            }
        }
    }

    @Override
    void requestPermissionWithListener() {
        Wrapper.PermissionRequestListener requestListener = getPermissionRequestListener();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (requestListener != null) {
                requestListener.permissionGranted();
            }
        } else {
            String permission = getPermission();
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager
                    .PERMISSION_GRANTED) {
                requestWhetherNeedRationaleWithListener();
            } else {
                // may granted
                mayGranted();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void requestWhetherNeedRationaleWithAnnotation() {
        if (getContext() instanceof android.support.v4.app.Fragment) {
            if (((android.support.v4.app.Fragment) getContext()).shouldShowRequestPermissionRationale
                    (getPermission())) {
                if (!proxy.customRationale(getContext(), getRequestCode())) {
                    proxy.rationale(getContext(), getRequestCode());
                    ((android.support.v4.app.Fragment) getContext()).requestPermissions(new
                            String[]{getPermission()}, getRequestCode());
                }
            } else {
                ((android.support.v4.app.Fragment) getContext()).requestPermissions(new
                                String[]{getPermission()},
                        getRequestCode());
            }
        } else {
            if (((android.app.Fragment) getContext()).shouldShowRequestPermissionRationale
                    (getPermission())) {
                if (!proxy.customRationale(getContext(), getRequestCode())) {
                    proxy.rationale(getContext(), getRequestCode());
                    ((android.app.Fragment) getContext()).requestPermissions(new String[]{getPermission()
                    }, getRequestCode());
                }
            } else {
                ((android.app.Fragment) getContext()).requestPermissions(new String[]{getPermission()},
                        getRequestCode());
            }
        }
    }

    private void mayGranted() {
        if (isRequestForce()) {
            ForceApplyPermissions.grantedOnResultWithListener(this);
        } else {
            NormalApplyPermissions.grantedWithListener(this);
        }
    }

    private void requestWhetherNeedRationaleWithListener() {
        if (getContext() instanceof android.support.v4.app.Fragment) {
            if (((android.support.v4.app.Fragment) getContext()).shouldShowRequestPermissionRationale
                    (getPermission())) {
                if (getPermissionRequestListener() != null) {
                    getPermissionRequestListener().permissionRationale();
                }
            }
            ((android.support.v4.app.Fragment) getContext()).requestPermissions(new
                    String[]{getPermission()}, getRequestCode());
        } else {
            if (((android.app.Fragment) getContext()).shouldShowRequestPermissionRationale
                    (getPermission())) {
                if (getPermissionRequestListener() != null) {
                    getPermissionRequestListener().permissionRationale();
                }
            }
            ((android.app.Fragment) getContext()).requestPermissions(new String[]{getPermission()},
                    getRequestCode());
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
