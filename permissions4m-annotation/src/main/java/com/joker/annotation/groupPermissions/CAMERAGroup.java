package com.joker.annotation.groupPermissions;

/**
 * Created by joker on 2017/8/5.
 */

public class CAMERAGroup implements PermissionsGroup {
    private final String CAMERA = "android.permission.CAMERA";

    @Override
    public String[] permissions() {
        return new String[]{CAMERA};
    }
}
