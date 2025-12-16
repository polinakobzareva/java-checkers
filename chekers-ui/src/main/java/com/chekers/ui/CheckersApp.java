package com.chekers.ui;

import javax.swing.*;

public class CheckersApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CheckersFrame frame = new CheckersFrame();
            frame.setVisible(true);
        });
    }
}