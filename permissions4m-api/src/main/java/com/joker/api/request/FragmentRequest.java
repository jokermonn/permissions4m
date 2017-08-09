package com.joker.api.request;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.joker.api.PermissionsProxy;

/**
 * Created by joker on 2017/8/3.
 */

public class FragmentRequest implements Requestable {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("unchecked")
    @Override
    public void requestPermission(Object object, String permission, int requestCode, PermissionsProxy
            instance) {
        if (((android.app.Fragment) object).shouldShowRequestPermissionRationale(permission)) {
            if (!instance.customRationale(object, requestCode)) {
                instance.rationale(object, requestCode);
                ((android.app.Fragment) object).requestPermissions(new String[]{permission},
                        requestCode);
            }
        } else {
            ((android.app.Fragment) object).requestPermissions(new String[]{permission}, requestCode);
        }
    }
}
