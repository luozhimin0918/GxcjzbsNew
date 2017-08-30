package com.jyh.gxcjzbs.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.jyh.gxcjzbs.R;
import com.jyh.gxcjzbs.base.BaseFragment;
import com.jyh.gxcjzbs.presenter.fragmentP.ZhiboPresenter;
import com.jyh.gxcjzbs.view.PageLoadLayout;
import com.library.widget.rolldotViewpager.RollDotViewPager;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/16.
 */

public class ZhiBoFragment extends BaseFragment {
    @BindView(R.id.convenientBanner) public ConvenientBanner convenientBanner;
    public RollDotViewPager rollDotViewpager;
    @BindView(R.id.page_load) public PageLoadLayout pageLoadLayout;
    @BindView(R.id.rollLiner) public LinearLayout rollLiner;
    @BindView(R.id.playBtn) public ImageView playBtn;
    @BindView(R.id.playBigBtn) public ImageView playBigBtn;
    ZhiboPresenter zhiboPresenter;
    @Override
    protected void onInitialize(Bundle savedInstanceState) {

            setContentView(R.layout.fragment_zb);
        zhiboPresenter=new ZhiboPresenter(this);
        zhiboPresenter.initConfig();
        pageLoadLayout.setOnAfreshLoadListener(new PageLoadLayout.OnAfreshLoadListener() {
            @Override
            public void OnAfreshLoad() {
                zhiboPresenter.initData();
            }
        });
        pageLoadLayout.startLoading();
        zhiboPresenter.initData();

        playBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                joinLive();
            }
        });
    }


}
