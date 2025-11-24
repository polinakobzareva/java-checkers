package com.polinakobzareva.checkers.model;

public class Cell {
    private final int row;
    private final int col;
    private Checker checker;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Checker getChecker() {
        return checker;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }

    public boolean isEmpty() {
        return checker == null;
    }
}