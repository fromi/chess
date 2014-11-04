package com.github.fromi.chess.material;

import com.github.fromi.chess.Color;
import com.google.common.eventbus.Subscribe;

@FunctionalInterface
public interface PieceMove {
    @Subscribe
    public void handle(Event event);

    public class Event {
        private final Color color;
        private final Piece piece;
        private final Square origin;
        private final Square destination;

        public Event(Color color, Piece piece, Square origin, Square destination) {
            this.color = color;
            this.piece = piece;
            this.origin = origin;
            this.destination = destination;
        }

        public Color getColor() {
            return color;
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
