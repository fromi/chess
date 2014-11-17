package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Board.FILES;
import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Type.PAWN;
import static com.github.fromi.chess.material.util.Pieces.*;
import static com.github.fromi.chess.material.util.Squares.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
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

import com.google.common.eventbus.EventBus;

@RunWith(MockitoJUnitRunner.class)
public class BoardTest {

    private Board board;

    private final EventBus eventBus = new EventBus();

    @Mock
    PieceMove pieceMove;
    private final ArgumentCaptor<PieceMove.Event> pieceMoveEventArgumentCaptor = forClass(PieceMove.Event.class);

    @Mock
    PieceCaptured pieceCaptured;
    private final ArgumentCaptor<PieceCaptured.Event> pieceCapturedEventArgumentCaptor = forClass(PieceCaptured.Event.class);

    @Before
    public void setUp() {
        board = new Board(eventBus);
        eventBus.register(pieceMove);
        eventBus.register(pieceCaptured);
    }

    @Test
    public void verify_setup() {
        Piece[] rank8 = {r, n, b, q, k, b, n, r};
        Piece[] rank7 = {p, p, p, p, p, p, p, p};
        Piece[] rank6 = {O, O, O, O, O, O, O, O};
        Piece[] rank5 = {O, O, O, O, O, O, O, O};
        Piece[] rank4 = {O, O, O, O, O, O, O, O};
        Piece[] rank3 = {O, O, O, O, O, O, O, O};
        Piece[] rank2 = {P, P, P, P, P, P, P, P};
        Piece[] rank1 = {R, N, B, Q, K, B, N, R};
        Piece[][] expectedBoard = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        for (int rankNumber = 0; rankNumber < 8 ; rankNumber++) {
            for (int fileNumber = 0; fileNumber < 8 ; fileNumber++) {
                assertThat(board.memento().get(FILES.get(fileNumber), rankNumber + 1), equalTo(expectedBoard[rankNumber][fileNumber]));
            }
        }
    }

    @Test
    public void move_piece() {
        board.movePiece(E2, E4);
        verify(pieceMove).handle(pieceMoveEventArgumentCaptor.capture());
    }

