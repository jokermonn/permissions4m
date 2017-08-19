package com.joker.api.wrapper;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.joker.api.apply.ForceApplyPermissions;
import com.joker.api.apply.NormalApplyPermissions;
import com.joker.api.apply.PermissionsChecker;
import com.joker.api.apply.util.SupportUtil;
import com.joker.api.support.PermissionsPageManager;

/**
 * Created by joker on 2017/8/17.
 */

abstract class FragmentBaseWrapper extends AbstractWrapper implements Wrapper {
    public FragmentBaseWrapper() {

    }

    @Override
    void requestPermissionWithAnnotation() {
        if (getContext() instanceof android.app.Fragment) {
            // Fragment
            fragmentRequest();
        } else if (getContext() instanceof android.support.v4.app.Fragment) {
            // SupportFragment
            supportFragmentRequest();
        } else {
            throw new IllegalArgumentException(getClass().getName() + "is not Fragment");
        }
    }

    @Override
    void requestPermissionWithListener() {
        if (getContext() instanceof android.app.Fragment) {
            requestPermissionWithListener((android.app.Fragment) getContext());
        } else if (getContext() instanceof android.support.v4.app.Fragment) {
            requestPermissionWithListener((android.support.v4.app.Fragment) getContext());
        } else {
            throw new IllegalArgumentException(getClass().getName() + "is not Fragment");
        }
    }

    @SuppressWarnings("unchecked")
    private void supportFragmentRequest() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(getContext());

        String permission = getPermission();
        int requestCode = getRequestCode();
        if (ContextCompat.checkSelfPermission(((android.support.v4.app.Fragment) getContext())
                .getActivity(), permission) != PackageManager
                .PERMISSION_GRANTED) {
            if (((android.support.v4.app.Fragment) getContext()).shouldShowRequestPermissionRationale
                    (permission)) {
                if (!proxy.customRationale(getContext(), requestCode)) {
                    proxy.rationale(getContext(), requestCode);
                    ((android.support.v4.app.Fragment) getContext()).requestPermissions(new
                                    String[]{permission},
                            requestCode);
                }
            } else {
                ((android.support.v4.app.Fragment) getContext()).requestPermissions(new
                                String[]{permission},
                        requestCode);
            }
        } else {
//             granted
            if (isRequestForce()) {
                if (PermissionsChecker.isPermissionGranted(((android.support.v4.app.Fragment)
                        getContext()).getActivity(), permission)) {
                    proxy.granted(getContext(), getRequestCode());
                } else {
                    proxy.denied(getContext(), getRequestCode());
                    if (SupportUtil.nonShowRationale(this) || PermissionsPageManager.isNonRationaleManufacturer()) {
                        proxy.intent(getContext(), getRequestCode());
                    }
                }
            } else {
                proxy.granted(getContext(), getRequestCode());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void fragmentRequest() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(getContext());

        String permission = getPermission();
        int requestCode = getRequestCode();
        if (ContextCompat.checkSelfPermission(((android.app.Fragment) getContext()).getActivity(),
                permission) != PackageManager
                .PERMISSION_GRANTED) {
            if (((android.app.Fragment) getContext()).shouldShowRequestPermissionRationale
                    (permission)) {
                if (!proxy.customRationale(getContext(), requestCode)) {
                    proxy.rationale(getContext(), requestCode);
                    ((android.app.Fragment) getContext()).requestPermissions(new String[]{permission},
                            requestCode);
                }
            } else {
                ((android.app.Fragment) getContext()).requestPermissions(new String[]{permission},
                        requestCode);
            }
        } else {
//             granted
            if (isRequestForce()) {
                if (PermissionsChecker.isPermissionGranted(((android.app.Fragment) getContext())
                        .getActivity(), permission)) {
                    proxy.granted(getContext(), getRequestCode());
                } else {
                    proxy.denied(getContext(), getRequestCode());
                    if (SupportUtil.nonShowRationale(this) || PermissionsPageManager.isNonRationaleManufacturer()) {
                        proxy.intent(getContext(), getRequestCode());
                    }
                }
            } else {
                proxy.granted(getContext(), getRequestCode());
            }
        }
    }

    private void requestPermissionWithListener(android.app.Fragment fragment) {
        Wrapper.PermissionRequestListener requestListener = getPermissionRequestListener();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || requestListener == null) {
            return;
        }

        String permission = getPermission();
        int requestCode = getRequestCode();
        if (ContextCompat.checkSelfPermission(fragment.getActivity(), permission) != PackageManager
                .PERMISSION_GRANTED) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                requestListener.permissionRationale();
            }
            fragment.requestPermissions(new String[]{permission}, requestCode);
        } else {
            // granted
            if (isRequestForce()) {
                ForceApplyPermissions.grantedOnResultWithListener(this);
            } else {
                NormalApplyPermissions.grantedWithListener(this);
            }
        }
    }

    private void requestPermissionWithListener(android.support.v4.app.Fragment fragment) {
        Wrapper.PermissionRequestListener requestListener = getPermissionRequestListener();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || requestListener == null) {
            return;
        }

        String permission = getPermission();
        int requestCode = getRequestCode();
        if (ContextCompat.checkSelfPermission(fragment.getActivity(), permission) != PackageManager
                .PERMISSION_GRANTED) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                requestListener.permissionRationale();
            }
            fragment.requestPermissions(new String[]{permission}, requestCode);
        } else {
            // granted
            if (isRequestForce()) {
                ForceApplyPermissions.grantedOnResultWithListener(this);
            } else {
                NormalApplyPermissions.grantedWithListener(this);
            }
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
