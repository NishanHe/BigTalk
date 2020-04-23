package com.example.bigtalk.bigtalk.chat.util;

/**
 * Created by Jacqueline on 2018/2/6.
 */

public class Util {
    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }
}
