package com.github.fromi.chess.material;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.EventBus;

public class MovesRecord {
    private final List<PieceMove.Event> moves;

    public MovesRecord(EventBus eventBus) {
        moves = new ArrayList<>();
        eventBus.register((PieceMove) moves::add);
    }

    public MovesRecord(EventBus eventBus, Data data) {
        moves = new ArrayList<>(data.getMoves());
        eventBus.register((PieceMove) moves::add);
    }

    public boolean hasMoved(Piece piece) {
        return moves.stream().anyMatch(move -> move.getDestination().equals(piece.position));
    }

    public static interface Data {
        List<PieceMove.Event> getMoves();
    }
}
