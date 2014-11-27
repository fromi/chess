package com.github.fromi.chess.material;

import java.util.stream.Stream;

public class King extends Piece {

    static final Character STARTING_FILE = 'e';

    public King(Color color, Board board) {
        super(color, board, Square.SQUARES.get(STARTING_FILE, color.getFirstRank()));
    }

    public King(Color color, Board board, Square position) {
        super(color, board, position);
    }

    @Override
    protected Type getType() {
        return Type.KING;
    }

    @Override
    protected boolean onEmptyBoardCouldMoveTo(Square destination) {
        return position.adjacentSquares().anyMatch(square -> square == destination);
    }

    @Override
    protected Stream<Square> potentialMinimalMoves() {
        return position.adjacentSquares();
    }

}
