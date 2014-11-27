package com.github.fromi.chess.material;

import com.google.common.eventbus.Subscribe;

@FunctionalInterface
public interface Promotion {

    @Subscribe
    void handle(Event event);

    public class Event {
        private final Piece.Type pieceType;
        private final Square position;

        public Event(Piece.Type pieceType, Square position) {
            this.pieceType = pieceType;
            this.position = position;
        }

        public Piece.Type getPieceType() {
            return pieceType;
        }

        public Square getPosition() {
            return position;
        }
    }
}
