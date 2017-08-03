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

    // sync request ==============================================
    public static void syncRequestPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(activity);
        syncRequest(activity);
    }

    public static void syncRequestPermissions(android.app.Fragment fragment) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        } 
        initProxy(fragment);
        syncRequest(fragment);
    }

    public static void syncRequestPermissions(android.support.v4.app.Fragment fragment) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(fragment);
        syncRequest(fragment);
    }

    @SuppressWarnings("unchecked")
    private static void syncRequest(Object object) {
        if ((object instanceof Activity) || (object instanceof android.app.Fragment) || (object
                instanceof android.support.v4.app.Fragment)) {
            instance.startSyncRequestPermissionsMethod(object);
        } else {
            throw new IllegalArgumentException(object.getClass().getName() + " is not supported!");
        }
    }

    // normal request ===========================================================================
    public static void requestPermission(Activity activity, String permission, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(activity);
        request(activity, permission, requestCode);
    }

    public static void requestPermission(android.app.Fragment fragment, String permission, int
            requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(fragment);
        request(fragment, permission, requestCode);
    }

    public static void requestPermission(android.support.v4.app.Fragment fragment, String permission, int
            requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(fragment);
        request(fragment, permission, requestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("unchecked")
    private static void request(Object object, String permission, int requestCode) {
        if (object instanceof Activity) {
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
        } else if (object instanceof android.app.Fragment) {
            if (((android.app.Fragment) object).shouldShowRequestPermissionRationale(permission)) {
                if (!instance.customRationale(object, requestCode)) {
                    instance.rationale(object, requestCode);
                    ((android.app.Fragment) object).requestPermissions(new String[]{permission},
                            requestCode);
                }
            } else {
                ((android.app.Fragment) object).requestPermissions(new String[]{permission}, requestCode);
            }
        } else if (object instanceof android.support.v4.app.Fragment) {
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
        } else {
            throw new IllegalArgumentException(object.getClass().getName() + " is not supported!");
        }
    }

    // custom rationale ==================================================================================
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

    private static void initProxy(Object object) {
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
