package com.joker.api.request;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.joker.api.PermissionsProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joker on 2017/8/3.
 */

public class FragmentRequest implements Requestable {
    private static Map<PermissionsProxy, FragmentRequest> map = new HashMap<>();
    private final PermissionsProxy instance;

    private FragmentRequest(PermissionsProxy instance) {
        this.instance = instance;
    }

    public static FragmentRequest getInstance(PermissionsProxy instance) {
        FragmentRequest request = map.get(instance);
        if (request == null) {
            request = new FragmentRequest(instance);
            map.put(instance, request);
        }

        return request;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("unchecked")
    @Override
    public void request(Object object, String permission, int requestCode) {
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
