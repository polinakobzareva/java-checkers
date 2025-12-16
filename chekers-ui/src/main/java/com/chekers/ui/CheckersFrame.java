package com.chekers.ui;

import com.polinakobzareva.checkers.engine.GameEngine;
import com.polinakobzareva.checkers.model.Cell;
import com.polinakobzareva.checkers.model.Checker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CheckersFrame extends JFrame {
    private static final int TILE_SIZE = 80;
    private static final int BOARD_SIZE = 8;

    private GameEngine gameEngine;
    private BoardPanel boardPanel;
    private JLabel statusLabel;
    private Cell selectedCell;

    public CheckersFrame() {
        gameEngine = new GameEngine();
        selectedCell = null;

        initUI();
    }

    private void initUI() {
        setTitle("Шашки");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("ШАШКИ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(0, 0, 139));

        statusLabel = new JLabel("Ход белых (WHITE)");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(statusLabel);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton restartButton = new JButton("Новая игра");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 14));
        restartButton.addActionListener(this::restartGame);

        JButton rulesButton = new JButton("Правила");
        rulesButton.setFont(new Font("Arial", Font.PLAIN, 14));
        rulesButton.addActionListener(this::showRules);

        panel.add(restartButton);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(rulesButton);

        return panel;
    }

    private void restartGame(ActionEvent e) {
        gameEngine = new GameEngine();
        selectedCell = null;
        boardPanel.repaint();
        updateStatus();
    }

    private void showRules(ActionEvent e) {
        String rules = """
            1. Ходят по очереди, начиная с белых
            2. Простые шашки ходят только вперед по диагонали
            3. Дамки ходят на любое расстояние по диагонали
            4. Если есть возможность бить - нужно бить
            5. Шашка становится дамкой, достигнув последнего ряда
            """;

        JOptionPane.showMessageDialog(this, rules, "Правила шашек", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateStatus() {
        String player = gameEngine.getCurrentPlayer();
        String color = gameEngine.isWhiteTurn() ? "белые" : "черные";
        statusLabel.setText("Ход: " + player + " (" + color + ")");
    }

    private void handleCellClick(int row, int col) {
        Cell clickedCell = gameEngine.getBoard().getCell(row, col);

        if (selectedCell == null) {
            if (clickedCell != null && !clickedCell.isEmpty()) {
                if ((gameEngine.isWhiteTurn() && clickedCell.getChecker().isWhite()) ||
                        (!gameEngine.isWhiteTurn() && !clickedCell.getChecker().isWhite())) {

                    selectedCell = clickedCell;
                    boardPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Сейчас ходит " + (gameEngine.isWhiteTurn() ? "белый" : "черный") + " игрок!",
                            "Не ваш ход", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            boolean validMove = gameEngine.makeMove(
                    selectedCell.getRow(), selectedCell.getCol(),
                    row, col
            );

            if (validMove) {
                selectedCell = null;
                boardPanel.repaint();
                updateStatus();

                if (gameEngine.isGameOver()) {
                    String winner = gameEngine.getWinner();
                    JOptionPane.showMessageDialog(this,
                            "Победитель: " + winner + "!",
                            "Игра окончена", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Этот ход невозможен!",
                        "Неверный ход", JOptionPane.WARNING_MESSAGE);
            }

            selectedCell = null;
            boardPanel.repaint();
        }
    }

    class BoardPanel extends JPanel {
        public BoardPanel() {
            setPreferredSize(new Dimension(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE));
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int col = e.getX() / TILE_SIZE;
                    int row = e.getY() / TILE_SIZE;

                    if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
                        if ((row + col) % 2 == 1) {
                            handleCellClick(row, col);
                        }
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawBoard(g2d);
            drawCheckers(g2d);
            drawSelection(g2d);
        }

        private void drawBoard(Graphics2D g2d) {
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    int x = col * TILE_SIZE;
                    int y = row * TILE_SIZE;

                    if ((row + col) % 2 == 0) {
                        g2d.setColor(new Color(240, 240, 200));
                    } else {
                        g2d.setColor(new Color(139, 69, 19));
                    }

                    g2d.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, y, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        private void drawCheckers(Graphics2D g2d) {
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    Cell cell = gameEngine.getBoard().getCell(row, col);
                    if (cell != null && !cell.isEmpty()) {
                        drawChecker(g2d, cell, col, row);
                    }
                }
            }
        }

        private void drawChecker(Graphics2D g2d, Cell cell, int col, int row) {
            int x = col * TILE_SIZE + TILE_SIZE / 2;
            int y = row * TILE_SIZE + TILE_SIZE / 2;
            int radius = TILE_SIZE / 2 - 5;

            Checker checker = cell.getChecker();

            if (checker.isWhite()) {
                g2d.setColor(Color.WHITE);
            } else {
                g2d.setColor(Color.BLACK);
            }

            g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);

            if (checker.isKing()) {
                g2d.setColor(Color.YELLOW);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);
                g2d.setStroke(new BasicStroke(1));

                g2d.setColor(Color.red);
                g2d.fillOval(x - radius/2, y - radius/2, radius, radius);
            } else {
                g2d.setColor(Color.GRAY);
                g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);
            }
        }

        private void drawSelection(Graphics2D g2d) {
            if (selectedCell != null) {
                int x = selectedCell.getCol() * TILE_SIZE;
                int y = selectedCell.getRow() * TILE_SIZE;

                g2d.setColor(Color.YELLOW);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
                g2d.setStroke(new BasicStroke(1));
            }
        }
    }
}