package com.joker.api.wrapper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.joker.api.Permissions4M;
import com.joker.api.apply.ForceApplyPermissions;
import com.joker.api.apply.NormalApplyPermissions;
import com.joker.api.apply.PermissionsChecker;
import com.joker.api.support.PermissionsPageManager;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joker on 2017/8/5.
 */

public abstract class AbstractWrapper implements PermissionWrapper {
    private static final String PERMISSIONS_PROXY = "$$PermissionsProxy";
    @Permissions4M.PageType
    private static final int DEFAULT_PAGE_TYPE = Permissions4M.PageType.ANDROID_SETTING_PAGE;
    private static final int DEFAULT_REQUEST_CODE = -1;
    private static final boolean DEFAULT_IS_FORCE = true;
    private static final boolean DEFAULT_IS_ALLOWED = false;
    private static Map<String, PermissionsProxy> proxyMap = new HashMap<>();
    private static Map<Key, WeakReference<PermissionWrapper>> wrapperMap = new HashMap<>();
    @Permissions4M.PageType
    private int pageType = DEFAULT_PAGE_TYPE;
    private int requestCode = DEFAULT_REQUEST_CODE;
    private int[] requestCodes;
    private String[] permissions;
    private String permission;
    private PermissionRequestListener permissionRequestListener;
    private PermissionPageListener permissionPageListener;
    private boolean force = DEFAULT_IS_FORCE;
    private boolean allow = DEFAULT_IS_ALLOWED;
    private boolean requestOnRationale;

    public AbstractWrapper() {
    }

    public static Map<Key, WeakReference<PermissionWrapper>> getWrapperMap() {
        return wrapperMap;
    }

