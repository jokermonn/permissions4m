package com.joker.api.wrapper;

import android.app.Activity;

import com.joker.api.Permissions4M;

/**
 * Created by joker on 2017/8/5.
 */

public interface Wrapper extends ListenerWrapper, AnnotationWrapper {
    /**
     * get activity
     *
     * @return
     */
    Activity getActivity();

    /**
     * request code
     *
     * @param code
     * @return
     */
    Wrapper requestCodes(int... code);

    /**
     * request permission
     *
     * @param permission {@link android.Manifest.permission}
     * @return
     */
    Wrapper requestPermissions(String... permission);

    /**
     * android setting page or phone manager page
     *
     * @param pageType {@link Permissions4M.PageType}
     * @return
     */
    Wrapper requestPageType(@Permissions4M.PageType int pageType);

    /**
     * whether should apply permission force
     *
     * @param force true if force
     * @return
     */
    Wrapper requestForce(boolean force);

    /**
     * called when use custom rationale, in case fall in to drop-dead
     * <p>
     * custom rationale -> ensure button -> Permissions4M.request ->
     * if(custom rationale) -> custom rationale
     *
     * @return
     */
    Wrapper requestOnRationale();

    /**
     * allow the android(under {@link android.os.Build.VERSION_CODES#M}, above
     * {@link android.os.Build.VERSION_CODES#LOLLIPOP}) to request permission
     *
     * @param allow true if allow
     * @return
     */
    Wrapper requestUnderM(boolean allow);

    /**
     * get the context({@link android.app.Activity}, {@link android.app.Fragment},
     * {@link android.support.v4.app.Fragment})
     *
     * @return {@link android.app.Activity} or {@link android.app.Fragment} or
     * {@link android.support.v4.app.Fragment}
     */
    Object getContext();

    /**
     * request permission
     */
    void request();

    /**
     * multiple permission request by sync
     */
    void requestSync();

    int[] getRequestCodes();

    String[] getRequestPermissions();

    boolean isRequestOnRationale();

    boolean isRequestUnderM();

    @Permissions4M.PageType
    int getPageType();

    boolean isRequestForce();
}
