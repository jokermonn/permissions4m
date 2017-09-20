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
    Wrapper requestListener(PermissionRequestListener listener);

    /**
     * according to {@link Wrapper#requestPageType(int)}, will return different Intent type
     *
     * @param listener {@link PermissionPageListener}
     * @return
     */
    Wrapper requestPage(PermissionPageListener listener);

    Wrapper requestCustomRationaleListener(PermissionCustomRationaleListener listener);

    PermissionRequestListener getPermissionRequestListener();

    PermissionPageListener getPermissionPageListener();

    PermissionCustomRationaleListener getPermissionCustomRationaleListener();

    interface PermissionRequestListener {
        void permissionGranted(int code);

        void permissionDenied(int code);

        void permissionRationale(int code);
    }

    interface PermissionPageListener {
        void pageIntent(int code, Intent intent);
    }

    interface PermissionCustomRationaleListener {
        void permissionCustomRationale(int code);
    }
}
