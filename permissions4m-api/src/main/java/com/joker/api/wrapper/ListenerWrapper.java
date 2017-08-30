package com.joker.api.wrapper;

import android.content.Intent;

/**
 * listener callback should implement this interface
 * <p>
 * Created by joker on 2017/8/16.
 */

public interface ListenerWrapper {
    /**
     * call back listener
     *
     * @param listener {@link PermissionPageListener}
     * @return
     */
    Wrapper requestCallback(PermissionRequestListener listener);

    /**
     * according to {@link Wrapper#requestPageType(int)}, will return different Intent type
     *
     * @param listener {@link PermissionPageListener}
     * @return
     */
    Wrapper requestPage(PermissionPageListener listener);

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
