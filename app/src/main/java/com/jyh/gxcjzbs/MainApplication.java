package com.jyh.gxcjzbs;

import android.app.Application;

import com.jyh.gxcjzbs.model.ChatMsgEntity;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(BuildConfig.LOG_DEBUG);
    }


    private List<ChatMsgEntity> chatMsgEntities;

    public List<ChatMsgEntity> getChatMsgEntities() {
        return chatMsgEntities;
    }

    public void setChatMsgEntities(List<ChatMsgEntity> chatMsgEntities) {
        this.chatMsgEntities = chatMsgEntities;
    }
}
