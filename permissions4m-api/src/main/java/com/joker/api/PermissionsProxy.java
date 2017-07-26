package com.joker.api;

/**
 * Created by joker on 2017/7/26.
 */

public interface PermissionsProxy<T> {
    void rationale(T object, int code);

    void denied(T object, int code);

    void granted(T object, int code);
}
