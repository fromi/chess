package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.Type.*;

import java.util.stream.Stream;

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
    private static final int KNIGHT_MOVE_DISTANCE = 3;

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

    public boolean moveAllowed(Square origin, Square destination) {
        if (type == PAWN) {
            return color.pawnMoveAllowed(origin, destination);
        } else {
            return type.moveAllowed(origin, destination);
        }
    }

    public Stream<Square> squaresGoingThrough(Square origin, Square destination) {
        if (type == KNIGHT) {
            return Stream.empty();
        } else {
            return origin.squaresInBetween(destination);
        }
    }

    public boolean attackAllowed(Square origin, Square destination) {
        if (type == PAWN) {
            return color.pawnAttackAllowed(origin, destination);
        } else {
            return type.moveAllowed(origin, destination);
        }
    }

    public enum Type implements Movable {
        KING {
            @Override
            public boolean moveAllowed(Square origin, Square destination) {
                return origin.adjacentSquares().anyMatch(square -> square == destination);
            }
        }, QUEEN {
            @Override
            public boolean moveAllowed(Square origin, Square destination) {
                return ROOK.moveAllowed(origin, destination) || BISHOP.moveAllowed(origin, destination);
            }
        }, BISHOP {
            @Override
            public boolean moveAllowed(Square origin, Square destination) {
                return origin != destination && origin.hasStraitDiagonalTo(destination);
            }
        }, KNIGHT {
            @Override
            public boolean moveAllowed(Square origin, Square destination) {
                return origin.fileDistanceTo(destination) + origin.rankDistanceTo(destination) == KNIGHT_MOVE_DISTANCE && !origin.hasStraitLineTo(destination);
            }
        }, ROOK {
            @Override
            public boolean moveAllowed(Square origin, Square destination) {
                return origin != destination && origin.hasStraitLineTo(destination);
            }
        }, PAWN {
            @Override
            public boolean moveAllowed(Square origin, Square destination) {
                throw new UnsupportedOperationException("Pawns allowed moves depends on the color");
            }
        }
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

        private boolean pawnMoveAllowed(Square origin, Square destination) {
            return destination.getFile() == origin.getFile() && movesForward(origin, destination) && pawnDistanceAllowed(origin, destination);
        }

        private boolean movesForward(Square origin, Square destination) {
            return this == WHITE ^ destination.getRank() < origin.getRank();
        }

        private boolean pawnDistanceAllowed(Square origin, Square destination) {
            int distance = Math.abs(destination.getRank() - origin.getRank());
            return distance == 1 || distance == PAWN_SPECIAL_FIRST_MOVE_DISTANCE && !pawnAlreadyMoved(origin);
        }

        private boolean pawnAlreadyMoved(Square currentSquare) {
            return pawnsStartingRank != currentSquare.getRank();
        }

        private boolean pawnAttackAllowed(Square origin, Square destination) {
            return movesForward(origin, destination) && origin.fileDistanceTo(destination) == 1 && origin.rankDistanceTo(destination) == 1;
        }

        public Color opponent() {
            return this == WHITE ? BLACK : WHITE;
        }
    }

    private static interface Movable {
        boolean moveAllowed(Square origin, Square destination);
    }

    @Override
    public String toString() {
        return color + " " + type;
    }
}
