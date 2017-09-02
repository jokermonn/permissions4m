package com.joker.api.wrapper;

import android.content.Intent;

/**
 * Created by joker on 2017/8/16.
 */

public interface AnnotationWrapper {
    /**
     * get the proxy class
     * @param className target class name
     * @return
     */
    PermissionsProxy getProxy(String className);

    interface PermissionsProxy<T> {
        void rationale(T object, int code);

        void denied(T object, int code);

        void granted(T object, int code);

        void intent(T object, int code, Intent intent);

        boolean customRationale(T object, int code);

        void startSyncRequestPermissionsMethod(T object);
    }
}
