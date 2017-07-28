package com.joker.permissions4m.other;

import android.widget.Toast;

/**
 * Created by joker on 2017/7/27.
 */

public class ToastUtil {
    private static Toast toast;

    public static void show(String content) {
        if (toast == null) {
            toast = Toast.makeText(App.getAppContext(), content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
