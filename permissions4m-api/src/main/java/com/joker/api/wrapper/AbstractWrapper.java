package com.joker.api.wrapper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.joker.api.Permissions4M;
import com.joker.api.apply.ForceApplyPermissions;
import com.joker.api.apply.NormalApplyPermissions;
import com.joker.api.apply.PermissionsChecker;
import com.joker.api.support.ManufacturerSupportUtil;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joker on 2017/8/5.
 */

public abstract class AbstractWrapper implements PermissionWrapper, Cloneable {
    private static final String PERMISSIONS_PROXY = "$$PermissionsProxy";
    @Permissions4M.PageType
    private static final int DEFAULT_PAGE_TYPE = Permissions4M.PageType.ANDROID_SETTING_PAGE;
    private static final int DEFAULT_REQUEST_CODE = -1;
    private static final boolean DEFAULT_IS_FORCE = true;
    private static final boolean DEFAULT_IS_ALLOWED = false;
    private static Map<Key, WeakReference<PermissionWrapper>> wrapperMap = new HashMap<>();
    @Permissions4M.PageType
    private int pageType = DEFAULT_PAGE_TYPE;
    private int requestCode = DEFAULT_REQUEST_CODE;
    private int[] requestCodes;
    private String[] permissions;
    private String permission;
    private PermissionRequestListener permissionRequestListener;
    private PermissionPageListener permissionPageListener;
    private PermissionCustomRationaleListener permissionCustomRationaleListener;
    private boolean force = DEFAULT_IS_FORCE;
    private boolean allowed = DEFAULT_IS_ALLOWED;
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
            return (PermissionsProxy) Class.forName(proxyName).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
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
    public Wrapper requestListener(PermissionRequestListener listener) {
        this.permissionRequestListener = listener;
        return this;
    }

    @Override
    public Wrapper requestPage(PermissionPageListener listener) {
        this.permissionPageListener = listener;
        return this;
    }

    @Override
    public Wrapper requestOnRationale() {
        requestOnRationale = true;
        return this;
    }

    @Override
    public Wrapper requestForce(boolean force) {
        this.force = force;
        return this;
    }

    @Override
    public Wrapper requestUnderM(boolean allow) {
        this.allowed = allow;
        return this;
    }

