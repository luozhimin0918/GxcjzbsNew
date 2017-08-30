package com.jyh.gxcjzbs.presenter;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jyh.gxcjzbs.MainActivity;
import com.jyh.gxcjzbs.R;
import com.jyh.gxcjzbs.annotion.BindObject;
import com.jyh.gxcjzbs.base.BasePresenter;
import com.jyh.gxcjzbs.base.IBaseView;
import com.jyh.gxcjzbs.fragment.ZhiBoFragment;
import com.jyh.gxcjzbs.view.TabEntity;
import com.library.base.http.VolleyRequest;

import java.util.ArrayList;
import java.util.List;


public class MainPresenter extends BasePresenter {

    @BindObject
    MainActivity mainActivity;
    private RequestQueue queue;
    private VolleyRequest request;
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<CustomTabEntity>();


    private String[] mTitles = {"首页", "社区", "购物车", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.home, R.mipmap.community,
            R.mipmap.car, R.mipmap.self};
    private int[] mIconSelectIds = {
            R.mipmap.home_selected, R.mipmap.community_selected,
            R.mipmap.car_selected, R.mipmap.self_selected};
    MyPagerAdapter myPagerAdapter;
    public MainPresenter(IBaseView iBaseView) {
        super(iBaseView);
    }

    public void initConfig() {
        queue = mainActivity.getQueue();
        request = new VolleyRequest(mContext, queue);




        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
            mFragments.add(new ZhiBoFragment());
        }
        myPagerAdapter=new MyPagerAdapter(mainActivity.getSupportFragmentManager());
        mainActivity.mViewPager.setAdapter(myPagerAdapter);

        mainActivity.mTabLayout_2.setTabData(mTabEntities);
        mainActivity.mTabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTabSelect(int position) {
                mainActivity.mViewPager.setCurrentItem(position,true);

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTabReselect(int position) {


            }
        });

        mainActivity.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(int position) {

                mainActivity.mTabLayout_2.setCurrentTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mainActivity.mViewPager.setCurrentItem(0);
    }




    private class MyPagerAdapter extends FragmentPagerAdapter {
        private List<String> tagList;
        FragmentManager fm;
        public MyPagerAdapter(FragmentManager fm) {

            super(fm);
            this.fm=fm;
            tagList=new ArrayList<String>();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            tagList.add(makeFragmentName(container.getId(),position));
            return super.instantiateItem(container, position);
        }

        public  String makeFragmentName(int viewId, int index) {
            return "android:switcher:" + viewId + ":" + index;
        }
    }



}
