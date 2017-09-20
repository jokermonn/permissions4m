package com.joker.api.apply;

import android.app.Activity;
import android.content.Intent;

import com.joker.api.Permissions4M;
import com.joker.api.support.PermissionsPageManager;
import com.joker.api.wrapper.AnnotationWrapper;
import com.joker.api.wrapper.ListenerWrapper;
import com.joker.api.wrapper.PermissionWrapper;
import com.joker.api.wrapper.Wrapper;


/**
 * Created by joker on 2017/8/9.
 */

public class ForceApplyPermissions {
    // listener module ===================================================================
    public static void grantedWithListener(PermissionWrapper wrapper) {
        if (PermissionsChecker.isPermissionGranted(getActivity(wrapper), wrapper.getRequestPermission())) {
            NormalApplyPermissions.grantedWithListener(wrapper);
        } else {
            NormalApplyPermissions.deniedWithListener(wrapper);
        }
    }

    // annotation module ================================================================
    @SuppressWarnings("unchecked")
    public static void grantedWithAnnotation(PermissionWrapper wrapper) {
        if (PermissionsChecker.isPermissionGranted(getActivity(wrapper), wrapper.getRequestPermission())) {
            NormalApplyPermissions.grantedWithAnnotation(wrapper);
        } else {
            NormalApplyPermissions.deniedWithAnnotation(wrapper);
        }
    }

    public static void deniedWithListenerForUnderM(PermissionWrapper wrapper) {
        Activity activity = getActivity(wrapper);
        ListenerWrapper.PermissionRequestListener requestListener = wrapper.getPermissionRequestListener();
        if (requestListener != null) {
            requestListener.permissionDenied(wrapper.getRequestCode());
        }

        ListenerWrapper.PermissionPageListener pageListener = wrapper.getPermissionPageListener();
        if (pageListener != null) {
            boolean androidPage = wrapper.getPageType() == Permissions4M.PageType.ANDROID_SETTING_PAGE;
            Intent intent = androidPage ? PermissionsPageManager.getSettingIntent(activity) :
                    PermissionsPageManager.getIntent(activity);
            pageListener.pageIntent(wrapper.getRequestCode(), intent);
        }
    }

    @SuppressWarnings("unchecked")
    public static void deniedWithAnnotationForUnderM(PermissionWrapper wrapper) {
        AnnotationWrapper.PermissionsProxy proxy = wrapper.getProxy(wrapper.getContext().getClass()
                .getName());
        proxy.denied(wrapper.getContext(), wrapper.getRequestCode());

        boolean androidPage = wrapper.getPageType() == Permissions4M.PageType
                .ANDROID_SETTING_PAGE;
        Intent intent = androidPage ? PermissionsPageManager.getSettingIntent(getActivity(wrapper)) :
                PermissionsPageManager.getIntent(getActivity(wrapper));

        proxy.intent(wrapper.getContext(), wrapper.getRequestCode(), intent);
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
