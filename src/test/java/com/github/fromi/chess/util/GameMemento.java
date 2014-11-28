package com.github.fromi.chess.util;

import static com.github.fromi.chess.Player.State.PLAYING;
import static com.github.fromi.chess.Player.State.WAITING;
import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;

import java.util.Map;

import com.github.fromi.chess.Game;
import com.github.fromi.chess.Player;
import com.github.fromi.chess.material.Board;
import com.github.fromi.chess.material.Piece;
import com.google.common.collect.ImmutableMap;

public class GameMemento implements Game.Memento {
    private final Board.Memento board;
    private final Player.State whiteState;
    private final Player.State blackState;

    public GameMemento(Board.Memento board) {
        this(board, WHITE);
    }

    public GameMemento(Board.Memento board, Piece.Color activePlayer) {
        this.board = board;
        whiteState = activePlayer == WHITE ? PLAYING : WAITING;
        blackState = activePlayer == BLACK ? PLAYING : WAITING;
    }

    @Override
    public Board.Memento getBoard() {
        return board;
    }

    @Override
    public Map<Piece.Color, Player.State> getPlayersStates() {
        return ImmutableMap.of(WHITE, whiteState, BLACK, blackState);
    }
}
