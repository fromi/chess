package com.github.fromi.chess;

import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.Type.PAWN;
import static com.github.fromi.chess.material.util.Squares.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.fromi.chess.material.PieceMove;
import com.github.fromi.chess.material.SquareEmpty;

@RunWith(MockitoJUnitRunner.class)
public class ChessTest {

    private Game game;

    @Mock
    PieceMove pieceMove;
    private final ArgumentCaptor<PieceMove.Event> pieceMoveEventArgumentCaptor = forClass(PieceMove.Event.class);

    @Mock
    NextPlayer nextPlayer;
    private final ArgumentCaptor<NextPlayer.Event> nextPlayerEventArgumentCaptor = forClass(NextPlayer.Event.class);

    @Before
    public void setUp() {
        game = new Game();
        game.register(pieceMove);
        game.register(nextPlayer);
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
        verify(nextPlayer).handle(nextPlayerEventArgumentCaptor.capture());
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

    @Test(expected = IllegalArgumentException.class)
    public void cannot_move_to_same_square() {
        game.player(WHITE).move(E1, E1);
    }
}
