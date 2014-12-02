package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Board.FILES;
import static com.github.fromi.chess.material.IllegalMove.MoveRule.*;
import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Type.PAWN;
import static com.github.fromi.chess.material.util.Boards.createBoardMemento;
import static com.github.fromi.chess.material.util.IllegalMoveMatcher.illegalMoveBecause;
import static com.github.fromi.chess.material.util.Pieces.*;
import static com.github.fromi.chess.material.util.Squares.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.fromi.chess.material.util.Pieces;
import com.google.common.eventbus.EventBus;

@SuppressWarnings("JavacQuirks")
@RunWith(MockitoJUnitRunner.class)
public class BoardTest {

    private Board board;

    private final EventBus eventBus = new EventBus();

    @Mock
    MovesRecord movesRecord;

    @Mock
    PieceMove pieceMove;
    private final ArgumentCaptor<PieceMove.Event> pieceMoveEventArgumentCaptor = forClass(PieceMove.Event.class);

    @Mock
    PieceCaptured pieceCaptured;
    private final ArgumentCaptor<PieceCaptured.Event> pieceCapturedEventArgumentCaptor = forClass(PieceCaptured.Event.class);

    @Rule
    public final ExpectedException exception = none();

    @Before
    public void setUp() {
        board = new Board(eventBus, movesRecord);
        eventBus.register(pieceMove);
        eventBus.register(pieceCaptured);
    }

    @Test
    public void verify_setup() {
        Pieces.Piece[] rank8 = {r, n, b, q, k, b, n, r};
        Pieces.Piece[] rank7 = {p, p, p, p, p, p, p, p};
        Pieces.Piece[] rank6 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank5 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank4 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank3 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank2 = {P, P, P, P, P, P, P, P};
        Pieces.Piece[] rank1 = {R, N, B, Q, K, B, N, R};
        Pieces.Piece[][] expectedBoard = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        board.data().getPieces().forEachRemaining(piece -> {
            Pieces.Piece expected = expectedBoard[piece.getPosition().getRank() - 1][FILES.indexOf(piece.getPosition().getFile())];
            assertThat(piece.getColor(), equalTo(expected.getColor()));
            assertThat(piece.getType(), equalTo(expected.getType()));
        });
    }

    @Test
    public void move_piece() {
        board.pieceAt(E2).ifPresent(piece -> piece.moveTo(E4));
        verify(pieceMove).handle(pieceMoveEventArgumentCaptor.capture());
    }

    @Test
    public void move_piece_twice() {
        Piece piece = board.pieceAt(E2).get();
        piece.moveTo(E4);
        piece.moveTo(E5);
        verify(pieceMove, times(2)).handle(pieceMoveEventArgumentCaptor.capture());
    }

