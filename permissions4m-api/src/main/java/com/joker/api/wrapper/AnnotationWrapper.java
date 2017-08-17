package com.joker.api.wrapper;

import com.joker.api.PermissionsProxy;

/**
 * Created by joker on 2017/8/16.
 */

interface AnnotationWrapper {
    /**
     * get the proxy class
     * @param className target class name
     * @return
     */
    PermissionsProxy getProxy(String className);
}
