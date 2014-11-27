package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Square.SQUARES;

import java.util.stream.Stream;

public class Queen extends Piece {

    static final Character STARTING_FILE = 'd';

    public Queen(Color color, Board board) {
        super(color, board, SQUARES.get(STARTING_FILE, color.getFirstRank()));
    }

    public Queen(Color color, Board board, Square position) {
        super(color, board, position);
    }

    @Override
    protected Type getType() {
        return Type.QUEEN;
    }

    @Override
    protected boolean onEmptyBoardCouldMoveTo(Square destination) {
        return position != destination && (position.hasStraitLineTo(destination) || position.hasStraitDiagonalTo(destination));
    }

    @Override
    protected Stream<Square> potentialMinimalMoves() {
        return position.adjacentSquares();
    }

}
