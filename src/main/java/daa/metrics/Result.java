package daa.metrics;

public record Result(String workload, String variant, String structure,
                     int n, double timeMs, long steps, long moves, long comparisons) {
}