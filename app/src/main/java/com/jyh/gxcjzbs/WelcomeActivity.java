package com.jyh.gxcjzbs;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.jyh.gxcjzbs.base.BaseActivity;
import com.jyh.gxcjzbs.presenter.MainPresenter;
import com.jyh.gxcjzbs.presenter.WelComePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity {
    public  @BindView(R.id.rl_splash) LinearLayout linearLayout;
    private WelComePresenter welComePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter, StatusBarColor.NO_COLOR);
        welComePresenter = new WelComePresenter(this);
        welComePresenter.initConfig();
    }

    @OnClick(R.id.rl_splash)
    public void onViewClicked() {
    }
}
