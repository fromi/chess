package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.*;
import static com.github.fromi.chess.material.Piece.PIECES;
import static com.github.fromi.chess.material.Piece.Type.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;

public class Board {

    private static final List<Integer> RANKS = range(1, 9).boxed().collect(toList());
    private static final List<Character> FILES = asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
    private static final int WHITE_FIRST_RANK = 1;
    private static final int WHITE_PAWNS_RANK = 2;
    private static final int BLACK_PAWNS_RANK = 7;
    private static final int BLACK_FIRST_RANK = 8;
    private static final Character KINGS_STARTING_FILE = 'e';
    private static final Character QUEENS_STARTING_FILE = 'd';
    private static final List<Character> BISHOPS_STARTING_FILES = asList('c', 'f');
    private static final List<Character> KNIGHTS_STARTING_FILES = asList('b', 'g');
    private static final List<Character> ROOKS_STARTING_FILES = asList('a', 'h');
    private final Table<Integer, Character, Piece> table = ArrayTable.create(RANKS, FILES);

    public Board() {
        putPiecesAtInitialPosition(WHITE, WHITE_FIRST_RANK, WHITE_PAWNS_RANK);
        putPiecesAtInitialPosition(BLACK, BLACK_FIRST_RANK, BLACK_PAWNS_RANK);
    }

    private void putPiecesAtInitialPosition(Piece.Color color, int firstRank, int pawnsRank) {
        table.put(firstRank, KINGS_STARTING_FILE, PIECES.get(color, KING));
        table.put(firstRank, QUEENS_STARTING_FILE, PIECES.get(color, QUEEN));
        BISHOPS_STARTING_FILES.forEach(file -> table.put(firstRank, file, PIECES.get(color, BISHOP)));
        KNIGHTS_STARTING_FILES.forEach(file -> table.put(firstRank, file, PIECES.get(color, KNIGHT)));
        ROOKS_STARTING_FILES.forEach(file -> table.put(firstRank, file, PIECES.get(color, ROOK)));
        table.columnKeySet().forEach(file -> table.put(pawnsRank, file, PIECES.get(color, PAWN)));
    }

    public Piece getPieceAt(Square square) {
        Piece piece = table.get(square.getRank(), square.getFile());
        if (piece == null) {
            throw new SquareEmpty();
        }
        return piece;
    }
}
