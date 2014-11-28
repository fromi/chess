package com.github.fromi.chess.material;

public class IllegalCastling extends IllegalMove {
    IllegalCastling(Piece piece, Board board, Square destination) {
        super(piece, board, destination);
    }
}
