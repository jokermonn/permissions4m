package com.joker.api.request;

import com.joker.api.PermissionsProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joker on 2017/8/3.
 */

public class SupportFragmentRequest implements Requestable {
    private static Map<PermissionsProxy, SupportFragmentRequest> map = new HashMap<>();
    private final PermissionsProxy instance;

    private SupportFragmentRequest(PermissionsProxy instance) {
        this.instance = instance;
    }

    public static SupportFragmentRequest getInstance(PermissionsProxy instance) {
        SupportFragmentRequest request = map.get(instance);
        if (request == null) {
            request = new SupportFragmentRequest(instance);
            map.put(instance, request);
        }

        return request;
    }

    @SuppressWarnings("unchecked")
    @Override

    public void request(Object object, String permission, int requestCode) {
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
