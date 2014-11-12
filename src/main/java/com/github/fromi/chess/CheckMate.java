package com.github.fromi.chess;

import com.github.fromi.chess.material.Piece;
import com.google.common.eventbus.Subscribe;

@FunctionalInterface
public interface CheckMate {
    @Subscribe
    void handle(Event event);

    class Event {
        private final Piece.Color winner;

        public Event(Piece.Color winner) {
            this.winner = winner;
        }

        public Piece.Color getWinner() {
            return winner;
        }
    }
}
