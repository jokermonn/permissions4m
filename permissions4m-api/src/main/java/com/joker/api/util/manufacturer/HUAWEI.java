package com.joker.api.util.manufacturer;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * support:
 * 1.mate7 android:6.0/emui 4.0.1
 * 2.畅享7 android:7.0/emui 5.1
 * <p>
 * manager permissions page, permissions manage page, or {@link Protogenesis#settingIntent(boolean)}
 * <p>
 * Created by joker on 2017/8/4.
 */

public class HUAWEI implements PermissionsPage {
    private final Context context;
    private final String PKG = "com.huawei.systemmanager";
    private final String MANAGER_OUT_CLS = "com.huawei.permissionmanager.ui.MainActivity";
//    private final String SINGLE_CLS = "com.huawei.permissionmanager.ui.SingleAppActivity";
//    private final String SINGLE_TAG = "SingleAppActivity";

    public HUAWEI(Context context) {
        this.context = context;
    }

    @Override
    public Intent settingIntent(boolean androidSetting) throws ActivityNotFoundException {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PACK_TAG, context.getPackageName());
        ComponentName comp = new ComponentName(PKG, MANAGER_OUT_CLS);

        // need "com.huawei.systemmanager.permission.ACCESS_INTERFACE" permission
//        try {
//            PackageInfo pi = context.getPackageManager().getPackageInfo(PKG,
//                    PackageManager.GET_ACTIVITIES);
//            for (ActivityInfo activityInfo : pi.activities) {
//                if (activityInfo.name.contains(SINGLE_TAG)) {
//                    comp = new ComponentName(PKG, SINGLE_CLS);
//                }
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }

        intent.setComponent(comp);

        return intent;
    }
}
