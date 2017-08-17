package com.joker.api.apply;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.joker.api.support.PermissionsPageManager;
import com.joker.api.wrapper.Wrapper;


/**
 * Created by joker on 2017/8/10.
 */

public class NormalApplyPermissions {
    @SuppressWarnings("unchecked")
    public static void grantedOnResultWithAnnotation(Wrapper wrapper) {
        grantedWithAnn(wrapper);
    }

    @SuppressWarnings("unchecked")
    public static void deniedOnResultWithAnnotation(Wrapper wrapper) {
        deniedWithAnn(wrapper);
    }

    @SuppressWarnings("unchecked")
    private static void grantedWithAnn(Wrapper wrapper) {
        wrapper.getProxy(wrapper.getContext().getClass().getName()).granted(wrapper.getContext(), wrapper
                .getRequestCode());
    }

    @SuppressWarnings("unchecked")
    private static void deniedWithAnn(Wrapper wrapper) {
        wrapper.getProxy(wrapper.getContext().getClass().getName()).denied(wrapper.getContext(), wrapper
                .getRequestCode());
    }

    public static void grantedOnResultWithListener(Wrapper wrapper) {
        if (wrapper.getPermissionRequestListener() != null) {
            wrapper.getPermissionRequestListener().permissionGranted();
        }
    }

    public static void deniedOnResultWithListener(Wrapper wrapper) {
        deniedWithListener(wrapper);
    }

    private static void deniedWithListener(Wrapper wrapper) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (wrapper.getPermissionRequestListener() != null) {
            wrapper.getPermissionRequestListener().permissionDenied();
        }

        Log.e("TAG", "deniedWithListener: 1 ");
        if (pageListenerNonNull(wrapper) || PermissionsPageManager.isNonRationaleManufacturer()) {
            Log.e("TAG", "deniedWithListener: 2 ");
            wrapper.getPermissionPageListener().pageIntent(PermissionsPageManager.getIntent());
        }
    }

    private static boolean pageListenerNonNull(Wrapper wrapper) {
        Activity activity;
        if (wrapper.getContext() instanceof android.app.Fragment) {
            activity = ((android.app.Fragment) wrapper.getContext()).getActivity();
        } else if (wrapper.getContext() instanceof android.support.v4.app.Fragment) {
            activity = ((android.support.v4.app.Fragment) wrapper.getContext()).getActivity();
        } else {
            activity = (Activity) wrapper.getContext();
        }

        return !ActivityCompat.shouldShowRequestPermissionRationale(activity, wrapper.getPermission()) &&
                wrapper.getPermissionPageListener() != null;
    }
}
