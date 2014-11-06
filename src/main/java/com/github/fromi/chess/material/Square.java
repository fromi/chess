package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Board.FILES;
import static java.util.stream.IntStream.range;

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
        return Math.abs(fileNumber - destination.fileNumber);
    }

    public int rankDistanceTo(Square destination) {
        return Math.abs(rankNumber - destination.rankNumber);
    }
}
