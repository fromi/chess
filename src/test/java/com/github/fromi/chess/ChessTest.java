package com.github.fromi.chess;

import com.github.fromi.chess.material.PieceMove;
import com.github.fromi.chess.material.Square;
import com.github.fromi.chess.material.SquareEmpty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.github.fromi.chess.material.Piece.Color.*;
import static com.github.fromi.chess.material.Piece.Type.PAWN;
import static com.github.fromi.chess.material.Square.SQUARES;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChessTest {

    private static final Square E2 = SQUARES.get('e', 2);
    private static final Square E4 = SQUARES.get('e', 4);
    private static final Square E5 = SQUARES.get('e', 5);
    private static final Square E7 = SQUARES.get('e', 7);

    private Game game;

    @Mock
    PieceMove pieceMove;
    private final ArgumentCaptor<PieceMove.Event> pieceMoveEventArgumentCaptor = forClass(PieceMove.Event.class);

    @Before
    public void setUp() {
        game = new Game();
        game.register(pieceMove);
    }

    @Test
    public void white_player_starts() {
        game.player(WHITE).move(E2, E4);
        verify(pieceMove).handle(pieceMoveEventArgumentCaptor.capture());
        PieceMove.Event event = pieceMoveEventArgumentCaptor.getValue();
        assertThat(event.getPiece().getColor(), equalTo(WHITE));
        assertThat(event.getPiece().getType(), equalTo(PAWN));
        assertThat(event.getOrigin(), equalTo(E2));
        assertThat(event.getDestination(), equalTo(E4));
    }

    @Test(expected = NotPlayerTurn.class)
    public void black_player_does_not_start() {
        game.player(BLACK).move(E7, E5);
    }

    @Test(expected = SquareEmpty.class)
    public void cannot_move_from_empty_square() {
        game.player(WHITE).move(E4, E5);
    }

    @Test(expected = CannotMoveOpponentPiece.class)
    public void cannot_move_opponent_piece() {
        game.player(WHITE).move(E7, E5);
    }

    @Test
    public void players_move_once_each_turn() {
        game.player(WHITE).move(E2, E4);
        game.player(BLACK).move(E7, E5);
        verify(pieceMove, times(2)).handle(pieceMoveEventArgumentCaptor.capture());
    }
}
