package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.PIECES;
import static com.github.fromi.chess.material.Piece.Type.BISHOP;
import static com.github.fromi.chess.material.Square.SQUARES;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BishopTest {
    private final Piece whiteBishop = PIECES.get(WHITE, BISHOP);
    private final Piece blackBishop = PIECES.get(BLACK, BISHOP);

    @Test
    public void bishop_can_move_in_diagonals_only() {
        assertFalse(whiteBishop.allowMove(SQUARES.get('a', 1), SQUARES.get('a', 7)));
        assertFalse(whiteBishop.allowMove(SQUARES.get('b', 5), SQUARES.get('b', 3)));
        assertFalse(blackBishop.allowMove(SQUARES.get('a', 1), SQUARES.get('a', 7)));
        assertFalse(blackBishop.allowMove(SQUARES.get('b', 5), SQUARES.get('b', 3)));
        assertFalse(whiteBishop.allowMove(SQUARES.get('g', 3), SQUARES.get('b', 3)));
        assertFalse(whiteBishop.allowMove(SQUARES.get('f', 2), SQUARES.get('a', 2)));
        assertFalse(blackBishop.allowMove(SQUARES.get('c', 6), SQUARES.get('h', 6)));
        assertFalse(blackBishop.allowMove(SQUARES.get('g', 5), SQUARES.get('a', 5)));

        assertTrue(whiteBishop.allowMove(SQUARES.get('a', 1), SQUARES.get('b', 2)));
        assertTrue(whiteBishop.allowMove(SQUARES.get('f', 4), SQUARES.get('d', 6)));
        assertTrue(whiteBishop.allowMove(SQUARES.get('h', 8), SQUARES.get('a', 1)));
        assertTrue(whiteBishop.allowMove(SQUARES.get('b', 6), SQUARES.get('e', 3)));
    }
}