package com.joker.api.wrapper;

import android.app.Activity;

/**
 * Created by joker on 2017/8/5.
 */

public class FragmentWrapper extends FragmentBaseWrapper implements Wrapper {
    private final android.app.Fragment fragment;

    public FragmentWrapper(android.app.Fragment fragment) {
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
        return ((android.app.Fragment) getContext()).getActivity();
    }
}
