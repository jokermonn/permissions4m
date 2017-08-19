package com.joker.api;

import android.content.Intent;

/**
 * Created by joker on 2017/7/26.
 */

public interface PermissionsProxy<T> {
    void rationale(T object, int code);

    void denied(T object, int code);

    void granted(T object, int code);

    void intent(T object, int code, Intent intent);

    boolean customRationale(T object, int code);

    void startSyncRequestPermissionsMethod(T object);
}
