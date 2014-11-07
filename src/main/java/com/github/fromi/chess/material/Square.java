package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Board.FILES;
import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.lang.Math.abs;
import static java.util.stream.IntStream.range;

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
}
