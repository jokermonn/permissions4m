package com.joker.api.wrapper;

import android.content.Intent;

import com.joker.api.Permissions4M;

/**
 * listener callback should implement this interface
 * <p>
 * Created by joker on 2017/8/16.
 */

public interface ListenerWrapper {
    /**
     * request code
     *
     * @param code
     * @return
     */
    Wrapper requestCode(int code);

    /**
     * request permission
     *
     * @param permission {@link android.Manifest.permission}
     * @return
     */
    Wrapper requestPermission(String permission);

    /**
     * android setting page or phone manager page
     *
     * @param pageType {@link com.joker.api.Permissions4M.PageType}
     * @return
     */
    Wrapper requestPageType(@Permissions4M.PageType int pageType);

    /**
     * call back listener
     *
     * @param listener {@link PermissionPageListener}
     * @return
     */
    Wrapper requestCallback(PermissionRequestListener listener);

    /**
     * according to {@link ListenerWrapper#requestPageType(int)}, will return different Intent type
     *
     * @param listener {@link PermissionPageListener}
     * @return
     */
    Wrapper requestPage(PermissionPageListener listener);

    /**
     * whether should apply permission force
     *
     * @param force true if force
     * @return
     */
    Wrapper requestForce(boolean force);

    /**
     * get the context({@link android.app.Activity}, {@link android.app.Fragment},
     * {@link android.support.v4.app.Fragment})
     *
     * @return {@link android.app.Activity} or {@link android.app.Fragment} or
     * {@link android.support.v4.app.Fragment}
     */
    Object getContext();

    /**
     * request permission
     */
    void request();

    /**
     * multiple permission request by sync
     */
    void requestSync();

    int getRequestCode();

    String getPermission();

    @Permissions4M.PageType
    int getPageType();

    boolean isRequestForce();

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
}
