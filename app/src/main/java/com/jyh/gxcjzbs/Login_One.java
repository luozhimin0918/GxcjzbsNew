package com.jyh.gxcjzbs;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyh.gxcjzbs.base.BaseActivity;
import com.jyh.gxcjzbs.presenter.LoginPresenter;
import com.jyh.gxcjzbs.utils.SystemUtils;


import butterknife.BindView;

/**
 * 强制登录界面
 *
 * @author Administrator
 */
public class Login_One extends BaseActivity  {

    @BindView(R.id.self_fk_img) public LinearLayout back;
    @BindView(R.id.title_tv) public TextView titleTv;
    @BindView(R.id.title) public RelativeLayout title;
    @BindView(R.id.login_logo) public ImageView loginLogo;
    @BindView(R.id.login_name) public EditText edit_name;
    @BindView(R.id.login_pwd) public EditText edit_pwd;
    @BindView(R.id.loginone_login) public Button login;
    @BindView(R.id.loginone_register) public Button register;
    @BindView(R.id.bg) public LinearLayout bg;
    @BindView(R.id.tab) public LinearLayout tab;

    private LoginPresenter loginPresenter;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        loginPresenter=new LoginPresenter(this);
        loginPresenter.initConfig();
    }





    public void superFinish() {
        super.finish();
    }




    private void setDialogStyle() {
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        float size = SystemUtils.getDpi(this);
        LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 1.0); // 宽度设置为屏幕的1.0
        p.alpha = 1.0f; // 设置本身透明度
        p.dimAmount = 0.0f; // 设置黑暗度
        p.height = (int) ((d.getHeight() - (float) 40 * size - SystemUtils.getStatuBarHeight(this)) / 2.75 * 1.75);
        getWindow().setAttributes(p); // 设置生效
        getWindow().setGravity(Gravity.BOTTOM); // 设置在底部
        getWindow().setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL, LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
         loginPresenter.onTouchTo(event);
        return super.onTouchEvent(event);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void finish() {
        // TODO Auto-generated method stub
         loginPresenter.toFinish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

}
