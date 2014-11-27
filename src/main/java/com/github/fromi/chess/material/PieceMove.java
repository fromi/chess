package com.github.fromi.chess.material;

import com.google.common.eventbus.Subscribe;

@FunctionalInterface
public interface PieceMove {
    @Subscribe
    public void handle(Event event);

    public class Event {
        private final Piece.Color pieceColor;
        private final Piece.Type pieceType;
        private final Square origin;
        private final Square destination;

        public Event(Piece.Color pieceColor, Piece.Type pieceType, Square origin, Square destination) {
            this.pieceColor = pieceColor;
            this.pieceType = pieceType;
            this.origin = origin;
            this.destination = destination;
        }

        public Piece.Color getPieceColor() {
            return pieceColor;
        }

        public Piece.Type getPieceType() {
            return pieceType;
        }

        public Square getOrigin() {
            return origin;
        }

        public Square getDestination() {
            return destination;
        }
    }
}
