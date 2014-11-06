package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.PIECES;
import static com.github.fromi.chess.material.Piece.Type.PAWN;
import static com.github.fromi.chess.material.Square.SQUARES;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PawnTest {
    private final Piece whitePawn = PIECES.get(WHITE, PAWN);
    private final Piece blackPawn = PIECES.get(BLACK, PAWN);

    @Test
    public void pawn_can_move_one_step_forward() {
        assertTrue(whitePawn.allowMove(SQUARES.get('e', 5), SQUARES.get('e', 6)));
        assertTrue(blackPawn.allowMove(SQUARES.get('e', 6), SQUARES.get('e', 5)));
    }

    @Test
    public void pawn_cannot_move_backwards() {
        assertFalse(whitePawn.allowMove(SQUARES.get('e', 6), SQUARES.get('e', 5)));
        assertFalse(blackPawn.allowMove(SQUARES.get('e', 5), SQUARES.get('e', 6)));
    }

    @Test
    public void pawn_cannot_change_file() {
        assertFalse(whitePawn.allowMove(SQUARES.get('e', 6), SQUARES.get('f', 5)));
        assertFalse(blackPawn.allowMove(SQUARES.get('a', 5), SQUARES.get('b', 6)));
    }

    @Test
    public void pawn_can_move_twice_on_first_move_only() {
        assertTrue(whitePawn.allowMove(SQUARES.get('e', 2), SQUARES.get('e', 4)));
        assertTrue(blackPawn.allowMove(SQUARES.get('e', 7), SQUARES.get('e', 5)));
        assertFalse(whitePawn.allowMove(SQUARES.get('e', 3), SQUARES.get('e', 5)));
        assertFalse(blackPawn.allowMove(SQUARES.get('e', 5), SQUARES.get('e', 3)));
    }
}