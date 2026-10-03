package daa;

import daa.bench.Benchmark;
import daa.metrics.CsvWriter;
import daa.metrics.Result;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Запуск бенчмарка...");
        List<Result> results = new Benchmark().run();
        CsvWriter.write("results/results.csv", results);
        System.out.println("Готово: results/results.csv");
    }
}