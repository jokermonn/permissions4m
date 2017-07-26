package com.joker.api;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joker on 2017/7/26.
 */

public class Permissions4M {
    private static PermissionsProxy instance;

    @SuppressWarnings("unchecked")
    public static void requestPermission(Activity activity, String permission, int requestCode) {
        init(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                instance.rationale(activity, requestCode);
            }
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    requestCode);
        } else {
            instance.granted(activity, requestCode);
        }
    }

    private static void init(Activity activity) {
        String name = activity.getClass().getName();
        String proxyName = name + "$$" + "PermissionsProxy";
        try {
            instance = (PermissionsProxy) Class.forName(proxyName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[]
            permissions,
                                                  @NonNull int[]
                                                          grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (deniedPermissions.size() > 0) {
            instance.denied(activity, requestCode);
        } else {
            instance.granted(activity, requestCode);
        }
    }
}
