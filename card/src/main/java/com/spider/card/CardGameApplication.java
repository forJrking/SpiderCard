package com.spider.card;

import android.app.Application;
import android.content.Context;

import com.spider.card.utils.KeyValueUtil;

public class CardGameApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KeyValueUtil.init(this);
    }
}
