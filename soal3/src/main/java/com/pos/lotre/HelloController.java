package com.pos.lotre;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Random;

public class HelloController {

    @FXML
    private BorderPane mainPane;

    private final int ROWS = 4;
    private final int COLS = 5;
    private final int BOMBS = 2;

    private Button[][] buttons;
    private int[][] data;
    private int safeBoxesRevealed;
    private boolean gameOver;
    private Label statusLabel;

    @FXML
    public void initialize() {
        statusLabel = new Label("Buka semua kotak aman untuk menang!");
        HBox statusBox = new HBox(statusLabel);
        statusBox.setAlignment(Pos.CENTER);
        statusBox.setPadding(new Insets(10));

        GridPane gridPane = createBoard();

        Button newGameButton = new Button("Game Baru");
        newGameButton.setOnAction(e -> resetGame());
        HBox buttonBox = new HBox(newGameButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        HBox statusContainer = new HBox(statusLabel);
        statusContainer.setAlignment(Pos.CENTER);
        statusContainer.setPadding(new Insets(5));

        BorderPane bottomPane = new BorderPane();
        bottomPane.setTop(statusContainer);
        bottomPane.setCenter(buttonBox);

        mainPane.setCenter(gridPane);
        mainPane.setBottom(bottomPane);
    }

    private GridPane createBoard() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10));

        buttons = new Button[ROWS][COLS];
        data = new int[ROWS][COLS];
        safeBoxesRevealed = 0;
        gameOver = false;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Button button = new Button();
                button.setPrefSize(60, 60);
                button.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                final int row = i;
                final int col = j;

                button.setOnAction(e -> handleButtonClick(row, col));

                buttons[i][j] = button;
                data[i][j] = 0;
                gridPane.add(button, j, i);
            }
        }

        placeBombs();

        return gridPane;
    }

    private void placeBombs() {
        Random random = new Random();
        int bombsPlaced = 0;

        while (bombsPlaced < BOMBS) {
            int row = random.nextInt(ROWS);
            int col = random.nextInt(COLS);

            if (data[row][col] != 1) {
                data[row][col] = 1;
                bombsPlaced++;
            }
        }
    }

    private void handleButtonClick(int row, int col) {
        if (gameOver || buttons[row][col].isDisabled()) {
            return;
        }

        buttons[row][col].setDisable(true);

        if (data[row][col] == 1) {
            buttons[row][col].setText("X");
            buttons[row][col].setStyle("-fx-background-color: #ff6666; -fx-font-size: 16px; -fx-font-weight: bold;");
            gameOver = true;
            revealAllBombs();
            showGameOverAlert(false);
        } else {
            buttons[row][col].setText("O");
            buttons[row][col].setStyle("-fx-background-color: #99cc99; -fx-font-size: 16px; -fx-font-weight: bold;");
            safeBoxesRevealed++;

            if (safeBoxesRevealed == (ROWS * COLS - BOMBS)) {
                gameOver = true;
                showGameOverAlert(true);
            }
        }
    }

    private void revealAllBombs() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (data[i][j] == 1) {
                    buttons[i][j].setText("X");
                    buttons[i][j].setDisable(true);
                    buttons[i][j].setStyle("-fx-background-color: #ff6666; -fx-font-size: 16px; -fx-font-weight: bold;");
                }
            }
        }
    }

    private void showGameOverAlert(boolean won) {
        Alert alert = new Alert(won ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Game Over");

        if (won) {
            alert.setHeaderText("Selamat!");
            alert.setContentText("Anda berhasil membuka semua kotak aman!");
            statusLabel.setText("Selamat! Anda menang!");
        } else {
            alert.setHeaderText("BOOM!");
            alert.setContentText("Anda mengenai bom! Game Over!");
            statusLabel.setText("BOOM! Anda kalah!");
        }

        alert.showAndWait();
    }

    private void resetGame() {
        mainPane.setCenter(createBoard());
        safeBoxesRevealed = 0;
        gameOver = false;
        statusLabel.setText("Buka semua kotak aman untuk menang!");
    }
}
