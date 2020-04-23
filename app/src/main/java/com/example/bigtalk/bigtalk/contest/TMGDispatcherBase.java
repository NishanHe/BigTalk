package com.example.bigtalk.bigtalk.contest;

import android.content.Intent;

import com.tencent.TMG.ITMGContext;

/**
 * Created by Yesq on 2018/5/8.
 */

public interface TMGDispatcherBase {
    public void OnEvent(ITMGContext.ITMG_MAIN_EVENT_TYPE type, Intent data);
}
