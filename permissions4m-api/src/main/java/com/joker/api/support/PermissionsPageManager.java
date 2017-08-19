package com.joker.api.support;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.joker.api.support.manufacturer.HUAWEI;
import com.joker.api.support.manufacturer.OPPO;
import com.joker.api.support.manufacturer.PermissionsPage;
import com.joker.api.support.manufacturer.Protogenesis;
import com.joker.api.support.manufacturer.VIVO;
import com.joker.api.support.manufacturer.XIAOMI;

/**
 * Created by joker on 2017/8/4.
 */

public class PermissionsPageManager {
    /**
     * Build.MANUFACTURER
     */
    public static final String MANUFACTURER_HUAWEI = "HUAWEI"; // 华为
    public static final String MANUFACTURER_XIAOMI = "XIAOMI"; // 小米
    public static final String MANUFACTURER_OPPO = "OPPO";     // oppo
    public static final String MANUFACTURER_VIVO = "vivo";     // vivo
    public static final String manufacturer = Build.MANUFACTURER;

    public static String getManufacturer() {
        return manufacturer;
    }

    public static Intent getIntent(Activity activity) {
        PermissionsPage permissionsPage = new Protogenesis();
        try {
            if (MANUFACTURER_HUAWEI.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new HUAWEI(activity);
            } else if (MANUFACTURER_OPPO.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new OPPO(activity);
            } else if (MANUFACTURER_VIVO.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new VIVO(activity);
            } else if (MANUFACTURER_XIAOMI.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new XIAOMI(activity);
            }

            return permissionsPage.settingIntent();
        } catch (Exception e) {
            Log.e("Permissions4M", "手机品牌为：" + manufacturer + "异常抛出，：" + e.getMessage());
            permissionsPage = new Protogenesis();
            return ((Protogenesis) permissionsPage).settingIntent();
        }
    }

    public static Intent getIntent() {
        return new Protogenesis().settingIntent();
    }

    /**
     * special manufacturer, can only request permissions once by default
     * like {@link XIAOMI}, {@link OPPO}, and there is no rationale
     *
     * @return true if special
     */
    public static boolean isNonRationaleManufacturer() {
        return getManufacturer().equalsIgnoreCase(MANUFACTURER_XIAOMI) || getManufacturer()
                .equalsIgnoreCase(MANUFACTURER_OPPO);
    }

    /**
     * whether is xiaomi
     * @return true if it is
     */
    public static boolean isXiaoMi() {
        return getManufacturer().equalsIgnoreCase(MANUFACTURER_XIAOMI);
    }
}
