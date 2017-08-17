package com.joker.api.apply;

import android.app.Activity;
import android.content.Intent;

import com.joker.api.Permissions4M;
import com.joker.api.support.PermissionsPageManager;
import com.joker.api.wrapper.Wrapper;

/**
 * Created by joker on 2017/8/9.
 */

public class ForceApplyPermissions {
    // listener module ===================================================================
    public static void grantedOnResultWithListener(Wrapper wrapper) {
        grantedWithListener(wrapper);
    }

    private static void grantedWithListener(Wrapper wrapper) {
        Activity activity = getActivity(wrapper);

        Wrapper.PermissionRequestListener requestListener = wrapper
                .getPermissionRequestListener();
        if (requestListener == null) {
            return;
        }

        if (PermissionsChecker.isPermissionGranted(activity, wrapper.getPermission())) {
            requestListener.permissionGranted();
        } else {
            requestListener.permissionDenied();
            Wrapper.PermissionPageListener pageListener = wrapper.getPermissionPageListener();
            if (pageListener != null) {
                boolean androidPage = wrapper.getPageType() == Permissions4M.PageType
                        .ANDROID_SETTING_PAGE;
                Intent intent = androidPage ? PermissionsPageManager.getIntent() :
                        PermissionsPageManager.getIntent(activity);
                pageListener.pageIntent(intent);
            }
        }
    }

    // annotation module ================================================================
    @SuppressWarnings("unchecked")
    public static void grantedOnResultWithAnnotation(Wrapper wrapper) {
        grantedWithAnnotation(wrapper);
    }

    @SuppressWarnings("unchecked")
    private static void grantedWithAnnotation(Wrapper wrapper) {
        Activity activity = getActivity(wrapper);

        if (PermissionsChecker.isPermissionGranted(activity, wrapper.getPermission())) {
            wrapper.getProxy(wrapper.getContext().getClass().getName()).granted(wrapper.getContext(), wrapper.getRequestCode());
        } else {
            wrapper.getProxy(wrapper.getContext().getClass().getName()).denied(wrapper.getContext(), wrapper.getRequestCode());
        }
    }

    private static Activity getActivity(Wrapper wrapper) {
        Activity activity;
        if (wrapper.getContext() instanceof android.app.Fragment) {
            activity = ((android.app.Fragment) wrapper.getContext()).getActivity();
        } else if (wrapper.getContext() instanceof android.support.v4.app.Fragment) {
            activity = ((android.support.v4.app.Fragment) wrapper.getContext()).getActivity();
        } else {
            activity = (Activity) wrapper.getContext();
        }

        return activity;
    }
}
