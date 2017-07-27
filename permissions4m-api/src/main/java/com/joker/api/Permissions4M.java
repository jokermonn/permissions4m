package com.joker.api;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by joker on 2017/7/26.
 */

public class Permissions4M {
    private static final String PERMISSIONS_PROXY = "$$PermissionsProxy";
    private static PermissionsProxy instance;

    @SuppressWarnings("unchecked")
    public static void requestPermission(Object object, String permission, int requestCode) {
        init(object);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (object instanceof Activity) {
            requestPermission((Activity) object, permission, requestCode);
        } else if (object instanceof android.support.v4.app.Fragment) {
            requestPermission((android.support.v4.app.Fragment) object, permission, requestCode);
        } else if (object instanceof android.app.Fragment) {
            requestPermission((android.app.Fragment) object, permission, requestCode);
        } else {
            throw new IllegalArgumentException(object.getClass().getName() + " is not supported!");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("unchecked")
    private static void requestPermission(Activity activity, String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                if (!instance.customRationale(activity, requestCode)) {
                    instance.rationale(activity, requestCode);
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        } else {
            instance.granted(activity, requestCode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("unchecked")
    private static void requestPermission(android.support.v4.app.Fragment fragment, String permission,
                                          int requestCode) {
        if (fragment.shouldShowRequestPermissionRationale(permission)) {
            instance.rationale(fragment, requestCode);
        }
        fragment.requestPermissions(new String[]{permission}, requestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("unchecked")
    private static void requestPermission(android.app.Fragment fragment, String permission, int
            requestCode) {
        if (fragment.shouldShowRequestPermissionRationale(permission)) {
            instance.rationale(fragment, requestCode);
        }
        fragment.requestPermissions(new String[]{permission}, requestCode);
    }

    private static void init(Object object) {
        String name = object.getClass().getName();
        String proxyName = name + PERMISSIONS_PROXY;
        try {
            instance = (PermissionsProxy) Class.forName(proxyName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("can not compile %s , something wrong when compiler" +
                    ".", object
                    .getClass().getSimpleName() + PERMISSIONS_PROXY));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("can not compile %s , something wrong when compiler" +
                    ".", object
                    .getClass().getSimpleName() + PERMISSIONS_PROXY));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("can not compile %s , something wrong when compiler" +
                    ".", object
                    .getClass().getSimpleName() + PERMISSIONS_PROXY));
        }
    }

    @SuppressWarnings("unchecked")
    public static void onRequestPermissionsResult(Object object, int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            instance.granted(object, requestCode);
        } else {
            instance.denied(object, requestCode);
        }
    }
}
