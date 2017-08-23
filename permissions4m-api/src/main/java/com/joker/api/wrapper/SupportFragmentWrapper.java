package com.joker.api.wrapper;

import android.support.v4.app.Fragment;

/**
 * Created by joker on 2017/8/5.
 */

public class SupportFragmentWrapper extends FragmentBaseWrapper implements Wrapper {
    private final Fragment fragment;

    public SupportFragmentWrapper(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public Object getContext() {
        return fragment;
    }

    @Override
    public void requestSync() {
        requestSync(fragment);
    }
}
