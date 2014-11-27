package com.github.fromi.chess;

import static com.github.fromi.chess.Player.State.PLAYING;
import static com.github.fromi.chess.Player.State.WAITING;

import java.util.Optional;

import com.github.fromi.chess.material.Board;
import com.github.fromi.chess.material.Pawn;
import com.github.fromi.chess.material.Piece;
import com.github.fromi.chess.material.Square;
import com.github.fromi.chess.material.SquareEmpty;
import com.google.common.eventbus.EventBus;

public class Player {
    private final Piece.Color color;
    private final EventBus eventBus;
    private final Board board;
    private State state;

    public Player(Piece.Color color, Board board, EventBus eventBus, State state) {
        this.color = color;
        this.board = board;
        this.eventBus = eventBus;
        this.state = state;
        watchGame(eventBus);
    }

    private void watchGame(EventBus eventBus) {
        eventBus.register((NextPlayer) event -> state = state == WAITING ? PLAYING : WAITING);
    }

    public void move(Square origin, Square destination) {
        state.move(this, origin, destination);
    }

    public void promoteTo(Piece.Type pieceType) {
        state.promoteTo(this, pieceType);
    }

    private void postConsequencesFromMoving(Piece piece) {
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

    public static enum State implements Action {
        PLAYING {
            @Override
            public String toString() {
                return "Player must move a piece";
            }

            @Override
            public void move(Player player, Square origin, Square destination) {
                Optional<Piece> pieceOptional = player.board.pieceAt(origin);
                pieceOptional.orElseThrow(SquareEmpty::new);
                Piece piece = pieceOptional.get();
                if (!piece.hasSame(player.color)) {
                    throw new CannotMoveOpponentPiece();
                }
                piece.moveTo(destination);
                if (piece instanceof Pawn && ((Pawn) piece).canBePromoted()) {
                    player.state = PROMOTING;
                } else {
                    player.postConsequencesFromMoving(piece);
                }
            }
        }, PROMOTING {
            @Override
            public String toString() {
                return "Player must promote the pawn";
            }

            @Override
            public void promoteTo(Player player, Piece.Type pieceType) {
                Piece piece = player.board.promotePawnTo(player.color, pieceType);
                player.postConsequencesFromMoving(piece);
            }
        }, WAITING {
            @Override
            public String toString() {
                return "Player waits his opponent to play";
            }
        }
    }

    public static interface Action {
        default void move(Player player, Square origin, Square destination) {
            throw new IllegalAction(player.state.toString());
        }

        default void promoteTo(Player player, Piece.Type pieceType) {
            throw new IllegalAction(player.state.toString());
        }
    }
}
