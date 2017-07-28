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

import com.cfeng.anew.R;
import com.cfeng.anew.WebViewActivity;
import com.cfeng.anew.bean.NewBean;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseFragment extends Fragment {
    Handler handler=new Handler();
    private ArrayList<NewBean> datalist=new ArrayList<>();
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
            LinearLayout a= (LinearLayout) ll.findViewById(R.id.img_list);
            a.removeAllViews();
            if (!datalist.get(position).getImgurl().isEmpty()) {
                ImageView iv=new ImageView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.width=0;
                params.weight=1;
                iv.setLayoutParams(params);
                a.addView(iv);
                Picasso.with(getContext()).load(datalist.get(position).getImgurl()).resize(1080,640).centerCrop().into(iv);
            }
            TextView tv= (TextView) ll.findViewById(R.id.title);
            TextView bv= (TextView) ll.findViewById(R.id.bottom);
            TextView dv=(TextView) ll.findViewById(R.id.dist);
            tv.setText(datalist.get(position).getTitle());
            dv.setText(datalist.get(position).getDigest());
            bv.setText("来源："+datalist.get(position).getSource()+"   时间："+datalist.get(position).getTime());

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
                intent.putExtra("url",datalist.get(position-1).getContent());
                intent.putExtra("title",datalist.get(position-1).getTitle());
                startActivity(intent);
            }
        });
        return fl;
    }

    public abstract void loadmore(ObservableEmitter<NewBean> e);

    public abstract  void update_data(ObservableEmitter<NewBean> e);
}
