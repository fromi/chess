package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Type.QUEEN;
import static com.github.fromi.chess.material.Square.SQUARES;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class QueenTest {
    // Queen position
    private static final Square D4 = SQUARES.get('d', 4);
    private static final Square B8 = SQUARES.get('b', 8);
    private static final Boolean N = false;
    // Invalid moves
    private static final Boolean O = false;
    // Valid moves
    private static final Boolean X = true;

    @Test
    public void queen_moves_from_D4() {
        Boolean[] rank8 = {O, O, O, X, O, O, O, X};
        Boolean[] rank7 = {X, O, O, X, O, O, X, O};
        Boolean[] rank6 = {O, X, O, X, O, X, O, O};
        Boolean[] rank5 = {O, O, X, X, X, O, O, O};
        Boolean[] rank4 = {X, X, X, N, X, X, X, X};
        Boolean[] rank3 = {O, O, X, X, X, O, O, O};
        Boolean[] rank2 = {O, X, O, X, O, X, O, O};
        Boolean[] rank1 = {X, O, O, X, O, O, X, O};
        Boolean[][] board = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        for (int fileNumber = 0 ; fileNumber < Board.SIZE ; fileNumber++) {
            for (int rankNumber = 0 ; rankNumber < Board.SIZE ; rankNumber++) {
                Square destination = SQUARES.get(Board.FILES.get(fileNumber), rankNumber + 1);
                if (destination.equals(D4)) continue;
                assertThat(QUEEN.moveAllowed(D4, destination), equalTo(board[rankNumber][fileNumber]));
            }
        }
    }

    @Test
    public void queen_moves_from_B8() {
        Boolean[] rank8 = {X, N, X, X, X, X, X, X};
        Boolean[] rank7 = {X, X, X, O, O, O, O, O};
        Boolean[] rank6 = {O, X, O, X, O, O, O, O};
        Boolean[] rank5 = {O, X, O, O, X, O, O, O};
        Boolean[] rank4 = {O, X, O, O, O, X, O, O};
        Boolean[] rank3 = {O, X, O, O, O, O, X, O};
        Boolean[] rank2 = {O, X, O, O, O, O, O, X};
        Boolean[] rank1 = {O, X, O, O, O, O, O, O};
        Boolean[][] board = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        for (int fileNumber = 0 ; fileNumber < Board.SIZE ; fileNumber++) {
            for (int rankNumber = 0 ; rankNumber < Board.SIZE ; rankNumber++) {
                Square destination = SQUARES.get(Board.FILES.get(fileNumber), rankNumber + 1);
                if (destination.equals(B8)) continue;
                assertThat(QUEEN.moveAllowed(B8, destination), equalTo(board[rankNumber][fileNumber]));
            }
        }
    }
}