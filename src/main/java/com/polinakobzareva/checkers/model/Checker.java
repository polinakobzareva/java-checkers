package com.polinakobzareva.checkers.model;

public class Checker {
    private final boolean isWhite;
    private CheckerType type;

    public Checker(boolean isWhite) {
        this.isWhite = isWhite;
        this.type = CheckerType.REGULAR;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public CheckerType getType() {
        return type;
    }

    public void setType(CheckerType type) {
        this.type = type;
    }

    public void makeKing() {
        this.type = CheckerType.KING;
    }

    public boolean isKing() {
        return this.type == CheckerType.KING;
    }
}