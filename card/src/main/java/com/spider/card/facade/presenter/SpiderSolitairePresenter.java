package com.spider.card.facade.presenter;

import com.spider.card.business.SpiderSolitaire;
import com.spider.card.business.SpiderSolitaire.CardPosition;
import com.spider.card.business.SpiderSolitaire.State.DrawCardsEvent;
import com.spider.card.business.SpiderSolitaire.State.GameCompleteEvent;
import com.spider.card.business.SpiderSolitaire.State.MoveEvent;
import com.spider.card.business.SpiderSolitaire.State.MoveOutEvent;
import com.spider.card.business.SpiderSolitaire.State.UndoEvent;
import com.spider.card.business.SpiderSolitaire.State.UpdateOpenIndexEvent;
import com.spider.card.domain.entity.Card;
import com.spider.card.facade.view.DrawingCardsView;
import com.spider.card.facade.view.GameCompleteView;
import com.spider.card.facade.view.GameScoreView;
import com.spider.card.facade.view.SortedCardsView;
import com.spider.card.facade.view.SpiderSolitaireView;

import java.util.List;

import rx.Observable;

public class SpiderSolitairePresenter {

    private final SpiderSolitaire game;

    private final SpiderSolitaireView view;

    private final DrawingCardsView drawingCardsView;

    private final SortedCardsView sortedCardsView;

    private final GameCompleteView gameCompleteView;

    /**
     * DES: 计步和积分
     */
    private final GameScoreView gameScoreView;

    public SpiderSolitairePresenter(
            SpiderSolitaire game,
            SpiderSolitaireView view,
            DrawingCardsView drawingCardsView,
            SortedCardsView sortedCardsView,
            GameScoreView gameScoreView,
            GameCompleteView gameCompleteView
    ) {
        this.game = game;
        this.view = view;
        this.drawingCardsView = drawingCardsView;
        this.sortedCardsView = sortedCardsView;
        this.gameScoreView = gameScoreView;
        this.gameCompleteView = gameCompleteView;
        init();
    }

    private void init() {
        // init view
        gameScoreView.reset();
        view.init(this);
        updateDrawingCardsView();
        view.setDrawingCardsView(drawingCardsView);
        updateSortedCardsView();
        view.setSortedCardsView(sortedCardsView);
        // bind game events to view
        final Observable<Object> eventBus = game.getState().getEventBus();
        eventBus.ofType(MoveEvent.class).subscribe(moveEvent -> view.moveCards(
                moveEvent.oldPosition.cardStackIndex,
                moveEvent.oldPosition.cardIndex,
                moveEvent.newPosition.cardStackIndex,
                moveEvent.newPosition.cardIndex
        ));
        eventBus.ofType(DrawCardsEvent.class).subscribe(drawCardsEvent -> {
            view.drawCards(drawCardsEvent.drawnCards);
            updateDrawingCardsView();
        });
        eventBus.ofType(MoveOutEvent.class).subscribe(moveOutEvent -> {
            view.moveOutSortedCards(moveOutEvent.cardStackIndex, moveOutEvent.cardIndex);
            updateSortedCardsView();
        });
        eventBus.ofType(UpdateOpenIndexEvent.class).subscribe(event ->
                view.updateOpenIndex(event.cardStackIndex, event.oldOpenIndex, event.newOpenIndex));
        eventBus.ofType(GameCompleteEvent.class).subscribe(gameCompleteEvent -> gameCompleteView.show());
        eventBus.ofType(UndoEvent.class).subscribe(this::onUndo);
        // bind events from drawingCardsView
        drawingCardsView.getEventBus().subscribe(
                drawEvent -> drawCards()
        );
    }

    private void updateDrawingCardsView() {
        drawingCardsView.setDecks(game.getState().cardsForDrawing.size() / 10);
    }

    private void updateSortedCardsView() {
        List<List<Card>> sortedCards = game.getState().sortedCards;
        int size = sortedCards.size();
        Card.Suit suit = (size > 0) ? sortedCards.get(0).get(0).getSuit() : Card.Suit.HEART;
        sortedCardsView.setDecks(size, suit);
    }

    private void onUndo(UndoEvent undoEvent) {
        final Object undoneEvent = undoEvent.undoneEvent;
        Class<?> eventClass = undoneEvent.getClass();
        if (eventClass.equals(MoveEvent.class)) {
            onUndoMove((MoveEvent) undoneEvent);
        } else if (eventClass.equals(DrawCardsEvent.class)) {
            onUndoDraw((DrawCardsEvent) undoneEvent);
        } else if (eventClass.equals(UpdateOpenIndexEvent.class)) {
            onUndoUpdateOpenIndex((UpdateOpenIndexEvent) undoneEvent);
        } else if (eventClass.equals(MoveOutEvent.class)) {
            onUndoMoveOutSortedCards((MoveOutEvent) undoneEvent);
        } // else for other undoneEvents, do nothing
    }

    private void onUndoMove(MoveEvent undoneEvent) {
        view.moveCards(
                undoneEvent.newPosition.cardStackIndex,
                undoneEvent.newPosition.cardIndex,
                undoneEvent.oldPosition.cardStackIndex,
                undoneEvent.oldPosition.cardIndex);
    }

    private void onUndoDraw(DrawCardsEvent undoneEvent) {
        view.undoDrawCards(undoneEvent.drawnCards);
        updateDrawingCardsView();
    }

    private void onUndoUpdateOpenIndex(UpdateOpenIndexEvent undoneEvent) {
        view.updateOpenIndex(
                undoneEvent.cardStackIndex, undoneEvent.newOpenIndex, undoneEvent.oldOpenIndex);
    }

    private void onUndoMoveOutSortedCards(MoveOutEvent undoneEvent) {
        final Card[] movedCards = new Card[13];
        for (int i = 0; i < 13; i++) {
            movedCards[i] = game.getState().getCard(
                    CardPosition.of(undoneEvent.cardStackIndex, undoneEvent.cardIndex + i));
        }
        view.undoMoveOutSortedCards(undoneEvent.cardStackIndex, undoneEvent.cardIndex, movedCards);
        updateSortedCardsView();
    }

    public SpiderSolitaire getGame() {
        return game;
    }

    public SpiderSolitaireView getView() {
        return view;
    }

    public boolean canMoveCards(int oldCardStackIndex, int oldCardIndex, int newCardStackIndex) {
        return game.canMove(
                CardPosition.of(oldCardStackIndex, oldCardIndex),
                CardPosition.of(newCardStackIndex, 0)
        );
    }

    public void moveCards(int oldCardStackIndex, int oldCardIndex, int newCardStackIndex) {
        if (!canMoveCards(oldCardStackIndex, oldCardIndex, newCardStackIndex)) {
            return;
        }
        gameScoreView.gauge();
        game.move(CardPosition.of(oldCardStackIndex, oldCardIndex),
                CardPosition.of(newCardStackIndex, 0));
    }

    public boolean canDrawCards() {
        return game.canDraw();
    }

    public void drawCards() {
        if (!canDrawCards()) {
            return;
        }
        game.draw();
    }

    public boolean canUndo() {
        return game.canUndo();
    }

    public boolean undo() {
        return game.undo();
    }
}
