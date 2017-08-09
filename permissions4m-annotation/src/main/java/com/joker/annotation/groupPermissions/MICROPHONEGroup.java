package com.joker.annotation.groupPermissions;

/**
 * Created by joker on 2017/8/5.
 */

public class MICROPHONEGroup implements PermissionsGroup {
    private final String  RECORD_AUDIO= "android.permission.RECORD_AUDIO";
    @Override
    public String[] permissions() {
        return new String[]{RECORD_AUDIO};
    }
}
