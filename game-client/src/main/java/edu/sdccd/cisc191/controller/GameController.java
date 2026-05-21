package edu.sdccd.cisc191.controller;

import edu.sdccd.cisc191.grpc.JoinMatchResponse;
import edu.sdccd.cisc191.grpc.MatchHistoryResponse;
import edu.sdccd.cisc191.grpc.MatchResultResponse;
import edu.sdccd.cisc191.model.MatchViewModel;
import edu.sdccd.cisc191.service.GameGrpcClient;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GameController {

    @FXML private TextField playerNameField;
    @FXML private Label statusLabel;
    @FXML private Label playerLabel;
    @FXML private Label opponentLabel;
    @FXML private Label winnerLabel;
    @FXML private Label matchSummaryLabel;
    @FXML private TextArea matchLog;
    @FXML private ComboBox<String> difficultyComboBox;
    @FXML private CheckBox rankedMatchCheckBox;

    private final MatchViewModel match = new MatchViewModel();
    private final GameGrpcClient grpcClient = new GameGrpcClient("localhost", 50051);

    @FXML
    private void initialize() {
        difficultyComboBox.getItems().addAll("Easy", "Normal", "Hard");
        difficultyComboBox.setValue("Normal");

        match.resetLocalState();
        updateView();

        log("Client loaded. Start the gRPC server, then click Join Match.");
    }


    @FXML
    private void handleJoinMatch() {

        String playerName = getPlayerName();
        String difficulty = difficultyComboBox.getValue();
        boolean ranked = rankedMatchCheckBox.isSelected();

        statusLabel.setText("Status: Joining match...");
        log(buildJoinLogMessage(playerName, difficulty, ranked));

        Task<JoinMatchResponse> task = grpcClient.joinMatchTask(playerName, difficulty, ranked);

        task.setOnSucceeded(e -> {
            JoinMatchResponse response = task.getValue();

            match.setMatchId(response.getMatchId());
            match.getPlayer().setName(response.getPlayerName());
            match.getOpponent().setName(response.getOpponentName());
            match.setMatchOver(false);
            match.setWinnerName("");

            statusLabel.setText("Status: Match ready");
            log(response.getMessage());
            updateView();
        });

        task.setOnFailed(e -> handleError("Could not join match", task.getException()));

        runInBackground(task);
    }


    @FXML
    private void handlePlayMatch() {

        if (!match.canPlayMatch()) {
            log("Join a match before playing.");
            return;
        }

        statusLabel.setText("Status: Playing match...");
        log("Server is choosing a winner...");

        Task<MatchResultResponse> task = grpcClient.playMatchTask(
                match.getMatchId(),
                match.getPlayer().getName()
        );

        task.setOnSucceeded(e -> {
            MatchResultResponse response = task.getValue();

            match.recordCompletedMatchThreadSafely(response.getWinnerName());

            statusLabel.setText(response.getPlayerWon()
                    ? "Status: You won!"
                    : "Status: You lost.");

            log(response.getMessage());
            updateView();
        });

        task.setOnFailed(e -> handleError("Could not play match", task.getException()));

        runInBackground(task);
    }


    @FXML
    private void handleLoadHistory() {

        String playerName = getPlayerName();

        log("Loading match history...");

        Task<MatchHistoryResponse> task = grpcClient.loadMatchHistoryTask(playerName);

        task.setOnSucceeded(e -> {
            MatchHistoryResponse response = task.getValue();

            log("Match history:");
            response.getMatchesList().forEach(m -> log("- " + m));
        });

        task.setOnFailed(e -> handleError("Could not load history", task.getException()));

        runInBackground(task);
    }

    @FXML
    private void handleResetLocalView() {
        match.resetLocalState();
        statusLabel.setText("Status: Local view reset");
        log("Client state reset.");
        updateView();
    }

    private String getPlayerName() {
        String name = playerNameField.getText();
        return (name == null || name.isBlank()) ? "Player" : name.trim();
    }

    private void log(String message) {
        matchLog.appendText(message + "\n");
    }

    private void handleError(String context, Throwable ex) {
        statusLabel.setText("Status: Error");
        log(context);
        log("Error: " + (ex != null ? ex.getMessage() : "unknown"));
    }


    private void updateView() {
        runOnFxThread(() -> {

            playerLabel.setText("Player: " + match.getPlayer().getName());
            opponentLabel.setText("Opponent: " + match.getOpponent().getName());

            winnerLabel.setText(match.getWinnerName().isBlank()
                    ? "Winner: TBD"
                    : "Winner: " + match.getWinnerName());

            if (matchSummaryLabel != null) {
                matchSummaryLabel.setText(
                        "Summary: " + match.buildMatchSummary(
                                difficultyComboBox.getValue(),
                                rankedMatchCheckBox.isSelected()
                        )
                );
            }
        });
    }



    public static String buildJoinLogMessage(String playerName, String difficulty, boolean ranked) {

        String safePlayer = (playerName == null || playerName.isBlank())
                ? "Player"
                : playerName.trim();

        String safeDifficulty = (difficulty == null || difficulty.isBlank())
                ? "Normal"
                : difficulty.trim();

        String type = ranked ? "ranked" : "casual";

        return "Joining " + type + " match as " + safePlayer +
                " on " + safeDifficulty + " difficulty...";
    }

    public static void runOnFxThread(Runnable action) {
        if (action == null) return;

        if (Platform.isFxApplicationThread()) {
            action.run();
        } else {
            Platform.runLater(action);
        }
    }

    private void runInBackground(Task<?> task) {
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }
}