package com.github.fromi.chess.material.util;

import static com.github.fromi.chess.material.Board.FILES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.fromi.chess.material.Board;
import com.github.fromi.chess.material.MovesRecord;
import com.github.fromi.chess.material.Piece;
import com.github.fromi.chess.material.Square;
import com.google.common.eventbus.EventBus;

public class Boards {
    public static Board.Data createBoardMemento(Pieces.Piece[][] pieces) {
        List<Piece.Data> piecesData = new ArrayList<>();
        for (int row = 0 ; row < 8 ; row++) {
            for (int column = 0 ; column < 8 ; column++) {
                Pieces.Piece piece = pieces[row][column];
                if (piece != null) {
                    piecesData.add(new PieceData(piece.getColor(), piece.getType(), Square.SQUARES.get(FILES.get(column), row + 1)));
                }
            }
        }
        return piecesData::iterator;
    }

    public static Board empty() {
        EventBus eventBus = new EventBus();
        return new Board(Collections::emptyIterator, eventBus, new MovesRecord(eventBus));
    }
}
