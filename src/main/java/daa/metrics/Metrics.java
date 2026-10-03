package daa.metrics;

public class Metrics {
    private long steps;
    private long moves;
    private long comparisons;
    private long startNs;
    private long elapsedNs;

    public void step()    { steps++; }
    public void move()    { moves++; }
    public void compare() { comparisons++; }

    public void reset() {
        steps = 0;
        moves = 0;
        comparisons = 0;
        elapsedNs = 0;
    }

    public void startTimer() { startNs = System.nanoTime(); }
    public void stopTimer()  { elapsedNs = System.nanoTime() - startNs; }

    public double getTimeMs() { return elapsedNs / 1_000_000.0; }
    public long getSteps()       { return steps; }
    public long getMoves()       { return moves; }
    public long getComparisons() { return comparisons; }
}