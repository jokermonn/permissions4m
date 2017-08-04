package com.joker.api.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.joker.api.util.manufacturer.HUAWEI;
import com.joker.api.util.manufacturer.OPPO;
import com.joker.api.util.manufacturer.PermissionsPage;
import com.joker.api.util.manufacturer.Protogenesis;
import com.joker.api.util.manufacturer.XIAOMI;

/**
 * Created by joker on 2017/8/4.
 */

public class PermissionsSettingContext {
    /**
     * Build.MANUFACTURER
     */
    private static final String MANUFACTURER_HUAWEI = "HUAWEI"; // 华为
    private static final String MANUFACTURER_XIAOMI = "XIAOMI"; // 小米
    private static final String MANUFACTURER_OPPO = "OPPO";     // oppo
    private static final String MANUFACTURER_VIVO = "vivo";     // vivo
    private static final String manufacturer = Build.MANUFACTURER;

    public static Intent getIntent(Context context, boolean androidSetting) {
        PermissionsPage permissionsPage = new Protogenesis();
        try {
            if (MANUFACTURER_HUAWEI.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new HUAWEI(context);
            } else if (MANUFACTURER_OPPO.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new OPPO(context);
            } else if (MANUFACTURER_VIVO.equalsIgnoreCase(manufacturer)) {
//                permissionsPage = new VIVO(context);
                permissionsPage = new Protogenesis();
            } else if (MANUFACTURER_XIAOMI.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new XIAOMI(context);
            }

            return permissionsPage.settingIntent(androidSetting);
        } catch (Exception e) {
            Log.e("Permissions4M", "手机品牌为：" + manufacturer + "异常抛出，：" + e.getMessage());
            permissionsPage = new Protogenesis();
            return ((Protogenesis) permissionsPage).settingIntent(androidSetting);
        }
    }

    public static Intent getIntent() {
        return new Protogenesis().settingIntent(true);
    }
}
