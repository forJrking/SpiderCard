package com.spider.card.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.spider.card.facade.view.Event;
import com.spider.card.facade.view.EventBusHelper;

import rx.Observable;

/**
 * @description:
 * @author: forjrking
 * @date: 2021/5/17 5:15 下午
 */
public class KeyValueUtil {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor edit;

    private static EventBusHelper<KeyValueEvent> eventBusHelper = EventBusHelper.create();

    public static void init(Context context) {
        preferences = context.getSharedPreferences("app_config", Context.MODE_PRIVATE);
        edit = preferences.edit();
        preferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            eventBusHelper.nextEvent(new KeyValueEvent(key));
        });
    }

    /**
     * DES: 困难度 1,2,4
     */
    public static int getLevel() {
        return preferences.getInt("level", 1);
    }

    public static void setLevel(int level) {
        edit.putInt("level", level).apply();
    }

    public static boolean openVoice() {
        return preferences.getBoolean("openVoice", true);
    }

    public static void setVoiceSwitch(boolean open) {
        edit.putBoolean("openVoice", open).apply();
    }

    public static int getSkin() {
        return preferences.getInt("skin", 1);
    }

    public static void setSkin(int level) {
        edit.putInt("skin", level).apply();
    }

    public static Observable<KeyValueEvent> getEventBus() {
        return eventBusHelper.getEventBus();
    }

    public static class KeyValueEvent extends Event {
        public String key;

        public KeyValueEvent(String key) {
            this.key = key;
        }
    }
}
