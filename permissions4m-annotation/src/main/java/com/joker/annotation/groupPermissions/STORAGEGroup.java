package com.joker.annotation.groupPermissions;

/**
 * Created by joker on 2017/8/5.
 */

public class STORAGEGroup implements PermissionsGroup {
    private final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    private final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    @Override
    public String[] permissions() {
        return new String[]{READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE};
    }
}
