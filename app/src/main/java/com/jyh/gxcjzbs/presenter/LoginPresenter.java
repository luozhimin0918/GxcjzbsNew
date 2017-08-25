package com.jyh.gxcjzbs.presenter;



import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.jyh.gxcjzbs.Login_One;
import com.jyh.gxcjzbs.MainActivity;
import com.jyh.gxcjzbs.R;
import com.jyh.gxcjzbs.WelcomeActivity;
import com.jyh.gxcjzbs.annotion.BindObject;
import com.jyh.gxcjzbs.base.BasePresenter;
import com.jyh.gxcjzbs.base.IBaseView;
import com.jyh.gxcjzbs.common.UrlConstant;
import com.jyh.gxcjzbs.model.VersionMole;
import com.jyh.gxcjzbs.sqlite.SCDataSqlte;
import com.jyh.gxcjzbs.utils.LoginInfoUtils;
import com.jyh.gxcjzbs.view.BounceTopEnter;
import com.jyh.gxcjzbs.view.NormalDialog;
import com.jyh.gxcjzbs.view.OnBtnClickL;
import com.jyh.gxcjzbs.view.SlideBottomExit;
import com.library.base.http.HttpListener;
import com.library.base.http.NewVolleyRequest;
import com.library.base.http.VolleyRequest;
import com.library.util.NetUtils;
import com.library.util.SystemUtil;
import com.library.widget.window.ToastView;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginPresenter extends BasePresenter implements View.OnClickListener {

    @BindObject  Login_One login_oneActivity;
    private RequestQueue queue;
    private NewVolleyRequest request;
    private String from;
    private String name, pwd;


    private boolean isFromLive;
    private BounceTopEnter bas_in;
    private SlideBottomExit bas_out;
    private NormalDialog dialog;
    private boolean isWelcome;
    public LoginPresenter(IBaseView iBaseView) {
        super(iBaseView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initConfig() {
        queue = login_oneActivity.getQueue();
        request= new NewVolleyRequest(mContext, mQueue);
        from =login_oneActivity.getIntent().getStringExtra("from");
        if (from != null) {
            if ("live".equals(from)) {
                isFromLive = true;
                isWelcome = false;
            } else if ("welcome".equals(from)) {
                isWelcome = true;
                isFromLive = false;
            } else if ("self".equals(from)) {
                isFromLive = false;
                isWelcome = false;
            } else if ("null".equals(from)) {
                isFromLive = true;
                isWelcome = false;
            } else {
                isFromLive = false;
                isWelcome = false;

            }
        } else {
            isFromLive = false;
            isWelcome = false;
        }

        if (isFromLive) {
            login_oneActivity.title.setBackgroundColor(Color.TRANSPARENT);
            login_oneActivity.tab.setBackgroundResource(R.mipmap.login_bg);
            login_oneActivity.bg.setBackground(null);
        } else if (isWelcome) {
            login_oneActivity.title.setBackgroundColor(Color.TRANSPARENT);
            login_oneActivity.tab.setBackgroundResource(R.mipmap.login_bg);
            login_oneActivity.bg.setBackground(null);
        } else {
            login_oneActivity.title.setBackgroundColor(Color.rgb(17, 119, 224));
            login_oneActivity.tab.setBackgroundColor(Color.rgb(17, 119, 224));
            login_oneActivity.bg.setBackgroundResource(R.mipmap.login_bg);
        }
        login_oneActivity.title.setVisibility(View.VISIBLE);


        login_oneActivity.back.setOnClickListener(this);

        login_oneActivity.login.setOnClickListener(this);
        login_oneActivity.register.setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public  void  toFinish(){
        if (isFromLive)
            hintKbTwo();
        if (LoginInfoUtils.needRequireLogin(mContext)) {

            bas_in = new BounceTopEnter();
            bas_out = new SlideBottomExit();
            dialog = new NormalDialog(mContext);
            dialog.isTitleShow(false)
                    // 设置背景颜色
                    .bgColor(Color.parseColor("#383838"))
                    // 设置dialog角度
                    .cornerRadius(5)
                    // 设置内容
                    .content("是否确定退出程序?")
                    // 设置居中
                    .contentGravity(Gravity.CENTER)
                    // 设置内容字体颜色
                    .contentTextColor(Color.parseColor("#ffffff"))
                    // 设置线的颜色
                    .dividerColor(Color.parseColor("#222222"))
                    // 设置字体
                    .btnTextSize(15.5f, 15.5f)
                    // 设置取消确定颜色
                    .btnTextColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"))//
                    .btnPressColor(Color.parseColor("#2B2B2B"))//
                    .widthScale(0.85f)//
                    .showAnim(bas_in)//
                    .dismissAnim(bas_out)//
                    .show();

            dialog.setOnBtnClickL(new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    dialog.dismiss();
                }
            }, new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    dialog.dismiss();
                    /**
                     * luo  todo
                     */
                   if (MainActivity.main != null && !MainActivity.main.isDestroyed())
                        MainActivity.main.finish();
                    System.exit(0);
                    System.gc();
                }
            });

        } else {
            if ("self".equals(from)) {
                if (MainActivity.main != null && !MainActivity.main.isDestroyed()) {
                  login_oneActivity.finish();
                } else {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("enter", "self");
                    intent.putExtra("isLoadImg", false);
                    mContext.startActivity(intent);
                    login_oneActivity.finish();
                }
            } else {
                login_oneActivity.finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public  boolean onTouchTo(MotionEvent event){
        if (isFromLive) {
            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                login_oneActivity.finish();
                return true;
            }
        }
        return false;
    }

    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) login_oneActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && login_oneActivity.getCurrentFocus() != null) {
            if (login_oneActivity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(login_oneActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginone_login:
                if (!NetUtils.isNetworkAvailable(mContext)) {
                    ToastView.makeText(mContext, "当前无网络，请稍后再试");
                    return;
                }
                name = login_oneActivity.edit_name.getText().toString().trim();
                pwd = login_oneActivity.edit_pwd.getText().toString().trim();
                Login();
                break;
            case R.id.loginone_register:
                Intent intent = new Intent(mContext, WelcomeActivity.class);//待写 // TODO: 2017/8/24
                if (isFromLive) {
                    intent.putExtra("from", "live");
                } else if (isWelcome) {
                    intent.putExtra("from", "welcome");
                }
                mContext.startActivity(intent);
                break;
            case R.id.self_fk_img:
                login_oneActivity.onBackPressed();
                break;
        }
    }

    private void Login() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("account", name);
        map.put("password", pwd);

        request.doGet(UrlConstant.URL_LOGIN,false, map,new HttpListener<String>(){
            private SCDataSqlte dataSqlte;
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            protected void onResponse(String testDialog) {
                String code;
                try {
                    JSONObject arg0=new JSONObject(testDialog);
                    code = arg0.getString("code");
                    if ("200".equals(code)) {
                        // 登录成功
                        JSONObject data = arg0.getJSONObject("data");
                        // "token": "457cede2bc6aa7a3683af6ffd4cb5a19",
                        // "member_id": "1",
                        // "expired_time": 1462350254,
                        // "user_info": {
                        // "id": "1",
                        // "name": "青之羽",
                        // "rid": "17"

                        LoginInfoUtils.login(mContext, data);

                        if (!isFromLive) {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            if ("self".equals(from))
                                intent.putExtra("enter", "self");
                            if ("zb".equals(from))
                                intent.putExtra("join", true);
                            mContext.startActivity(intent);
                            login_oneActivity.superFinish();
                        } else {
                            login_oneActivity.finish();
                        }
                    } else {
                        // 登录失败,
                        ToastView.makeText(mContext, "登录失败," + arg0.getString("msg"));
                    }
                } catch (JSONException e) {
                    // 登录失败
                    ToastView.makeText(mContext, "登录失败," + e.toString());
                    e.printStackTrace();
                }

            }

            @Override
            protected void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
                // 登录失败
                ToastView.makeText(mContext, "登录失败," +error.toString());
            }
        });
    }
}
