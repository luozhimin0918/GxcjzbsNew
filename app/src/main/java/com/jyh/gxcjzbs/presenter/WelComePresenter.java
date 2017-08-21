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
import com.jyh.gxcjzbs.common.SpConstant;
import com.jyh.gxcjzbs.common.UrlConstant;
import com.jyh.gxcjzbs.fragment.RankFragment;
import com.jyh.gxcjzbs.model.InfoBean;
import com.jyh.gxcjzbs.view.BounceTopEnter;
import com.jyh.gxcjzbs.view.MaterialDialog;
import com.jyh.gxcjzbs.view.OnBtnClickL;
import com.jyh.gxcjzbs.view.SlideBottomExit;
import com.jyh.gxcjzbs.view.TabEntity;
import com.library.base.http.HttpListener;
import com.library.base.http.NewVolleyRequest;
import com.library.base.http.VolleyRequest;
import com.library.util.SPUtils;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


public class WelComePresenter extends BasePresenter {

    @BindObject WelcomeActivity welcomeActivity;
    private RequestQueue queue;
    private NewVolleyRequest request;
    private  MaterialDialog testDialog;// 网络异常提示Dialog
    private BounceTopEnter bas_in;
    private SlideBottomExit bas_out;

    public WelComePresenter(IBaseView iBaseView) {
        super(iBaseView);
    }

    public void initDefault() {
        MobclickAgent.setDebugMode(false);
        queue = welcomeActivity.getQueue();
        bas_in = new BounceTopEnter();
        bas_out = new SlideBottomExit();
        testDialog = new MaterialDialog(mContext);
        if (request == null) {
            request = new NewVolleyRequest(mContext, mQueue);
            request.setTag(getClass().getName());
        }

    }

    public void initConfig() {
        request.doGet(UrlConstant.URL_INDEX, new HttpListener<InfoBean>(){

            @Override
            protected void onResponse(InfoBean infoBean) {

                if(infoBean!=null){
                    KLog.json(""+ JSON.toJSONString(infoBean));
                    if(infoBean.getAppinfo()!=null){
                        SPUtils.save(mContext,SpConstant.APPINFO_REQUIRE_LOGIN,infoBean.getAppinfo().getRequire_login());
                    }
                }

            }

            @Override
            protected void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        });
    }

    public  void  showNotNetwork(){
        if (testDialog!=null&&!testDialog.isShowing()) {
            testDialog
                    .btnNum(1).content("网络异常，请检查网络。")
                    .btnText("确定")//
                    .showAnim(bas_in)//
                    .dismissAnim(bas_out)//
                    .show();
            testDialog.setOnBtnClickL(new OnBtnClickL() {

                @Override
                public void onBtnClick() {
                    // TODO Auto-generated method stub
                    if(testDialog!=null){
                        testDialog.dismiss();
                    }
                    if(backlistening!=null){
                        backlistening.finishW();
                    }

                }
            });
            testDialog.setCanceledOnTouchOutside(false);
        }
    }

    public  BackListening  backlistening;

    public  interface  BackListening {
        public void  finishW();
    }
    public  void  setBackListening(BackListening backListen){
        this.backlistening=backListen;
    }



}
