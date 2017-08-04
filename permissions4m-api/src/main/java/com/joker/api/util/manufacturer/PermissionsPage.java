package com.joker.api.util.manufacturer;

import android.content.Intent;

/**
 * Created by joker on 2017/8/4.
 */

public interface PermissionsPage {
    String PACK_TAG = "packageName";

    // normally, ActivityNotFoundException
    Intent settingIntent(boolean androidSetting) throws Exception;
}
