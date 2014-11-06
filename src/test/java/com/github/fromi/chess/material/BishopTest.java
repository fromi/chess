package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Type.BISHOP;
import static com.github.fromi.chess.material.Square.SQUARES;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BishopTest {

    @Test
    public void bishop_can_move_in_diagonals_only() {
        assertFalse(BISHOP.moveAllowed(SQUARES.get('a', 1), SQUARES.get('a', 7)));
        assertFalse(BISHOP.moveAllowed(SQUARES.get('b', 5), SQUARES.get('b', 3)));
        assertFalse(BISHOP.moveAllowed(SQUARES.get('a', 1), SQUARES.get('a', 7)));
        assertFalse(BISHOP.moveAllowed(SQUARES.get('b', 5), SQUARES.get('b', 3)));
        assertFalse(BISHOP.moveAllowed(SQUARES.get('g', 3), SQUARES.get('b', 3)));
        assertFalse(BISHOP.moveAllowed(SQUARES.get('f', 2), SQUARES.get('a', 2)));
        assertFalse(BISHOP.moveAllowed(SQUARES.get('c', 6), SQUARES.get('h', 6)));
        assertFalse(BISHOP.moveAllowed(SQUARES.get('g', 5), SQUARES.get('a', 5)));

        assertTrue(BISHOP.moveAllowed(SQUARES.get('a', 1), SQUARES.get('b', 2)));
        assertTrue(BISHOP.moveAllowed(SQUARES.get('f', 4), SQUARES.get('d', 6)));
        assertTrue(BISHOP.moveAllowed(SQUARES.get('h', 8), SQUARES.get('a', 1)));
        assertTrue(BISHOP.moveAllowed(SQUARES.get('b', 6), SQUARES.get('e', 3)));
    }
}