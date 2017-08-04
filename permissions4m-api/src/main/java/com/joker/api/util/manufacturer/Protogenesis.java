package com.joker.api.util.manufacturer;

import android.content.Intent;
import android.provider.Settings;

/**
 * Created by joker on 2017/8/4.
 */

public class Protogenesis implements PermissionsPage {
    // system setting page
    @Override
    public Intent settingIntent(boolean androidSetting) {
        return new Intent(Settings.ACTION_SETTINGS);
    }
}
