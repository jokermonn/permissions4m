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

    /**
     * called when use custom rationale, in case fall in to drop-dead
     *
     * custom rationale -> ensure button -> Permissions4M.request ->
     * if(custom rationale) -> custom rationale
     */
    Wrapper requestOnRationale();

    boolean isRequestOnRationale();
}
