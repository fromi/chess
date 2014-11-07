package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.PIECES;
import static com.github.fromi.chess.material.Piece.Type.BISHOP;
import static com.github.fromi.chess.material.Piece.Type.KING;
import static com.github.fromi.chess.material.Piece.Type.KNIGHT;
import static com.github.fromi.chess.material.Piece.Type.PAWN;
import static com.github.fromi.chess.material.Piece.Type.QUEEN;
import static com.github.fromi.chess.material.Piece.Type.ROOK;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;

import com.google.common.collect.ArrayTable;
import com.google.common.eventbus.EventBus;

public class Board {

    private static final List<Integer> RANKS = range(1, 9).boxed().collect(toList());
    public static final List<Character> FILES = asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
    private static final Character KINGS_STARTING_FILE = 'e';
    private static final Character QUEENS_STARTING_FILE = 'd';
    private static final List<Character> BISHOPS_STARTING_FILES = asList('c', 'f');
    private static final List<Character> KNIGHTS_STARTING_FILES = asList('b', 'g');
    private static final List<Character> ROOKS_STARTING_FILES = asList('a', 'h');
    public static final int SIZE = 8;
    private final ArrayTable<Integer, Character, Piece> table = ArrayTable.create(RANKS, FILES);
    private final EventBus eventBus;

    public Board(EventBus eventBus) {
        this.eventBus = eventBus;
        putPiecesAtInitialPosition(WHITE);
        putPiecesAtInitialPosition(BLACK);
    }

    private void putPiecesAtInitialPosition(Piece.Color color) {
        table.put(color.getFirstRank(), KINGS_STARTING_FILE, PIECES.get(color, KING));
        table.put(color.getFirstRank(), QUEENS_STARTING_FILE, PIECES.get(color, QUEEN));
        BISHOPS_STARTING_FILES.forEach(file -> table.put(color.getFirstRank(), file, PIECES.get(color, BISHOP)));
        KNIGHTS_STARTING_FILES.forEach(file -> table.put(color.getFirstRank(), file, PIECES.get(color, KNIGHT)));
        ROOKS_STARTING_FILES.forEach(file -> table.put(color.getFirstRank(), file, PIECES.get(color, ROOK)));
        table.columnKeySet().forEach(file -> table.put(color.getPawnsStartingRank(), file, PIECES.get(color, PAWN)));
    }

    public Piece getPieceAt(Square square) {
        Piece piece = table.get(square.getRank(), square.getFile());
        if (piece == null) {
            throw new SquareEmpty();
        }
        return piece;
    }

    public void movePiece(Square origin, Square destination) {
        Piece piece = getPieceAt(origin);
        if (piece.squaresGoingThrough(origin, destination).anyMatch(square -> table.get(square.getRank(), square.getFile()) != null)) {
            throw new CannotGoThroughAnotherPiece();
        }
        Piece target = table.get(destination.getRank(), destination.getFile());
        if (target != null) {
            if (target.getColor() == piece.getColor()) {
                throw new CannotLandOnFriend();
            } else {
                if (!piece.attackAllowed(origin, destination)) {
                    throw new CannotAttackThisWay();
                }
            }
        } else {
            if (!piece.moveAllowed(origin, destination)) {
                throw new PieceCannotMoveThisWay();
            }
        }
        table.erase(origin.getRank(), origin.getFile());
        table.put(destination.getRank(), destination.getFile(), piece);
        eventBus.post(new PieceMove.Event(piece, origin, destination));
        if (target != null) {
            eventBus.post(new PieceCaptured.Event(target, destination));
        }
    }

    public Memento memento() {
        return (file, rank) -> table.get(rank, file);
    }

    public static interface Memento {
        Piece get(char file, int rank);
    }
}
