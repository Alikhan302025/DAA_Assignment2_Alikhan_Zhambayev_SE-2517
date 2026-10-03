package daa.metrics;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class CsvWriter {
    public static void write(String file, List<Result> results) {
        try {
            Path path = Path.of(file);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (PrintWriter w = new PrintWriter(Files.newBufferedWriter(path))) {
                w.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");
                for (Result r : results) {
                    w.println(String.format(Locale.US, "%s,%s,%s,%d,%.3f,%d,%d,%d",
                            r.workload(), r.variant(), r.structure(), r.n(),
                            r.timeMs(), r.steps(), r.moves(), r.comparisons()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot write CSV", e);
        }
    }
}