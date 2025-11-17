package com.polinakobzareva.checkers.demo;

import com.polinakobzareva.checkers.engine.GameEngine;
import com.polinakobzareva.checkers.model.Board;
import com.polinakobzareva.checkers.model.Cell;
import com.polinakobzareva.checkers.model.Checker;

import java.util.Scanner;

/**
 * Консольная демка игры в шашки
 */
public class ConsoleCheckers {
    private GameEngine gameEngine;
    private Scanner scanner;

    public ConsoleCheckers() {
        this.gameEngine = new GameEngine();
        this.scanner = new Scanner(System.in);
    }

    public void startGame() {
        System.out.println("=== ИГРА В ШАШКИ ===");
        System.out.println("Формат хода: строка_начало столбец_начало строка_конец столбец_конец");
        System.out.println("Пример: 5 0 4 1 - ход из клетки (5,0) в (4,1)");
        System.out.println();

        while (!gameEngine.isGameOver()) {
            printBoard();
            System.out.println("Ход игрока: " + gameEngine.getCurrentPlayer());
            System.out.print("Введите ход: ");

            try {
                int startRow = scanner.nextInt();
                int startCol = scanner.nextInt();
                int endRow = scanner.nextInt();
                int endCol = scanner.nextInt();

                boolean validMove = gameEngine.makeMove(startRow, startCol, endRow, endCol);
                if (!validMove) {
                    System.out.println("Неверный ход! Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка ввода! Используйте формат: строка столбец строка столбец");
                scanner.nextLine();
            }
        }

        printBoard();
        System.out.println("Игра окончена! Победитель: " + gameEngine.getWinner());
        scanner.close();
    }

    private void printBoard() {
        System.out.println("\n  0 1 2 3 4 5 6 7");
        for (int row = 0; row < Board.SIZE; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < Board.SIZE; col++) {
                Cell cell = gameEngine.getBoard().getCell(row, col);
                if (cell.isEmpty()) {
                    System.out.print(". ");
                } else {
                    Checker checker = cell.getChecker();
                    if (checker.isWhite()) {
                        System.out.print(checker.isKing() ? "W " : "w ");
                    } else {
                        System.out.print(checker.isKing() ? "B " : "b ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ConsoleCheckers game = new ConsoleCheckers();
        game.startGame();
    }
}