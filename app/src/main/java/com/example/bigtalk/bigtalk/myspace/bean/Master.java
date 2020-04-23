package com.example.bigtalk.bigtalk.myspace.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jacqueline on 2018/2/21.
 */

public class Master extends BmobObject {
    private String user_id;
    private String master_type;
    private String master_field;

    public String getUser_id(){
        return user_id;
    }
    public void setUser_id(String user_id){
        this.user_id = user_id;
    }
    public String getMaster_type(){
        return master_type;
    }
    public void setMaster_type(String master_type){
        this.master_type = master_type;
    }
    public String getMaster_field(){
        return master_field;
    }
    public void setMaster_field(String master_field){
        this.master_field = master_field;
    }

}
