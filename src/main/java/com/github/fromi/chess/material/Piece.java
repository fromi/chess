package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.Type.BISHOP;
import static com.github.fromi.chess.material.Piece.Type.KING;
import static com.github.fromi.chess.material.Piece.Type.KNIGHT;
import static com.github.fromi.chess.material.Piece.Type.PAWN;
import static com.github.fromi.chess.material.Piece.Type.QUEEN;
import static com.github.fromi.chess.material.Piece.Type.ROOK;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

public class Piece {

    public static final Table<Color, Type, Piece> PIECES = new ImmutableTable.Builder<Color, Type, Piece>()
            .put(WHITE, KING, new Piece(WHITE, KING))
            .put(WHITE, QUEEN, new Piece(WHITE, QUEEN))
            .put(WHITE, BISHOP, new Piece(WHITE, BISHOP))
            .put(WHITE, KNIGHT, new Piece(WHITE, KNIGHT))
            .put(WHITE, ROOK, new Piece(WHITE, ROOK))
            .put(WHITE, PAWN, new Piece(WHITE, PAWN))
            .put(BLACK, KING, new Piece(BLACK, KING))
            .put(BLACK, QUEEN, new Piece(BLACK, QUEEN))
            .put(BLACK, BISHOP, new Piece(BLACK, BISHOP))
            .put(BLACK, KNIGHT, new Piece(BLACK, KNIGHT))
            .put(BLACK, ROOK, new Piece(BLACK, ROOK))
            .put(BLACK, PAWN, new Piece(BLACK, PAWN))
            .build();

    private final Color color;
    private final Type type;

    private Piece(Color color, Type type) {
        this.color = color;
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public enum Type {KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN}

    public static enum Color {BLACK, WHITE}
}
