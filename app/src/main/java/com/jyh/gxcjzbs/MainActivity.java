package com.jyh.gxcjzbs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.flyco.tablayout.CommonTabLayout;
import com.jyh.gxcjzbs.base.BaseActivity;
import com.jyh.gxcjzbs.presenter.MainPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    public  @BindView(R.id.vp_2) ViewPager mViewPager;
    public  @BindView(R.id.tl_2) CommonTabLayout mTabLayout_2;
    private MainPresenter mainPresenter;
    public static Activity main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main, StatusBarColor.NO_COLOR);
        main=this;
        mainPresenter = new MainPresenter(this);
        mainPresenter.initConfig();
    }

    @OnClick(R.id.vp_2)
    public void onViewClicked() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        main = null;
    }
}
