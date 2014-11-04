package com.github.fromi.chess;

import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.google.common.collect.Maps.toMap;
import static java.util.EnumSet.allOf;

import java.util.Map;

import com.github.fromi.chess.material.Board;
import com.github.fromi.chess.material.Piece;
import com.google.common.eventbus.EventBus;

public class Game {
    private final EventBus eventBus = new EventBus();
    private final Map<Piece.Color, Player> players;
    private final Board board;

    public Game() {
        board = new Board();
        players = toMap(allOf(Piece.Color.class), color -> new Player(color, board, eventBus, color == WHITE));
    }

    public void register(Object listener) {
        eventBus.register(listener);
    }

    public Player player(Piece.Color color) {
        return players.get(color);
    }
}
