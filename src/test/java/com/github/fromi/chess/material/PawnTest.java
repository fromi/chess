package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.util.Squares.*;

import org.junit.Rule;
import org.junit.Test;

import com.github.fromi.chess.material.util.Boards;

@SuppressWarnings("JavacQuirks")
public class PawnTest {
    // Pawn position
    private static final Boolean O = false;
    // Invalid moves
    private static final Boolean _ = false;
    // Valid moves
    private static final Boolean X = true;

    @Rule
    public final MoveRule moveRule = new MoveRule();

    @Test
    public void pawn_can_move_one_step_forward() {
        Pawn pawn = new Pawn(WHITE, Boards.empty(), D4);
        Boolean[] rank8 = {_, _, _, _, _, _, _, _};
        Boolean[] rank7 = {_, _, _, _, _, _, _, _};
        Boolean[] rank6 = {_, _, _, _, _, _, _, _};
        Boolean[] rank5 = {_, _, _, X, _, _, _, _};
        Boolean[] rank4 = {_, _, _, O, _, _, _, _};
        Boolean[] rank3 = {_, _, _, _, _, _, _, _};
        Boolean[] rank2 = {_, _, _, _, _, _, _, _};
        Boolean[] rank1 = {_, _, _, _, _, _, _, _};
        Boolean[][] validMoves = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        moveRule.with(pawn).expect(validMoves);
    }

    @Test
    public void for_black_pawn_forward_is_the_other_way() {
        Pawn pawn = new Pawn(BLACK, Boards.empty(), D4);
        Boolean[] rank8 = {_, _, _, _, _, _, _, _};
        Boolean[] rank7 = {_, _, _, _, _, _, _, _};
        Boolean[] rank6 = {_, _, _, _, _, _, _, _};
        Boolean[] rank5 = {_, _, _, _, _, _, _, _};
        Boolean[] rank4 = {_, _, _, O, _, _, _, _};
        Boolean[] rank3 = {_, _, _, X, _, _, _, _};
        Boolean[] rank2 = {_, _, _, _, _, _, _, _};
        Boolean[] rank1 = {_, _, _, _, _, _, _, _};
        Boolean[][] validMoves = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        moveRule.with(pawn).expect(validMoves);
    }

    @Test
    public void white_pawn_can_move_twice_on_first_move() {
        Pawn pawn = new Pawn(WHITE, Boards.empty(), B2);
        Boolean[] rank8 = {_, _, _, _, _, _, _, _};
        Boolean[] rank7 = {_, _, _, _, _, _, _, _};
        Boolean[] rank6 = {_, _, _, _, _, _, _, _};
        Boolean[] rank5 = {_, _, _, _, _, _, _, _};
        Boolean[] rank4 = {_, X, _, _, _, _, _, _};
        Boolean[] rank3 = {_, X, _, _, _, _, _, _};
        Boolean[] rank2 = {_, O, _, _, _, _, _, _};
        Boolean[] rank1 = {_, _, _, _, _, _, _, _};
        Boolean[][] validMoves = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        moveRule.with(pawn).expect(validMoves);
    }

    @Test
    public void black_pawn_can_move_twice_on_first_move() {
        Pawn pawn = new Pawn(BLACK, Boards.empty(), C7);
        Boolean[] rank8 = {_, _, _, _, _, _, _, _};
        Boolean[] rank7 = {_, _, O, _, _, _, _, _};
        Boolean[] rank6 = {_, _, X, _, _, _, _, _};
        Boolean[] rank5 = {_, _, X, _, _, _, _, _};
        Boolean[] rank4 = {_, _, _, _, _, _, _, _};
        Boolean[] rank3 = {_, _, _, _, _, _, _, _};
        Boolean[] rank2 = {_, _, _, _, _, _, _, _};
        Boolean[] rank1 = {_, _, _, _, _, _, _, _};
        Boolean[][] validMoves = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        moveRule.with(pawn).expect(validMoves);
    }

    /*
    @Test
    public void pawn_only_attack_forward_diagonally() {
        assertTrue(whitePawn.attackAllowed(E2, D3));
        assertTrue(whitePawn.attackAllowed(E2, F3));
        assertFalse(whitePawn.attackAllowed(E2, D1));
        assertFalse(whitePawn.attackAllowed(E2, F1));
        assertFalse(whitePawn.attackAllowed(E2, E3));
        assertFalse(whitePawn.attackAllowed(E2, E4));
        assertFalse(whitePawn.attackAllowed(E2, D2));
        assertFalse(whitePawn.attackAllowed(E2, F2));
    }

    @Test
    public void test_to_string() {
        assertThat(whitePawn.toString(), equalTo("WHITE PAWN"));
    }
*/
}