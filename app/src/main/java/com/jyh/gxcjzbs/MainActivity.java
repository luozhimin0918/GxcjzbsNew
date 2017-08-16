package com.jyh.gxcjzbs;

import android.os.Bundle;
import android.widget.TextView;

import com.jyh.gxcjzbs.base.BaseActivity;
import com.jyh.gxcjzbs.presenter.MainPresenter;
import com.library.base.LibActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    public @BindView(R.id.sss) TextView sss;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main, LibActivity.StatusBarColor.NO_COLOR);
        mainPresenter = new MainPresenter(this);
        mainPresenter.initConfig();
    }

    @OnClick(R.id.sss)
    public void onViewClicked() {
    }
}
