package com.example.bigtalk.bigtalk.contest;

import android.content.Intent;

/**
 * Created by Yesq on 2018/5/8.
 */

public class TMGCallbackHelper {
    static class Params2{
        int nErrCode;
        String strErrMsg;
    };
    private static Params2 params2 = new Params2();
    public static Params2 ParseIntentParams2(Intent intent)
    {
        params2.nErrCode = intent.getIntExtra("result" , -1);
        params2.strErrMsg = intent.getStringExtra("error_info");
        return params2;
    }

    ////
    static class ParamsUerInfo{
        int nEventID;
        String[] identifierList;
    };
    private static ParamsUerInfo paramsUerInfo = new ParamsUerInfo();
    static ParamsUerInfo ParseUserInfoUpdateInfoIntent(Intent intent)
    {
        paramsUerInfo.nEventID = intent.getIntExtra("event_id", 0);
        paramsUerInfo.identifierList = intent.getStringArrayExtra("user_list");
        return paramsUerInfo;
    }

    //
    //
    //
    static class ParamsAudioDeviceInfo{
        boolean bState;
        int nErrCode;
    };
    private static ParamsAudioDeviceInfo paramsAudioDeviceInfo = new ParamsAudioDeviceInfo();
    static ParamsAudioDeviceInfo ParseAudioDeviceInfoIntent(Intent intent)
    {
        paramsAudioDeviceInfo.bState = intent.getBooleanExtra("audio_state", false);
        paramsAudioDeviceInfo.nErrCode = intent.getIntExtra("audio_errcode", 0);
        return paramsAudioDeviceInfo;
    }
}