    @Test
    public void move_piece_twice() {
        board.movePiece(E2, E4);
        board.movePiece(E4, E5);
        verify(pieceMove, times(2)).handle(pieceMoveEventArgumentCaptor.capture());
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

    @Test(expected = PieceCannotMoveThisWay.class)
    public void pieces_cannot_move_anywhere() {
        board.movePiece(E2, F3);
    }

    @Test(expected = CannotLandOnFriend.class)
    public void pieces_cannot_land_on_friends() {
        board.movePiece(A1, A2);
    }

    @Test(expected = CannotGoThroughAnotherPiece.class)
    public void rook_cannot_jump() {
        board.movePiece(A8, A6);
    }

    @Test
    public void knight_can_jump() {
        board.movePiece(B1, C3);
    }

    @Test
    public void capture() {
        board.movePiece(E2, E4);
        board.movePiece(F7, F5);
        board.movePiece(E4, F5);
        verify(pieceCaptured).handle(pieceCapturedEventArgumentCaptor.capture());
        PieceCaptured.Event event = pieceCapturedEventArgumentCaptor.getValue();
        assertThat(event.getPiece(), equalTo(Piece.PIECES.get(BLACK, PAWN)));
        assertThat(event.getPosition(), equalTo(F5));
    }

    @Test(expected = CannotAttackThisWay.class)
    public void pawn_cannot_attack_in_front() {
        board.movePiece(E2, E4);
        board.movePiece(E7, E5);
        board.movePiece(E4, E5);
    }

    @Test
    public void queen_capture_and_captured() {
        board.movePiece(E2, E4);
        board.movePiece(H7, H5);
        board.movePiece(D1, H5);
        board.movePiece(H8, H5);
        verify(pieceCaptured, times(2)).handle(pieceCapturedEventArgumentCaptor.capture());
    }

    @Test(expected = CannotBeInCheckAfterMove.class)
    public void king_cannot_be_in_check_after_his_move() {
        Piece[] rank8 = {O, O, O, k, O, O, O, O};
        Piece[] rank7 = {O, O, O, O, O, O, O, O};
        Piece[] rank6 = {O, O, O, O, O, O, O, O};
        Piece[] rank5 = {O, O, O, O, O, O, O, O};
        Piece[] rank4 = {O, O, O, O, O, O, O, O};
        Piece[] rank3 = {O, O, O, O, O, O, O, O};
        Piece[] rank2 = {O, O, O, O, O, O, O, O};
        Piece[] rank1 = {O, O, O, O, Q, O, O, O};
        Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Board board = new Board((file, rank) -> pieces[rank-1][FILES.indexOf(file)], eventBus);
        board.movePiece(D8, E7);
    }

    @Test(expected = CannotBeInCheckAfterMove.class)
    public void king_cannot_be_in_check_after_any_move() {
        Piece[] rank8 = {O, O, O, q, O, O, O, O};
        Piece[] rank7 = {O, O, O, O, O, O, O, O};
        Piece[] rank6 = {O, O, O, O, O, O, O, O};
        Piece[] rank5 = {O, O, O, O, O, O, O, O};
        Piece[] rank4 = {O, O, O, O, O, O, O, O};
        Piece[] rank3 = {O, O, O, O, O, O, O, O};
        Piece[] rank2 = {O, O, O, N, O, O, O, O};
        Piece[] rank1 = {O, O, O, K, O, O, O, O};
        Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Board board = new Board((file, rank) -> pieces[rank-1][FILES.indexOf(file)], eventBus);
        board.movePiece(D2, E4);
    }

    @Test(expected = CannotBeInCheckAfterMove.class)
    public void king_cannot_be_in_check_even_after_attack() {
        Piece[] rank8 = {O, k, O, O, O, O, O, O};
        Piece[] rank7 = {O, P, O, O, O, O, O, O};
        Piece[] rank6 = {O, O, K, O, O, O, O, O};
        Piece[] rank5 = {O, O, O, O, O, O, O, O};
        Piece[] rank4 = {O, O, O, O, O, O, O, O};
        Piece[] rank3 = {O, O, O, O, O, O, O, O};
        Piece[] rank2 = {O, O, O, O, O, O, O, O};
        Piece[] rank1 = {O, O, O, O, O, O, O, O};
        Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Board board = new Board((file, rank) -> pieces[rank-1][FILES.indexOf(file)], eventBus);
        board.movePiece(B8, B7);
    }

    @Test(expected = CannotBeInCheckAfterMove.class)
    public void when_in_check_you_must_protect_the_king() {
        Piece[] rank8 = {O, O, O, q, k, O, O, O};
        Piece[] rank7 = {O, O, O, O, O, O, O, O};
        Piece[] rank6 = {O, O, O, O, O, O, O, O};
        Piece[] rank5 = {O, O, O, O, O, O, O, O};
        Piece[] rank4 = {O, O, O, O, O, O, O, O};
        Piece[] rank3 = {O, O, O, O, O, O, O, O};
        Piece[] rank2 = {O, O, O, O, O, O, O, O};
        Piece[] rank1 = {O, O, O, O, Q, K, O, O};
        Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Board board = new Board((file, rank) -> pieces[rank-1][FILES.indexOf(file)], eventBus);
        board.movePiece(D8, F6);
    }

    @Test
    public void defend_check_by_removing_attacker() {
        Piece[] rank8 = {r, n, b, q, k, b, n, r};
        Piece[] rank7 = {p, p, p, p, p, p, p, p};
        Piece[] rank6 = {O, O, O, N, O, O, O, O};
        Piece[] rank5 = {O, O, O, O, O, O, O, O};
        Piece[] rank4 = {O, O, O, O, O, O, O, O};
        Piece[] rank3 = {O, O, O, O, O, O, O, O};
        Piece[] rank2 = {P, P, P, P, P, P, P, P};
        Piece[] rank1 = {R, N, B, Q, K, B, O, R};
        Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Board board = new Board((file, rank) -> pieces[rank-1][FILES.indexOf(file)], eventBus);
        assertFalse(board.checkMate(D6));
    }

    @Test
    public void defend_check_by_interposition() {
        Piece[] rank8 = {r, n, b, q, k, b, n, r};
        Piece[] rank7 = {p, p, p, p, p, O, p, p};
        Piece[] rank6 = {O, O, O, O, O, p, O, O};
        Piece[] rank5 = {O, O, O, O, O, O, O, Q};
        Piece[] rank4 = {O, O, O, O, O, O, O, O};
        Piece[] rank3 = {O, O, O, O, P, O, O, O};
        Piece[] rank2 = {P, P, P, P, O, P, P, P};
        Piece[] rank1 = {R, N, B, O, K, B, N, R};
        Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Board board = new Board((file, rank) -> pieces[rank-1][FILES.indexOf(file)], eventBus);
        assertFalse(board.checkMate(H5));
    }
}