package com.joker.api.support;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.joker.api.support.manufacturer.HUAWEI;
import com.joker.api.support.manufacturer.MEIZU;
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
    private static final String MANUFACTURER_HUAWEI = "HUAWEI";
    private static final String MANUFACTURER_XIAOMI = "XIAOMI";
    private static final String MANUFACTURER_OPPO = "OPPO";
    private static final String MANUFACTURER_VIVO = "vivo";
    private static final String MANUFACTURER_MEIZU = "meizu";
    private static final String manufacturer = Build.MANUFACTURER;

    public static String getManufacturer() {
        return manufacturer;
    }

    public static Intent getIntent(Activity activity) {
        PermissionsPage permissionsPage = new Protogenesis(activity);
        try {
            if (MANUFACTURER_HUAWEI.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new HUAWEI(activity);
            } else if (MANUFACTURER_OPPO.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new OPPO(activity);
            } else if (MANUFACTURER_VIVO.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new VIVO(activity);
            } else if (MANUFACTURER_XIAOMI.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new XIAOMI(activity);
            } else if (MANUFACTURER_MEIZU.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new MEIZU(activity);
            }

            return permissionsPage.settingIntent();
        } catch (Exception e) {
            Log.e("Permissions4M", "手机品牌为：" + manufacturer + "异常抛出，：" + e.getMessage());
            permissionsPage = new Protogenesis(activity);
            return ((Protogenesis) permissionsPage).settingIntent();
        }
    }

    public static Intent getSettingIntent(Activity activity) {
        return new Protogenesis(activity).settingIntent();
    }

    public static boolean isXIAOMI() {
        return getManufacturer().equalsIgnoreCase(MANUFACTURER_XIAOMI);
    }

    public static boolean isOPPO() {
        return getManufacturer().equalsIgnoreCase(MANUFACTURER_OPPO);
    }

    public static boolean isMEIZU() {
        return getManufacturer().equalsIgnoreCase(MANUFACTURER_MEIZU);
    }

    /**
     * those manufacturer that need request by some special measures, above
     * {@link android.os.Build.VERSION_CODES#M}
     *
     * @return
     */
    public static boolean isForceManufacturer() {
        return (getManufacturer().equalsIgnoreCase(MANUFACTURER_MEIZU) || getManufacturer()
                .equalsIgnoreCase(MANUFACTURER_XIAOMI));
    }

    /**
     * those manufacturer that need request permissions under {@link android.os.Build.VERSION_CODES#M},
     * above {@link android.os.Build.VERSION_CODES#LOLLIPOP}
     *
     * @return
     */
    public static boolean isUnderMHasPermissionRequestManufacturer() {
        return (getManufacturer().equalsIgnoreCase(MANUFACTURER_MEIZU) || getManufacturer()
                .equalsIgnoreCase(MANUFACTURER_XIAOMI));
    }

    /**
     * 1.is under {@link android.os.Build.VERSION_CODES#M}, above
     * {@link android.os.Build.VERSION_CODES#LOLLIPOP}
     * 2.has permissions check
     * 3.open under check
     * <p>
     * now, we know {@link PermissionsPageManager#isXIAOMI()}, {@link PermissionsPageManager#isMEIZU()}
     *
     * @param isUnderMNeedChecked {@link com.joker.api.wrapper.Wrapper#requestUnderM(boolean)}
     * @return
     */
    public static boolean isUnderMNeedChecked(boolean isUnderMNeedChecked) {
        return isUnderMHasPermissionRequestManufacturer() && isUnderMNeedChecked &&
                isAndroidL();
    }

    /**
     * Build version code is under 6.0 but above 5.0
     *
     * @return
     */
    public static boolean isAndroidL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES
                .LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }
}
