package com.joker.api.stream.wrapper;

import android.app.Fragment;

/**
 * Created by joker on 2017/8/5.
 */

public class FragmentWrapper extends AbstractWrapper implements Wrapper {
    private final Fragment fragment;

    public FragmentWrapper(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void request() {

    }
}
