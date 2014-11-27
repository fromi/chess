package com.github.fromi.chess;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import com.github.fromi.chess.material.Board;
import com.github.fromi.chess.material.Piece;
import com.github.fromi.chess.material.Square;
import com.github.fromi.chess.material.SquareEmpty;
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
        checkArgument(!origin.equals(destination));
        if (!isPlaying) {
            throw new NotPlayerTurn();
        }
        Optional<Piece> pieceOptional = board.pieceAt(origin);
        if (!pieceOptional.isPresent()) {
            throw new SquareEmpty();
        }
        Piece piece = pieceOptional.get();
        if (!piece.hasSame(color)) {
            throw new CannotMoveOpponentPiece();
        }
        piece.moveTo(destination);
        if (piece.checkMate()) {
            eventBus.post(new CheckMate.Event(color));
        } else if (opponentIsStalemate()) {
            eventBus.post(new Stalemate.Event());
        } else {
            eventBus.post(new NextPlayer.Event());
        }
    }

    private boolean opponentIsStalemate() {
        return !board.pieces(color.opponent()).stream().anyMatch(Piece::canMove);
    }
}
