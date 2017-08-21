package com.jyh.gxcjzbs.presenter;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jyh.gxcjzbs.MainActivity;
import com.jyh.gxcjzbs.R;
import com.jyh.gxcjzbs.WelcomeActivity;
import com.jyh.gxcjzbs.annotion.BindObject;
import com.jyh.gxcjzbs.base.BasePresenter;
import com.jyh.gxcjzbs.base.IBaseView;
import com.jyh.gxcjzbs.common.UrlConstant;
import com.jyh.gxcjzbs.fragment.RankFragment;
import com.jyh.gxcjzbs.model.InfoBean;
import com.jyh.gxcjzbs.view.TabEntity;
import com.library.base.http.HttpListener;
import com.library.base.http.NewVolleyRequest;
import com.library.base.http.VolleyRequest;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;


public class WelComePresenter extends BasePresenter {

    @BindObject WelcomeActivity welcomeActivity;
    private RequestQueue queue;
    private NewVolleyRequest request;

    public WelComePresenter(IBaseView iBaseView) {
        super(iBaseView);
    }

    public void initConfig() {
        queue = welcomeActivity.getQueue();
        if (request == null) {
            request = new NewVolleyRequest(mContext, mQueue);
            request.setTag(getClass().getName());
        }
        request.doGet(UrlConstant.URL_INDEX, new HttpListener<InfoBean>(){

            @Override
            protected void onResponse(InfoBean infoBean) {
                KLog.json(""+ JSON.toJSONString(infoBean));
            }

            @Override
            protected void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        });
    }

}
