package com.joker.api.wrapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

/**
 * Created by joker on 2017/8/5.
 */

public class ActivityWrapper extends AbstractWrapper implements Wrapper {
    private final Activity activity;

    public ActivityWrapper(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Object getContext() {
        return activity;
    }

    @Override
    public void requestSync() {
        requestSync(activity);
    }

    @Override
    void originalRequest() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{getRequestPermission()},
                getRequestCode());
    }

    @SuppressWarnings("unchecked")
    @SuppressLint("NewApi")
    void tryRequestWithAnnotation() {
        if ((getActivity()).shouldShowRequestPermissionRationale(getRequestPermission())) {
            if (!getProxy(getContext().getClass().getName()).customRationale(getActivity(),
                    getRequestCode())) {
                getProxy(getContext().getClass().getName()).rationale(getActivity(), getRequestCode());
                ActivityCompat.requestPermissions(getActivity(), new String[]{getRequestPermission()},
                        getRequestCode());
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{getRequestPermission()},
                    getRequestCode());
        }
    }

    @SuppressWarnings("unchecked")
    void tryRequestWithListener() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, getRequestPermission())) {
            PermissionRequestListener requestListener = getPermissionRequestListener();
            if (requestListener != null) {
                requestListener.permissionRationale(getRequestCode());
            }
        }
        ActivityCompat.requestPermissions(activity, new String[]{getRequestPermission()}, getRequestCode());
    }

    @Override
    public Activity getActivity() {
        return (Activity) getContext();
    }
}
