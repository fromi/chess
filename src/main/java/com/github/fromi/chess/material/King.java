package com.github.fromi.chess.material;

import java.util.Optional;
import java.util.stream.Stream;

public class King extends Piece {

    static final Character STARTING_FILE = 'e';
    private static final int CASTLING_MOVE_DISTANCE = 2;

    public King(Color color, Board board) {
        super(color, board, Square.SQUARES.get(STARTING_FILE, color.getFirstRank()));
    }

    public King(Color color, Board board, Square position) {
        super(color, board, position);
    }

    @Override
    public void moveTo(Square destination) {
        if (position.equals(startingPosition()) && isCastlingMoveTo(destination)) {
            castleTo(destination);
        } else {
            super.moveTo(destination);
        }
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


    private Square startingPosition() {
        return Square.SQUARES.get(STARTING_FILE, color.getFirstRank());
    }

    private boolean isCastlingMoveTo(Square destination) {
        return position.fileDistanceTo(destination) == CASTLING_MOVE_DISTANCE && position.rankDistanceTo(destination) == 0;
    }

    private void castleTo(Square destination) {
        if (!canCastleTo(destination)) {
            throw new IllegalCastling(this, board, destination);
        }
        Optional<Piece> expectedRook = rookCastlingWithWhenMovingTo(destination);
        if (!expectedRook.isPresent() || !canCastleWith(expectedRook.get())) {
            throw new IllegalCastling(this, board, destination);
        }
        Square rookDestination = position.squaresInPathTo(destination).findFirst().get();
        definitelyMoveTo(destination);
        expectedRook.get().definitelyMoveTo(rookDestination);
    }

    private boolean canCastleTo(Square destination) {
        return neverMoved()
                && kingIsSafe()
                && !castlingThroughCheck(destination)
                && !opponentCanAttack(destination);
    }

    private boolean castlingThroughCheck(Square destination) {
        return position.squaresInPathTo(destination).anyMatch(this::opponentCanAttack);
    }

    private boolean opponentCanAttack(Square square) {
        return board.pieces(color.opponent()).stream().anyMatch(piece -> piece.hasUnderAttack(square));
    }

    private Optional<Piece> rookCastlingWithWhenMovingTo(Square destination) {
        if (destination == queenSideCastlingDestination()) {
            return board.pieceAt(queenSideRookPosition());
        } else if (destination == kingSideCastlingDestination()) {
            return board.pieceAt(kingSideRookPosition());
        } else {
            return Optional.empty();
        }
    }

    private Square queenSideCastlingDestination() {
        return Square.SQUARES.get('c', color.getFirstRank());
    }

    private Square queenSideRookPosition() {
        return Square.SQUARES.get('a', color.getFirstRank());
    }

    private Square kingSideCastlingDestination() {
        return Square.SQUARES.get('g', color.getFirstRank());
    }

    private Square kingSideRookPosition() {
        return Square.SQUARES.get('h', color.getFirstRank());
    }

    private boolean canCastleWith(Piece rook) {
        return rook.neverMoved() && pathEmptyTo(rook.position);
    }
}
