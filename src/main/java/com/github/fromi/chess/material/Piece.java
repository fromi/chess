package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Type.KING;

import java.util.Optional;
import java.util.stream.Stream;

public abstract class Piece {
    private static final int WHITE_FIRST_RANK = 1;
    private static final int WHITE_PAWN_RANK = 2;
    private static final int BLACK_FIRST_RANK = 8;
    private static final int BLACK_PAWN_RANK = 7;

    protected final Color color;
    private final transient Board board;
    protected Square position;

    protected Piece(Color color, Board board, Square position) {
        this.color = color;
        this.board = board;
        this.position = position;
    }

    public boolean hasSame(Color expectedColor) {
        return color == expectedColor;
    }

    public void moveTo(Square destination) {
        if (!canMoveTo(destination)) {
            throw new IllegalMove(this, board, destination);
        }
        board.eventBus.post(new PieceMove.Event(color, getType(), position, destination));
        board.pieceAt(destination).ifPresent(piece -> board.eventBus.post(new PieceCaptured.Event(piece.color, piece.getType(), destination)));
        doMoveTo(destination);
    }

    public boolean canMove() {
        return potentialMinimalMoves().anyMatch(this::canMoveTo);
    }

    public boolean checkMate() {
        return check() && mate();
    }

    protected abstract Type getType();

    protected abstract boolean onEmptyBoardCouldMoveTo(Square destination);

    protected boolean onEmptyBoardCouldAttack(Square destination) {
        return onEmptyBoardCouldMoveTo(destination);
    }

    protected abstract Stream<Square> potentialMinimalMoves();

    // TODO en passant
    private boolean canMoveTo(Square destination) {
        if (board.pieceAt(destination).isPresent()) {
            return canAttack(board.pieceAt(destination).get());
        } else {
            return canMoveToEmpty(destination);
        }
    }

    private boolean canAttack(Piece target) {
        return !target.hasSame(color) && canAttack(target.position);
    }

    private boolean canAttack(Square destination) {
        return onEmptyBoardCouldAttack(destination) && pathEmptyTo(destination) && myKingWillNotBeCheckIfIMoveTo(destination);
    }

    private boolean canMoveToEmpty(Square destination) {
        return onEmptyBoardCouldMoveTo(destination) && pathEmptyTo(destination) && myKingWillNotBeCheckIfIMoveTo(destination);
    }

    private void doMoveTo(Square destination) {
        board.table.erase(position.getRank(), position.getFile());
        board.table.put(destination.getRank(), destination.getFile(), this);
        position = destination;
    }

    private void revertMove(Square origin, Square destination, Optional<Piece> target) {
        board.table.put(origin.getRank(), origin.getFile(), this);
        board.table.put(destination.getRank(), destination.getFile(), target.orElse(null));
        position = origin;
    }

    private boolean myKingWillNotBeCheckIfIMoveTo(Square destination) {
        Square origin = position;
        Optional<Piece> targetPiece = board.pieceAt(destination);
        try {
            doMoveTo(destination);
            return !kingIsCheck();
        } finally {
            revertMove(origin, destination, targetPiece);
        }
    }

    private boolean kingIsCheck() {
        return board.pieces(color.opponent()).stream().anyMatch(Piece::check);
    }

    private boolean check() {
        King opponentKing = board.kings.get(color.opponent());
        return hasUnderAttack(opponentKing.position);
    }

    private boolean hasUnderAttack(Square square) {
        return onEmptyBoardCouldAttack(square) && pathEmptyTo(square);
    }

    private boolean pathEmptyTo(Square destination) {
        return position.squaresInPathTo(destination).allMatch(square -> !board.pieceAt(square).isPresent());
    }

    private boolean mate() {
        King opponentKing = board.kings.get(color.opponent());
        return !opponentKing.canMove() && cannotBeAttackedAnotherPiece() && !opponentCanInterposePiece();
    }

    private boolean cannotBeAttackedAnotherPiece() {
        return board.pieces(color.opponent()).stream().filter(piece -> piece.getType() != KING)
                .allMatch(piece -> !piece.canAttack(this));
    }

    private boolean opponentCanInterposePiece() {
        King opponentKing = board.kings.get(color.opponent());
        return position.squaresInPathTo(opponentKing.position).anyMatch(
                square -> board.pieces(color.opponent()).stream()
                        .filter(piece -> opponentKing != piece)
                        .anyMatch(piece -> piece.onEmptyBoardCouldMoveTo(square) && piece.pathEmptyTo(square)));
    }

    @Override
    public String toString() {
        return String.format("%s %s in %s", color, getType(), position);
    }

    public Memento memento() {
        return new Memento() {
            @Override
            public Square getPosition() {
                return position;
            }

            @Override
            public Color getColor() {
                return color;
            }

            @Override
            public Type getType() {
                return Piece.this.getType();
            }
        };
    }

    public static enum Color {
        WHITE(WHITE_FIRST_RANK, WHITE_PAWN_RANK), BLACK(BLACK_FIRST_RANK, BLACK_PAWN_RANK);

        private final int firstRank;
        private final int pawnsStartingRank;

        private Color(int firstRank, int pawnsStartingRank) {
            this.firstRank = firstRank;
            this.pawnsStartingRank = pawnsStartingRank;
        }

        public int getFirstRank() {
            return firstRank;
        }

        public int getPawnsStartingRank() {
            return pawnsStartingRank;
        }

        public Color opponent() {
            return this == WHITE ? BLACK : WHITE;
        }

    }

    public enum Type implements Factory {
        KING {
            @Override
            public Piece createPiece(Color color, Board board, Square position) {
                return new King(color, board, position);
            }
        }, QUEEN {
            @Override
            public Piece createPiece(Color color, Board board, Square position) {
                return new Queen(color, board, position);
            }
        }, BISHOP {
            @Override
            public Piece createPiece(Color color, Board board, Square position) {
                return new Bishop(color, board, position);
            }
        }, KNIGHT {
            @Override
            public Piece createPiece(Color color, Board board, Square position) {
                return new Knight(color, board, position);
            }
        }, ROOK {
            @Override
            public Piece createPiece(Color color, Board board, Square position) {
                return new Rook(color, board, position);
            }
        }, PAWN {
            @Override
            public Piece createPiece(Color color, Board board, Square position) {
                return new Pawn(color, board, position);
            }
        }
    }

    public static interface Memento {
        Square getPosition();

        Color getColor();

        Type getType();
    }

    public static interface Factory {
        Piece createPiece(Color color, Board board, Square position);
    }
}
