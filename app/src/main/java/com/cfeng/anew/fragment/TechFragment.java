package com.cfeng.anew.fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cfeng.anew.Base.BaseFragment;
import com.cfeng.anew.MyApp;
import com.cfeng.anew.WebConfig;
import com.cfeng.anew.bean.NewBean;
import com.cfeng.anew.bean.NewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.ObservableEmitter;

/**
 * Created by Administrator on 2017/7/26.
 */

public class TechFragment extends BaseFragment {
    String url= WebConfig.Url+"tableNum="+WebConfig.tech+"&pagesize=20";
    int page=1;
    @Override
    public void loadmore(final ObservableEmitter<NewBean> e) {
        page++;
        MyApp app= (MyApp) getActivity().getApplication();
        app.requestQueue.add(new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jo= (JSONObject) jsonArray.get(i);
                        e.onNext(NewUtils.JSonToNew(jo));
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
        }));
    }

    @Override
    public void update_data(final ObservableEmitter<NewBean> e) {
        page=1;
        MyApp app= (MyApp) getActivity().getApplication();
        app.requestQueue.add(new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jo= (JSONObject) jsonArray.get(i);
                        e.onNext(NewUtils.JSonToNew(jo));
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
        }));
    }
}
