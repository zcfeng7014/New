package com.cfeng.anew.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.webkit.URLUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cfeng.anew.MyApp;
import com.cfeng.anew.WebConfig;
import com.cfeng.anew.bean.NewBean;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.ObservableEmitter;

/**
 * Created by Administrator on 2017/8/24/024.
 */

public class NewsRequestUtils {
public static JsonObjectRequest get(String type,int sum,int start, final ObservableEmitter<NewBean> e) {

    String url= null;
    try {
        url = WebConfig.getnew+"?channel="+ URLEncoder.encode(type,"utf-8")+"&num="+sum+"&start="+start;
    } catch (UnsupportedEncodingException e1) {
        e1.printStackTrace();
    }
    return new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {

                JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++) {
                    Gson gson=new Gson();
                    NewBean bean=gson.fromJson(jsonArray.get(i).toString(),NewBean.class);
                    e.onNext(bean);
                }
                e.onComplete();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }) {

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "APPCODE " + WebConfig.AppCode);
            return headers;
        }
    };
}
}
