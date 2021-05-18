package com.spider.card.ui.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.percentlayout.widget.PercentFrameLayout;
import androidx.percentlayout.widget.PercentLayoutHelper;

import com.spider.card.R;

import com.spider.card.domain.entity.Card;


public class AndroidCardView extends PercentFrameLayout {

    private final AppCompatImageView contentView;

    private final Card card;

    private boolean open;

    public AndroidCardView(Context context, Card card, boolean open) {
        super(context);
        contentView = new AppCompatImageView(context);
        this.card = card;
        this.open = open;
        init();
    }

    private void init() {
        contentView.setBackgroundResource(R.drawable.card_background);
        contentView.setScaleType(ImageView.ScaleType.FIT_XY);
        updateContentView();

        addView(contentView, 0, 0);
        final LayoutParams layoutParams = (LayoutParams) contentView.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        final PercentLayoutHelper.PercentLayoutInfo layoutInfo = layoutParams.getPercentLayoutInfo();
        // https://en.wikipedia.org/wiki/Standard_52-card_deck
        layoutInfo.aspectRatio = 0.71428571428571428571428571428571f;// 2.5 / 3.5
        layoutInfo.widthPercent = 1f;
    }

    public Card getCard() {
        return card;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        if (this.open == open) {
            return;
        }
        this.open = open;
        updateContentView();
    }

    private void updateContentView() {
        if (open) {
            contentView.setImageResource(toDrawableRes(card));
        } else {
            contentView.setImageResource(R.drawable.card_back);
        }
    }

    @DrawableRes
    private int toDrawableRes(Card card) {
        return getResources()
                .getIdentifier(toDrawableResName(card), "drawable", getContext().getPackageName());
    }

    /**
     * <string>Note</string>: not support Joker cards
     */
    private static String toDrawableResName(Card card) {
        return "card_" + card.getSuit().name().toLowerCase() + "_" + card.getRank().getId();
    }

}
