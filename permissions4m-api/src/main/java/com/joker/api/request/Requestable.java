package com.joker.api.request;

/**
 * Created by joker on 2017/8/3.
 */
// implement this interface to support requesting permission
interface Requestable {
    void request(Object object, String permission, int requestCode);
}
