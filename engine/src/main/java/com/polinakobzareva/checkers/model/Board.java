package com.polinakobzareva.checkers.model;

/**
 * Игровую доска.
 */
public class Board {
    public static final int SIZE = 8;
    private Cell[][] grid;

    public Board() {
        grid = new Cell[SIZE][SIZE];
        initializeBoard();
    }

    /**
     * Класс создает клетки и расставляет шашки.
     */
    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                grid[row][col] = new Cell(row, col);
            }
        }


        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 1) {
                    grid[row][col].setChecker(new Checker(false));
                }
            }
        }

        for (int row = 5; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 1) {
                    grid[row][col].setChecker(new Checker(true));
                }
            }
        }
    }

    public Cell getCell(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            return grid[row][col];
        }
        return null;
    }
    /**
     * Проверка на игровую клетку.
     */
    public boolean isDarkCell(int row, int col) {
        return (row + col) % 2 == 1;
    }
}