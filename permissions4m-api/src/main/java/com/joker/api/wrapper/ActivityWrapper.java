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
        int requestCode = getRequestCode();
        if ((getActivity()).shouldShowRequestPermissionRationale(getRequestPermission())) {
            Object context = getContext();
            if (!getProxy(context.getClass().getName()).customRationale(getActivity(),
                    requestCode)) {
                getProxy(context.getClass().getName()).rationale(getActivity(), requestCode);
                ActivityCompat.requestPermissions(getActivity(), new String[]{getRequestPermission()},
                        requestCode);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{getRequestPermission()},
                    requestCode);
        }
    }

    @SuppressWarnings("unchecked")
    void tryRequestWithListener() {
        PermissionCustomRationaleListener customRationaleListener = getPermissionCustomRationaleListener();
        int requestCode = getRequestCode();

        if (customRationaleListener != null) {
            customRationaleListener.permissionCustomRationale(requestCode);
        } else {
            String requestPermission = getRequestPermission();
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                PermissionRequestListener requestListener = getPermissionRequestListener();
                if (requestListener != null) {
                    requestListener.permissionRationale(requestCode);
                }
            }
            ActivityCompat.requestPermissions(activity, new String[]{requestPermission},
                    requestCode);
        }
    }

    @Override
    public Activity getActivity() {
        return (Activity) getContext();
    }
}
