package com.joker.annotation.groupPermissions;

/**
 * Created by joker on 2017/8/5.
 */

public class CONTACTSGroup implements PermissionsGroup {
    private final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    private final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";
    private final String READ_CONTACTS = "android.permission.READ_CONTACTS";

    @Override
    public String[] permissions() {
        return new String[]{WRITE_CONTACTS, GET_ACCOUNTS, READ_CONTACTS};
    }
}
