package com.github.fromi.chess.util;

import static com.github.fromi.chess.Player.State.PLAYING;
import static com.github.fromi.chess.Player.State.WAITING;
import static com.github.fromi.chess.material.Piece.Color.BLACK;
import static com.github.fromi.chess.material.Piece.Color.WHITE;

import java.util.Map;

import com.github.fromi.chess.Game;
import com.github.fromi.chess.Player;
import com.github.fromi.chess.material.Board;
import com.github.fromi.chess.material.MovesRecord;
import com.github.fromi.chess.material.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class GameData implements Game.Data {
    private final Board.Data board;
    private final Player.State whiteState;
    private final Player.State blackState;

    public GameData(Board.Data board) {
        this(board, WHITE);
    }

    public GameData(Board.Data board, Piece.Color activePlayer) {
        this.board = board;
        whiteState = activePlayer == WHITE ? PLAYING : WAITING;
        blackState = activePlayer == BLACK ? PLAYING : WAITING;
    }

    @Override
    public Board.Data getBoard() {
        return board;
    }

    @Override
    public Map<Piece.Color, Player.State> getPlayersStates() {
        return ImmutableMap.of(WHITE, whiteState, BLACK, blackState);
    }

    @Override
    public MovesRecord.Data getMovesRecord() {
        return ImmutableList::of;
    }
}
