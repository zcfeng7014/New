package com.cfeng.anew;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2017/7/26.
 */

public class MyApp extends Application {
    public RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue= Volley.newRequestQueue(getApplicationContext());
    }
}
