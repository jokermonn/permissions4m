package com.joker.api.request;

import com.joker.api.PermissionsProxy;

/**
 * Created by joker on 2017/8/3.
 */
// implement this interface to support requesting permission
interface Requestable {
    // annotation request
    void requestPermission(Object object, String permission, int requestCode, PermissionsProxy instance);
}
