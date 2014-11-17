package com.github.fromi.chess;

import static com.github.fromi.chess.material.Board.FILES;
import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.github.fromi.chess.material.Piece.Type.PAWN;
import static com.github.fromi.chess.material.util.Pieces.*;
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

import com.github.fromi.chess.material.Piece;
import com.github.fromi.chess.material.PieceCaptured;
import com.github.fromi.chess.material.PieceMove;
import com.github.fromi.chess.material.SquareEmpty;
import com.github.fromi.chess.util.GameMemento;

@RunWith(MockitoJUnitRunner.class)
public class ChessTest {

    private Game game;
    private Player whitePlayer;
    private Player blackPlayer;

    @Mock
    PieceMove pieceMove;
    private final ArgumentCaptor<PieceMove.Event> pieceMoveEventArgumentCaptor = forClass(PieceMove.Event.class);

    @Mock
    NextPlayer nextPlayer;
    private final ArgumentCaptor<NextPlayer.Event> nextPlayerEventArgumentCaptor = forClass(NextPlayer.Event.class);

    @Mock
    PieceCaptured pieceCaptured;
    private final ArgumentCaptor<PieceCaptured.Event> pieceCapturedEventArgumentCaptor = forClass(PieceCaptured.Event.class);

    @Mock
    CheckMate checkMate;
    private final ArgumentCaptor<CheckMate.Event> checkMateEventArgumentCaptor = forClass(CheckMate.Event.class);

    @Mock
    Stalemate stalemate;
    private final ArgumentCaptor<Stalemate.Event> stalemateEventArgumentCaptor = forClass(Stalemate.Event.class);

    @Before
    public void setUp() {
        game = new Game();
        whitePlayer = game.player(WHITE);
        blackPlayer = game.player(BLACK);
        game.register(pieceMove);
        game.register(nextPlayer);
        game.register(pieceCaptured);
        game.register(checkMate);
        game.register(stalemate);
    }

    @Test
    public void white_player_starts() {
        whitePlayer.move(E2, E4);
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
        blackPlayer.move(E7, E5);
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
        whitePlayer.move(E2, E4);
        blackPlayer.move(E7, E5);
        verify(pieceMove, times(2)).handle(pieceMoveEventArgumentCaptor.capture());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannot_move_to_same_square() {
        whitePlayer.move(E1, E1);
    }

    @Test
    public void fools_mate() {
        whitePlayer.move(F2, F3);
        blackPlayer.move(E7, E5);
        whitePlayer.move(G2, G4);
        blackPlayer.move(D8, H4);
        verify(checkMate).handle(checkMateEventArgumentCaptor.capture());
        CheckMate.Event event = checkMateEventArgumentCaptor.getValue();
        assertThat(event.getWinner(), equalTo(BLACK));
    }

    @Test
    public void scholars_mate() {
        whitePlayer.move(E2, E4);
        blackPlayer.move(E7, E5);
        whitePlayer.move(D1, H5);
        blackPlayer.move(B8, C6);
        whitePlayer.move(F1, C4);
        blackPlayer.move(G8, F6);
        whitePlayer.move(H5, F7);
        verify(checkMate).handle(checkMateEventArgumentCaptor.capture());
        CheckMate.Event event = checkMateEventArgumentCaptor.getValue();
        assertThat(event.getWinner(), equalTo(WHITE));
    }

    @Test
    public void king_can_attack_checking_piece() {
        whitePlayer.move(E2, E4);
        blackPlayer.move(E7, E5);
        whitePlayer.move(D1, H5);
        blackPlayer.move(B8, C6);
        whitePlayer.move(H5, F7);
        blackPlayer.move(E8, F7);
        verify(pieceCaptured, times(2)).handle(pieceCapturedEventArgumentCaptor.capture());
    }

    @Test
    public void stalemate() {
        Piece[] rank8 = {O, O, O, k, O, O, O, O};
        Piece[] rank7 = {O, O, O, P, O, O, O, O};
        Piece[] rank6 = {O, O, O, O, K, O, O, O};
        Piece[] rank5 = {O, O, O, O, O, O, O, O};
        Piece[] rank4 = {O, O, O, O, O, O, O, O};
        Piece[] rank3 = {O, O, O, O, O, O, O, O};
        Piece[] rank2 = {O, O, O, O, O, O, O, O};
        Piece[] rank1 = {O, O, O, O, O, O, O, O};
        Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Game game = new Game(new GameMemento((file, rank) -> pieces[rank-1][FILES.indexOf(file)], WHITE));
        game.register(stalemate);
        game.player(WHITE).move(E6, D6);
        verify(stalemate).handle(stalemateEventArgumentCaptor.capture());
    }
}
