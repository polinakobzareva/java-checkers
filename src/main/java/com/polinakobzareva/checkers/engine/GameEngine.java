package com.polinakobzareva.checkers.engine;

import com.polinakobzareva.checkers.model.Board;
import com.polinakobzareva.checkers.model.Cell;
import com.polinakobzareva.checkers.model.Checker;

import java.util.ArrayList;
import java.util.List;

/**
 * Основная логика шашек
 */
public class GameEngine {
    private Board board;
    private boolean isWhiteTurn;

    public GameEngine() {
        this.board = new Board();
        this.isWhiteTurn = true;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    /**
     * Попытка сделать ход
     */
    public boolean makeMove(int startRow, int startCol, int endRow, int endCol) {
        Cell startCell = board.getCell(startRow, startCol);
        Cell endCell = board.getCell(endRow, endCol);


        if (startCell == null || endCell == null || startCell.isEmpty()) {
            return false;
        }

        Checker movingChecker = startCell.getChecker();


        if (isWhiteTurn != movingChecker.isWhite()) {
            return false;
        }


        if (!endCell.isEmpty()) {
            return false;
        }


        int rowDiff = endRow - startRow;
        int colDiff = Math.abs(endCol - startCol);

        if (colDiff != Math.abs(rowDiff)) {
            return false;
        }


        if (!movingChecker.isKing()) {
            int direction = movingChecker.isWhite() ? -1 : 1;


            if (rowDiff == direction && colDiff == 1) {
                performMove(startCell, endCell);
                return true;
            }

            if (rowDiff == 2 * direction && colDiff == 2) {
                int middleRow = startRow + direction;
                int middleCol = (startCol + endCol) / 2;
                Cell middleCell = board.getCell(middleRow, middleCol);

                if (middleCell != null && !middleCell.isEmpty() &&
                        middleCell.getChecker().isWhite() != movingChecker.isWhite()) {
                    performMoveWithCapture(startCell, endCell, middleCell);
                    return true;
                }
            }
        } else {

            return makeKingMove(startRow, startCol, endRow, endCol, movingChecker);
        }

        return false;
    }

    /**
     * Логика хода для дамки
     */
    private boolean makeKingMove(int startRow, int startCol, int endRow, int endCol, Checker king) {
        int rowDirection = Integer.compare(endRow, startRow);
        int colDirection = Integer.compare(endCol, startCol);


        List<Cell> path = new ArrayList<>();
        List<Cell> enemyCheckersOnPath = new ArrayList<>();

        int currentRow = startRow + rowDirection;
        int currentCol = startCol + colDirection;

        while (currentRow != endRow || currentCol != endCol) {
            Cell currentCell = board.getCell(currentRow, currentCol);
            if (currentCell == null) {
                return false;
            }

            path.add(currentCell);

            if (!currentCell.isEmpty()) {
                if (currentCell.getChecker().isWhite() == king.isWhite()) {
                    return false;
                } else {
                    enemyCheckersOnPath.add(currentCell);
                }
            }

            currentRow += rowDirection;
            currentCol += colDirection;
        }


        if (enemyCheckersOnPath.size() == 0) {

            performMove(board.getCell(startRow, startCol), board.getCell(endRow, endCol));
            return true;
        } else if (enemyCheckersOnPath.size() == 1) {

            Cell enemyCell = enemyCheckersOnPath.get(0);
            performMoveWithCapture(
                    board.getCell(startRow, startCol),
                    board.getCell(endRow, endCol),
                    enemyCell
            );
            return true;
        }

        return false;
    }

    private void performMove(Cell from, Cell to) {
        to.setChecker(from.getChecker());
        from.setChecker(null);
        checkForKing(to);
        switchTurn();
    }

    private void performMoveWithCapture(Cell from, Cell to, Cell captured) {
        to.setChecker(from.getChecker());
        from.setChecker(null);
        captured.setChecker(null);
        checkForKing(to);
        switchTurn();
    }

    private void checkForKing(Cell cell) {
        Checker checker = cell.getChecker();
        int row = cell.getRow();

        if (checker.isWhite() && row == 0) {
            checker.makeKing();
        } else if (!checker.isWhite() && row == Board.SIZE - 1) {
            checker.makeKing();
        }
    }

    private void switchTurn() {
        isWhiteTurn = !isWhiteTurn;
    }

    /**
     * Проверка окончания игры
     */
    public boolean isGameOver() {
        int whiteCount = 0;
        int blackCount = 0;

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Cell cell = board.getCell(row, col);
                if (!cell.isEmpty()) {
                    if (cell.getChecker().isWhite()) {
                        whiteCount++;
                    } else {
                        blackCount++;
                    }
                }
            }
        }

        return whiteCount == 0 || blackCount == 0;
    }

    /**
     * Получить цвет победителя
     */
    public String getWinner() {
        if (!isGameOver()) return null;

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Cell cell = board.getCell(row, col);
                if (!cell.isEmpty()) {
                    return cell.getChecker().isWhite() ? "WHITE" : "BLACK";
                }
            }
        }
        return null;
    }

    /**
     * Получить текущего игрока
     */
    public String getCurrentPlayer() {
        return isWhiteTurn ? "WHITE" : "BLACK";
    }
}