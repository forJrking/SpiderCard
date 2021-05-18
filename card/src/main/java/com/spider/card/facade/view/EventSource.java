package com.spider.card.facade.view;

import rx.Observable;

public interface EventSource<T extends Event> {

  Observable<T> getEventBus();

}
