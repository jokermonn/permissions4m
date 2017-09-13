package com.joker.api;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.joker.api.apply.ForceApplyPermissions;
import com.joker.api.apply.NormalApplyPermissions;
import com.joker.api.wrapper.AbstractWrapper;
import com.joker.api.wrapper.ActivityWrapper;
import com.joker.api.wrapper.FragmentWrapper;
import com.joker.api.wrapper.ListenerWrapper;
import com.joker.api.wrapper.PermissionWrapper;
import com.joker.api.wrapper.SupportFragmentWrapper;
import com.joker.api.wrapper.Wrapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

import static com.joker.api.Permissions4M.PageType.ANDROID_SETTING_PAGE;
import static com.joker.api.Permissions4M.PageType.MANAGER_PAGE;

/**
 * Created by joker on 2017/8/5.
 */

public class Permissions4M {
    public static Wrapper get(Activity activity) {
        return new ActivityWrapper(activity);
    }

    public static Wrapper get(android.app.Fragment fragment) {
        return new FragmentWrapper(fragment);
    }

    public static Wrapper get(android.support.v4.app.Fragment fragment) {
        return new SupportFragmentWrapper(fragment);
    }

    @SuppressWarnings("unchecked")
    /**
     * in {@link android.app.Activity}, once permission request only call this method once
     */
    public static void onRequestPermissionsResult(Activity activity, int
            requestCode, @NonNull int[] grantResults) {
        onPrivateRequestPermissionsResult(activity, requestCode, grantResults);
    }

    @SuppressWarnings("unchecked")
    /**
     * in {@link android.app.Fragment}, once permission request only call this method once
     */
    public static void onRequestPermissionsResult(android.app.Fragment fragment, int
            requestCode, @NonNull int[] grantResults) {
        onPrivateRequestPermissionsResult(fragment, requestCode, grantResults);
    }

    @SuppressWarnings("unchecked")
    /**
     * in {@link android.support.v4.app.android.support.v4.app.Fragment}, once permission request
     * will call this method **twice**
     */
    public static void onRequestPermissionsResult(android.support.v4.app.Fragment fragment, int
            requestCode, @NonNull int[] grantResults) {
        onPrivateRequestPermissionsResult(fragment, requestCode, grantResults);
    }

    private static void onPrivateRequestPermissionsResult(Object object, int
            requestCode, @NonNull int[] grantResults) {
        AbstractWrapper.Key key = new AbstractWrapper.Key(object, requestCode);
        WeakReference<PermissionWrapper> reference = AbstractWrapper.getWrapperMap().get(key);
        // because SupportFragment request permissions will call Activity callback first
        // and then call SupportFragment callback
        // and the first time will throw NullPointerException
        if (reference == null) {
            return;
        }
        PermissionWrapper wrapper = reference.get();
        // because SupportFragment request permissions will call Activity callback first
        // and then call SupportFragment callback
        // and the first time will throw NullPointerException
        if (wrapper == null) {
            return;
        }
        ListenerWrapper.PermissionRequestListener requestListener = wrapper
                .getPermissionRequestListener();
        // listener callback
        if (requestListener != null) {
            listenerCallback(grantResults, wrapper);
        } else {
            // annotation callback
            annotationCallback(grantResults, wrapper);
        }
    }

    private static void annotationCallback(@NonNull int[] grantResults, PermissionWrapper wrapper) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (wrapper.isRequestForce()) {
                ForceApplyPermissions.grantedWithAnnotation(wrapper);
            } else {
                NormalApplyPermissions.grantedWithAnnotation(wrapper);
            }
        } else {
            NormalApplyPermissions.deniedWithAnnotation(wrapper);
        }
    }

    private static void listenerCallback(@NonNull int[] grantResults, PermissionWrapper wrapper) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (wrapper.isRequestForce()) {
                ForceApplyPermissions.grantedWithListener(wrapper);
            } else {
                NormalApplyPermissions.grantedWithListener(wrapper);
            }
        } else {
            NormalApplyPermissions.deniedWithListener(wrapper);
        }
    }

    public static void requestPermission(Activity activity, String permission, int requestCode) {
        new ActivityWrapper(activity)
                .requestPermissions(permission)
                .requestCodes(requestCode)
                .request();
    }

    public static void requestPermission(android.app.Fragment fragment, String
            permission, int requestCode) {
        new FragmentWrapper(fragment)
                .requestPermissions(permission)
                .requestCodes(requestCode)
                .request();
    }

    public static void requestPermission(android.support.v4.app.Fragment fragment, String permission, int
            requestCode) {
        new SupportFragmentWrapper(fragment)
                .requestPermissions(permission)
                .requestCodes(requestCode)
                .request();
    }

    @IntDef({MANAGER_PAGE, ANDROID_SETTING_PAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PageType {
        int MANAGER_PAGE = 1;
        int ANDROID_SETTING_PAGE = 0;
    }
}
