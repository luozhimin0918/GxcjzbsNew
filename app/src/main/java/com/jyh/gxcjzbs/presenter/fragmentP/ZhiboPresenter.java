package com.jyh.gxcjzbs.presenter.fragmentP;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.jyh.gxcjzbs.MainActivity;
import com.jyh.gxcjzbs.R;
import com.jyh.gxcjzbs.WebActivity;
import com.jyh.gxcjzbs.adapter.MarketGridAdapter;
import com.jyh.gxcjzbs.annotion.BindObject;
import com.jyh.gxcjzbs.base.BaseFragment;
import com.jyh.gxcjzbs.base.BasePresenter;
import com.jyh.gxcjzbs.base.IBaseView;
import com.jyh.gxcjzbs.common.SpConstant;
import com.jyh.gxcjzbs.common.UrlConstant;
import com.jyh.gxcjzbs.fragment.ZhiBoFragment;
import com.jyh.gxcjzbs.model.NavIndextEntity;
import com.library.base.http.HttpListener;
import com.library.base.http.NewVolleyRequest;
import com.library.base.http.VolleyRequest;
import com.library.util.NetUtils;
import com.library.util.SPUtils;
import com.library.util.SystemUtil;
import com.library.widget.rolldotViewpager.RollDotViewPager;
import com.library.widget.rolldotViewpager.RollViewPager;
import com.library.widget.window.AlertDialog;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import static com.jyh.gxcjzbs.R.id.rollLiner;
import static java.security.AccessController.getContext;

/**
 * Created by Administrator on 2017/8/29.
 */

public class ZhiboPresenter extends BasePresenter {

    @BindObject ZhiBoFragment zhiBoFragment;
    private RequestQueue queue;
    private NewVolleyRequest request;

    private static List<NavIndextEntity.DataBean.SlideshowBean> slideShow;
    private List<NavIndextEntity.DataBean.ButtonBean> buttonShow;

    public ZhiboPresenter(IBaseView iBaseView) {
        super(iBaseView);
    }


    public void initConfig(){
        queue = zhiBoFragment.getQueue();
        request = new NewVolleyRequest(mContext, queue);
    }


    public void initData(){
        if(!NetUtils.isNetworkAvailable(mContext)){
            zhiBoFragment.pageLoadLayout.loadError("请检查网络连接");
            return;
        }
        request.doGet(UrlConstant.URL_NAV_INDEX, false, new HttpListener<String>() {
            @Override
            protected void onResponse(String navIndex) {
                KLog.json(navIndex);
                try {

                    NavIndextEntity navinEntity= JSON.parseObject(navIndex.toString(),NavIndextEntity.class);
                    if(navinEntity!=null&&navinEntity.getCode()==200){
                        slideShow=navinEntity.getData().getSlideshow();
                        buttonShow=navinEntity.getData().getButton();
                        if(slideShow!=null&&slideShow.size()>0){
                            optionView();
                        }else{
                            zhiBoFragment.convenientBanner.setVisibility(View.GONE);
                        }
                        if(buttonShow!=null&&buttonShow.size()>0){
                            optionViewTwo();
                        }
                        zhiBoFragment.pageLoadLayout.loadSuccess();

                    }else{
                        zhiBoFragment.pageLoadLayout.loadNoData("暂无数据");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    zhiBoFragment.pageLoadLayout.loadNoData("暂无数据");
                }
            }

            @Override
            protected void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
            }
        });


    }


    private List<String> imageList=new ArrayList<>();
    private Intent intent2 = new Intent(mContext, WebActivity.class);

    private void optionView() {
        imageList.clear();
        for(NavIndextEntity.DataBean.SlideshowBean jj:slideShow){
            imageList.add(jj.getImage());

        }
        if(imageList!=null&&imageList.size()==1){
            zhiBoFragment.convenientBanner.setCanLoop(false);
        }
        zhiBoFragment.convenientBanner.startTurning(4000);
//        convenientBanner.setPageTransformer(new AccordionTransformer());
        zhiBoFragment.convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },imageList)
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        List<String>  banAccessRole =slideShow.get(position).getBan_access_role();
                        String loginRid = SPUtils.getInteger(mContext, SpConstant.USERINFO_LOGIN_RID)+"";//
                        if(banAccessRole!=null&&banAccessRole.size()>0&&banAccessRole.contains(loginRid)){
                            new AlertDialog(mContext)
                                    .builder()
                                    .setCancelable(true)
                                    .setTitle("温馨提醒")
                                    .setMsg(""+slideShow.get(position).getBan_access_msg())
                                    .setPositiveButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                        }
                                    })
                                    .show();


                        }else{
                            if(!TextUtils.isEmpty(slideShow.get(position).getUrl())){
                                intent2.putExtra("url", slideShow.get(position).getUrl());
                                intent2.putExtra("from", "main");
                                intent2.putExtra("title", slideShow.get(position).getTitle());
                                mContext.startActivity(intent2);
                            }

                        }

                    }
                });;


    }

    private void optionViewTwo() {
       /* for(int i=0;i<3;i++){
            NavIndextEntity.DataBean.ButtonBean  buttonBean=new NavIndextEntity.DataBean.ButtonBean();
            buttonBean.setImage("http://cdn0.108tec.com/gxsp/Uploads/Picture/2017-08-07/598801f1a584d.png");
            buttonBean.setTitle("wotu"+i);
            buttonBean.setUrl("wwww.baidu.com");
            buttonShow.add(buttonBean);
        }*/
        zhiBoFragment.rollDotViewpager=new RollDotViewPager(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                SystemUtil.dp2px(mContext, 115));

        zhiBoFragment.rollDotViewpager.setLayoutParams(lp);

        zhiBoFragment.rollDotViewpager.setShowPaddingLine(false);
        RollViewPager recommendView =  zhiBoFragment.rollDotViewpager.getRollViewPager();
        recommendView
                .setGridMaxCount(4)
                .setDataList(buttonShow)
                .setGridViewItemData(new RollViewPager.GridViewItemData() {
                    @Override
                    public void itemData(List dataSubList, GridView gridView) {
                        MarketGridAdapter adapter = new MarketGridAdapter(mContext,dataSubList);
                        gridView.setAdapter(adapter);
                    }
                });
        zhiBoFragment.rollDotViewpager.build();
        zhiBoFragment.rollLiner.addView(zhiBoFragment.rollDotViewpager);

    }

    public class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }
        @Override
        public void UpdateUI(Context context, int position, String data) {
            imageView.setImageResource(R.mipmap.ic_default_adimage);
            Glide.with(context).load(data).into(imageView);
//            ImageLoader.getInstance().displayImage(data,imageView);
        }
    }
}
