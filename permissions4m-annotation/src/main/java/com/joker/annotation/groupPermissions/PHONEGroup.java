package com.joker.annotation.groupPermissions;

/**
 * Created by joker on 2017/8/5.
 */

public class PHONEGroup implements PermissionsGroup {
    private final String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    private final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    private final String CALL_PHONE = "android.permission.CALL_PHONE";
    private final String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";
    private final String USE_SIP = "android.permission.USE_SIP";
    private final String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";
    private final String ADD_VOICEMAIL = "com.android.voicemail.permission.ADD_VOICEMAIL";

    @Override
    public String[] permissions() {
        return new String[]{READ_CALL_LOG, READ_PHONE_STATE, CALL_PHONE, WRITE_CALL_LOG, USE_SIP,
                PROCESS_OUTGOING_CALLS, ADD_VOICEMAIL};
    }
}
