package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Type.ROOK;
import static com.github.fromi.chess.material.Square.SQUARES;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RookTest {

    @Test
    public void rook_can_move_in_strait_lines_only() {
        assertTrue(ROOK.moveAllowed(SQUARES.get('a', 1), SQUARES.get('a', 7)));
        assertTrue(ROOK.moveAllowed(SQUARES.get('b', 5), SQUARES.get('b', 3)));
        assertTrue(ROOK.moveAllowed(SQUARES.get('a', 1), SQUARES.get('a', 7)));
        assertTrue(ROOK.moveAllowed(SQUARES.get('b', 5), SQUARES.get('b', 3)));
        assertTrue(ROOK.moveAllowed(SQUARES.get('g', 3), SQUARES.get('b', 3)));
        assertTrue(ROOK.moveAllowed(SQUARES.get('f', 2), SQUARES.get('a', 2)));
        assertTrue(ROOK.moveAllowed(SQUARES.get('c', 6), SQUARES.get('h', 6)));
        assertTrue(ROOK.moveAllowed(SQUARES.get('g', 5), SQUARES.get('a', 5)));

        assertFalse(ROOK.moveAllowed(SQUARES.get('a', 1), SQUARES.get('b', 2)));
    }
}