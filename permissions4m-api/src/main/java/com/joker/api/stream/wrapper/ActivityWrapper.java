package com.joker.api.stream.wrapper;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.joker.api.support.PermissionsPageManager;

/**
 * Created by joker on 2017/8/5.
 */

public class ActivityWrapper extends AbstractWrapper implements Wrapper {
    private final Activity activity;

    public ActivityWrapper(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void request() {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            Log.e("TAG", "request: 1 ");
            // show rationale
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                Log.e("TAG", "request: 2 ");
                if (permissionRequestListener != null) {
                    Log.e("TAG", "request: 3 ");
                    permissionRequestListener.permissionRationale();
                }
            }
            // 申请权限
            ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
        } else {
            // first time
            if (isRequestForce()) {
                Log.e("TAG", "request: 4 ");
                applyPermissionForGranted(activity, permission);
            } else {
                Log.e("TAG", "request: 5 ");
                // not first time
                if (permissionRequestListener != null) {
                    permissionRequestListener.permissionGranted();
                }
            }
        }
    }

    private void applyPermissionForGranted(Activity activity, String permission) {
        if (permission.equals(Manifest.permission.READ_CONTACTS)) {
            Wrapper.PermissionRequestListener listener = permissionRequestListener;
            if (listener == null) return;
            try {
                ContentResolver resolver = activity.getContentResolver();
                // 获取手机联系人
                Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        null, null, null);
                if (phoneCursor != null) {
                    listener.permissionGranted();
                    phoneCursor.close();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) &&
                            getPermissionPageListener() != null) {
                        permissionPageListener.pageIntent(PermissionsPageManager.getIntent());
                    }
                    listener.permissionDenied();
                }
            } catch (Exception e) {
                listener.permissionDenied();
            }
        }
    }
}
