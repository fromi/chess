package com.github.fromi.chess.material.util;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.Type.*;

@SuppressWarnings({"UnusedDeclaration", "JavacQuirks"})
public class Pieces {
    public static final Piece _ = null;

    public static final Piece K = new Piece(WHITE, KING);
    public static final Piece Q = new Piece(WHITE, QUEEN);
    public static final Piece B = new Piece(WHITE, BISHOP);
    public static final Piece N = new Piece(WHITE, KNIGHT);
    public static final Piece R = new Piece(WHITE, ROOK);
    public static final Piece P = new Piece(WHITE, PAWN);

    public static final Piece k = new Piece(BLACK, KING);
    public static final Piece q = new Piece(BLACK, QUEEN);
    public static final Piece b = new Piece(BLACK, BISHOP);
    public static final Piece n = new Piece(BLACK, KNIGHT);
    public static final Piece r = new Piece(BLACK, ROOK);
    public static final Piece p = new Piece(BLACK, PAWN);

    public static class Piece {
        private final com.github.fromi.chess.material.Piece.Color color;
        private final com.github.fromi.chess.material.Piece.Type type;

        private Piece(com.github.fromi.chess.material.Piece.Color color, com.github.fromi.chess.material.Piece.Type type) {
            this.color = color;
            this.type = type;
        }

        public com.github.fromi.chess.material.Piece.Color getColor() {
            return color;
        }

        public com.github.fromi.chess.material.Piece.Type getType() {
            return type;
        }

        @Override
        public String toString() {
            return color + " " + type;
        }
    }
}