    @Override
    public Wrapper requestCustomRationaleListener(PermissionCustomRationaleListener listener) {
        this.permissionCustomRationaleListener = listener;
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
    public PermissionCustomRationaleListener getPermissionCustomRationaleListener() {
        return permissionCustomRationaleListener;
    }

    @Override
    public boolean isRequestForce() {
        return force;
    }

    @Override
    public boolean isRequestOnRationale() {
        return requestOnRationale;
    }

    @Override
    public boolean isRequestUnderM() {
        return allowed;
    }

    @Override
    public void request() {
        if (isRequestOnRationale()) {
            addEntity(getRequestPermissions()[0], getRequestCodes()[0], false);
            originalRequest();
        } else {
            PermissionRequestListener requestListener = getPermissionRequestListener();
            if (requestListener != null) {
                initArrayAndEntity();
                requestPermissionWithListener();
            } else {
                addEntity(getRequestPermissions()[0], getRequestCodes()[0], true);
                requestPermissionWithAnnotation();
            }
        }
    }

    /**
     * init {@link #getRequestPermissions()} array and {@link #getRequestCodes()} array
     * and call {@link #addEntity(String, int, boolean)} method.
     */
    private void initArrayAndEntity() {
        String[] permissions = getRequestPermissions();
        String[] targetPermissions = new String[permissions.length];
        if (permissions.length != requestCodes.length) {
            throw new IllegalArgumentException("permissions' length is different from codes' length");
        }
        int[] requestCodes = getRequestCodes();
        int[] targetCodes = new int[requestCodes.length];
        for (int i = permissions.length - 1; i >= 0; i--) {
            targetPermissions[permissions.length - i - 1] = permissions[i];
            targetCodes[permissions.length - i - 1] = requestCodes[i];
        }

        for (int i = 0; i < targetPermissions.length; i++) {
            if (i == 0) {
                requestCodes(DEFAULT_REQUEST_CODE);
                requestPermissions("");
            } else {
                requestCodes(targetCodes[i - 1]);
                requestPermissions(targetPermissions[i - 1]);
            }

            addEntity(targetPermissions[i], targetCodes[i], true);
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
     * @param add
     */
    private void addEntity(String permission, int requestCode, boolean add) {
        requestCode(requestCode);
        requestPermission(permission);
        // use a map to hold wrappers
        if (add) {
            try {
                // use clone() method!
                wrapperMap.put(new Key(getContext(), requestCode), new WeakReference<>(
                        (PermissionWrapper) this.clone()));
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
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
     * {@link ManufacturerSupportUtil#isUnderMHasPermissionRequestManufacturer()} manufacturer (those are
     * some manufacturers that has permission manage under M) or it's the
     * {@link ManufacturerSupportUtil#isUnderMHasPermissionRequestManufacturer()}, but under
     * {@link android.os.Build.VERSION_CODES#LOLLIPOP}
     * <p>
     * 2.it's not the {@link ManufacturerSupportUtil#isUnderMHasPermissionRequestManufacturer()}, and
     * Version Code is above than {@link android.os.Build.VERSION_CODES#M}
     * <p>
     * 3.under {@link android.os.Build.VERSION_CODES#M}, and it's
     * {@link ManufacturerSupportUtil#isUnderMHasPermissionRequestManufacturer()}
     */
    @SuppressWarnings("unchecked")
    private void requestPermissionWithAnnotation() {
        if (ManufacturerSupportUtil.isUnderMNeedChecked(isRequestUnderM())) {
            if (PermissionsChecker.isPermissionGranted(getActivity(), getRequestPermission())) {
                NormalApplyPermissions.grantedWithAnnotation(this);
            } else {
                ForceApplyPermissions.deniedWithAnnotationForUnderM(this);
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
            NormalApplyPermissions.grantedWithAnnotation(this);
        }
    }

    private void mayGrantedWithAnnotation() {
        if (isRequestForce()) {
            ForceApplyPermissions.grantedWithAnnotation(this);
        } else {
            NormalApplyPermissions.grantedWithAnnotation(this);
        }
    }

    /**
     * 1.under {@link android.os.Build.VERSION_CODES#M}, and it's not the
     * {@link ManufacturerSupportUtil#isUnderMHasPermissionRequestManufacturer()} manufacturer (those are
     * some manufacturers that has permission manage under M)
     * <p>
     * 2.it's not the {@link ManufacturerSupportUtil#isUnderMHasPermissionRequestManufacturer()}, and
     * Version Code is bigger than {@link android.os.Build.VERSION_CODES#M}
     * <p>
     * 3.under {@link android.os.Build.VERSION_CODES#M}, and it's
     * {@link ManufacturerSupportUtil#isUnderMHasPermissionRequestManufacturer()}
     */
    public void requestPermissionWithListener() {
        if (ManufacturerSupportUtil.isUnderMNeedChecked(isRequestUnderM())) {
            if (PermissionsChecker.isPermissionGranted(getActivity(), getRequestPermission())) {
                NormalApplyPermissions.grantedWithListener(this);
            } else {
                ForceApplyPermissions.deniedWithListenerForUnderM(this);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), getRequestPermission()) != PackageManager
                    .PERMISSION_GRANTED) {
                tryRequestWithListener();
            } else {
                mayGrantedWithListener();
            }
        } else {
            NormalApplyPermissions.grantedWithListener(this);
        }
    }

    private void mayGrantedWithListener() {
        if (isRequestForce()) {
            ForceApplyPermissions.grantedWithListener(this);
        } else {
            NormalApplyPermissions.grantedWithListener(this);
        }
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

    @Override
    public String toString() {
        return "AbstractWrapper{" +
                "pageType=" + pageType +
                ", requestCode=" + requestCode +
                ", requestCodes=" + Arrays.toString(requestCodes) +
                ", permissions=" + Arrays.toString(permissions) +
                ", permission='" + permission + '\'' +
                ", permissionRequestListener=" + permissionRequestListener +
                ", permissionPageListener=" + permissionPageListener +
                ", permissionCustomRationaleListener=" + permissionCustomRationaleListener +
                ", force=" + force +
                ", allowed=" + allowed +
                ", requestOnRationale=" + requestOnRationale +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

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
            return (object.get().hashCode() + requestCode) / 10;
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