    @Test
    public void after_move_origin_is_empty() {
        board.pieceAt(E2).ifPresent(piece -> piece.moveTo(E4));
        assertFalse(board.pieceAt(E2).isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void destination_must_differ_from_origin() {
        board.pieceAt(E2).ifPresent(piece -> piece.moveTo(E2));
    }

    @Test
    public void pieces_cannot_move_anywhere() {
        exception.expect(illegalMoveBecause(CANNOT_MOVE_THIS_WAY));
        board.pieceAt(E2).ifPresent(piece -> piece.moveTo(F3));
    }

    @Test
    public void pieces_cannot_land_on_friends() {
        exception.expect(illegalMoveBecause(CANNOT_MOVE_ON_FRIEND));
        board.pieceAt(A1).ifPresent(piece -> piece.moveTo(A2));
    }

    @Test
    public void rook_cannot_jump() {
        exception.expect(illegalMoveBecause(CANNOT_GO_THROUGH_ANOTHER_PIECE));
        board.pieceAt(A8).ifPresent(piece -> piece.moveTo(A6));
    }

    @Test
    public void cannot_attack_through_another_piece() {
        exception.expect(illegalMoveBecause(CANNOT_GO_THROUGH_ANOTHER_PIECE));
        board.pieceAt(A8).ifPresent(piece -> piece.moveTo(A1));
    }

    @Test
    public void knight_can_jump() {
        board.pieceAt(B1).ifPresent(piece -> piece.moveTo(C3));
    }

    @Test
    public void capture() {
        board.pieceAt(E2).ifPresent(piece -> piece.moveTo(E4));
        board.pieceAt(F7).ifPresent(piece -> piece.moveTo(F5));
        board.pieceAt(E4).ifPresent(piece -> piece.moveTo(F5));
        verify(pieceCaptured).handle(pieceCapturedEventArgumentCaptor.capture());
        PieceCaptured.Event event = pieceCapturedEventArgumentCaptor.getValue();
        assertThat(event.getPieceColor(), equalTo(BLACK));
        assertThat(event.getPieceType(), equalTo(PAWN));
        assertThat(event.getPosition(), equalTo(F5));
    }

    @Test
    public void pawn_cannot_attack_in_front() {
        exception.expect(illegalMoveBecause(CANNOT_ATTACK_THIS_WAY));
        board.pieceAt(E2).ifPresent(piece -> piece.moveTo(E4));
        board.pieceAt(E7).ifPresent(piece -> piece.moveTo(E5));
        board.pieceAt(E4).ifPresent(piece -> piece.moveTo(E5));
    }

    @Test
    public void queen_capture_and_captured() {
        board.pieceAt(E2).ifPresent(piece -> piece.moveTo(E4));
        board.pieceAt(H7).ifPresent(piece -> piece.moveTo(H5));
        board.pieceAt(D1).ifPresent(piece -> piece.moveTo(H5));
        board.pieceAt(H8).ifPresent(piece -> piece.moveTo(H5));
        verify(pieceCaptured, times(2)).handle(pieceCapturedEventArgumentCaptor.capture());
    }

    @Test
    public void king_cannot_be_in_check_after_his_move() {
        exception.expect(illegalMoveBecause(KING_MUST_NOT_BE_IN_CHECK));
        Pieces.Piece[] rank8 = {_, _, _, k, _, _, _, _};
        Pieces.Piece[] rank7 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank6 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank5 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank4 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank3 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank2 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank1 = {_, _, _, _, Q, _, _, _};
        Pieces.Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Board board = new Board(createBoardMemento(pieces), eventBus, movesRecord);
        board.pieceAt(D8).ifPresent(piece -> piece.moveTo(E7));
    }

    @Test
    public void king_cannot_be_in_check_after_any_move() {
        exception.expect(illegalMoveBecause(KING_MUST_NOT_BE_IN_CHECK));
        Pieces.Piece[] rank8 = {_, _, _, q, k, _, _, _};
        Pieces.Piece[] rank7 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank6 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank5 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank4 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank3 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank2 = {_, _, _, N, _, _, _, _};
        Pieces.Piece[] rank1 = {_, _, _, K, _, _, _, _};
        Pieces.Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Board board = new Board(createBoardMemento(pieces), eventBus, movesRecord);
        board.pieceAt(D2).ifPresent(piece -> piece.moveTo(E4));
    }

    @Test
    public void king_cannot_be_in_check_even_after_attack() {
        exception.expect(illegalMoveBecause(KING_MUST_NOT_BE_IN_CHECK));
        Pieces.Piece[] rank8 = {_, k, _, _, _, _, _, _};
        Pieces.Piece[] rank7 = {_, P, _, _, _, _, _, _};
        Pieces.Piece[] rank6 = {_, _, K, _, _, _, _, _};
        Pieces.Piece[] rank5 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank4 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank3 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank2 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank1 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Board board = new Board(createBoardMemento(pieces), eventBus, movesRecord);
        board.pieceAt(B8).ifPresent(piece -> piece.moveTo(B7));
    }

    @Test
    public void when_in_check_you_must_protect_the_king() {
        exception.expect(illegalMoveBecause(KING_MUST_NOT_BE_IN_CHECK));
        Pieces.Piece[] rank8 = {_, _, _, q, k, _, _, _};
        Pieces.Piece[] rank7 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank6 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank5 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank4 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank3 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank2 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank1 = {_, _, _, _, Q, K, _, _};
        Pieces.Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Board board = new Board(createBoardMemento(pieces), eventBus, movesRecord);
        board.pieceAt(D8).ifPresent(piece -> piece.moveTo(F6));
    }

    @Test
    public void defend_check_by_removing_attacker() {
        Pieces.Piece[] rank8 = {r, n, b, q, k, b, n, r};
        Pieces.Piece[] rank7 = {p, p, p, p, p, p, p, p};
        Pieces.Piece[] rank6 = {_, _, _, N, _, _, _, _};
        Pieces.Piece[] rank5 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank4 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank3 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank2 = {P, P, P, P, P, P, P, P};
        Pieces.Piece[] rank1 = {R, N, B, Q, K, B, _, R};
        Pieces.Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Board board = new Board(createBoardMemento(pieces), eventBus, movesRecord);
        assertFalse(board.pieceAt(D6).get().checkMate());
    }

    @Test
    public void defend_check_by_interposition() {
        Pieces.Piece[] rank8 = {r, n, b, q, k, b, n, r};
        Pieces.Piece[] rank7 = {p, p, p, p, p, _, p, p};
        Pieces.Piece[] rank6 = {_, _, _, _, _, p, _, _};
        Pieces.Piece[] rank5 = {_, _, _, _, _, _, _, Q};
        Pieces.Piece[] rank4 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank3 = {_, _, _, _, P, _, _, _};
        Pieces.Piece[] rank2 = {P, P, P, P, _, P, P, P};
        Pieces.Piece[] rank1 = {R, N, B, _, K, B, N, R};
        Pieces.Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Board board = new Board(createBoardMemento(pieces), eventBus, movesRecord);
        assertFalse(board.pieceAt(H5).get().checkMate());
    }

    @Test
    public void stalemate_by_piece_interposition() {
        Pieces.Piece[] rank8 = {k, _, K, _, _, _, _, _};
        Pieces.Piece[] rank7 = {_, p, _, _, _, _, _, _};
        Pieces.Piece[] rank6 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank5 = {_, _, B, Q, _, _, _, _};
        Pieces.Piece[] rank4 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank3 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank2 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[] rank1 = {_, _, _, _, _, _, _, _};
        Pieces.Piece[][] pieces = {rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8};
        Board board = new Board(createBoardMemento(pieces), eventBus, movesRecord);
        assertFalse(board.pieceAt(B7).get().canMove());
        exception.expect(IllegalMove.class);
        exception.expectMessage("BLACK PAWN in b7 cannot move to b6 because " + KING_MUST_NOT_BE_IN_CHECK.toString());
        board.pieceAt(B7).get().moveTo(B6);
    }
}