package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.Type.BISHOP;
import static com.github.fromi.chess.material.Piece.Type.KING;
import static com.github.fromi.chess.material.Piece.Type.KNIGHT;
import static com.github.fromi.chess.material.Piece.Type.PAWN;
import static com.github.fromi.chess.material.Piece.Type.QUEEN;
import static com.github.fromi.chess.material.Piece.Type.ROOK;
import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

public class Piece {

    public static final Table<Color, Type, Piece> PIECES = new ImmutableTable.Builder<Color, Type, Piece>()
            .put(WHITE, KING, new Piece(WHITE, KING))
            .put(WHITE, QUEEN, new Piece(WHITE, QUEEN))
            .put(WHITE, BISHOP, new Piece(WHITE, BISHOP))
            .put(WHITE, KNIGHT, new Piece(WHITE, KNIGHT))
            .put(WHITE, ROOK, new Piece(WHITE, ROOK))
            .put(WHITE, PAWN, new Piece(WHITE, PAWN))
            .put(BLACK, KING, new Piece(BLACK, KING))
            .put(BLACK, QUEEN, new Piece(BLACK, QUEEN))
            .put(BLACK, BISHOP, new Piece(BLACK, BISHOP))
            .put(BLACK, KNIGHT, new Piece(BLACK, KNIGHT))
            .put(BLACK, ROOK, new Piece(BLACK, ROOK))
            .put(BLACK, PAWN, new Piece(BLACK, PAWN))
            .build();
    private static final int WHITE_FIRST_RANK = 1;
    private static final int BLACK_FIRST_RANK = 8;
    private static final int WHITE_PAWNS_STARTING_RANK = 2;
    private static final int BLACK_PAWNS_STARTING_RANK = 7;
    private static final int PAWN_SPECIAL_FIRST_MOVE_DISTANCE = 2;

    private final Color color;
    private final Type type;

    private Piece(Color color, Type type) {
        this.color = color;
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public boolean allowMove(Square origin, Square destination) {
        checkArgument(!origin.equals(destination));
        switch (type) {
            case KING:
                return true;
            case QUEEN:
                return true;
            case BISHOP:
                return true;
            case KNIGHT:
                return true;
            case ROOK:
                return true;
            default:
                return pawnMoveAllowed(origin, destination);
        }
    }

    private boolean pawnMoveAllowed(Square origin, Square destination) {
        return destination.getFile() == origin.getFile() && movesForward(origin, destination) && pawnDistanceAllowed(origin, destination);
    }

    private boolean pawnDistanceAllowed(Square origin, Square destination) {
        int distance = Math.abs(destination.getRank() - origin.getRank());
        return distance == 1 || distance == PAWN_SPECIAL_FIRST_MOVE_DISTANCE && !pawnAlreadyMoved(origin);
    }

    private boolean pawnAlreadyMoved(Square currentSquare) {
        return color.getPawnsStartingRank() != currentSquare.getRank();
    }

    private boolean movesForward(Square origin, Square destination) {
        return color == WHITE ^ destination.getRank() < origin.getRank();
    }

    public enum Type {
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
    }

    public static enum Color {
        WHITE(WHITE_FIRST_RANK, WHITE_PAWNS_STARTING_RANK), BLACK(BLACK_FIRST_RANK, BLACK_PAWNS_STARTING_RANK);

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
    }
}
