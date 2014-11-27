package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.util.Squares.G4;

import org.junit.Rule;
import org.junit.Test;

import com.github.fromi.chess.material.util.Boards;

@SuppressWarnings("JavacQuirks")
public class BishopTest {
    // Bishop position
    private static final Boolean O = false;
    // Invalid moves
    private static final Boolean _ = false;
    // Valid moves
    private static final Boolean X = true;

    @Rule
    public final MoveRule moveRule = new MoveRule();

    @Test
    public void bishop_can_move_in_diagonals_only() {
        Bishop bishop = new Bishop(WHITE, Boards.empty(), G4);
        Boolean[] rank8 = {_, _, X, _, _, _, _, _};
        Boolean[] rank7 = {_, _, _, X, _, _, _, _};
        Boolean[] rank6 = {_, _, _, _, X, _, _, _};
        Boolean[] rank5 = {_, _, _, _, _, X, _, X};
        Boolean[] rank4 = {_, _, _, _, _, _, O, _};
        Boolean[] rank3 = {_, _, _, _, _, X, _, X};
        Boolean[] rank2 = {_, _, _, _, X, _, _, _};
        Boolean[] rank1 = {_, _, _, X, _, _, _, _};
        Boolean[][] validMoves = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        moveRule.with(bishop).expect(validMoves);
    }
}