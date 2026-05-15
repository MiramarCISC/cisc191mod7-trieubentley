package edu.sdccd.cisc191.model;

import java.util.concurrent.atomic.AtomicInteger;

public class MatchViewModel {
    private String matchId;
    private final Player player = new Player("Player");
    private final Player opponent = new Player("Opponent");
    private boolean matchOver;
    private String winnerName = "";

    // TODO 7: Thread-safe counter using AtomicInteger
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
    public synchronized void recordCompletedMatchThreadSafely(String winnerName) {
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
        if (!hasJoinedMatch()) {
            return "No match";
        }

        String effectiveDifficulty = (difficulty == null || difficulty.isBlank()) ? "Normal" : difficulty.trim();
        String rankedLabel = ranked ? "ranked" : "casual";

        return "Match " + matchId + ": "
                + player.getName() + " vs " + opponent.getName()
                + " (" + effectiveDifficulty + ", " + rankedLabel + ")";
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
