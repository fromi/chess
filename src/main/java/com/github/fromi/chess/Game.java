package com.github.fromi.chess;

import static com.github.fromi.chess.Player.State.PLAYING;
import static com.github.fromi.chess.Player.State.WAITING;
import static com.github.fromi.chess.material.Piece.Color.WHITE;
import static com.google.common.collect.Maps.toMap;
import static java.util.EnumSet.allOf;

import java.util.Map;

import com.github.fromi.chess.material.Board;
import com.github.fromi.chess.material.MovesRecord;
import com.github.fromi.chess.material.Piece;
import com.google.common.eventbus.EventBus;

public class Game {
    private final EventBus eventBus = new EventBus();
    private final Map<Piece.Color, Player> players;
    private final Board board;
    private final MovesRecord movesRecord;

    public Game() {
        movesRecord = new MovesRecord(eventBus);
        board = new Board(eventBus, movesRecord);
        players = toMap(allOf(Piece.Color.class), color -> new Player(color, board, eventBus, color == WHITE ? PLAYING : WAITING));
    }

    public Game(Data data) {
        movesRecord = new MovesRecord(eventBus, data.getMovesRecord());
        board = new Board(data.getBoard(), eventBus, movesRecord);
        players = toMap(allOf(Piece.Color.class), color -> new Player(color, board, eventBus, data.getPlayersStates().get(color)));
    }

    public void register(Object listener) {
        eventBus.register(listener);
    }

    public Player player(Piece.Color color) {
        return players.get(color);
    }

    public static interface Data {
        Board.Data getBoard();
        Map<Piece.Color, Player.State> getPlayersStates();
        MovesRecord.Data getMovesRecord();
    }
}
