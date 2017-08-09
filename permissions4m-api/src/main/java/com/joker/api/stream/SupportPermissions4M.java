package com.joker.api.stream;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.joker.api.stream.apply.ForceApplyPermissions;
import com.joker.api.stream.wrapper.ActivityWrapper;
import com.joker.api.stream.wrapper.FragmentWrapper;
import com.joker.api.stream.wrapper.SupportFragmentWrapper;
import com.joker.api.stream.wrapper.Wrapper;
import com.joker.api.support.PermissionsPageManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

import static com.joker.api.stream.SupportPermissions4M.PageType.ANDROID_SETTING_PAGE;
import static com.joker.api.stream.SupportPermissions4M.PageType.MANAGER_PAGE;

/**
 * Created by joker on 2017/8/5.
 */

public class SupportPermissions4M {
    private static Map<Object, Wrapper> map = new HashMap<>();

    public static Wrapper get(Activity activity) {
        if (map.containsKey(activity)) {
            return map.get(activity);
        } else {
            ActivityWrapper wrapper = new ActivityWrapper(activity);
            map.put(activity, wrapper);
            return wrapper;
        }
    }

    public static Wrapper get(android.app.Fragment fragment) {
        if (map.containsKey(fragment)) {
            return map.get(fragment);
        } else {
            FragmentWrapper wrapper = new FragmentWrapper(fragment);
            map.put(fragment, wrapper);
            return wrapper;
        }
    }

    public static Wrapper get(android.support.v4.app.Fragment fragment) {
        if (map.containsKey(fragment)) {
            return map.get(fragment);
        } else {
            SupportFragmentWrapper wrapper = new SupportFragmentWrapper(fragment);
            map.put(fragment, wrapper);
            return wrapper;
        }
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull int[]
            grantResults) {
        Wrapper wrapper = map.get(activity);
        Wrapper.PermissionRequestListener requestListener = wrapper
                .getPermissionRequestListener();
        if (requestListener == null) return;

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (wrapper.isRequestForce()) {
                ForceApplyPermissions.grantedOnRequestPermissionsResult(activity, wrapper, requestCode);
            } else {
                requestListener.permissionGranted();
            }
        } else {
            whetherShowIntent(activity, wrapper);
            requestListener.permissionDenied();
        }
    }

    private static void whetherShowIntent(Activity activity, Wrapper wrapper) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, wrapper
                .getPermission()) &&
                wrapper.getPermissionPageListener() != null && !PermissionsPageManager
                .getManufacturer().equals(PermissionsPageManager.MANUFACTURER_XIAOMI)) {
            wrapper.getPermissionPageListener().pageIntent(PermissionsPageManager.getIntent());
        }
    }

    @IntDef({MANAGER_PAGE, ANDROID_SETTING_PAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PageType {
        int MANAGER_PAGE = 0;
        int ANDROID_SETTING_PAGE = 1;
    }
}
