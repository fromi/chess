package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Type.ROOK;
import static com.github.fromi.chess.material.Squares.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RookTest {

    @Test
    public void rook_can_move_in_strait_lines_only() {
        assertTrue(ROOK.moveAllowed(A1, A7));
        assertTrue(ROOK.moveAllowed(B5, B3));
        assertTrue(ROOK.moveAllowed(A1, A7));
        assertTrue(ROOK.moveAllowed(B5, B3));
        assertTrue(ROOK.moveAllowed(G3, B3));
        assertTrue(ROOK.moveAllowed(F2, A2));
        assertTrue(ROOK.moveAllowed(C6, H6));
        assertTrue(ROOK.moveAllowed(G5, A5));

        assertFalse(ROOK.moveAllowed(A1, B2));
    }
}