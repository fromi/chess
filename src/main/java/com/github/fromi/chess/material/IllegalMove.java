package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.IllegalMove.MoveRule.*;

import java.util.Optional;

public class IllegalMove extends IllegalArgumentException {

    private final Piece piece;
    private final Board board;
    private final Square destination;

    IllegalMove(Piece piece, Board board, Square destination) {
        this.piece = piece;
        this.board = board;
        this.destination = destination;
    }

    public MoveRule reason() {
        return getReasonsNotToAttack().orElse(getReasonsNotToMove().orElse(getOtherReason()));
    }

    private Optional<MoveRule> getReasonsNotToAttack() {
        Optional<Piece> potentialTargetPiece = board.pieceAt(destination);
        if (potentialTargetPiece.isPresent()) {
            Piece targetPiece = potentialTargetPiece.get();
            if (targetPiece.hasSame(piece.color)) {
                return Optional.of(CANNOT_MOVE_ON_FRIEND);
            } else if (!piece.onEmptyBoardCouldAttack(destination)) {
                return Optional.of(CANNOT_ATTACK_THIS_WAY);
            }
        }
        return Optional.empty();
    }

    private Optional<MoveRule> getReasonsNotToMove() {
        if (board.pieceAt(destination).isPresent()) {
            return Optional.empty();
        } else if (!piece.onEmptyBoardCouldMoveTo(destination)) {
            return Optional.of(CANNOT_MOVE_THIS_WAY);
        } else {
            return Optional.empty();
        }
    }

    private MoveRule getOtherReason() {
        if (piece.position.squaresInPathTo(destination).anyMatch(square -> board.pieceAt(square).isPresent())) {
            return CANNOT_GO_THROUGH_ANOTHER_PIECE;
        } else {
            return KING_MUST_NOT_BE_IN_CHECK;
        }
    }

    @Override
    public String toString() {
        return String.format("%s cannot move to %s because %s", piece, destination, reason());
    }

    public enum MoveRule {
        CANNOT_MOVE_ON_FRIEND("You may not move a piece to a square with another piece the same color"),
        KING_MUST_NOT_BE_IN_CHECK("You may not make a move that leaves or puts your king in check"),
        CANNOT_ATTACK_THIS_WAY("This piece cannot attack this way"),
        CANNOT_MOVE_THIS_WAY("This piece cannot move this way"),
        CANNOT_GO_THROUGH_ANOTHER_PIECE("This piece cannot go through another piece");

        private final String details;

        MoveRule(String details) {
            this.details = details;
        }

        @Override
        public String toString() {
            return details;
        }
    }
}
