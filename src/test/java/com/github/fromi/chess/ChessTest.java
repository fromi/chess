package com.github.fromi.chess;

import static com.github.fromi.chess.Color.BLACK;
import static com.github.fromi.chess.Color.WHITE;
import static com.github.fromi.chess.material.Piece.PAWN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.fromi.chess.material.PieceMove;
import com.github.fromi.chess.material.Square;

@RunWith(MockitoJUnitRunner.class)
public class ChessTest {

    private static final Square E2 = new Square('E', 2);
    private static final Square E4 = new Square('E', 4);
    private static final Square E5 = new Square('E', 5);
    private static final Square E7 = new Square('E', 7);

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
        assertThat(event.getColor(), equalTo(WHITE));
        assertThat(event.getPiece(), equalTo(PAWN));
        assertThat(event.getOrigin(), equalTo(E2));
        assertThat(event.getDestination(), equalTo(E4));
    }

    @Test(expected = NotPlayerTurn.class)
    public void black_player_does_not_start() {
        game.player(BLACK).move(E7, E5);
    }
}