package com.github.fromi.chess;

public class IllegalAction extends IllegalStateException {
    public IllegalAction(String message) {
        super(message);
    }
}
