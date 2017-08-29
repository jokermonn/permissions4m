package com.joker.api.apply;

import android.app.Activity;
import android.content.Intent;

import com.joker.api.Permissions4M;
import com.joker.api.PermissionsProxy;
import com.joker.api.apply.util.SupportUtil;
import com.joker.api.support.PermissionsPageManager;
import com.joker.api.wrapper.ListenerWrapper;
import com.joker.api.wrapper.Wrapper;


/**
 * Created by joker on 2017/8/9.
 */

public class ForceApplyPermissions {
    // listener module ===================================================================
    public static void grantedOnResultWithListener(Wrapper wrapper) {
        Activity activity = getActivity(wrapper);

        Wrapper.PermissionRequestListener requestListener = wrapper
                .getPermissionRequestListener();

        if (PermissionsChecker.isPermissionGranted(activity, wrapper.getPermission())) {
            if (requestListener != null) {
                requestListener.permissionGranted();
            }
        } else {
            if (requestListener != null) {
                requestListener.permissionDenied();
            }
            Wrapper.PermissionPageListener pageListener = wrapper.getPermissionPageListener();
            if (SupportUtil.pageListenerNonNull(wrapper) && SupportUtil.nonShowRationale(wrapper)) {
                boolean androidPage = wrapper.getPageType() == Permissions4M.PageType
                        .ANDROID_SETTING_PAGE;
                Intent intent = androidPage ? PermissionsPageManager.getSettingIntent(activity) :
                        PermissionsPageManager.getIntent(activity);
                pageListener.pageIntent(intent);
            }
        }
    }

    // annotation module ================================================================
    @SuppressWarnings("unchecked")
    public static void grantedOnResultWithAnnotation(Wrapper wrapper) {
        Activity activity = getActivity(wrapper);

        PermissionsProxy proxy = wrapper.getProxy(wrapper.getContext().getClass().getName());
        if (PermissionsChecker.isPermissionGranted(activity, wrapper.getPermission())) {
            proxy.granted(wrapper.getContext(), wrapper.getRequestCode());
        } else {
            proxy.denied(wrapper.getContext(), wrapper.getRequestCode());

            if (SupportUtil.nonShowRationale(wrapper)) {
                boolean androidPage = wrapper.getPageType() == Permissions4M.PageType
                        .ANDROID_SETTING_PAGE;
                Intent intent = androidPage ? PermissionsPageManager.getSettingIntent(activity) :
                        PermissionsPageManager.getIntent(getActivity(wrapper));

                proxy.intent(wrapper.getContext(), wrapper.getRequestCode(), intent);
            }
        }
    }

    public static void deniedOnResultWithListenerForUnderMManufacturer(Wrapper wrapper) {
        Activity activity = getActivity(wrapper);
        ListenerWrapper.PermissionRequestListener requestListener = wrapper.getPermissionRequestListener();
        if (requestListener != null) {
            requestListener.permissionDenied();
        }

        ListenerWrapper.PermissionPageListener pageListener = wrapper.getPermissionPageListener();
        if (pageListener != null) {
            boolean androidPage = wrapper.getPageType() == Permissions4M.PageType.ANDROID_SETTING_PAGE;
            Intent intent = androidPage ? PermissionsPageManager.getSettingIntent(activity) :
                    PermissionsPageManager.getIntent(activity);
            pageListener.pageIntent(intent);
        }
    }

    @SuppressWarnings("unchecked")
    public static void deniedOnResultWithAnnotationForUnderMManufacturer(Wrapper wrapper) {
        PermissionsProxy proxy = wrapper.getProxy(wrapper.getContext().getClass().getName());
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
