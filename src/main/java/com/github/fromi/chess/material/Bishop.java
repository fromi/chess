package com.github.fromi.chess.material;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.stream.Stream;

public class Bishop extends Piece {

    static final List<Character> STARTING_FILES = asList('c', 'f');

    public Bishop(Color color, Board board, Character file) {
        super(color, board, Square.SQUARES.get(file, color.getFirstRank()));
    }

    public Bishop(Color color, Board board, Square position) {
        super(color, board, position);
    }

    @Override
    protected Type getType() {
        return Type.BISHOP;
    }

    @Override
    protected boolean onEmptyBoardCouldMoveTo(Square destination) {
        return position != destination && position.hasStraitDiagonalTo(destination);
    }

    @Override
    protected Stream<Square> potentialMinimalMoves() {
        return position.adjacentDiagonalSquares();
    }

}
