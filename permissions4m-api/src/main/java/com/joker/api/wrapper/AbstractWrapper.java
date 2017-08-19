package com.joker.api.wrapper;

import android.app.Activity;
import android.os.Build;

import com.joker.api.Permissions4M;
import com.joker.api.PermissionsProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joker on 2017/8/5.
 */

public abstract class AbstractWrapper implements Wrapper {
    private static final String PERMISSIONS_PROXY = "$$PermissionsProxy";
    @Permissions4M.PageType
    private static final int DEFAULT_PAGE_TYPE = Permissions4M.PageType.MANAGER_PAGE;
    private static final int DEFAULT_REQUEST_CODE = -1;
    protected static PermissionsProxy proxy;
    private static Map<String, PermissionsProxy> map = new HashMap<>();
    private static Map<Key, Wrapper> wrapperMap = new HashMap<>();
    @Permissions4M.PageType
    private int pageType = DEFAULT_PAGE_TYPE;
    private int requestCode = DEFAULT_REQUEST_CODE;
    private String permission;
    private PermissionRequestListener permissionRequestListener;
    private PermissionPageListener permissionPageListener;
    private boolean force;
    private boolean requestOnRationale;

    public AbstractWrapper() {
    }

    protected static void initProxy(Object object) {
        String name = object.getClass().getName();
        String proxyName = name + PERMISSIONS_PROXY;
        PermissionsProxy proxy = map.get(proxyName);
        try {
            if (proxy == null) {
                AbstractWrapper.proxy = (PermissionsProxy) Class.forName(proxyName).newInstance();
                map.put(proxyName, AbstractWrapper.proxy);
            } else {
                AbstractWrapper.proxy = proxy;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Map<Key, Wrapper> getWrapperMap() {
        return wrapperMap;
    }

    @Override
    public PermissionsProxy getProxy(String className) {
        return map.get(className + PERMISSIONS_PROXY);
    }

    @Override
    public Wrapper requestCode(int code) {
        if (code < 0) {
            throw new IllegalArgumentException("request code must bigger than 0, current is " + code);
        }
        this.requestCode = code;
        return this;
    }

    @Override
    public Wrapper requestPermission(String permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public Wrapper requestPageType(@Permissions4M.PageType int pageType) {
        this.pageType = pageType;
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

    public int getRequestCode() {
        return requestCode;
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

    @Override
    public Wrapper requestOnRationale() {
        requestOnRationale = true;
        return this;
    }

    @Override
    public boolean isRequestOnRationale() {
        return requestOnRationale;
    }

    public void request() {
        // use a map to hold wrappers
        Key key = new Key(getContext(), getRequestCode());
        wrapperMap.put(key, this);

        // on rationale, it should use normal request
        if (isRequestOnRationale()) {
            normalRequest();
        } else {
            // not on rationale, judge condition
            if (permissionRequestListener == null) {
                requestPermissionWithAnnotation();
            } else {
                requestPermissionWithListener();
            }
        }
    }

    abstract void normalRequest();

    protected void requestSync(Activity activity) {
        privateRequestSync(activity);
    }

    protected void requestSync(android.app.Fragment fragment) {
        privateRequestSync(fragment);
    }

    protected void requestSync(android.support.v4.app.Fragment fragment) {
        privateRequestSync(fragment);
    }

    @SuppressWarnings("unchecked")
    private void privateRequestSync(Object object) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        initProxy(object);
        proxy.startSyncRequestPermissionsMethod(object);
    }

    abstract void requestPermissionWithAnnotation();

    abstract void requestPermissionWithListener();

    public static class Key {
        private int requestCode;
        private Object object;

        public Key(Object object, int requestCode) {
            this.object = object;
            this.requestCode = requestCode;
        }

        @Override
        public String toString() {
            return "Key{" +
                    "requestCode=" + requestCode +
                    ", object=" + object +
                    '}';
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Key && ((Key) obj).getRequestCode() == requestCode &&
                    object.getClass().getName().equals(((Key) obj).getObject().getClass().getName());
        }

        @Override
        public int hashCode() {
            return object.hashCode() >> 1 + requestCode;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }
}
