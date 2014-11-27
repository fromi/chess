package com.github.fromi.chess.material;

import static java.lang.Math.*;
import static java.util.stream.IntStream.range;

import java.util.Arrays;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

public class Square {

    public static final Table<Character, Integer, Square> SQUARES;

    static {
        ImmutableTable.Builder<Character, Integer, Square> squaresBuilder = new ImmutableTable.Builder<>();
        range(0, Board.SIZE).forEach(
                column -> range(0, Board.SIZE).forEach(
                        row -> squaresBuilder.put(Board.FILES.get(column), Board.RANKS.get(row), new Square(column, row))));
        SQUARES = squaresBuilder.build();
    }

    private static final int[][] ADJACENT_DIAGONAL_SQUARES_DELTA = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    private static final int[][] ADJACENT_LINE_SQUARES_DELTA = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
    private static final int[][] ADJACENT_SQUARES_DELTA = Stream.concat(
            Arrays.stream(ADJACENT_DIAGONAL_SQUARES_DELTA),
            Arrays.stream(ADJACENT_LINE_SQUARES_DELTA)).toArray(int[][]::new);
    private static final int[][] KNIGHT_MOVES_DELTA = {{-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {-2, 1}, {-2, -1}, {2, -1}, {2, 1}};

    private final int column;
    private final int row;

    private Square(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public char getFile() {
        return Board.FILES.get(column);
    }

    public int getRank() {
        return Board.RANKS.get(row);
    }

    public boolean hasStraitLineTo(Square destination) {
        return column == destination.column || row == destination.row;
    }

    public boolean hasStraitDiagonalTo(Square destination) {
        return fileDistanceTo(destination) == rankDistanceTo(destination);
    }

    public int fileDistanceTo(Square destination) {
        return abs(column - destination.column);
    }

    public int rankDistanceTo(Square destination) {
        return abs(row - destination.row);
    }

    public Stream<Square> squaresInPathTo(Square destination) {
        if (column == destination.column) {
            return fileSquaresBetween(min(row, destination.row), max(row, destination.row));
        } else if (row == destination.row) {
            return rankSquaresBetween(min(column, destination.column), max(column, destination.column));
        } else if (hasStraitDiagonalTo(destination)) {
            return diagonalSquaresBetweenThisAnd(destination);
        } else {
            return Stream.empty();
        }
    }

    private Stream<Square> fileSquaresBetween(int min, int max) {
        return range(min + 1, max).boxed()
                .map(rankNumber -> SQUARES.get(getFile(), rankNumber + 1));
    }

    private Stream<Square> rankSquaresBetween(int min, int max) {
        return range(min + 1, max).boxed()
                .map(fileNumber -> SQUARES.get(Board.FILES.get(fileNumber), row + 1));
    }

    private Stream<Square> diagonalSquaresBetweenThisAnd(Square destination) {
        int distance = destination.column - column;
        return range(min(row, destination.row) + 1, max(row, destination.row)).boxed()
                .map(currentRankNumber -> SQUARES.get(
                        Board.FILES.get(column + abs(currentRankNumber - row) * distance / abs(distance)),
                        currentRankNumber + 1));
    }

    public Stream<Square> adjacentSquares() {
        return deltasToSquares(ADJACENT_SQUARES_DELTA);
    }

    public Stream<Square> adjacentLineSquares() {
        return deltasToSquares(ADJACENT_LINE_SQUARES_DELTA);
    }

    public Stream<Square> adjacentDiagonalSquares() {
        return deltasToSquares(ADJACENT_DIAGONAL_SQUARES_DELTA);
    }

    public Stream<Square> knightMoveSquares() {
        return deltasToSquares(KNIGHT_MOVES_DELTA);
    }

    private Stream<Square> deltasToSquares(int[][] squaresDeltas) {
        return Arrays.stream(squaresDeltas)
                .filter(deltas -> column + deltas[0] >= 0)
                .filter(deltas -> column + deltas[0] < Board.SIZE)
                .filter(deltas -> row + deltas[1] >= 0)
                .filter(deltas -> row + deltas[1] < Board.SIZE)
                .map((int[] deltas) -> SQUARES.get(Board.FILES.get(column + deltas[0]), row + 1 + deltas[1]));
    }

    @Override
    public String toString() {
        return getFile() + String.valueOf(getRank());
    }
}
