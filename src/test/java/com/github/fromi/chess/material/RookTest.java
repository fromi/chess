package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.util.Squares.C3;

import org.junit.Rule;
import org.junit.Test;

import com.github.fromi.chess.material.util.Boards;

@SuppressWarnings("JavacQuirks")
public class RookTest {
    // Rook position
    private static final Boolean O = false;
    // Invalid moves
    private static final Boolean _ = false;
    // Valid moves
    private static final Boolean X = true;

    @Rule
    public final MoveRule moveRule = new MoveRule();

    @Test
    public void rook_can_move_in_strait_lines_only() {
        Rook rook = new Rook(WHITE, Boards.empty(), C3);
        Boolean[] rank8 = {_, _, X, _, _, _, _, _};
        Boolean[] rank7 = {_, _, X, _, _, _, _, _};
        Boolean[] rank6 = {_, _, X, _, _, _, _, _};
        Boolean[] rank5 = {_, _, X, _, _, _, _, _};
        Boolean[] rank4 = {_, _, X, _, _, _, _, _};
        Boolean[] rank3 = {X, X, O, X, X, X, X, X};
        Boolean[] rank2 = {_, _, X, _, _, _, _, _};
        Boolean[] rank1 = {_, _, X, _, _, _, _, _};
        Boolean[][] validMoves = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        moveRule.with(rook).expect(validMoves);
    }
}