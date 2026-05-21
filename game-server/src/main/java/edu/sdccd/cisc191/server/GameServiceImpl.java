package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.grpc.GameServiceGrpc;
import edu.sdccd.cisc191.grpc.JoinMatchRequest;
import edu.sdccd.cisc191.grpc.JoinMatchResponse;
import edu.sdccd.cisc191.grpc.MatchHistoryRequest;
import edu.sdccd.cisc191.grpc.MatchHistoryResponse;
import edu.sdccd.cisc191.grpc.MatchResultResponse;
import edu.sdccd.cisc191.grpc.PlayMatchRequest;
import io.grpc.stub.StreamObserver;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameServiceImpl extends GameServiceGrpc.GameServiceImplBase {

    private final Map<String, ServerMatch> matches = new ConcurrentHashMap<>();
    private final MatchStatistics statistics = new MatchStatistics();
    private final Random random = new Random();

    @Override
    public void joinMatch(
            JoinMatchRequest request,
            StreamObserver<JoinMatchResponse> responseObserver
    ) {
        String playerName = request.getPlayerName().isBlank()
                ? "Player"
                : request.getPlayerName();

        String difficulty = request.getDifficulty().isBlank()
                ? "Normal"
                : request.getDifficulty();

        boolean ranked = request.getRanked();
        String matchId = UUID.randomUUID().toString();

        ServerMatch match = new ServerMatch(
                matchId,
                playerName,
                "Bot (" + difficulty + ")",
                difficulty,
                ranked
        );

        matches.put(matchId, match);
        statistics.recordJoin();

        JoinMatchResponse response = JoinMatchResponse.newBuilder()
                .setMatchId(matchId)
                .setPlayerName(match.playerName())
                .setOpponentName(match.opponentName())
                .setMessage("Joined " + match.matchType() + " match " + matchId
                        + " on " + difficulty + " difficulty. Click Play Match to let the server choose a winner.")
<<<<<<< HEAD
                .setSummary(buildJoinSummary(
                        matchId,
                        match.playerName(),
                        match.opponentName(),
                        difficulty,
                        ranked
                ))
=======
                .setSummary(buildJoinSummary(matchId, playerName, match.opponentName(), difficulty, ranked))
>>>>>>> 5c7e5d2cb4f798d0f6c151274b60aab884c6d06b
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * TODO 6: Builds a readable match summary for the gRPC response.
     * Format: Match match-001: Ada vs Bot (Hard, ranked)
     */
    public static String buildJoinSummary(
            String matchId,
            String playerName,
            String opponentName,
            String difficulty,
            boolean ranked
    ) {
<<<<<<< HEAD

=======
>>>>>>> 5c7e5d2cb4f798d0f6c151274b60aab884c6d06b
        if (matchId == null || matchId.isBlank()) {
            return "No match";
        }

<<<<<<< HEAD
        String safePlayer =
                (playerName == null || playerName.isBlank())
                        ? "Player"
                        : playerName.trim();

        String safeOpponent =
                (opponentName == null || opponentName.isBlank())
                        ? "Bot"
                        : opponentName.trim();

        String safeDifficulty =
                (difficulty == null || difficulty.isBlank())
                        ? "Normal"
                        : difficulty.trim();

        String matchType = ranked ? "ranked" : "casual";

        return String.format(
                "Match %s: %s vs %s (%s, %s)",
                matchId,
                safePlayer,
                safeOpponent,
                safeDifficulty,
                matchType
        );
=======
        String effectivePlayer = (playerName == null || playerName.isBlank()) ? "Player" : playerName.trim();
        String effectiveOpponent = (opponentName == null || opponentName.isBlank()) ? "Bot" : opponentName.trim();
        String effectiveDifficulty = (difficulty == null || difficulty.isBlank()) ? "Normal" : difficulty.trim();
        String rankedLabel = ranked ? "ranked" : "casual";

        return "Match " + matchId.trim() + ": "
                + effectivePlayer + " vs " + effectiveOpponent
                + " (" + effectiveDifficulty + ", " + rankedLabel + ")";
>>>>>>> 5c7e5d2cb4f798d0f6c151274b60aab884c6d06b
    }

    @Override
    public void playMatch(
            PlayMatchRequest request,
            StreamObserver<MatchResultResponse> responseObserver
    ) {
        ServerMatch match = matches.get(request.getMatchId());

        if (match == null) {
            responseObserver.onNext(MatchResultResponse.newBuilder()
                    .setMatchId(request.getMatchId())
                    .setWinnerName("No winner")
                    .setLoserName("No loser")
                    .setMessage("Match not found. Join a match first.")
                    .setPlayerWon(false)
                    .build());
            responseObserver.onCompleted();
            return;
        }

        boolean playerWon = random.nextBoolean();
        statistics.recordCompletion();

        String winner = playerWon ? match.playerName() : match.opponentName();
        String loser = playerWon ? match.opponentName() : match.playerName();

        MatchResultResponse response = MatchResultResponse.newBuilder()
                .setMatchId(match.matchId())
                .setWinnerName(winner)
                .setLoserName(loser)
                .setPlayerWon(playerWon)
                .setMessage("Server result: " + winner + " defeated " + loser + " in a "
                        + match.matchType() + " " + match.difficulty() + " match.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void loadMatchHistory(
            MatchHistoryRequest request,
            StreamObserver<MatchHistoryResponse> responseObserver
    ) {
        String playerName = request.getPlayerName().isBlank()
                ? "Player"
                : request.getPlayerName();

        MatchHistoryResponse response = MatchHistoryResponse.newBuilder()
                .addMatches(playerName + " vs Bot: Win")
                .addMatches(playerName + " vs Bot: Loss")
                .addMatches(playerName + " vs Bot: Win")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public MatchStatistics getStatisticsForTesting() {
        return statistics;
    }

    private record ServerMatch(
            String matchId,
            String playerName,
            String opponentName,
            String difficulty,
            boolean ranked
    ) {
        private String matchType() {
            return ranked ? "ranked" : "casual";
        }
    }
}
