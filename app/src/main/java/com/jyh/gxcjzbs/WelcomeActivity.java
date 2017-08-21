package com.jyh.gxcjzbs;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.jyh.gxcjzbs.base.BaseActivity;
import com.jyh.gxcjzbs.presenter.MainPresenter;
import com.jyh.gxcjzbs.presenter.WelComePresenter;
import com.library.util.NetUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity implements WelComePresenter.BackListening{
    public  @BindView(R.id.rl_splash) LinearLayout linearLayout;
    private WelComePresenter welComePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTastRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_enter, StatusBarColor.NO_COLOR);
        welComePresenter = new WelComePresenter(this);
        welComePresenter.setBackListening(this);
        welComePresenter.initDefault();
        if(NetUtils.isNetworkAvailable(getContext())){
            welComePresenter.initConfig();
        }else{
            welComePresenter.showNotNetwork();
        }

    }

    @OnClick(R.id.rl_splash)
    public void onViewClicked() {
    }


    /**
     * 判断mainactivity是否处于栈顶
     *
     * @return true在栈顶false不在栈顶
     */
    private boolean isTastRoot() {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(WelcomeActivity.class.getName());
    }

    @Override
    public void finishW() {
        finish();
        System.exit(0);
        System.gc();
    }
}
