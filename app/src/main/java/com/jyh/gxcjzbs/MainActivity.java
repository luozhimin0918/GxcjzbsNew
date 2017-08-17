package com.jyh.gxcjzbs;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.jyh.gxcjzbs.base.BaseActivity;
import com.jyh.gxcjzbs.presenter.MainPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    public  @BindView(R.id.vp_2) ViewPager mViewPager;
    public  @BindView(R.id.tl_2) CommonTabLayout mTabLayout_2;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main, StatusBarColor.NO_COLOR);
        mainPresenter = new MainPresenter(this);
        mainPresenter.initConfig();
    }

    @OnClick(R.id.vp_2)
    public void onViewClicked() {
    }
}
