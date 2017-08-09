package com.joker.annotation.groupPermissions;

/**
 * Created by joker on 2017/8/5.
 */

public class SENSORSGroup implements PermissionsGroup {
    private final String BODY_SENSORS = "android.permission.BODY_SENSORS";

    @Override
    public String[] permissions() {
        return new String[]{BODY_SENSORS};
    }
}
