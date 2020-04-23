package com.example.bigtalk.bigtalk.contest;

import android.content.Intent;

import com.tencent.TMG.ITMGContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yesq on 2018/5/8.
 */

public class TMGCallbackDispatcher {
    private Map<ITMGContext.ITMG_MAIN_EVENT_TYPE, List<TMGDispatcherBase>> mapCallbacks = new HashMap<>();
    private ITMGContext.ITMGDelegate itmgDelegate = null;
    private static TMGCallbackDispatcher s_dispatcher = null;

    static public TMGCallbackDispatcher getInstance()
    {
        if (s_dispatcher == null)
        {
            s_dispatcher = new TMGCallbackDispatcher();
        }
        return s_dispatcher;
    }

    private TMGCallbackDispatcher()
    {
        itmgDelegate= new ITMGContext.ITMGDelegate() {
            @Override
            public void OnEvent(ITMGContext.ITMG_MAIN_EVENT_TYPE type, Intent data) {
                if (mapCallbacks.containsKey(type))
                {
                    List<TMGDispatcherBase> lst = mapCallbacks.get(type);
                    for (int i = 0; i < lst.size(); i++)
                    {
                        lst.get(i).OnEvent(type, data);
                    }
                }
                super.OnEvent(type, data);
            }
        };
    }

    public void AddDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE type, TMGDispatcherBase dispatcher)
    {
        if(mapCallbacks.containsKey(type))
        {
            List<TMGDispatcherBase> lstDispacher = mapCallbacks.get(type);
            if (!lstDispacher.contains(dispatcher))
            {
                lstDispacher.add(dispatcher);
            }
            return;
        }
        else
        {
            List<TMGDispatcherBase> lstCallbacks = new ArrayList<>();
            lstCallbacks.add(dispatcher);
            mapCallbacks.put(type, lstCallbacks);
        }
    }

    public void RemoveDelegate(ITMGContext.ITMG_MAIN_EVENT_TYPE type, TMGDispatcherBase dispatcher)
    {
        if(mapCallbacks.containsKey(type))
        {
            List<TMGDispatcherBase> lstDispacher = mapCallbacks.get(type);
            if (lstDispacher.contains(dispatcher))
            {
                lstDispacher.remove(dispatcher);
            }
            return;
        }
    }

    public ITMGContext.ITMGDelegate getItmgDelegate()
    {
        return itmgDelegate;
    }

}
