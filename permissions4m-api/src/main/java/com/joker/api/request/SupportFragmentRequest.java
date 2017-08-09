package com.joker.api.request;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.joker.api.PermissionsProxy;

/**
 * Created by joker on 2017/8/3.
 */

public class SupportFragmentRequest implements Requestable {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    @SuppressWarnings("unchecked")
    public void requestPermission(Object object, String permission, int requestCode, PermissionsProxy
            instance) {
        if (((android.support.v4.app.Fragment) object).shouldShowRequestPermissionRationale
                (permission)) {
            if (!instance.customRationale(object, requestCode)) {
                instance.rationale(object, requestCode);
                ((android.support.v4.app.Fragment) object).requestPermissions(new
                        String[]{permission}, requestCode);
            }
        } else {
            ((android.support.v4.app.Fragment) object).requestPermissions(new String[]{permission},
                    requestCode);
        }
    }
}
