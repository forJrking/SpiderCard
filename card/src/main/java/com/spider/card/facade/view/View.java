package com.spider.card.facade.view;

public interface View {

  class Event extends com.spider.card.facade.view.Event {
    final View origin;

    public Event(View origin) {
      this.origin = origin;
    }
  }

}
