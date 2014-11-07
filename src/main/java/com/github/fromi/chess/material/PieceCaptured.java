package com.github.fromi.chess.material;

import com.google.common.eventbus.Subscribe;

@FunctionalInterface
public interface PieceCaptured {
    @Subscribe
    public void handle(Event event);

    public class Event {

        private final Piece piece;
        private final Square position;

        public Event(Piece piece, Square position) {
            this.piece = piece;
            this.position = position;
        }

        public Piece getPiece() {
            return piece;
        }

        public Square getPosition() {
            return position;
        }
    }
}
