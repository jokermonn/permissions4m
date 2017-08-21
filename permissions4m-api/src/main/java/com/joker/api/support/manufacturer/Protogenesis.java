package com.joker.api.support.manufacturer;

import android.content.Intent;
import android.provider.Settings;

/**
 * Created by joker on 2017/8/4.
 */

public class Protogenesis implements PermissionsPage {
    // system details setting page
    @Override
    public Intent settingIntent() {
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    }
}
