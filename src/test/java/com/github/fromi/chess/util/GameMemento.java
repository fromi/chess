package com.github.fromi.chess.util;

import java.util.Optional;

import com.github.fromi.chess.Game;
import com.github.fromi.chess.material.Board;
import com.github.fromi.chess.material.Piece;

public class GameMemento implements Game.Memento {
    private final Board.Memento board;
    private final Optional<Piece.Color> activePlayer;

    public GameMemento(Board.Memento board) {
        this.board = board;
        this.activePlayer = Optional.of(Piece.Color.WHITE);
    }

    @Override
    public Board.Memento getBoard() {
        return board;
    }

    @Override
    public Optional<Piece.Color> getActivePlayer() {
        return activePlayer;
    }
}
