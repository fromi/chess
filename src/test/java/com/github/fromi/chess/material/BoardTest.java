package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Square.SQUARES;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.eventbus.EventBus;

@RunWith(MockitoJUnitRunner.class)
public class BoardTest {

    private static final Square E2 = SQUARES.get('e', 2);
    private static final Square E4 = SQUARES.get('e', 4);
    private static final Square E5 = SQUARES.get('e', 5);
    private static final Square F3 = SQUARES.get('f', 3);
    private Board board;

    @Mock
    private EventBus eventBus;
    private final ArgumentCaptor<PieceMove.Event> pieceMoveEventArgumentCaptor = forClass(PieceMove.Event.class);

    @Before
    public void setUp() {
        board = new Board(eventBus);
    }

    @Test
    public void move_piece() {
        board.movePiece(E2, E4);
        verify(eventBus).post(pieceMoveEventArgumentCaptor.capture());
    }

    @Test
    public void move_piece_twice() {
        board.movePiece(E2, E4);
        board.movePiece(E4, E5);
        verify(eventBus, times(2)).post(pieceMoveEventArgumentCaptor.capture());
    }

    @Test(expected = SquareEmpty.class)
    public void after_move_origin_is_empty() {
        board.movePiece(E2, E4);
        board.getPieceAt(E2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void destination_must_differ_from_origin() {
        board.movePiece(E2, E2);
    }

    @Test(expected = IllegalMoveException.class)
    public void pieces_cannot_move_anywhere() {
        board.movePiece(E2, F3);
    }
}