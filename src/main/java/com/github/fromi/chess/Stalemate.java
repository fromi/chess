package com.github.fromi.chess;

import com.google.common.eventbus.Subscribe;

@FunctionalInterface
public interface Stalemate {
    @Subscribe
    public void handle(Event event);

    class Event {
    }
}
