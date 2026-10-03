package daa.bench;

import daa.ds.DynamicArray;
import daa.ds.IntList;
import daa.ds.MinHeap;
import daa.ds.MyLinkedList;
import daa.metrics.Metrics;
import daa.metrics.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Benchmark {
    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    private static final int WARMUP = 3;          // прогоны, которые отбрасываем
    private static final int RUNS = 5;            // прогоны, из которых берём медиану
    private static final int GET_QUERIES = 10_000;
    private static final int SEARCH_QUERIES = 1_000;
    private static final int UPDATE_OPS = 1_000;
    private static final long SEED = 42;

    public List<Result> run() {
        List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            System.out.println("n = " + n);
            int[] data = generateData(n);

            for (String structure : new String[]{"DynamicArray", "MyLinkedList"}) {
                System.out.println("  " + structure);
                results.add(runW1(structure, n, data));
                results.add(runW2(structure, n, data));
                results.add(runW3(structure, n, data, "head"));
                results.add(runW3(structure, n, data, "middle"));
            }
            System.out.println("  MinHeap");
            results.add(runW4(n, data));
        }
        return results;
    }

    private int[] generateData(int n) {
        Random rnd = new Random(SEED);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = rnd.nextInt(1_000_000) * 2;
        }
        return data;
    }

    private IntList create(String structure, Metrics m) {
        if (structure.equals("DynamicArray")) {
            return new DynamicArray(m);
        }
        return new MyLinkedList(m);
    }

    private double median(double[] times) {
        double[] sorted = times.clone();
        Arrays.sort(sorted);
        return sorted[sorted.length / 2];
    }

    //W1
    private Result runW1(String structure, int n, int[] data) {
        double[] times = new double[RUNS];
        Metrics m = null;

        Random rnd = new Random(SEED);
        int[] indices = new int[GET_QUERIES];
        for (int q = 0; q < GET_QUERIES; q++) {
            indices[q] = rnd.nextInt(n);
        }

        for (int run = 0; run < WARMUP + RUNS; run++) {
            m = new Metrics();
            IntList list = create(structure, m);
            for (int v : data) {
                list.add(v);
            }
            m.reset();

            m.startTimer();
            for (int q = 0; q < GET_QUERIES; q++) {
                list.get(indices[q]);
            }
            m.stopTimer();

            if (run >= WARMUP) {
                times[run - WARMUP] = m.getTimeMs();
            }
        }
        return new Result("W1_random_access", "-", structure, n, median(times),
                m.getSteps(), m.getMoves(), m.getComparisons());
    }

    // W2 поиск половина значений есть, половина нет
    private Result runW2(String structure, int n, int[] data) {
        double[] times = new double[RUNS];
        Metrics m = null;

        Random rnd = new Random(SEED);
        int[] queries = new int[SEARCH_QUERIES];
        for (int q = 0; q < SEARCH_QUERIES; q++) {
            if (q % 2 == 0) {
                queries[q] = data[rnd.nextInt(n)];
            } else {
                queries[q] = rnd.nextInt(1_000_000) * 2 + 1;
            }
        }

        for (int run = 0; run < WARMUP + RUNS; run++) {
            m = new Metrics();
            IntList list = create(structure, m);
            for (int v : data) {
                list.add(v);
            }
            m.reset();

            m.startTimer();
            for (int q = 0; q < SEARCH_QUERIES; q++) {
                list.contains(queries[q]);
            }
            m.stopTimer();

            if (run >= WARMUP) {
                times[run - WARMUP] = m.getTimeMs();
            }
        }
        return new Result("W2_search", "-", structure, n, median(times),
                m.getSteps(), m.getMoves(), m.getComparisons());
    }

    //W3 вставки и удаления
    private Result runW3(String structure, int n, int[] data, String variant) {
        double[] times = new double[RUNS];
        Metrics m = null;

        int index = variant.equals("head") ? 0 : n / 2;

        for (int run = 0; run < WARMUP + RUNS; run++) {
            m = new Metrics();
            IntList list = create(structure, m);
            for (int v : data) {
                list.add(v);
            }
            m.reset();

            m.startTimer();
            for (int i = 0; i < UPDATE_OPS; i++) {
                list.add(index, i);                  // 1000 вставок
            }
            for (int i = 0; i < UPDATE_OPS; i++) {
                list.remove(index);                  // 1000 удалений
            }
            m.stopTimer();

            if (run >= WARMUP) {
                times[run - WARMUP] = m.getTimeMs();
            }
        }
        return new Result("W3_insert_remove", variant, structure, n, median(times),
                m.getSteps(), m.getMoves(), m.getComparisons());
    }

    // W4 приоритетная очередь
    private Result runW4(int n, int[] data) {
        double[] times = new double[RUNS];
        Metrics m = null;
        int[] out = new int[n];

        for (int run = 0; run < WARMUP + RUNS; run++) {
            m = new Metrics();
            MinHeap heap = new MinHeap(m);

            m.startTimer();
            for (int v : data) {
                heap.insert(v);
            }
            for (int i = 0; i < n; i++) {
                out[i] = heap.extractMin();
            }
            m.stopTimer();

            for (int i = 1; i < n; i++) {
                if (out[i - 1] > out[i]) {
                    throw new IllegalStateException("W4: output is not sorted");
                }
            }

            if (run >= WARMUP) {
                times[run - WARMUP] = m.getTimeMs();
            }
        }
        return new Result("W4_priority", "-", "MinHeap", n, median(times),
                m.getSteps(), m.getMoves(), m.getComparisons());
    }
}