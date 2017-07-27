package com.joker.api;

import android.annotation.TargetApi;
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

    public static void requestPermission(Activity activity, String permission, int requestCode) {
        init(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        request(activity, permission, requestCode);
    }

    public static void requestPermission(android.app.Fragment fragment, String permission, int
            requestCode) {
        init(fragment);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        request(fragment, permission, requestCode);
    }

    public static void requestPermission(android.support.v4.app.Fragment fragment, String permission, int
            requestCode) {
        init(fragment);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        request(fragment, permission, requestCode);
    }

    public static void requestPermissionOnCustomRationale(Activity activity, String[]
            permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissionOnCustomRationale(android.app.Fragment fragment, String[]
            permissions, int requestCode) {
        fragment.requestPermissions(permissions, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissionOnCustomRationale(android.support.v4.app.Fragment fragment, String[]
            permissions, int requestCode) {
        fragment.requestPermissions(permissions, requestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("unchecked")
    private static void request(Activity activity, String permission, int requestCode) {
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
    private static void request(android.app.Fragment fragment, String permission, int requestCode) {
        if (fragment.shouldShowRequestPermissionRationale(permission)) {
            if (!instance.customRationale(fragment, requestCode)) {
                instance.rationale(fragment, requestCode);
                fragment.requestPermissions(new String[]{permission}, requestCode);
            }
        } else {
            fragment.requestPermissions(new String[]{permission}, requestCode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("unchecked")
    private static void request(android.support.v4.app.Fragment fragment, String permission,
                                int requestCode) {
        if (fragment.shouldShowRequestPermissionRationale(permission)) {
            if (!instance.customRationale(fragment, requestCode)) {
                instance.rationale(fragment, requestCode);
                fragment.requestPermissions(new String[]{permission}, requestCode);
            }
        } else {
            fragment.requestPermissions(new String[]{permission}, requestCode);
        }
    }

    private static void init(Object object) {
        String name = object.getClass().getName();
        String proxyName = name + PERMISSIONS_PROXY;
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
