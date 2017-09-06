package com.cfeng.anew;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cfeng.anew.Base.BaseFragment;
import com.cfeng.anew.Utils.NewsRequestUtils;
import com.cfeng.anew.bean.NewBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.ObservableEmitter;

public class MainActivity extends AppCompatActivity {
    private   ArrayList<String> mTitles =new ArrayList<>();
    ArrayList<Fragment> list=new ArrayList<>();
    ViewPager vp;
    TabLayout tableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        vp= (ViewPager) findViewById(R.id.vp);
        tableLayout= (TabLayout) findViewById(R.id.nav);
        MyApp app= (MyApp) getApplication();
        app.requestQueue.add(new JsonObjectRequest(Request.Method.GET, WebConfig.getchannel, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println(jsonObject.toString());
                try {
                    JSONArray array=jsonObject.getJSONArray("result");
                    for (int i=0;i<array.length();i++){
                        mTitles.add(array.getString(i));
                        list.add(BaseFragment.getInstance(array.getString(i)));
                    }
                    vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                        @Override
                        public Fragment getItem(int position) {
                            return list.get(position);
                        }

                        @Override
                        public int getCount() {
                            return list.size();
                        }
                        @Override
                        public CharSequence getPageTitle(int position) {
                            return mTitles.get(position) ;
                        }
                    });
                    tableLayout.setupWithViewPager(vp);
                } catch (JSONException e) {
                    e.printStackTrace();
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
        });

    }
}
