package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.PIECES;
import static com.github.fromi.chess.material.Piece.Type.PAWN;
import static com.github.fromi.chess.material.util.Squares.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

public class PawnTest {
    private final Piece whitePawn = PIECES.get(WHITE, PAWN);
    private final Piece blackPawn = PIECES.get(BLACK, PAWN);

    @Test
    public void pawn_can_move_one_step_forward() {
        assertTrue(whitePawn.moveAllowed(E5, E6));
        assertTrue(blackPawn.moveAllowed(E6, E5));
    }

    @Test
    public void pawn_cannot_move_backwards() {
        assertFalse(whitePawn.moveAllowed(E6, E5));
        assertFalse(blackPawn.moveAllowed(E5, E6));
    }

    @Test
    public void pawn_cannot_change_file() {
        assertFalse(whitePawn.moveAllowed(E6, F5));
        assertFalse(blackPawn.moveAllowed(A5, B6));
    }

    @Test
    public void pawn_can_move_twice_on_first_move_only() {
        assertTrue(whitePawn.moveAllowed(E2, E4));
        assertTrue(blackPawn.moveAllowed(E7, E5));
        assertFalse(whitePawn.moveAllowed(E3, E5));
        assertFalse(blackPawn.moveAllowed(E5, E3));
    }

    @Test
    public void pawn_cannot_move_more_than_2_squares() {
        assertFalse(whitePawn.moveAllowed(E2, E5));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void pawn_move_capability_depends_on_their_color() {
        PAWN.moveAllowed(E6, E5);
    }

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
}