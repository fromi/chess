package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.PIECES;
import static com.github.fromi.chess.material.Piece.Type.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
    private final transient Map<Piece.Color, Square> kingSquares = new EnumMap<>(Piece.Color.class);

    public Board(EventBus eventBus) {
        this.eventBus = eventBus;
        putPiecesAtInitialPosition(WHITE);
        putPiecesAtInitialPosition(BLACK);
    }

    public Board(Memento memento, EventBus eventBus) {
        this.eventBus = eventBus;
        table.cellSet().forEach(cell -> putPieceAt(memento.get(cell.getColumnKey(), cell.getRowKey()), cell.getColumnKey(), cell.getRowKey()));
    }

    private void putPiecesAtInitialPosition(Piece.Color color) {
        table.put(color.getFirstRank(), KINGS_STARTING_FILE, PIECES.get(color, KING));
        table.put(color.getFirstRank(), QUEENS_STARTING_FILE, PIECES.get(color, QUEEN));
        BISHOPS_STARTING_FILES.forEach(file -> table.put(color.getFirstRank(), file, PIECES.get(color, BISHOP)));
        KNIGHTS_STARTING_FILES.forEach(file -> table.put(color.getFirstRank(), file, PIECES.get(color, KNIGHT)));
        ROOKS_STARTING_FILES.forEach(file -> table.put(color.getFirstRank(), file, PIECES.get(color, ROOK)));
        table.columnKeySet().forEach(file -> table.put(color.getPawnsStartingRank(), file, PIECES.get(color, PAWN)));
        kingSquares.put(color, Square.SQUARES.get(KINGS_STARTING_FILE, color.getFirstRank()));
    }

    private void putPieceAt(Piece piece, Character file, Integer rank) {
        if (piece != null) {
            table.put(rank, file, piece);
            if (piece.getType() == KING) {
                kingSquares.put(piece.getColor(), Square.SQUARES.get(file, rank));
            }
        }
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
        if (thereIsAnotherPieceInTheWay(piece, origin, destination)) {
            throw new CannotGoThroughAnotherPiece();
        }
        Piece target = table.get(destination.getRank(), destination.getFile());
        if (target == null) {
            moveToEmptySquare(origin, destination, piece);
        } else {
            attack(piece, origin, target, destination);
        }
    }

    private void moveToEmptySquare(Square origin, Square destination, Piece piece) {
        if (!piece.moveAllowed(origin, destination)) {
            throw new PieceCannotMoveThisWay();
        }
        try {
            movePieceInTable(origin, destination, piece);
        } catch (CannotBeInCheckAfterMove e) {
            table.erase(destination.getRank(), destination.getFile());
            table.put(origin.getRank(), origin.getFile(), piece);
            throw e;
        }
    }

    private void attack(Piece piece, Square origin, Piece target, Square destination) {
        if (target.getColor() == piece.getColor()) {
            throw new CannotLandOnFriend();
        } else {
            if (!piece.attackAllowed(origin, destination)) {
                throw new CannotAttackThisWay();
            }
        }
        try {
            movePieceInTable(origin, destination, piece);
        } catch (CannotBeInCheckAfterMove e) {
            table.put(origin.getRank(), origin.getFile(), piece);
            table.put(destination.getRank(), destination.getFile(), target);
            throw e;
        }
        eventBus.post(new PieceCaptured.Event(target, destination));
    }

    private void movePieceInTable(Square origin, Square destination, Piece piece) {
        table.erase(origin.getRank(), origin.getFile());
        table.put(destination.getRank(), destination.getFile(), piece);
        if (piece.getType() == KING) {
            kingSquares.put(piece.getColor(), destination);
        }
        if (playerIsCheck(piece.getColor())) {
            throw new CannotBeInCheckAfterMove();
        }
        eventBus.post(new PieceMove.Event(piece, origin, destination));
    }

    private boolean playerIsCheck(Piece.Color color) {
        Square kingSquare = kingSquares.get(color);
        return squareIsUnderAttack(kingSquare, color.opponent());
    }

    private boolean squareIsUnderAttack(Square square, Piece.Color attacker) {
        return table.cellSet().stream()
                .filter(cell -> cell.getValue() != null)
                .filter(cell -> cell.getValue().getColor() == attacker)
                .anyMatch(cell -> attackAllowed(Square.SQUARES.get(cell.getColumnKey(), cell.getRowKey()), square));
    }

    private boolean squareIsUnderNonKingAttack(Square square, Piece.Color attacker) {
        return table.cellSet().stream()
                .filter(cell -> cell.getValue() != null)
                .filter(cell -> cell.getValue().getColor() == attacker)
                .filter(cell -> cell.getValue().getType() != KING)
                .anyMatch(cell -> attackAllowed(Square.SQUARES.get(cell.getColumnKey(), cell.getRowKey()), square));
    }

    private boolean thereIsAnotherPieceInTheWay(Piece piece, Square origin, Square destination) {
        return piece.squaresGoingThrough(origin, destination).anyMatch(square -> table.get(square.getRank(), square.getFile()) != null);
    }

    public boolean checkMate(Square checkingPieceSquare) {
        return check(checkingPieceSquare) && mate(checkingPieceSquare);
    }

    private boolean check(Square checkingPieceSquare) {
        Square kingSquare = kingSquares.get(getPieceAt(checkingPieceSquare).getColor().opponent());
        return attackAllowed(checkingPieceSquare, kingSquare);
    }

    private boolean attackAllowed(Square attackerSquare, Square target) {
        Piece attacker = getPieceAt(attackerSquare);
        return attacker.attackAllowed(attackerSquare, target)
                && !thereIsAnotherPieceInTheWay(attacker, attackerSquare, target);
    }

    private boolean mate(Square checkingPieceSquare) {
        Piece.Color kingColor = getPieceAt(checkingPieceSquare).getColor().opponent();
        return !kingCanEscape(kingColor)
                && !squareIsUnderNonKingAttack(checkingPieceSquare, kingColor)
                && !canInterposePiece(checkingPieceSquare, kingSquares.get(kingColor));
    }

    private boolean kingCanEscape(Piece.Color checkedKingColor) {
        return kingSquares.get(checkedKingColor).adjacentSquares()
                .anyMatch(square -> !containsFriendPiece(square, checkedKingColor)
                        && !squareIsUnderAttack(square, checkedKingColor.opponent()));
    }

    private boolean containsFriendPiece(Square square, Piece.Color color) {
        Piece piece = table.get(square.getRank(), square.getFile());
        return piece != null && piece.getColor() == color;
    }

    private boolean canInterposePiece(Square checkingPieceSquare, Square kingSquare) {
        Piece checkingPiece = getPieceAt(checkingPieceSquare);
        Piece.Color kingColor = checkingPiece.getColor().opponent();
        return checkingPiece.squaresGoingThrough(checkingPieceSquare, kingSquare)
                .anyMatch(square -> canMoveNonKingPieceTo(kingColor, square));
    }

    private boolean canMoveNonKingPieceTo(Piece.Color playerColor, Square square) {
        return table.cellSet().stream()
                .filter(cell -> cell.getValue() != null)
                .filter(cell -> cell.getValue().getColor() == playerColor)
                .filter(cell -> cell.getValue().getType() != KING)
                .filter(cell -> cell.getValue().moveAllowed(Square.SQUARES.get(cell.getColumnKey(), cell.getRowKey()), square))
                .anyMatch(cell -> !thereIsAnotherPieceInTheWay(cell.getValue(), Square.SQUARES.get(cell.getColumnKey(), cell.getRowKey()), square));
    }

    public Memento memento() {
        return (file, rank) -> table.get(rank, file);
    }

    public static interface Memento {
        Piece get(char file, int rank);
    }
}
