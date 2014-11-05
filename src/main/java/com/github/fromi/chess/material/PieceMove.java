package com.github.fromi.chess.material;

import com.google.common.eventbus.Subscribe;

@FunctionalInterface
public interface PieceMove {
    @Subscribe
    public void handle(Event event);

    public class Event {
        private final Piece piece;
        private final Square origin;
        private final Square destination;

        public Event(Piece piece, Square origin, Square destination) {
            this.piece = piece;
            this.origin = origin;
            this.destination = destination;
        }

        public Piece getPiece() {
            return piece;
        }

        public Square getOrigin() {
            return origin;
        }

        public Square getDestination() {
            return destination;
        }
    }
}
