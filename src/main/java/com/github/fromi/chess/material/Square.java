package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Board.FILES;
import static com.github.fromi.chess.material.Board.SIZE;
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
                fileNumber -> range(0, Board.SIZE).forEach(
                        rankNumber -> squaresBuilder.put(FILES.get(fileNumber), rankNumber + 1, new Square(fileNumber, rankNumber))));
        SQUARES = squaresBuilder.build();
    }

    private static final int[][] ADJACENT_DIAGONAL_SQUARES_DELTA = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    private static final int[][] ADJACENT_LINE_SQUARES_DELTA = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
    private static final int[][] ADJACENT_SQUARES_DELTA = Stream.concat(
            Arrays.stream(ADJACENT_DIAGONAL_SQUARES_DELTA),
            Arrays.stream(ADJACENT_LINE_SQUARES_DELTA)).toArray(int[][]::new);
    private static final int[][] KNIGHT_MOVES_DELTA = {{-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {-2, 1}, {-2, -1}, {2, -1}, {2, 1}};

    private final int fileNumber;
    private final int rankNumber;

    private Square(int fileNumber, int rankNumber) {
        this.fileNumber = fileNumber;
        this.rankNumber = rankNumber;
    }

    public char getFile() {
        return FILES.get(fileNumber);
    }

    public int getRank() {
        return rankNumber + 1;
    }

    public boolean hasStraitLineTo(Square destination) {
        return fileNumber == destination.fileNumber || rankNumber == destination.rankNumber;
    }

    public boolean hasStraitDiagonalTo(Square destination) {
        return fileDistanceTo(destination) == rankDistanceTo(destination);
    }

    public int fileDistanceTo(Square destination) {
        return abs(fileNumber - destination.fileNumber);
    }

    public int rankDistanceTo(Square destination) {
        return abs(rankNumber - destination.rankNumber);
    }

    public Stream<Square> squaresInBetween(Square destination) {
        if (fileNumber == destination.fileNumber) {
            return fileSquaresBetween(min(rankNumber, destination.rankNumber), max(rankNumber, destination.rankNumber));
        } else if (rankNumber == destination.rankNumber) {
            return rankSquaresBetween(min(fileNumber, destination.fileNumber), max(fileNumber, destination.fileNumber));
        } else if (hasStraitDiagonalTo(destination)) {
            int distance = destination.fileNumber - fileNumber;
            return range(min(rankNumber, destination.rankNumber) + 1, max(rankNumber, destination.rankNumber)).boxed()
                    .map(currentRankNumber -> SQUARES.get(
                            FILES.get(fileNumber + abs(currentRankNumber - rankNumber) * distance / abs(distance)),
                            currentRankNumber + 1));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private Stream<Square> fileSquaresBetween(int min, int max) {
        return range(min + 1, max).boxed()
                .map(rankNumber -> SQUARES.get(getFile(), rankNumber + 1));
    }

    private Stream<Square> rankSquaresBetween(int min, int max) {
        return range(min + 1, max).boxed()
                .map(fileNumber -> SQUARES.get(FILES.get(fileNumber), rankNumber + 1));
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
                .filter(deltas -> fileNumber + deltas[0] >= 0)
                .filter(deltas -> fileNumber + deltas[0] < SIZE)
                .filter(deltas -> rankNumber + deltas[1] >= 0)
                .filter(deltas -> rankNumber + deltas[1] < SIZE)
                .map((int[] deltas) -> SQUARES.get(FILES.get(fileNumber + deltas[0]), rankNumber + 1 + deltas[1]));
    }

    @Override
    public String toString() {
        return getFile() + String.valueOf(getRank());
    }
}
