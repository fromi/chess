package com.github.fromi.chess.material;

import com.google.common.eventbus.Subscribe;

@FunctionalInterface
public interface PieceMove {
    @Subscribe
    public void handle(Event event);

    public class Event {
        private final Piece.Color color;
        private final Piece.Type piece;
        private final Square origin;
        private final Square destination;

        public Event(Piece.Color color, Piece.Type piece, Square origin, Square destination) {
            this.color = color;
            this.piece = piece;
            this.origin = origin;
            this.destination = destination;
        }

        public Piece.Color getColor() {
            return color;
        }

        public Piece.Type getPiece() {
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
