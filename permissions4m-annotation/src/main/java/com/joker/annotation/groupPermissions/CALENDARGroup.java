package com.joker.annotation.groupPermissions;

/**
 * Created by joker on 2017/8/5.
 */

public class CALENDARGroup implements PermissionsGroup {
    private final String READ_CALENDAR = "android.permission.READ_CALENDAR";
    private final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";

    @Override
    public String[] permissions() {
        return new String[]{READ_CALENDAR, WRITE_CALENDAR};
    }
}
