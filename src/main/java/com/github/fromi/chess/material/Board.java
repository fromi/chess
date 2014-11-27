package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.AbstractSet;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import com.google.common.eventbus.EventBus;

public class Board {

    public static final int SIZE = 8;
    public static final List<Integer> RANKS = range(1, 9).boxed().collect(toList());
    public static final List<Character> FILES = asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');

    final ArrayTable<Integer, Character, Piece> table = ArrayTable.create(RANKS, FILES);
    final EventBus eventBus;
    final transient Map<Piece.Color, King> kings = new EnumMap<>(Piece.Color.class);

    public Board(EventBus eventBus) {
        this.eventBus = eventBus;
        putPiecesAtInitialPosition(WHITE);
        putPiecesAtInitialPosition(BLACK);
    }

    public Board(Memento memento, EventBus eventBus) {
        this.eventBus = eventBus;
        memento.getPieces().forEachRemaining(this::createPieceOnBoardFrom);
    }

    private void putPiecesAtInitialPosition(Piece.Color color) {
        createKing(color);
        table.put(color.getFirstRank(), Queen.STARTING_FILE, new Queen(color, this));
        Bishop.STARTING_FILES.forEach(file -> table.put(color.getFirstRank(), file, new Bishop(color, this, file)));
        Knight.STARTING_FILES.forEach(file -> table.put(color.getFirstRank(), file, new Knight(color, this, file)));
        Rook.STARTING_FILES.forEach(file -> table.put(color.getFirstRank(), file, new Rook(color, this, file)));
        FILES.forEach(file -> table.put(color.getPawnsStartingRank(), file, new Pawn(color, this, file)));
    }

    private void createKing(Piece.Color color) {
        King king = new King(color, this);
        table.put(color.getFirstRank(), King.STARTING_FILE, king);
        kings.put(color, king);
    }

    private void createPieceOnBoardFrom(Piece.Memento pieceMemento) {
        Piece piece = pieceMemento.getType().createPiece(pieceMemento.getColor(), this, pieceMemento.getPosition());
        table.put(pieceMemento.getPosition().getRank(), pieceMemento.getPosition().getFile(), piece);
        if (piece instanceof King) {
            kings.put(pieceMemento.getColor(), (King) piece);
        }
    }

    public Optional<Piece> pieceAt(Square square) {
        return Optional.ofNullable(table.get(square.getRank(), square.getFile()));
    }

    public Set<Piece> pieces(Piece.Color color) {
        return new PieceSet(color);
    }

    public Piece promotePawnTo(Piece.Color color, Piece.Type type) {
        return pieces(color).stream()
                .filter(piece -> piece instanceof Pawn).map(piece -> (Pawn) piece)
                .filter(Pawn::canBePromoted).findFirst().get().promoteTo(type);
    }

    public Memento memento() {
        return () -> table.cellSet().stream().filter(cell -> cell.getValue() != null).map(cell -> cell.getValue().memento()).iterator();
    }

    public static interface Memento {
        Iterator<Piece.Memento> getPieces();
    }

    private class PieceSet extends AbstractSet<Piece> {

        private final Piece.Color color;

        private PieceSet(Piece.Color color) {
            this.color = color;
        }

        @Override
        @Nonnull
        public Iterator<Piece> iterator() {
            return filteredStream()
                    .map(Table.Cell::getValue)
                    .iterator();
        }

        @Override
        public int size() {
            return (int) filteredStream().count();
        }

        private Stream<Table.Cell<Integer, Character, Piece>> filteredStream() {
            return table.cellSet().stream()
                    .filter(cell -> cell.getValue() != null)
                    .filter(cell -> cell.getValue().hasSame(color));
        }
    }
}
