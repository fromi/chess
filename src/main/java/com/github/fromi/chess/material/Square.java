package com.github.fromi.chess.material;

public class Square {

    private final char file;
    private final int rank;

    public Square(char file, int rank) {
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
