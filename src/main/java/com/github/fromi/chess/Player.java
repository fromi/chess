package com.github.fromi.chess;

import com.github.fromi.chess.material.Piece;
import com.github.fromi.chess.material.PieceMove;
import com.github.fromi.chess.material.Square;
import com.google.common.eventbus.EventBus;

public class Player {
    private final Color color;
    private final EventBus eventBus;
    private boolean isPlaying;

    public Player(Color color, boolean isPlaying, EventBus eventBus) {
        this.color = color;
        this.isPlaying = isPlaying;
        this.eventBus = eventBus;
    }

    public void move(Square origin, Square destination) {
        if (!isPlaying) {
            throw new NotPlayerTurn();
        }
        eventBus.post(new PieceMove.Event(color, Piece.PAWN, origin, destination));
    }

}
