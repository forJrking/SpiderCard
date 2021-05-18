package com.spider.card.business;

import com.spider.card.domain.entity.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * @Time: 2021/5/18 12:00 下午
 * @Version: 1.0.0  数据构建model
 * <lp>
 * @UpdateUser: SpiderSolitaires
 * @UpdateDate: 2021/5/18 12:00 下午
 * @UpdateRemark: SpiderSolitaires
 **/
public class SpiderSolitaires {

    private static final int MAX_CARDS = 52;

    public static List<Card> newCards(int decks, Card.Suit[] suit) {
        int typeCount = 4;
        List<Card> result = new ArrayList<>(decks * MAX_CARDS);
        for (Card.Rank rank : Card.Rank.values()) {
            for (int i = 0; i < typeCount * decks; i++) {
                result.add(new Card(suit[i % typeCount], rank));
            }
        }
        return result;
    }

    public static List<Card> random(List<Card> cards) {
        List<Card> copy = new ArrayList<>(cards);
        List<Card> result = new ArrayList<>(cards.size());
        while (!copy.isEmpty()) {
            // DES：随机获取到
            final int location = (int) (Math.random() * copy.size());
            result.add(copy.remove(location));
        }
        return result;
    }

    public static SpiderSolitaire.State newGameState(int level) {
        // DES：使用2副牌
        int decks = 2;
        List<Card> cards = null;
        // DES：1个花色 2个 4个
        if (level == 0) {
            //明牌
            return getSampleSpiderSolitaireState();
        } else if (level == 1) {
            Card.Suit[] suits = Card.Suit.values();
            final int location = (int) (Math.random() * suits.length);
            //4个使用同种花色
            cards = newCards(decks, new Card.Suit[]{suits[location], suits[location], suits[location], suits[location]});
        } else if (level == 2) {
            Card.Suit[] suit = {Card.Suit.HEART, Card.Suit.SPADE, Card.Suit.HEART, Card.Suit.SPADE};
            cards = newCards(decks, suit);
        } else if (level == 4) {
            Card.Suit[] suits = Card.Suit.values();
            cards = newCards(decks, suits);
        }

        //打乱卡片
        cards = random(cards);
        SpiderSolitaire.State result = new SpiderSolitaire.State();
        for (int i = 0; i < 54; i++) {
            result.cardStacks.get(i % 10).cards.add(cards.remove(cards.size() - 1));
        }
        for (SpiderSolitaire.State.CardStack stack : result.cardStacks) {
            stack.openIndex = stack.cards.size() - 1;
        }
        result.cardsForDrawing.addAll(cards);
        return result;
    }

    public static SpiderSolitaire newGame(int level) {
        return new SpiderSolitaire(newGameState(level), new RamEventLogger<>());
    }

    public static SpiderSolitaire.State getSampleSpiderSolitaireState() {
        SpiderSolitaire.State result = newGameState(1);
        for (SpiderSolitaire.State.CardStack stack : result.cardStacks) {
            stack.openIndex = 0;
        }
        return result;
    }

}
