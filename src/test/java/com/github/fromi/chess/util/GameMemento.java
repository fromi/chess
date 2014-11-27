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

    public GameMemento(Board.Memento board) {
        this.board = board;
    }

    @Override
    public Board.Memento getBoard() {
        return board;
    }

    @Override
    public Map<Piece.Color, Player.State> getPlayersStates() {
        return ImmutableMap.of(WHITE, PLAYING, BLACK, WAITING);
    }
}
