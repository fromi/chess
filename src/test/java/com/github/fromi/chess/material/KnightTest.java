package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.PIECES;
import static com.github.fromi.chess.material.Piece.Type.KNIGHT;
import static com.github.fromi.chess.material.Square.SQUARES;
import static com.github.fromi.chess.material.util.Squares.B8;
import static com.github.fromi.chess.material.util.Squares.D4;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class KnightTest {
    // Knight position
    private static final Boolean N = false;
    // Invalid moves
    private static final Boolean O = false;
    // Valid moves
    private static final Boolean X = true;

    @Test
    public void knight_moves_from_D4() {
        Boolean[] rank8 = {O, O, O, O, O, O, O, O};
        Boolean[] rank7 = {O, O, O, O, O, O, O, O};
        Boolean[] rank6 = {O, O, X, O, X, O, O, O};
        Boolean[] rank5 = {O, X, O, O, O, X, O, O};
        Boolean[] rank4 = {O, O, O, N, O, O, O, O};
        Boolean[] rank3 = {O, X, O, O, O, X, O, O};
        Boolean[] rank2 = {O, O, X, O, X, O, O, O};
        Boolean[] rank1 = {O, O, O, O, O, O, O, O};
        Boolean[][] board = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        for (int fileNumber = 0; fileNumber < Board.SIZE; fileNumber++) {
            for (int rankNumber = 0; rankNumber < Board.SIZE; rankNumber++) {
                Square destination = SQUARES.get(Board.FILES.get(fileNumber), rankNumber + 1);
                if (destination.equals(D4))
                    continue;
                assertThat(KNIGHT.moveAllowed(D4, destination), equalTo(board[rankNumber][fileNumber]));
            }
        }
    }

    @Test
    public void knight_moves_from_B8() {
        Boolean[] rank8 = {O, N, O, O, O, O, O, O};
        Boolean[] rank7 = {O, O, O, X, O, O, O, O};
        Boolean[] rank6 = {X, O, X, O, O, O, O, O};
        Boolean[] rank5 = {O, O, O, O, O, O, O, O};
        Boolean[] rank4 = {O, O, O, O, O, O, O, O};
        Boolean[] rank3 = {O, O, O, O, O, O, O, O};
        Boolean[] rank2 = {O, O, O, O, O, O, O, O};
        Boolean[] rank1 = {O, O, O, O, O, O, O, O};
        Boolean[][] board = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        for (int fileNumber = 0; fileNumber < Board.SIZE; fileNumber++) {
            for (int rankNumber = 0; rankNumber < Board.SIZE; rankNumber++) {
                Square destination = SQUARES.get(Board.FILES.get(fileNumber), rankNumber + 1);
                if (destination.equals(B8))
                    continue;
                assertThat(KNIGHT.moveAllowed(B8, destination), equalTo(board[rankNumber][fileNumber]));
            }
        }
    }

    @Test
    public void knight_moves_does_not_depend_on_color() {
        final Piece whiteKnight = PIECES.get(WHITE, KNIGHT);
        final Piece blackKnight = PIECES.get(BLACK, KNIGHT);
        SQUARES.values().forEach(origin -> SQUARES.values().stream().filter(
                destination -> !destination.equals(origin)).forEach(
                destination -> assertEquals(
                        whiteKnight.moveAllowed(origin, destination),
                        blackKnight.moveAllowed(origin, destination)
                )
        ));
    }
}