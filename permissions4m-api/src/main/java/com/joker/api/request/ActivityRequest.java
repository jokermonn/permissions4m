package com.joker.api.request;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.joker.api.PermissionsProxy;

/**
 * Created by joker on 2017/8/3.
 */

public class ActivityRequest implements Requestable {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("unchecked")
    @Override
    public void requestPermission(Object object, String permission, int requestCode, PermissionsProxy
            instance) {
        if (ContextCompat.checkSelfPermission((Activity) object, permission) != PackageManager
                .PERMISSION_GRANTED) {
            if (((Activity) object).shouldShowRequestPermissionRationale(permission)) {
                if (!instance.customRationale(object, requestCode)) {
                    instance.rationale(object, requestCode);
                    ActivityCompat.requestPermissions((Activity) object, new String[]{permission},
                            requestCode);
                }
            } else {
                ActivityCompat.requestPermissions((Activity) object, new String[]{permission},
                        requestCode);
            }
        } else {
            instance.granted(object, requestCode);
        }
    }
}
