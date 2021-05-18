package com.spider.card.facade.view;

import com.spider.card.domain.entity.Card;

public interface SortedCardsView extends View {

    void setDecks(int decks, Card.Suit suit);

}
