package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Square.SQUARES;

import java.util.stream.Stream;

class Pawn extends Piece {

    private static final int FIRST_MOVE_MAX_DISTANCE = 2;

    public Pawn(Color color, Board board, Character file) {
        super(color, board, SQUARES.get(file, color.getPawnsStartingRank()));
    }

    public Pawn(Color color, Board board, Square position) {
        super(color, board, position);
    }

    @Override
    protected Type getType() {
        return Type.PAWN;
    }

    @Override
    protected boolean onEmptyBoardCouldMoveTo(Square destination) {
        return destination.getFile() == position.getFile()
                && isForwardCurrentPosition(destination)
                && position.rankDistanceTo(destination) <= maxMoveDistance();
    }

    @Override
    protected boolean onEmptyBoardCouldAttack(Square destination) {
        return isForwardCurrentPosition(destination) && position.fileDistanceTo(destination) == 1 && position.rankDistanceTo(destination) == 1;
    }

    @Override
    protected Stream<Square> potentialMinimalMoves() {
        return position.adjacentSquares().filter(this::isForwardCurrentPosition);
    }

    private boolean isForwardCurrentPosition(Square destination) {
        if (color == WHITE) {
            return destination.getRank() > position.getRank();
        } else {
            return destination.getRank() < position.getRank();
        }
    }

    private int maxMoveDistance() {
        return pawnAlreadyMoved() ? 1 : FIRST_MOVE_MAX_DISTANCE;
    }

    private boolean pawnAlreadyMoved() {
        return color.getPawnsStartingRank() != position.getRank();
    }
}
