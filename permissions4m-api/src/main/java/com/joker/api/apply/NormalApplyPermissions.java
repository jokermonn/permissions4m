package com.joker.api.apply;

import android.app.Activity;
import android.content.Intent;

import com.joker.api.Permissions4M;
import com.joker.api.apply.util.SupportUtil;
import com.joker.api.support.PermissionsPageManager;
import com.joker.api.wrapper.AbstractWrapper;
import com.joker.api.wrapper.AnnotationWrapper;
import com.joker.api.wrapper.ListenerWrapper;
import com.joker.api.wrapper.PermissionWrapper;
import com.joker.api.wrapper.Wrapper;

import java.lang.ref.WeakReference;


/**
 * Created by joker on 2017/8/10.
 */

public class NormalApplyPermissions {
    // annotation module ===================================================================================
    @SuppressWarnings("unchecked")
    public static void grantedWithAnnotation(PermissionWrapper wrapper) {
        wrapper.getProxy(wrapper.getContext().getClass().getName())
                .granted(wrapper.getContext(), wrapper.getRequestCode());
    }

    @SuppressWarnings("unchecked")
    public static void deniedWithAnnotation(PermissionWrapper wrapper) {
        AnnotationWrapper.PermissionsProxy proxy = wrapper.getProxy(wrapper.getContext().getClass()
                .getName());

        proxy.denied(wrapper.getContext(), wrapper.getRequestCode());

        if (SupportUtil.nonShowRationale(wrapper)) {
            Activity activity = getActivity(wrapper);

            boolean androidPage = wrapper.getPageType() == Permissions4M.PageType
                    .ANDROID_SETTING_PAGE;
            Intent intent = androidPage ? PermissionsPageManager.getSettingIntent(activity) :
                    PermissionsPageManager.getIntent(activity);

            proxy.intent(wrapper.getContext(), wrapper.getRequestCode(), intent);
        }
    }

    // listener module ===================================================================================
    public static void grantedWithListener(PermissionWrapper wrapper) {
        if (wrapper.getPermissionRequestListener() != null) {
            wrapper.getPermissionRequestListener().permissionGranted(wrapper.getRequestCode());
        }

        requestNextPermissionWithListener(wrapper);
    }

    /**
     * 1.call {@link com.joker.api.wrapper.ListenerWrapper.PermissionRequestListener#permissionDenied(int)}
     * 2.request next permission if required
     * 3.show page intent if required
     *
     * @param wrapper
     */
    public static void deniedWithListener(PermissionWrapper wrapper) {
        ListenerWrapper.PermissionRequestListener requestListener = wrapper.getPermissionRequestListener();
        if (requestListener != null) {
            requestListener.permissionDenied(wrapper.getRequestCode());
        }

        requestNextPermissionWithListener(wrapper);

        if (SupportUtil.pageListenerNonNull(wrapper) && SupportUtil.nonShowRationale(wrapper)) {
            Activity activity = getActivity(wrapper);

            boolean androidPage = wrapper.getPageType() == Permissions4M.PageType
                    .ANDROID_SETTING_PAGE;
            Intent intent = androidPage ? PermissionsPageManager.getSettingIntent(getActivity
                    (wrapper)) :
                    PermissionsPageManager.getIntent(activity);

            wrapper.getPermissionPageListener().pageIntent(wrapper.getRequestCode(), intent);
        }
    }

    private static void requestNextPermissionWithListener(PermissionWrapper wrapper) {
        int nextCode = wrapper.getRequestCodes()[0];
        if (wrapper.getRequestCode() != nextCode) {
            WeakReference<PermissionWrapper> reference = AbstractWrapper.getWrapperMap().get(new
                    AbstractWrapper.Key(wrapper.getContext(), nextCode));
            if (reference != null) {
                AbstractWrapper permissionWrapper = (AbstractWrapper) reference.get();
                if (permissionWrapper != null) {
                    permissionWrapper.requestPermissionWithListener();
                }
            }
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
