package com.github.fromi.chess.material;

import com.google.common.eventbus.Subscribe;

@FunctionalInterface
public interface PieceCaptured {
    @Subscribe
    public void handle(Event event);

    public class Event {

        private final Piece.Color pieceColor;
        private final Piece.Type pieceType;
        private final Square position;

        public Event(Piece.Color pieceColor, Piece.Type pieceType, Square position) {
            this.pieceColor = pieceColor;
            this.pieceType = pieceType;
            this.position = position;
        }

        public Piece.Color getPieceColor() {
            return pieceColor;
        }

        public Piece.Type getPieceType() {
            return pieceType;
        }

        public Square getPosition() {
            return position;
        }
    }
}
