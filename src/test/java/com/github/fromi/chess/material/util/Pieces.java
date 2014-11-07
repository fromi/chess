package com.github.fromi.chess.material.util;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.PIECES;
import static com.github.fromi.chess.material.Piece.Type.*;

import com.github.fromi.chess.material.Piece;

@SuppressWarnings("UnusedDeclaration")
public class Pieces {
    public static final Piece O = null;

    public static final Piece K = PIECES.get(WHITE, KING);
    public static final Piece Q = PIECES.get(WHITE, QUEEN);
    public static final Piece B = PIECES.get(WHITE, BISHOP);
    public static final Piece N = PIECES.get(WHITE, KNIGHT);
    public static final Piece R = PIECES.get(WHITE, ROOK);
    public static final Piece P = PIECES.get(WHITE, PAWN);

    public static final Piece k = PIECES.get(BLACK, KING);
    public static final Piece q = PIECES.get(BLACK, QUEEN);
    public static final Piece b = PIECES.get(BLACK, BISHOP);
    public static final Piece n = PIECES.get(BLACK, KNIGHT);
    public static final Piece r = PIECES.get(BLACK, ROOK);
    public static final Piece p = PIECES.get(BLACK, PAWN);
}
