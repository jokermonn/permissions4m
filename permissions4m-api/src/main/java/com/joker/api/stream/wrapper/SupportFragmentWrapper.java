package com.joker.api.stream.wrapper;

import android.support.v4.app.Fragment;

/**
 * Created by joker on 2017/8/5.
 */

public class SupportFragmentWrapper extends AbstractWrapper implements Wrapper {
    private final Fragment fragment;

    public SupportFragmentWrapper(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void request() {

    }
}
