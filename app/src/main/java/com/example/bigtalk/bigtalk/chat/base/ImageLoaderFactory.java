package com.example.bigtalk.bigtalk.chat.base;

/**
 * Created by Jacqueline on 2018/2/1.
 */

public class ImageLoaderFactory {
    private static volatile ILoader sInstance;

    private ImageLoaderFactory() {}

    public static ILoader getLoader() {
        if (sInstance == null) {
            synchronized (ImageLoaderFactory.class) {
                if (sInstance == null) {
                    sInstance = new UniversalImageLoader();
                }
            }
        }
        return sInstance;
    }
}
