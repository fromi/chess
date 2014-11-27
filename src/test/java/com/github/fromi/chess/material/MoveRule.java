package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.Square.SQUARES;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.rules.Verifier;

public class MoveRule extends Verifier {

    private final Collection<PieceMoveRule> pieceMoveRules = new ArrayList<>();

    @Override
    protected void verify() throws Throwable {
        for (PieceMoveRule pieceMoveRule : pieceMoveRules) {
            pieceMoveRule.apply();
        }
    }

    public PieceMoveRule with(Piece piece) {
        PieceMoveRule pieceMoveRule = new PieceMoveRule(piece);
        pieceMoveRules.add(pieceMoveRule);
        return pieceMoveRule;
    }

    public class PieceMoveRule {

        private final Piece piece;
        private Boolean[][] validMoves;

        private PieceMoveRule(Piece piece) {
            this.piece = piece;
        }

        protected void apply() {
            for (int column = 0; column < Board.SIZE; column++) {
                for (int row = 0; row < Board.SIZE; row++) {
                    Square destination = SQUARES.get(Board.FILES.get(column), Board.RANKS.get(row));
                    assertThat(String.format("%s move to %s", piece, destination),
                               piece.onEmptyBoardCouldMoveTo(destination), equalTo(validMoves[row][column]));
                }
            }
        }

        public void expect(Boolean[][] validMoves) {
            this.validMoves = validMoves;
        }
    }
}
