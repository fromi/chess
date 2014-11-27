package com.github.fromi.chess.material;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.stream.Stream;

public class Knight extends Piece {
    private static final int MOVE_DISTANCE = 3;
    static final List<Character> STARTING_FILES = asList('b', 'g');

    public Knight(Color color, Board board, Character file) {
        super(color, board, Square.SQUARES.get(file, color.getFirstRank()));
    }

    public Knight(Color color, Board board, Square position) {
        super(color, board, position);
    }

    @Override
    protected Type getType() {
        return Type.KNIGHT;
    }

    @Override
    protected boolean onEmptyBoardCouldMoveTo(Square destination) {
        return position.fileDistanceTo(destination) + position.rankDistanceTo(destination) == MOVE_DISTANCE && !position.hasStraitLineTo(destination);
    }

    @Override
    protected Stream<Square> potentialMinimalMoves() {
        return position.knightMoveSquares();
    }
}
