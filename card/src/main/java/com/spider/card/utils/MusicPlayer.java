package com.spider.card.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.spider.card.R;

import java.util.HashMap;

public class MusicPlayer {
    private Context mContext;
    private static MusicPlayer sInstance;
    private final HashMap<Integer, Integer> pool = new HashMap<>();
    private SoundPool mSp;

    private MusicPlayer(Context context) {
        mContext = context;
        mSp = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
        int deckId = mSp.load(mContext, R.raw.solitaire_deck, 1);
        pool.put(R.raw.solitaire_deck, deckId);
    }

    public static MusicPlayer getInstance(Context context) {
        if (sInstance == null)
            sInstance = new MusicPlayer(context.getApplicationContext());
        return sInstance;
    }

    public void play(int raw) {
        if (KeyValueUtil.openVoice()) {
            int id = pool.get(raw);
            mSp.play(id, 1, 1, 0, 0, 1.8f);
        }

        if (!pool.containsKey(R.raw.solitaire_win)) {
            int winId = mSp.load(mContext, R.raw.solitaire_win, 1);
            pool.put(R.raw.solitaire_win, winId);
        }
        if (!pool.containsKey(R.raw.solitaire_flip)) {
            int flipId = mSp.load(mContext, R.raw.solitaire_flip, 1);
            pool.put(R.raw.solitaire_flip, flipId);
        }
        if (!pool.containsKey(R.raw.solitaire_shuffle)) {
            int flipId = mSp.load(mContext, R.raw.solitaire_shuffle, 1);
            pool.put(R.raw.solitaire_shuffle, flipId);
        }
    }
}