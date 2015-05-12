package com.ktl.moment.utils;

import com.ktl.moment.android.MomentApplication;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	
	private static Toast toast;
	private static int duration = Toast.LENGTH_SHORT;

    public static void show(Context context, CharSequence text) {
        if (toast == null) {
            if (context == null) {
                return;
            }
            toast = Toast.makeText(MomentApplication.getApplication(), text, duration);
        }
        toast.setText("" + text); // 为安全起见，此处转换成string类型，防止text为int时出错
        toast.setDuration(duration);
        toast.show();
    }
}
