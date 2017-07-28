package com.cfeng.anew;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.cfeng.anew.fragment.CarFragment;
import com.cfeng.anew.fragment.EntFragment;
import com.cfeng.anew.fragment.JokeFragment;
import com.cfeng.anew.fragment.MoneyFragment;
import com.cfeng.anew.fragment.SportFragment;
import com.cfeng.anew.fragment.TechFragment;
import com.cfeng.anew.fragment.TopFragment;
import com.cfeng.anew.fragment.WarFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String[] mTitles = new String[]{"头条", "娱乐","军事","汽车","财经","笑话","体育","科技"};
    ArrayList<Fragment> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager vp= (ViewPager) findViewById(R.id.vp);
        TabLayout tableLayout= (TabLayout) findViewById(R.id.nav);
            list.add(new TopFragment());
            list.add(new EntFragment());
            list.add(new WarFragment());
            list.add(new CarFragment());
            list.add(new MoneyFragment());
            list.add(new JokeFragment());
            list.add(new SportFragment());
            list.add(new TechFragment());
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
                return mTitles[position];
            }
        });
        tableLayout.setupWithViewPager(vp);
    }
}
