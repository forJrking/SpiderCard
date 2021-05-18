package com.spider.card.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.spider.card.R;
import com.spider.card.domain.entity.Card;
import com.spider.card.facade.view.DrawingCardsView;
import com.spider.card.facade.view.EventBusHelper;
import com.spider.card.ui.widget.CardStackLayout;
import com.spider.card.utils.MusicPlayer;

import rx.Observable;

import static com.spider.card.ui.widget.WidgetUtils.withNumberOfChildren;


public class AndroidDrawingCardsView extends CardStackLayout implements DrawingCardsView {

    private final EventBusHelper<DrawEvent> eventBusHelper = EventBusHelper.create();

    public AndroidDrawingCardsView(Context context) {
        super(context);
        init();
    }

    public AndroidDrawingCardsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AndroidDrawingCardsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setDelta(getResources().getDimensionPixelSize(R.dimen.piled_cards_delta));
        super.setOnClickListener(v -> {
            MusicPlayer.getInstance(getContext()).play(R.raw.solitaire_shuffle);
            eventBusHelper.nextEvent(new DrawEvent(this));
        });
    }

    @Override
    public void setDecks(int decks) {
        setVisibility(decks > 0 ? VISIBLE : INVISIBLE);
        withNumberOfChildren(
                this,
                decks,
                () -> new AndroidCardView(getContext(), new Card(Card.Suit.HEART, Card.Rank.KING), false)
        );
    }

    @Override
    public Observable<DrawEvent> getEventBus() {
        return eventBusHelper.getEventBus();
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        throw new UnsupportedOperationException();
    }

}
