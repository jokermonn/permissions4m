package com.joker.api.support.manufacturer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

/**
 * Created by joker on 2017/8/24.
 */

public class MEIZU implements PermissionsPage {
    private final Activity activity;
    private final String TWO = "com.meizu.safe.permission.PermissionMainActivity";
    private final String PKG = "com.meizu.safe";

    public MEIZU(Activity activity) {
        this.activity =activity;
    }

    @Override
    public Intent settingIntent() throws Exception {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PACK_TAG, activity.getPackageName());
        ComponentName comp = new ComponentName(PKG, TWO);
        intent.setComponent(comp);

        return intent;
    }
}
