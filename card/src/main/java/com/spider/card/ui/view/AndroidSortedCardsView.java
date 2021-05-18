package com.spider.card.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.spider.card.R;
import com.spider.card.ui.widget.CardStackLayout;

import com.spider.card.domain.entity.Card;
import com.spider.card.facade.view.SortedCardsView;

import static com.spider.card.ui.widget.WidgetUtils.withNumberOfChildren;


public class AndroidSortedCardsView extends CardStackLayout implements SortedCardsView {

    public AndroidSortedCardsView(Context context) {
        super(context);
        init();
    }

    public AndroidSortedCardsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AndroidSortedCardsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setDelta(getResources().getDimensionPixelSize(R.dimen.piled_cards_delta));
    }

    @Override
    public void setDecks(int decks, Card.Suit suit) {
        setVisibility(decks > 0 ? VISIBLE : INVISIBLE);
        withNumberOfChildren(
                this,
                decks,
                () -> new AndroidCardView(getContext(), new Card(suit, Card.Rank.ACE), true)
        );
    }

}
