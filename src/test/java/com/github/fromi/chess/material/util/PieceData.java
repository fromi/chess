package com.github.fromi.chess.material.util;

import com.github.fromi.chess.material.Piece;
import com.github.fromi.chess.material.Square;

public class PieceData implements Piece.Data {

    private final Piece.Color color;
    private final Piece.Type type;
    private final Square position;

    public PieceData(Piece.Color color, Piece.Type type, Square position) {
        this.color = color;
        this.type = type;
        this.position = position;
    }

    @Override
    public Square getPosition() {
        return position;
    }

    @Override
    public Piece.Color getColor() {
        return color;
    }

    @Override
    public Piece.Type getType() {
        return type;
    }
}
