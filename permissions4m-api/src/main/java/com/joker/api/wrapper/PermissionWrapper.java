package com.joker.api.wrapper;

/**
 * Created by joker on 2017/9/2.
 */

public interface PermissionWrapper extends Wrapper {
    String getRequestPermission();

    int getRequestCode();

    Wrapper requestPermission(String permission);

    Wrapper requestCode(int code);
}
