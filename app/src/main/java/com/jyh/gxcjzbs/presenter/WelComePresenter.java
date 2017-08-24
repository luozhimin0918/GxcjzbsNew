package com.jyh.gxcjzbs.presenter;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.jyh.gxcjzbs.sqlite.SCDataSqlte;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WelComePresenter extends BasePresenter {

    @BindObject WelcomeActivity welcomeActivity;
    private RequestQueue queue;
    private NewVolleyRequest request;
    private  MaterialDialog testDialog;// 网络异常提示Dialog
    private BounceTopEnter bas_in;
    private SlideBottomExit bas_out;
    private boolean isFirstLoading = true;// 用以防止数据库重复读写
    SCDataSqlte     sqlOpenHelper;
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


                   InfoBean.AppinfoBean  appinfoBean = infoBean.getAppinfo();
                    if(appinfoBean!=null){
                        SPUtils.save(mContext,SpConstant.APPINFO_REQUIRE_LOGIN,appinfoBean.getRequire_login());
                        SPUtils.save(mContext, SpConstant.APPINFO_APPID, appinfoBean.getAppid());
                        SPUtils.save(mContext, SpConstant.APPINFO_APPNAME, appinfoBean.getName());
                        SPUtils.save(mContext, SpConstant.APPINFO_GATE, appinfoBean.getGate());
                        SPUtils.save(mContext, SpConstant.APPINFO_KEFU_URL, appinfoBean.getKefu_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_USERLIST_URL, appinfoBean.getUserlist_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_IMAGES_URL, appinfoBean.getImages_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_COURSE_URL, appinfoBean.getCourse_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_SUMMARY_URL, appinfoBean.getSummary_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_BULLETIN_URL, appinfoBean.getBulletin_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_FN_NAV_URL, appinfoBean.getFn_nav_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_FN_URL,appinfoBean.getFn_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_CJRL_URL, appinfoBean.getCjrl_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_ALTERS_URL, appinfoBean.getAlters_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_HQ_URL, appinfoBean.getHq_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_UPLOAD_IMAGES_URL, appinfoBean.getUpload_images_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_REGISTER_URL, appinfoBean.getRegister_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_CL_URL, appinfoBean.getTactics_url());
                        SPUtils.save(mContext, SpConstant.APPINFO_CL_BAN, appinfoBean.getTactics_ban_rid());
                        SPUtils.save(mContext, SpConstant.APPINFO_INDEX_BG, appinfoBean.getIndex_bg());
                        SPUtils.save(mContext, SpConstant.APPINFO_EMOJI_VERSION_NEW, appinfoBean.getPhiz_version());
                    }

                    // userinfo 用户信息
                    InfoBean.UserinfoBean userinfoJob = infoBean.getUserinfo();
                    // videoinfo 直播室信息
                    InfoBean.VideoInfo2Bean videoinfoJob = infoBean.getVideo_info2();
                    InfoBean.VideoInfo2Bean.LiveGenseeBeanX detailJob = null;
                    InfoBean.VideoInfo2Bean.Live108BeanX qinjia = null;
                    if (videoinfoJob.getType().equals("live_gensee")) {
                        detailJob = videoinfoJob.getLive_gensee();
                    } else if (videoinfoJob.getType().equals("live_108")) {
                        qinjia = videoinfoJob.getLive_108();
                    }
                    // roomrole 用户角色信息
                  List<InfoBean.RoomroleBean> roomrolesJoA = infoBean.getRoomrole();




                    SPUtils.save(mContext, SpConstant.USERINFO_NAME, userinfoJob.getName());
                    SPUtils.save(mContext, SpConstant.USERINFO_RID, userinfoJob.getRid());
                    SPUtils.save(mContext,SpConstant.USERINFO_LOGIN_RID,userinfoJob.getRid());
                    SPUtils.save(mContext, SpConstant.USERINFO_UID, userinfoJob.getId());

                    SPUtils.save(mContext, SpConstant.VIDEO_TYPE,videoinfoJob.getType());
                    if (videoinfoJob.getType().equals("live_108")) {
                        if (qinjia != null || !"".equals(qinjia)) {
                            // gensee
                            SPUtils.save(mContext, SpConstant.VIDEO_GENSEE_ID, "");
                            SPUtils.save(mContext, SpConstant.VIDEO_GENSEE_ROOMID, "");
                            SPUtils.save(mContext, SpConstant.VIDEO_GENSEE_PWD, "");
                            SPUtils.save(mContext, SpConstant.VIDEO_GENSEE_SITE, "");
                            SPUtils.save(mContext, SpConstant.VIDEO_GENSEE_CTXZ, "");
                            // Gotye
                            SPUtils.save(mContext, SpConstant.VIDEO_GOTYEROOMID, qinjia.getROOMID());
                            SPUtils.save(mContext, SpConstant.VIDEO_GOTYEPASSWORD, qinjia.getPASSWORD());
                        }
                    } else {
                        if (detailJob != null || !"".equals(detailJob)) {
                            // gensee
                            SPUtils.save(mContext, SpConstant.VIDEO_GENSEE_ID, detailJob.getID());
                            SPUtils.save(mContext, SpConstant.VIDEO_GENSEE_ROOMID, detailJob.getROOMID());
                            SPUtils.save(mContext, SpConstant.VIDEO_GENSEE_PWD, detailJob.getPASSWORD());
                            SPUtils.save(mContext, SpConstant.VIDEO_GENSEE_SITE, detailJob.getSITE());
                            SPUtils.save(mContext, SpConstant.VIDEO_GENSEE_CTXZ, detailJob.getCTX());
                            // Gotye
                            SPUtils.save(mContext, SpConstant.VIDEO_GOTYEROOMID, "");
                            SPUtils.save(mContext, SpConstant.VIDEO_GOTYEPASSWORD, "");
                        }
                    }

                    if (isFirstLoading) {
                        // 防止多次存储数据
                             sqlOpenHelper = new SCDataSqlte(mContext);
                        SQLiteDatabase dbw = sqlOpenHelper.getWritableDatabase();

                        Map<String, String> map = new HashMap<String, String>();
                        for (int i1 = 0; i1 < roomrolesJoA.size(); i1++) {

                            InfoBean.RoomroleBean roomRole = roomrolesJoA.get(i1);
                            if ("1".equals(roomRole.getId())) {
                                SPUtils.save(mContext, SpConstant.USERINFO_R_NAME, roomRole.getName());
                                SPUtils.save(mContext, SpConstant.USERINFO_LIMIT_CHAT_TIME, roomRole.getLimit_chat_time());
                                SPUtils.save(mContext, SpConstant.USERINFO_POWER_PRIVATE, roomRole.getPower_whisper());
                                SPUtils.save(mContext, SpConstant.USERINFO_LIMIT_COLORBAR_TIME, roomRole.getLimit_colorbar_time());
                                SPUtils.save(mContext, SpConstant.USERINFO_IMAGE, roomRole.getImage());
                                SPUtils.save(mContext, SpConstant.USERINFO_POWER_VISIT_ROOM, roomRole.getPower_visit_room());
                            }
                            dbw.execSQL(
                                    "insert into roomrole (id,name,type, limit_chat_time, power_whisper,"
                                            + "limit_colorbar_time,power_upload_pic,limit_account_time,"
                                            + "status,sort,power_visit_room,style_chat_text,image) values (?,?,?,?,?,?,?,?,?,?,?,?,?);",
                                    new Object[]{roomRole.getId(), roomRole.getName(), roomRole.getType(), roomRole.getLimit_chat_time(),
                                            roomRole.getPower_whisper(), roomRole.getLimit_colorbar_time(), roomRole.getPower_upload_pic(),
                                            roomRole.getLimit_account_time(), roomRole.getStatus(), roomRole.getSort(),
                                            roomRole.getPower_visit_room(), roomRole.getStyle_chat_text(), roomRole.getImage()});
                        }
                        dbw.close();
                        isFirstLoading = false;
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
