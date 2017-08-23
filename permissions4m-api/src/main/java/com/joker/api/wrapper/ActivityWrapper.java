package com.joker.api.wrapper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.joker.api.Permissions4M;
import com.joker.api.apply.ForceApplyPermissions;
import com.joker.api.apply.NormalApplyPermissions;
import com.joker.api.apply.util.SupportUtil;
import com.joker.api.support.PermissionsPageManager;

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
            proxy.granted(getContext(), getRequestCode());
        } else {
            initProxy(activity);

            String permission = getPermission();
            int requestCode = getRequestCode();
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager
                    .PERMISSION_GRANTED) {
                if ((activity).shouldShowRequestPermissionRationale(permission)) {
                    if (!proxy.customRationale(activity, requestCode)) {
                        proxy.rationale(activity, requestCode);
                        ActivityCompat.requestPermissions(activity, new String[]{permission},
                                requestCode);
                    }
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{permission},
                            requestCode);
                }
            } else {
                // force
                if (isRequestForce()) {
                    // ensure granted
                    ForceApplyPermissions.grantedOnResultWithAnnotation(this);
                } else {
                    proxy.granted(activity, requestCode);
                }
            }
        }
    }

    @Override
    void requestPermissionWithListener() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (getPermissionRequestListener() != null) {
                getPermissionRequestListener().permissionGranted();
            }
        } else {
            String permission = getPermission();
            int requestCode = getRequestCode();
            Wrapper.PermissionRequestListener requestListener = getPermissionRequestListener();
            if (requestListener == null) {
                return;
            }

            // denied
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager
                    .PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    requestListener.permissionRationale();
                }
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            } else {
                // granted
                if (isRequestForce()) {
                    ForceApplyPermissions.grantedOnResultWithListener(this);
                } else {
                    NormalApplyPermissions.grantedWithListener(this);
                }
            }
        }
    }
}
