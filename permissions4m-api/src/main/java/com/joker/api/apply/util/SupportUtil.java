package com.joker.api.apply.util;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;

import com.joker.api.wrapper.Wrapper;

/**
 * Created by joker on 2017/8/19.
 */

public class SupportUtil {
    public static boolean pageListenerNonNull(Wrapper wrapper) {
        return wrapper.getPermissionPageListener() != null;
    }

    public static boolean nonShowRationale(Wrapper wrapper) {
        return !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(wrapper), wrapper
                .getPermission());
    }

    private static Activity getActivity(Wrapper wrapper) {
        Activity activity;
        if (wrapper.getContext() instanceof android.app.Fragment) {
            activity = ((android.app.Fragment) wrapper.getContext()).getActivity();
        } else if (wrapper.getContext() instanceof android.support.v4.app.Fragment) {
            activity = ((android.support.v4.app.Fragment) wrapper.getContext()).getActivity();
        } else {
            activity = (Activity) wrapper.getContext();
        }

        return activity;
    }
}
