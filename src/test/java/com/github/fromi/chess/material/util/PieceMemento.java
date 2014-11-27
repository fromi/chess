package com.github.fromi.chess.material.util;

import com.github.fromi.chess.material.Piece;
import com.github.fromi.chess.material.Square;

public class PieceMemento implements Piece.Memento {

    private final Piece.Color color;
    private final Piece.Type type;
    private final Square position;

    public PieceMemento(Piece.Color color, Piece.Type type, Square position) {
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
