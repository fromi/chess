package com.github.fromi.chess;

import static com.github.fromi.chess.material.Piece.Type.PAWN;

import com.github.fromi.chess.material.Board;
import com.github.fromi.chess.material.Piece;
import com.github.fromi.chess.material.PieceMove;
import com.github.fromi.chess.material.Square;
import com.google.common.eventbus.EventBus;

public class Player {
    private final Piece.Color color;
    private final EventBus eventBus;
    private final Board board;
    private boolean isPlaying;

    public Player(Piece.Color color, Board board, EventBus eventBus, boolean isPlaying) {
        this.color = color;
        this.board = board;
        this.isPlaying = isPlaying;
        this.eventBus = eventBus;
        watchGame(eventBus);
    }

    private void watchGame(EventBus eventBus) {
        eventBus.register((NextPlayer) event -> isPlaying = !isPlaying);
    }

    public void move(Square origin, Square destination) {
        if (!isPlaying) {
            throw new NotPlayerTurn();
        }
        if (!board.getPieceAt(origin).isSameColorAs(color)) {
            throw new CannotMoveOpponentPiece();
        }
        eventBus.post(new PieceMove.Event(color, PAWN, origin, destination));
        eventBus.post(new NextPlayer.Event());
    }

}
