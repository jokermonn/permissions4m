package com.joker.api.stream.wrapper;

import android.content.Intent;

import com.joker.api.stream.SupportPermissions4M;

/**
 * Created by joker on 2017/8/5.
 */

abstract class AbstractWrapper implements Wrapper {
    protected int pageType = SupportPermissions4M.PageType.MANAGER_PAGE;
    protected int code;
    protected String permission;
    protected Intent intent;
    protected PermissionRequestListener permissionRequestListener;
    protected PermissionPageListener permissionPageListener;
    protected PermissionRationaleListener permissionRationaleListener;
    protected boolean force;

    public AbstractWrapper() {

    }

    @Override
    public Wrapper requestCode(int code) {
        this.code = code;
        return this;
    }

    @Override
    public Wrapper requestPermission(String permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public Wrapper requestPageType(@SupportPermissions4M.PageType int pageType) {
        this.pageType = pageType;
        return this;
    }

    @Override
    public Wrapper requestRationale(PermissionRationaleListener listener) {
        this.permissionRationaleListener = listener;
        return this;
    }

    @Override
    public Wrapper requestCallback(PermissionRequestListener listener) {
        this.permissionRequestListener = listener;
        return this;
    }

    @Override
    public Wrapper requestPage(PermissionPageListener listener) {
        this.permissionPageListener = listener;
        return this;
    }

    @Override
    public Wrapper requestForce(boolean force) {
        this.force = force;
        return this;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public int getPageType() {
        return pageType;
    }

    @Override
    public PermissionRationaleListener getPermissionRationaleListener() {
        return permissionRationaleListener;
    }

    @Override
    public PermissionRequestListener getPermissionRequestListener() {
        return permissionRequestListener;
    }

    @Override
    public PermissionPageListener getPermissionPageListener() {
        return permissionPageListener;
    }

    @Override
    public boolean isRequestForce() {
        return force;
    }

    public abstract void request();
}
