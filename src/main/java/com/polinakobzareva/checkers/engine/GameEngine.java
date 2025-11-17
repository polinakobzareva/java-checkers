package com.polinakobzareva.checkers.engine;

import com.polinakobzareva.checkers.model.Board;
import com.polinakobzareva.checkers.model.Cell;
import com.polinakobzareva.checkers.model.Checker;


public class GameEngine {
    private Board board;
    private boolean isWhiteTurn;

    public GameEngine() {
        this.board = new Board();
        this.isWhiteTurn = true; // Белые начинают
    }

    public Board getBoard() {
        return board;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public boolean makeMove(int startRow, int startCol, int endRow, int endCol) {
        System.out.println("Ход из (" + startRow + "," + startCol + ") в (" + endRow + "," + endCol + ")");
        return true;
    }

    public boolean isGameOver() {
        return false;
    }
}