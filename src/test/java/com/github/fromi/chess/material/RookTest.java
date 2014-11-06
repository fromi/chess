package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.PIECES;
import static com.github.fromi.chess.material.Piece.Type.ROOK;
import static com.github.fromi.chess.material.Square.SQUARES;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RookTest {
    private final Piece whiteRook = PIECES.get(WHITE, ROOK);
    private final Piece blackRook = PIECES.get(BLACK, ROOK);

    @Test
    public void rook_can_move_in_strait_lines_only() {
        assertTrue(whiteRook.allowMove(SQUARES.get('a', 1), SQUARES.get('a', 7)));
        assertTrue(whiteRook.allowMove(SQUARES.get('b', 5), SQUARES.get('b', 3)));
        assertTrue(blackRook.allowMove(SQUARES.get('a', 1), SQUARES.get('a', 7)));
        assertTrue(blackRook.allowMove(SQUARES.get('b', 5), SQUARES.get('b', 3)));
        assertTrue(whiteRook.allowMove(SQUARES.get('g', 3), SQUARES.get('b', 3)));
        assertTrue(whiteRook.allowMove(SQUARES.get('f', 2), SQUARES.get('a', 2)));
        assertTrue(blackRook.allowMove(SQUARES.get('c', 6), SQUARES.get('h', 6)));
        assertTrue(blackRook.allowMove(SQUARES.get('g', 5), SQUARES.get('a', 5)));

        assertFalse(whiteRook.allowMove(SQUARES.get('a', 1), SQUARES.get('b', 2)));
    }
}