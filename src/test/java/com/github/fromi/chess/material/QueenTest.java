package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.util.Squares.B8;
import static com.github.fromi.chess.material.util.Squares.D4;

import org.junit.Rule;
import org.junit.Test;

import com.github.fromi.chess.material.util.Boards;

@SuppressWarnings("JavacQuirks")
public class QueenTest {
    // Queen position
    private static final Boolean O = false;
    // Invalid moves
    private static final Boolean _ = false;
    // Valid moves
    private static final Boolean X = true;

    @Rule
    public final MoveRule moveRule = new MoveRule();

    @Test
    public void queen_moves_from_D4() {
        Queen queen = new Queen(WHITE, Boards.empty(), D4);
        Boolean[] rank8 = {_, _, _, X, _, _, _, X};
        Boolean[] rank7 = {X, _, _, X, _, _, X, _};
        Boolean[] rank6 = {_, X, _, X, _, X, _, _};
        Boolean[] rank5 = {_, _, X, X, X, _, _, _};
        Boolean[] rank4 = {X, X, X, O, X, X, X, X};
        Boolean[] rank3 = {_, _, X, X, X, _, _, _};
        Boolean[] rank2 = {_, X, _, X, _, X, _, _};
        Boolean[] rank1 = {X, _, _, X, _, _, X, _};
        Boolean[][] validMoves = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        moveRule.with(queen).expect(validMoves);
    }

    @Test
    public void queen_moves_from_B8() {
        Queen queen = new Queen(WHITE, Boards.empty(), B8);
        Boolean[] rank8 = {X, O, X, X, X, X, X, X};
        Boolean[] rank7 = {X, X, X, _, _, _, _, _};
        Boolean[] rank6 = {_, X, _, X, _, _, _, _};
        Boolean[] rank5 = {_, X, _, _, X, _, _, _};
        Boolean[] rank4 = {_, X, _, _, _, X, _, _};
        Boolean[] rank3 = {_, X, _, _, _, _, X, _};
        Boolean[] rank2 = {_, X, _, _, _, _, _, X};
        Boolean[] rank1 = {_, X, _, _, _, _, _, _};
        Boolean[][] validMoves = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        moveRule.with(queen).expect(validMoves);
    }
}