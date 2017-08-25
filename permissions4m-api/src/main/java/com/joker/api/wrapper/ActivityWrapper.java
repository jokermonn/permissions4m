package com.joker.api.wrapper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.joker.api.PermissionsProxy;
import com.joker.api.apply.ForceApplyPermissions;
import com.joker.api.apply.NormalApplyPermissions;

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
    void normalRequest() {
        activity.requestPermissions(new String[]{getPermission()}, getRequestCode());
    }

    @SuppressWarnings("unchecked")
    @Override
    void requestPermissionWithAnnotation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getProxy(getContext().getClass().getName()).granted(activity, getRequestCode());
        } else {
            String permission = getPermission();
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager
                    .PERMISSION_GRANTED) {
                tryRequestWithAnnotation();
            } else {
                // force
                mayGrantedWithAnnotation();
            }
        }
    }

    @Override
    void requestPermissionWithListener() {
        String permission = getPermission();
        int requestCode = getRequestCode();

        // denied
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager
                .PERMISSION_GRANTED) {
            tryRequestWithListener(permission, requestCode);
        } else {
            // may granted
            mayGrantedWithListener();
        }
    }

    @SuppressWarnings("unchecked")
    private void tryRequestWithAnnotation() {
        if ((getActivity()).shouldShowRequestPermissionRationale(getPermission())) {
            if (!getProxy(getContext().getClass().getName()).customRationale(getActivity(),
                    getRequestCode())) {
                getProxy(getContext().getClass().getName()).rationale(getActivity(), getRequestCode());
                ActivityCompat.requestPermissions(getActivity(), new String[]{getPermission()},
                        getRequestCode());
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{getPermission()},
                    getRequestCode());
        }
    }

    @SuppressWarnings("unchecked")
    private void mayGrantedWithAnnotation() {
        if (isRequestForce()) {
            // ensure granted
            ForceApplyPermissions.grantedOnResultWithAnnotation(this);
        } else {
            NormalApplyPermissions.grantedWithAnnotation(this);
        }
    }

    private void tryRequestWithListener(String permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            PermissionRequestListener requestListener = getPermissionRequestListener();
            if (requestListener != null) {
                requestListener.permissionRationale();
            }
        }
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    private void mayGrantedWithListener() {
        if (isRequestForce()) {
            ForceApplyPermissions.grantedOnResultWithListener(this);
        } else {
            NormalApplyPermissions.grantedWithListener(this);
        }
    }

    @Override
    public Activity getActivity() {
        return (Activity) getContext();
    }
}
