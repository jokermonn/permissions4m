package com.joker.api.request;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.joker.api.PermissionsProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joker on 2017/8/3.
 */

public class ActivityRequest implements Requestable {
    private static Map<PermissionsProxy, ActivityRequest> map = new HashMap<>();
    private final PermissionsProxy instance;

    private ActivityRequest(PermissionsProxy instance) {
        this.instance = instance;
    }

    public static ActivityRequest getInstance(PermissionsProxy instance) {
        ActivityRequest request = map.get(instance);
        if (request == null) {
            request = new ActivityRequest(instance);
            map.put(instance, request);
        }

        return request;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("unchecked")
    @Override
    public void request(Object object, String permission, int requestCode) {
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
