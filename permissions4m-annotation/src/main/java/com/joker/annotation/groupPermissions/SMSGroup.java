package com.joker.annotation.groupPermissions;

/**
 * Created by joker on 2017/8/5.
 */

public class SMSGroup implements PermissionsGroup {
    private final String READ_SMS = "android.permission.READ_SMS";
    private final String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";
    private final String RECEIVE_MMS = "android.permission.RECEIVE_MMS";
    private final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    private final String SEND_SMS = "android.permission.SEND_SMS";
    private final String READ_CELL_BROADCASTS = "android.permission.READ_CELL_BROADCASTS";

    @Override
    public String[] permissions() {
        return new String[]{READ_SMS, RECEIVE_WAP_PUSH, RECEIVE_MMS, SEND_SMS, RECEIVE_SMS,
                READ_CELL_BROADCASTS};
    }
}
