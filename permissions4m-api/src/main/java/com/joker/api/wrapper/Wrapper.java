package com.joker.api.wrapper;

import android.app.Activity;

/**
 * Created by joker on 2017/8/5.
 */

public interface Wrapper extends ListenerWrapper, AnnotationWrapper {
    /**
     * get activity
     * @return
     */
    Activity getActivity();
}
