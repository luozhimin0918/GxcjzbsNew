package com.jyh.gxcjzbs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyh.gxcjzbs.utils.LoginInfoUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 广告界面
 *
 * @author beginner
 * @version 1.0
 * @date 创建时间：2015年7月21日 下午4:53:38
 */
public class AdActivity extends FragmentActivity implements OnClickListener {

    private ImageView img;
    private String url;
    private Intent intent;
    private String imgpath;

    private boolean isNeedLogin;

    private Handler handler = new Handler(new Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 1:
                    countDown(5).subscribe(new Subscriber<Integer>() {
                        @Override
                        public void onCompleted() {
                           startA_ctivity();
                        }

                        @Override
                        public void onError(Throwable e) {
                            startA_ctivity();
                        }

                        @Override
                        public void onNext(Integer integer) {
                            ad_btn.setText(integer+"S 跳过");
                        }
                    });
                    break;
                case 2:
                    startA_ctivity();
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    private Observable<Integer> countDown(int time){
        final int countTime = time;
        return Observable.interval(0,1, TimeUnit.SECONDS)
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long aLong) {
                        return countTime-aLong.intValue();
                    }
                })
                .take(countTime+1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());

    }
    private WebView webView;
    private TextView ad_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ad);
        ad_btn = (TextView) findViewById(R.id.ad_btn);
        ad_btn.setOnClickListener(this);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String titl) {
                // TODO Auto-generated method stub
                super.onReceivedTitle(view, titl);
                ((TextView) findViewById(R.id.ad_title_tv)).setText(titl);
            }

        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && !url.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivityForResult(intent, 200);
                }
            }
        });

        if (LoginInfoUtils.isLogin(this)) {
            // 登录有效
            isNeedLogin = false;
        } else {
            isNeedLogin = LoginInfoUtils.needRequireLogin(this);
        }

        imgpath = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");
        img = (ImageView) findViewById(R.id.img);
        Glide.with(this).load(imgpath).into(img);
        img.setOnClickListener(this);
        findViewById(R.id.ad_img_back).setOnClickListener(this);
        intent = new Intent(AdActivity.this, MainActivity.class);

        handler.sendEmptyMessage(1);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img:
                // 广告跳转
//                Log.i("url", url);
//                if (url != null && !url.equals("")) {
//                    timer.cancel();
//                    timer.purge();
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivityForResult(intent, 200);
//                }
                if (url != null && !url.equals("")) {

                    ad_btn.setVisibility(View.GONE);
                    img.setVisibility(View.GONE);
                    findViewById(R.id.webviewId).setVisibility(View.VISIBLE);
                    webView.loadUrl(url);
                }
                break;
            case R.id.ad_img_back:
            case R.id.ad_btn:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startA_ctivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            startA_ctivity();
        }
    }

    private void startA_ctivity() {
        if (isNeedLogin) {
            Intent LoginIntent = new Intent(AdActivity.this, MainActivity.class);//暂且用MainActivity 代替 Login_one
            LoginIntent.putExtra("from", "welcome");
            startActivity(LoginIntent);
            finish();
        } else {
            startActivity(intent);
            finish();
        }
    }

}
