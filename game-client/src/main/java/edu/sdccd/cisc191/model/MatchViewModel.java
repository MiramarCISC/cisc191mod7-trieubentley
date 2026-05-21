package edu.sdccd.cisc191.model;
<<<<<<< HEAD
import java.util.concurrent.atomic.AtomicInteger;
=======

import java.util.concurrent.atomic.AtomicInteger;

>>>>>>> 5c7e5d2cb4f798d0f6c151274b60aab884c6d06b
public class MatchViewModel {
    private String matchId;
    private final Player player = new Player("Player");
    private final Player opponent = new Player("Opponent");
    private boolean matchOver;
    private String winnerName = "";

<<<<<<< HEAD
    // TODO 7: Make this shared counter thread-safe.
    // Use either an AtomicInteger field or synchronized methods so background tasks cannot lose updates.
=======
    // TODO 7: Thread-safe counter using AtomicInteger
>>>>>>> 5c7e5d2cb4f798d0f6c151274b60aab884c6d06b
    private final AtomicInteger completedMatchCount = new AtomicInteger(0);

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public boolean isMatchOver() {
        return matchOver;
    }

    public void setMatchOver(boolean matchOver) {
        this.matchOver = matchOver;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName == null ? "" : winnerName;
    }

    public int getCompletedMatchCount() {
        return completedMatchCount.get();
    }

    /**
     * TODO 7: Thread-safe using AtomicInteger.incrementAndGet() and synchronized.
     * Concurrent calls cannot lose updates.
     */
<<<<<<< HEAD
    public void recordCompletedMatchThreadSafely(String winnerName) {
=======
    public synchronized void recordCompletedMatchThreadSafely(String winnerName) {
>>>>>>> 5c7e5d2cb4f798d0f6c151274b60aab884c6d06b
        completedMatchCount.incrementAndGet();
        setWinnerName(winnerName);
        matchOver = true;
    }

    public boolean hasJoinedMatch() {
        return matchId != null && !matchId.isBlank();
    }

    public boolean canPlayMatch() {
        return hasJoinedMatch() && !matchOver;
    }

    /**
     * TODO 2: MVC helper that builds a readable match summary.
     * Format: Match match-001: Ada vs Bot (Hard, ranked)
     * Returns "No match" when matchId is null or blank.
     */
    public String buildMatchSummary(String difficulty, boolean ranked) {
<<<<<<< HEAD

        if (matchId == null || matchId.isBlank()) {
            return "No match";
        }

        String safeDifficulty =
                (difficulty == null || difficulty.isBlank())
                        ? "Normal"
                        : difficulty.trim();

        String matchType = ranked ? "ranked" : "casual";

        return String.format(
                "Match %s: %s vs %s (%s, %s)",
                matchId,
                player.getName(),
                opponent.getName(),
                safeDifficulty,
                matchType
        );
=======
        if (!hasJoinedMatch()) {
            return "No match";
        }

        String effectiveDifficulty = (difficulty == null || difficulty.isBlank()) ? "Normal" : difficulty.trim();
        String rankedLabel = ranked ? "ranked" : "casual";

        return "Match " + matchId + ": "
                + player.getName() + " vs " + opponent.getName()
                + " (" + effectiveDifficulty + ", " + rankedLabel + ")";
>>>>>>> 5c7e5d2cb4f798d0f6c151274b60aab884c6d06b
    }

    public void resetLocalState() {
        matchId = null;
        player.setName("Player");
        opponent.setName("Opponent");
        matchOver = false;
        winnerName = "";
        completedMatchCount.set(0);
    }
}
