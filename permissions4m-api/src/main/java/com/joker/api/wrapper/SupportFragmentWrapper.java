package com.joker.api.wrapper;

import android.app.Activity;

/**
 * Created by joker on 2017/8/5.
 */

public class SupportFragmentWrapper extends FragmentBaseWrapper implements Wrapper {
    private final android.support.v4.app.Fragment fragment;

    public SupportFragmentWrapper(android.support.v4.app.Fragment fragment) {
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

    @Override
    public Activity getActivity() {
        return ((android.support.v4.app.Fragment) getContext()).getActivity();
    }
}
