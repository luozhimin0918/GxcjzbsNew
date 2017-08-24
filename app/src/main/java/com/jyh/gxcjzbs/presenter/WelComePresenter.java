package com.jyh.gxcjzbs.presenter;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jyh.gxcjzbs.AdActivity;
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
import com.jyh.gxcjzbs.model.VersionMole;
import com.jyh.gxcjzbs.view.BounceTopEnter;
import com.jyh.gxcjzbs.view.MaterialDialog;
import com.jyh.gxcjzbs.view.OnBtnClickL;
import com.jyh.gxcjzbs.view.SlideBottomExit;
import com.jyh.gxcjzbs.view.TabEntity;
import com.library.base.http.HttpListener;
import com.library.base.http.NewVolleyRequest;
import com.library.base.http.VolleyRequest;
import com.library.util.SPUtils;
import com.library.util.SystemUtil;
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
        request.doGet(UrlConstant.URL_INDEX,true, new HttpListener<InfoBean>(){

            @Override
            protected void onResponse(InfoBean infoBean) {

                if(infoBean!=null){
                    KLog.json(""+ JSON.toJSONString(infoBean));


                    if(infoBean.getAppinfo()!=null){
                        SPUtils.save(mContext,SpConstant.APPINFO_REQUIRE_LOGIN,infoBean.getAppinfo().getRequire_login());
                    }

                    if(infoBean.getLoad_ad()!=null&& !TextUtils.isEmpty(infoBean.getLoad_ad().getImage())){
                        Intent intent = new Intent(mContext, AdActivity.class);
                        intent.putExtra("image", infoBean.getLoad_ad().getImage());
                        intent.putExtra("url", infoBean.getLoad_ad().getUrl());
                        mContext.startActivity(intent);
                        welcomeActivity.overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        welcomeActivity.finish();
                    }

                }

            }

            @Override
            protected void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        });
    }
    public void showVersionUpdate(){
        request.doGet(UrlConstant.URL_VERSION,false, new HttpListener<VersionMole>(){

            @Override
            protected void onResponse(final VersionMole versionMole) {

                if(versionMole!=null){
                    KLog.json(""+ JSON.toJSONString(versionMole));
                     int versionCode = SystemUtil.getVersionCode(mContext);
                    if(versionMole.getVersionCode()>versionCode){
                        // 启动更新
                        if (testDialog!=null&&!testDialog.isShowing()) {
                            testDialog.content(versionMole.getDescription())//
                                    .btnText("取消", "确定")
                                    .showAnim(bas_in)
                                    .dismissAnim(bas_out)
                                    .show();
                            testDialog.setOnBtnClickL(new OnBtnClickL() {// left btn
                                @Override
                                public void onBtnClick() {
                                    handler.sendEmptyMessageDelayed(111, 100);

                                    testDialog.dismiss();
                                }
                            }, new OnBtnClickL() {// right btn click listener
                                @Override
                                public void onBtnClick() {
                                    if(!TextUtils.isEmpty(versionMole.getUrl())){
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(versionMole.getUrl()));
                                        mContext.startActivity(intent);
                                    }

                                    handler.sendEmptyMessageDelayed(111, 100);
                                    testDialog.dismiss();
                                }
                            });
                            testDialog.setCanceledOnTouchOutside(false);
                        }
                    }else{
                        handler.sendEmptyMessage(111);//initConfig
                    }
                }else{
                    handler.sendEmptyMessage(111);//initConfig
                }

            }

            @Override
            protected void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
                handler.sendEmptyMessage(111);//initConfig
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
                   handler.sendEmptyMessage(222);

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



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 111:
                    initConfig();
                    break;
                case 222:
                    if(backlistening!=null){
                        backlistening.finishW();
                    }
                    break;
                case 333:

                    break;
            }
        }
    };


}
