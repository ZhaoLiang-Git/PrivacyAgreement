package com.gyyx.androidsdk;

public class UAUtil {
    // 上次接口调用时间
    private static long lastClickTime = 0;

    // 两次点击时间间隔（时间间隔内多次调用无效）
    private final static long TIME_INTERVAL = 500;

    public static boolean isClickAvaliable() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime > TIME_INTERVAL) {
            lastClickTime = time;
            return true;
        }
        return false;
    }
}
