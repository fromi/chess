package com.github.fromi.chess;

import static com.github.fromi.chess.Color.WHITE;
import static com.google.common.collect.Maps.toMap;
import static java.util.EnumSet.allOf;

import java.util.Map;

import com.google.common.eventbus.EventBus;

public class Game {
    private final EventBus eventBus = new EventBus();
    private final Map<Color, Player> players;

    public Game() {
        players = toMap(allOf(Color.class), color -> new Player(color, color == WHITE, eventBus));
    }

    public void register(Object listener) {
        eventBus.register(listener);
    }

    public Player player(Color color) {
        return players.get(color);
    }
}
