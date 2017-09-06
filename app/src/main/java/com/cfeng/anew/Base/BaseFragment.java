package com.cfeng.anew.Base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cfeng.anew.MyApp;
import com.cfeng.anew.R;
import com.cfeng.anew.Utils.NewsRequestUtils;
import com.cfeng.anew.WebViewActivity;
import com.cfeng.anew.bean.NewBean;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BaseFragment extends Fragment {
    Handler handler=new Handler();
    private ArrayList<NewBean> datalist=new ArrayList<>();
    private String type;
    private int page=0;
    private int page_size=40;

    public static BaseFragment  getInstance(String txt) {

        BaseFragment baseFragment=new BaseFragment();
        baseFragment.type=txt;
    return baseFragment;
    }
    public BaseFragment() {
        // Required empty public constructor

    }

    BaseAdapter ba=new BaseAdapter() {
        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView= (LinearLayout) getLayoutInflater(getArguments()).inflate(R.layout.newitem,null);
            }
            LinearLayout ll= (LinearLayout) convertView;
            TextView tv= (TextView) ll.findViewById(R.id.title);
            TextView bv= (TextView) ll.findViewById(R.id.bottom);
            ImageView iv= (ImageView) ll.findViewById(R.id.pic);
            tv.setText(datalist.get(position).getTitle());
            bv.setText(datalist.get(position).getTime()+"  "+datalist.get(position).getSrc());
            if (datalist.get(position).getPic().isEmpty()){
                Picasso.with(getContext()).load(R.drawable.sina).fit().into(iv);
            }
            else
                Picasso.with(getContext()).load(datalist.get(position).getPic()).fit().into(iv);
            return ll;
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fl=  inflater.inflate(R.layout.fragment_base, container, false);
        final PullToRefreshListView pullToRefreshListView= (PullToRefreshListView) fl.findViewById(R.id.lv);
            datalist.add(new NewBean());
        pullToRefreshListView.setAdapter(ba);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置下拉时显示的日期和时间
                String label = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // 更新显示的label
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // 开始执行异步任务，传入适配器来进行数据改变
                Observable.create(new ObservableOnSubscribe<NewBean>() {

                    @Override
                    public void subscribe(ObservableEmitter<NewBean> o) throws Exception {
                        update_data(o);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<NewBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        datalist.clear();
                    }

                    @Override
                    public void onNext(NewBean value) {
                            datalist.add(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        ba.notifyDataSetChanged();
                        pullToRefreshListView.onRefreshComplete();
                    }
                });
            }});
        // 添加滑动到底部的监听器
        pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                Observable.create(new ObservableOnSubscribe<NewBean>() {

                    @Override
                    public void subscribe(ObservableEmitter<NewBean> e) throws Exception {
                        loadmore(e);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<NewBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewBean value) {
                        datalist.add(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        ba.notifyDataSetChanged();
                    }
                });
            }
        });
        pullToRefreshListView.setRefreshing(true);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),WebViewActivity.class);
                intent.putExtra("url",datalist.get(position-1).getUrl());
                intent.putExtra("title",datalist.get(position-1).getTitle());
                startActivity(intent);
            }
        });
        return fl;
    }

    public void loadmore(final ObservableEmitter<NewBean> e) {
        page++;
        MyApp app= (MyApp) getActivity().getApplication();
        app.requestQueue.add(NewsRequestUtils.get(type,page_size,page_size*page,e));
    }

    public void update_data(final ObservableEmitter<NewBean> e) {
        page=0;
        MyApp app= (MyApp) getActivity().getApplication();
        app.requestQueue.add(NewsRequestUtils.get(type,page_size,0,e));
    }
}
