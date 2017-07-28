package com.cfeng.anew.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/7/26.
 */

public class NewUtils {
    public  static NewBean JSonToNew(JSONObject jo){
        try {
            return new NewBean(jo.getString("top_image"),jo.getString("text_image0"),jo.getString("text_image1"),jo.getLong("edit_time"),
                    jo.getString("title"),jo.getString("source"),jo.getString("content"),jo.getString("digest"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
