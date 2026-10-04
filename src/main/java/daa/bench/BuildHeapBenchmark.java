package daa.bench;

import daa.ds.MinHeap;
import daa.metrics.CsvWriter;
import daa.metrics.Metrics;
import daa.metrics.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BuildHeapBenchmark {
    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    private static final int WARMUP = 5;
    private static final int RUNS = 5;
    private static final long SEED = 42;

    public static void main(String[] args) {
        System.out.println("Warm-up...");
        int[] warm = randomData(1_000);
        for (int i = 0; i < 3; i++) {
            run(1_000, warm, "random", true);
            run(1_000, warm, "random", false);
        }

        List<Result> results = new ArrayList<>();
        for (int n : SIZES) {
            System.out.println("n = " + n);
            int[] random = randomData(n);
            int[] descending = descendingCopy(random);

            results.add(run(n, random, "random", true));
            results.add(run(n, random, "random", false));
            results.add(run(n, descending, "descending", true));
            results.add(run(n, descending, "descending", false));
        }
        CsvWriter.write("results/bonus_buildheap.csv", results);
        System.out.println("Done: results/bonus_buildheap.csv");
    }

    private static int[] randomData(int n) {
        Random rnd = new Random(SEED);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = rnd.nextInt(1_000_000) * 2;
        }
        return data;
    }

    // Worst case for n insert(x): every new element is the smallest and bubbles up to the root
    private static int[] descendingCopy(int[] data) {
        int[] sorted = data.clone();
        Arrays.sort(sorted);
        int[] desc = new int[sorted.length];
        for (int i = 0; i < sorted.length; i++) {
            desc[i] = sorted[sorted.length - 1 - i];
        }
        return desc;
    }

    private static double median(double[] times) {
        double[] sorted = times.clone();
        Arrays.sort(sorted);
        return sorted[sorted.length / 2];
    }

    // floyd = true: one buildHeap(data) call; floyd = false: n separate insert calls
    private static Result run(int n, int[] data, String variant, boolean floyd) {
        double[] times = new double[RUNS];
        Metrics m = null;

        for (int run = 0; run < WARMUP + RUNS; run++) {
            m = new Metrics();
            MinHeap heap = new MinHeap(m);

            m.startTimer();
            if (floyd) {
                heap.buildHeap(data);
            } else {
                for (int v : data) {
                    heap.insert(v);
                }
            }
            m.stopTimer();

            if (heap.size() != n || !heap.isValidHeap()) {
                throw new IllegalStateException("Bonus B: result is not a valid heap");
            }
            if (run >= WARMUP) {
                times[run - WARMUP] = m.getTimeMs();
            }
        }
        String structure = floyd ? "MinHeap_buildHeap" : "MinHeap_insert";
        return new Result("B_buildHeap", variant, structure, n, median(times),
                m.getSteps(), m.getMoves(), m.getComparisons());
    }
}