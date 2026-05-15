package edu.sdccd.cisc191.server;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tracks server-wide match statistics shared by many gRPC request threads.
 */
public class MatchStatistics {

    // TODO 9: Thread-safe counters using AtomicInteger
    private final AtomicInteger joinedMatchCount = new AtomicInteger(0);
    private final AtomicInteger completedMatchCount = new AtomicInteger(0);

    /**
     * TODO 9: Thread-safe join recording using AtomicInteger.incrementAndGet().
     */
    public void recordJoin() {
        joinedMatchCount.incrementAndGet();
    }

    /**
     * TODO 9: Thread-safe completion recording using AtomicInteger.incrementAndGet().
     */
    public void recordCompletion() {
        completedMatchCount.incrementAndGet();
    }

    public int getJoinedMatchCount() {
        return joinedMatchCount.get();
    }

    public int getCompletedMatchCount() {
        return completedMatchCount.get();
    }

    /**
     * TODO 9: Thread-safe statistics summary.
     * Format: Server stats: 3 joined, 2 completed
     */
    public String buildStatusLine() {
        return "Server stats: " + joinedMatchCount.get() + " joined, " + completedMatchCount.get() + " completed";
    }
}
