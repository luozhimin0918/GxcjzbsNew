package com.jyh.gxcjzbs.presenter;

import com.android.volley.RequestQueue;
import com.jyh.gxcjzbs.MainActivity;
import com.jyh.gxcjzbs.annotion.BindObject;
import com.jyh.gxcjzbs.base.BasePresenter;
import com.jyh.gxcjzbs.base.IBaseView;
import com.library.base.http.VolleyRequest;



public class MainPresenter extends BasePresenter {

    @BindObject
    MainActivity mainActivity;
    private RequestQueue queue;
    private VolleyRequest request;


    public MainPresenter(IBaseView iBaseView) {
        super(iBaseView);
    }

    public void initConfig() {
        queue = mainActivity.getQueue();
        request = new VolleyRequest(mContext, queue);
    }




}
