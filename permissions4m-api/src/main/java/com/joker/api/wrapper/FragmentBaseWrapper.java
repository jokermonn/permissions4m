package com.joker.api.wrapper;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.joker.api.apply.ForceApplyPermissions;
import com.joker.api.apply.NormalApplyPermissions;
import com.joker.api.apply.PermissionsChecker;

import static android.R.attr.fragment;

/**
 * Created by joker on 2017/8/17.
 */

abstract class FragmentBaseWrapper extends AbstractWrapper implements Wrapper {
    public FragmentBaseWrapper() {

    }

    @Override
    public Object getContext() {
        return fragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void requestSync() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(fragment);
        proxy.startSyncRequestPermissionsMethod(fragment);
    }

    @SuppressWarnings("unchecked")
    void requestPermissionWithAnnotation(android.app.Fragment fragment) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(fragment);

        String permission = getPermission();
        int requestCode = getRequestCode();
        if (ContextCompat.checkSelfPermission(fragment.getActivity(), permission) != PackageManager
                .PERMISSION_GRANTED) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                if (!proxy.customRationale(fragment, requestCode)) {
                    proxy.rationale(fragment, requestCode);
                    fragment.requestPermissions(new String[]{permission},
                            requestCode);
                }
            } else {
                fragment.requestPermissions(new String[]{permission},
                        requestCode);
            }
        } else {
//             granted
            if (isRequestForce()) {
                if (PermissionsChecker.isPermissionGranted(fragment.getActivity(), permission)) {
                    proxy.granted(fragment, getRequestCode());
                } else {
                    proxy.denied(fragment, getRequestCode());
                }
            } else {
                proxy.granted(fragment, getRequestCode());
            }
        }
    }

    @SuppressWarnings("unchecked")
    void requestPermissionWithAnnotation(android.support.v4.app.Fragment fragment) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(fragment);

        String permission = getPermission();
        int requestCode = getRequestCode();
        if (ContextCompat.checkSelfPermission(fragment.getActivity(), permission) != PackageManager
                .PERMISSION_GRANTED) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                if (!proxy.customRationale(fragment, requestCode)) {
                    proxy.rationale(fragment, requestCode);
                    fragment.requestPermissions(new String[]{permission},
                            requestCode);
                }
            } else {
                fragment.requestPermissions(new String[]{permission},
                        requestCode);
            }
        } else {
//             granted
            if (isRequestForce()) {
                if (PermissionsChecker.isPermissionGranted(fragment.getActivity(), permission)) {
                    proxy.granted(fragment, getRequestCode());
                } else {
                    proxy.denied(fragment, getRequestCode());
                }
            } else {
                proxy.granted(fragment, getRequestCode());
            }
        }
    }

    abstract void requestPermissionWithAnnotation();

    void requestPermissionWithListener(android.app.Fragment fragment) {
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
                NormalApplyPermissions.grantedOnResultWithListener(this);
            }
        }
    }

    void requestPermissionWithListener(android.support.v4.app.Fragment fragment) {
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
                NormalApplyPermissions.grantedOnResultWithListener(this);
            }
        }
    }

    abstract void requestPermissionWithListener();
}
