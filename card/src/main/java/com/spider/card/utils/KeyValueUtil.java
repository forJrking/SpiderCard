package com.spider.card.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @description:
 * @author: forjrking
 * @date: 2021/5/17 5:15 下午
 */
public class KeyValueUtil {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor edit;

    public static void init(Context context) {
        preferences = context.getSharedPreferences("app_config", Context.MODE_PRIVATE);
        edit = preferences.edit();
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

}
