package com.spider.card.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.spider.card.facade.view.GameScoreView;

public class AndroidGameScoreView extends androidx.appcompat.widget.AppCompatTextView implements GameScoreView {

    private int num = 0;

    public AndroidGameScoreView(Context context) {
        super(context);
    }

    public AndroidGameScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AndroidGameScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void reset() {
        num = 0;
        setText(num + "");
    }

    @Override
    public void gauge() {
        num++;
        setText(num + "");
    }
}
