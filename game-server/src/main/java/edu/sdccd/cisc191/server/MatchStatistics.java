package edu.sdccd.cisc191.server;
<<<<<<< HEAD
import java.util.concurrent.atomic.AtomicInteger;
=======

import java.util.concurrent.atomic.AtomicInteger;

>>>>>>> 5c7e5d2cb4f798d0f6c151274b60aab884c6d06b
/**
 * Tracks server-wide match statistics shared by many gRPC request threads.
 */
public class MatchStatistics {

<<<<<<< HEAD
    // TODO 9: Replace these counters with a thread-safe design.
    // Recommended: AtomicInteger joinedMatchCount and AtomicInteger completedMatchCount.
=======
    // TODO 9: Thread-safe counters using AtomicInteger
>>>>>>> 5c7e5d2cb4f798d0f6c151274b60aab884c6d06b
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
<<<<<<< HEAD
        return String.format(
                "Server stats: %d joined, %d completed",
                joinedMatchCount.get(),
                completedMatchCount.get()
        );
=======
        return "Server stats: " + joinedMatchCount.get() + " joined, " + completedMatchCount.get() + " completed";
>>>>>>> 5c7e5d2cb4f798d0f6c151274b60aab884c6d06b
    }
}
