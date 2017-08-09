package com.joker.api.stream.apply;

import android.app.Activity;
import android.content.Intent;

import com.joker.api.stream.SupportPermissions4M;
import com.joker.api.stream.wrapper.Wrapper;
import com.joker.api.support.PermissionsPageManager;

/**
 * Created by joker on 2017/8/9.
 */

public class ForceApplyPermissions {
    public static void grantedOnRequestPermissionsResult(Activity activity, Wrapper wrapper, int
            requestCode) {
        Wrapper.PermissionRequestListener requestListener = wrapper
                .getPermissionRequestListener();
        if (requestListener == null) {
            return;
        }

        if (PermissionsCheck.isPermissionGranted(activity, wrapper.getPermission())) {
            requestListener.permissionGranted();
        } else {
            requestListener.permissionDenied();

            Wrapper.PermissionPageListener pageListener = wrapper.getPermissionPageListener();
            if (pageListener != null) {
                boolean androidPage = wrapper.getPageType() == SupportPermissions4M.PageType
                        .ANDROID_SETTING_PAGE;
                Intent intent = androidPage ? PermissionsPageManager.getIntent() :
                        PermissionsPageManager.getIntent(activity);
                pageListener.pageIntent(intent);
            }
        }
    }
}
