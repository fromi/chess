package com.github.fromi.chess;

import com.google.common.eventbus.Subscribe;

@FunctionalInterface
public interface NextPlayer {
    @Subscribe
    public void handle(Event event);

    public static class Event {
    }
}