    @Override
    public PermissionsProxy getProxy(String className) {
        String proxyName = className + PERMISSIONS_PROXY;
        try {
            if (proxyMap.get(proxyName) == null) {
                proxyMap.put(proxyName, (PermissionsProxy) Class.forName(proxyName).newInstance());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return proxyMap.get(proxyName);
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
    public Wrapper requestCodes(int... codes) {
        this.requestCodes = codes;
        return this;
    }

    @Override
    public Wrapper requestPermission(String permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public Wrapper requestPermissions(String... permissions) {
        this.permissions = permissions;
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

    @Override
    public int getRequestCode() {
        return requestCode;
    }

    @Override
    public int[] getRequestCodes() {
        return requestCodes;
    }

    @Override
    public String getRequestPermission() {
        return permission;
    }

    @Override
    public String[] getRequestPermissions() {
        return permissions;
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

    @Override
    public Wrapper requestUnderM(boolean allow) {
        this.allow = allow;
        return this;
    }

    @Override
    public boolean isRequestUnderM() {
        return allow;
    }

    @Override
    public void request() {
        if (isRequestOnRationale()) {
            addEntity(getRequestPermissions()[0], getRequestCodes()[0]);
            originalRequest();
        } else {
            PermissionRequestListener requestListener = getPermissionRequestListener();
            if (requestListener != null) {
                String[] permissions = getRequestPermissions();
                int[] requestCodes = getRequestCodes();
                for (int i = 0; i < permissions.length; i++) {
                    addEntity(permissions[i], requestCodes[i % requestCodes.length]);
                    requestPermissionWithListener();
                }
            } else {
                addEntity(getRequestPermissions()[0], getRequestCodes()[0]);
                requestPermissionWithAnnotation();
            }
        }
    }

    /**
     * we normal use {@link PermissionWrapper#getRequestCode()} and
     * {@link PermissionWrapper#getRequestPermission()} not
     * {@link Wrapper#getRequestCodes()} or
     * {@link Wrapper#getRequestPermissions()}
     * <p>
     * <p>
     * add an entity to map
     * entity struct:
     * permission -> context
     *
     * @param permission
     * @param requestCode
     */
    private void addEntity(String permission, int requestCode) {
        requestCode(requestCode);
        requestPermission(permission);
        // use a map to hold wrappers
        Key key = new Key(getContext(), getRequestCode());
        wrapperMap.put(key, new WeakReference<PermissionWrapper>(this));
    }

    void requestSync(Activity activity) {
        privateRequestSync(activity);
    }

    void requestSync(android.app.Fragment fragment) {
        privateRequestSync(fragment);
    }

    void requestSync(android.support.v4.app.Fragment fragment) {
        privateRequestSync(fragment);
    }

    @SuppressWarnings("unchecked")
    private void privateRequestSync(Object object) {
        getProxy(object.getClass().getName()).startSyncRequestPermissionsMethod(object);
    }

    /**
     * 1.under {@link android.os.Build.VERSION_CODES#M}, and it's not the
     * {@link PermissionsPageManager#isUnderMHasPermissionRequestManufacturer()} manufacturer (those are
     * some manufacturers that has permission manage under M) or it's the
     * {@link PermissionsPageManager#isUnderMHasPermissionRequestManufacturer()}, but under
     * {@link android.os.Build.VERSION_CODES#LOLLIPOP}
     * <p>
     * 2.it's not the {@link PermissionsPageManager#isUnderMHasPermissionRequestManufacturer()}, and
     * Version Code is above than {@link android.os.Build.VERSION_CODES#M}
     * <p>
     * 3.under {@link android.os.Build.VERSION_CODES#M}, and it's
     * {@link PermissionsPageManager#isUnderMHasPermissionRequestManufacturer()}
     */
    @SuppressWarnings("unchecked")
    private void requestPermissionWithAnnotation() {
        PermissionsProxy proxy = getProxy(getContext().getClass().getName());

        if (underMAboveLShouldRequest()) {
            if (PermissionsChecker.isPermissionGranted(getActivity(), getRequestPermission())) {
                proxy.granted(getContext(), getRequestCode());
            } else {
                ForceApplyPermissions.deniedOnResultWithAnnotationForUnderMManufacturer(this);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permission = getRequestPermission();
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager
                    .PERMISSION_GRANTED) {
                tryRequestWithAnnotation();
            } else {
                mayGrantedWithAnnotation();
            }
        } else {
            proxy.granted(getContext(), getRequestCode());
        }
    }

    private void mayGrantedWithAnnotation() {
        if (isRequestForce()) {
            ForceApplyPermissions.grantedOnResultWithAnnotation(this);
        } else {
            NormalApplyPermissions.grantedWithAnnotation(this);
        }
    }

    /**
     * 1.under {@link android.os.Build.VERSION_CODES#M}, and it's not the
     * {@link PermissionsPageManager#isUnderMHasPermissionRequestManufacturer()} manufacturer (those are
     * some manufacturers that has permission manage under M)
     * <p>
     * 2.it's not the {@link PermissionsPageManager#isUnderMHasPermissionRequestManufacturer()}, and
     * Version Code is bigger than {@link android.os.Build.VERSION_CODES#M}
     * <p>
     * 3.under {@link android.os.Build.VERSION_CODES#M}, and it's
     * {@link PermissionsPageManager#isUnderMHasPermissionRequestManufacturer()}
     */
    private void requestPermissionWithListener() {
        PermissionRequestListener listener = getPermissionRequestListener();

        if (underMAboveLShouldRequest()) {
            if (PermissionsChecker.isPermissionGranted(getActivity(), getRequestPermission())) {
                listener.permissionGranted(getRequestCode());
            } else {
                ForceApplyPermissions.deniedOnResultWithListenerForUnderMManufacturer(this);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), getRequestPermission()) != PackageManager
                    .PERMISSION_GRANTED) {
                tryRequestWithListener();
            } else {
                mayGrantedWithListener();
            }
        } else {
            listener.permissionGranted(getRequestCode());
        }
    }

    private void mayGrantedWithListener() {
        if (isRequestForce()) {
            ForceApplyPermissions.grantedOnResultWithListener(this);
        } else {
            NormalApplyPermissions.grantedWithListener(this);
        }
    }

    /**
     * the condition of {@link PermissionsPageManager#isUnderMHasPermissionRequestManufacturer()}:
     * -> like {@link com.joker.api.support.manufacturer.XIAOMI} and
     * {@link com.joker.api.support.manufacturer.MEIZU}
     * could request permission:
     * 1. is {@link PermissionsPageManager#isUnderMHasPermissionRequestManufacturer()} device
     * 2. version code is 5.0~6.0
     * 3. {@link Wrapper#isRequestUnderM()} return true
     *
     * @return
     */
    private boolean underMAboveLShouldRequest() {
        return PermissionsPageManager.isUnderMHasPermissionRequestManufacturer() &&
                PermissionsPageManager.BuildVersionUnderMAboveL() && isRequestUnderM();
    }

    /**
     * request in second request, should use original request with no callback.
     */
    abstract void originalRequest();

    /**
     * use annotation request
     */
    abstract void tryRequestWithAnnotation();

    /**
     * use listener request
     */
    abstract void tryRequestWithListener();

    public static class Key {
        private int requestCode;
        private WeakReference<Object> object;

        public Key(Object object, int requestCode) {
            this.object = new WeakReference<>(object);
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
            return obj instanceof Key && ((Key) obj)
                    .getObject().get() != null && object.get().getClass() != null && ((Key) obj)
                    .getRequestCode() == requestCode && object.get().getClass().getName().equals(((Key)
                    obj).getObject().get().getClass()
                    .getName());
        }

        @Override
        public int hashCode() {
            return object.get().hashCode() >> 1 + requestCode;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public WeakReference<Object> getObject() {
            return object;
        }

        public void setObject(WeakReference<Object> object) {
            this.object = object;
        }
    }
}
