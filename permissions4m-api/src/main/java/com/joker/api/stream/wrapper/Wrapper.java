package com.joker.api.stream.wrapper;

import android.content.Intent;

import com.joker.api.stream.SupportPermissions4M;


/**
 * Created by joker on 2017/8/5.
 */

public interface Wrapper {
    Wrapper requestCode(int code);

    Wrapper requestPermission(String permission);

    Wrapper requestPageType(@SupportPermissions4M.PageType int pageType);

    Wrapper requestRationale(PermissionRationaleListener listener);

    Wrapper requestCallback(PermissionRequestListener listener);

    Wrapper requestPage(PermissionPageListener listener);

    Wrapper requestForce(boolean force);

    void request();

    int getCode();

    String getPermission();

    @SupportPermissions4M.PageType
    int getPageType();

    boolean isRequestForce();

    PermissionRationaleListener getPermissionRationaleListener();

    PermissionRequestListener getPermissionRequestListener();

    PermissionPageListener getPermissionPageListener();

    interface PermissionRequestListener {
        void permissionGranted();

        void permissionDenied();

        void permissionRationale();
    }

    interface PermissionPageListener {
        void pageIntent(Intent intent);
    }

    interface PermissionRationaleListener {
        void rationale();
    }
}
