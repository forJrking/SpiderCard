package com.spider.card.business;

public interface EventLogger<E> {

  void push(E event);

  E pop();

  boolean isEmpty();

}
