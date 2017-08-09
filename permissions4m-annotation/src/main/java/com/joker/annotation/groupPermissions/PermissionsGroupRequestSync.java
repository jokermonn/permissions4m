package com.joker.annotation.groupPermissions;

/**
 * Created by joker on 2017/8/5.
 */

public @interface PermissionsGroupRequestSync {
    Class<? extends PermissionsGroup>[] permissions();

    int[] value();
}
