package com.github.fromi.chess.material;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

public class Square {

    public static final Table<Character, Integer, Square> SQUARES;
    static {
        ImmutableTable.Builder<Character, Integer, Square> squaresBuilder = new ImmutableTable.Builder<>();
        Board.FILES.forEach(file -> Board.RANKS.forEach(rank -> squaresBuilder.put(file, rank, new Square(file, rank))));
        SQUARES = squaresBuilder.build();
    }

    private final char file;
    private final int rank;

    private Square(char file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    public char getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Square square = (Square) o;

        return file == square.file && rank == square.rank;

    }

    @Override
    public int hashCode() {
        int result = (int) file;
        result = 31 * result + rank;
        return result;
    }
}
