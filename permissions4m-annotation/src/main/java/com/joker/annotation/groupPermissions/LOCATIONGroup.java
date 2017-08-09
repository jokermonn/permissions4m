package com.joker.annotation.groupPermissions;

/**
 * Created by joker on 2017/8/5.
 */

public class LOCATIONGroup implements PermissionsGroup {
    private final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    private final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";

    @Override
    public String[] permissions() {
        return new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
    }
}
