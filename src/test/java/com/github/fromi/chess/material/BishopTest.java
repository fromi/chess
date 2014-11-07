package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Type.BISHOP;
import static com.github.fromi.chess.material.util.Squares.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BishopTest {

    @Test
    public void bishop_can_move_in_diagonals_only() {
        assertFalse(BISHOP.moveAllowed(A1, A7));
        assertFalse(BISHOP.moveAllowed(B5, B3));
        assertFalse(BISHOP.moveAllowed(A1, A7));
        assertFalse(BISHOP.moveAllowed(B5, B3));
        assertFalse(BISHOP.moveAllowed(G3, B3));
        assertFalse(BISHOP.moveAllowed(F2, A2));
        assertFalse(BISHOP.moveAllowed(C6, H6));
        assertFalse(BISHOP.moveAllowed(G5, A5));

        assertTrue(BISHOP.moveAllowed(A1, B2));
        assertTrue(BISHOP.moveAllowed(F4, D6));
        assertTrue(BISHOP.moveAllowed(H8, A1));
        assertTrue(BISHOP.moveAllowed(B6, E3));
    }
}