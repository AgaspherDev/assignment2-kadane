package org.example.metrics;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PerformanceTracker {
    
    public static class Metrics {
        private int comparisons;
        private int arrayAccesses;
        private int memoryAllocations;
        private int assignments;
        
        public Metrics() {
            this.comparisons = 0;
            this.arrayAccesses = 0;
            this.memoryAllocations = 0;
            this.assignments = 0;
        }
        
        public void incrementComparisons() { comparisons++; }
        public void incrementArrayAccesses() { arrayAccesses++; }
        public void incrementMemoryAllocations() { memoryAllocations++; }
        public void incrementAssignments() { assignments++; }
        
        public int getComparisons() { return comparisons; }
        public int getArrayAccesses() { return arrayAccesses; }
        public int getMemoryAllocations() { return memoryAllocations; }
        public int getAssignments() { return assignments; }
        
        public void reset() {
            comparisons = 0;
            arrayAccesses = 0;
            memoryAllocations = 0;
            assignments = 0;
        }
        
        public Metrics copy() {
            Metrics copy = new Metrics();
            copy.comparisons = this.comparisons;
            copy.arrayAccesses = this.arrayAccesses;
            copy.memoryAllocations = this.memoryAllocations;
            copy.assignments = this.assignments;
            return copy;
        }
        
        @Override
        public String toString() {
            return String.format("Metrics[comparisons=%d, arrayAccesses=%d, memoryAllocations=%d, assignments=%d]",
                comparisons, arrayAccesses, memoryAllocations, assignments);
        }
    }
    
    public static class BenchmarkResult {
        private final String algorithmName;
        private final int arraySize;
        private final long executionTimeNanos;
        private final Metrics metrics;
        private final int result;
        private final String timestamp;
        
        public BenchmarkResult(String algorithmName, int arraySize, long executionTimeNanos, 
                              Metrics metrics, int result) {
            this.algorithmName = algorithmName;
            this.arraySize = arraySize;
            this.executionTimeNanos = executionTimeNanos;
            this.metrics = metrics.copy();
            this.result = result;
            this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        
        public String getAlgorithmName() { return algorithmName; }
        public int getArraySize() { return arraySize; }
        public long getExecutionTimeNanos() { return executionTimeNanos; }
        public Metrics getMetrics() { return metrics; }
        public int getResult() { return result; }
        public String getTimestamp() { return timestamp; }
        
        public double getExecutionTimeMillis() {
            return executionTimeNanos / 1_000_000.0;
        }
        
        public double getExecutionTimeMicros() {
            return executionTimeNanos / 1_000.0;
        }
        
        @Override
        public String toString() {
            return String.format("%s (size=%d): %d result, %.3f ms, %s",
                algorithmName, arraySize, result, getExecutionTimeMillis(), metrics);
        }
        
        public String toCsvRow() {
            return String.format("%s,%d,%.3f,%.3f,%d,%d,%d,%d,%d,%s",
                algorithmName, arraySize, getExecutionTimeMillis(), getExecutionTimeMicros(),
                metrics.getComparisons(), metrics.getArrayAccesses(), 
                metrics.getMemoryAllocations(), metrics.getAssignments(),
                result, timestamp);
        }
    }
    
    private final List<BenchmarkResult> results;
    private final String metricsDir;
    
    public PerformanceTracker() {
        this.results = new ArrayList<>();
        this.metricsDir = "src/main/java/org/example/metrics/data";
        createMetricsDirectory();
    }
    
    private void createMetricsDirectory() {
        try {
            Path path = Paths.get(metricsDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not create metrics directory: " + e.getMessage());
        }
    }
    
    public void addResult(BenchmarkResult result) {
        results.add(result);
    }
    
    public void addResult(String algorithmName, int arraySize, long executionTimeNanos, 
                         Metrics metrics, int algorithmResult) {
        results.add(new BenchmarkResult(algorithmName, arraySize, executionTimeNanos, 
                                       metrics, algorithmResult));
    }
    
    public List<BenchmarkResult> getResults() {
        return new ArrayList<>(results);
    }
    
    public List<BenchmarkResult> getResultsForAlgorithm(String algorithmName) {
        return results.stream()
                .filter(r -> r.getAlgorithmName().equals(algorithmName))
                .toList();
    }
    
    public void clearResults() {
        results.clear();
    }
    
    public void exportToCsv(String filename) throws IOException {
        String fullPath = metricsDir + "/" + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fullPath))) {
            writer.println("Algorithm,ArraySize,ExecutionTimeMs,ExecutionTimeMicros,Comparisons,ArrayAccesses,MemoryAllocations,Assignments,Result,Timestamp");
            
            for (BenchmarkResult result : results) {
                writer.println(result.toCsvRow());
            }
        }
        
        System.out.println("Results exported to: " + fullPath);
    }
    
    public void exportToCsvWithTimestamp() throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String filename = "benchmark_results_" + timestamp + ".csv";
        exportToCsv(filename);
    }
    
    public void printSummary() {
        if (results.isEmpty()) {
            System.out.println("No benchmark results available.");
            return;
        }
        
        for (BenchmarkResult result : results) {
            System.out.println(result);
        }
    }
    
    public void printComparison() {
        if (results.size() < 2) {
            System.out.println("Need at least 2 results for comparison.");
            return;
        }
                
        var groupedResults = results.stream()
                .collect(Collectors.groupingBy(BenchmarkResult::getArraySize));
        
        for (var entry : groupedResults.entrySet()) {
            int size = entry.getKey();
            List<BenchmarkResult> sizeResults = entry.getValue();
            
            if (sizeResults.size() > 1) {
                System.out.println("\nArray size: " + size);
                
                BenchmarkResult fastest = sizeResults.stream()
                        .min((a, b) -> Long.compare(a.getExecutionTimeNanos(), b.getExecutionTimeNanos()))
                        .orElse(null);
                
                for (BenchmarkResult result : sizeResults) {
                    double speedup = fastest != null && fastest != result ? 
                            (double) result.getExecutionTimeNanos() / fastest.getExecutionTimeNanos() : 1.0;
                    
                    System.out.printf("  %s: %.3f ms (%.2fx)%n", 
                            result.getAlgorithmName(), result.getExecutionTimeMillis(), speedup);
                }
            }
        }
    }
    
    public void generateFullReport() {
        try {
            exportToCsvWithTimestamp();
            System.out.println("- Metrics directory: " + metricsDir);
        } catch (IOException e) {
            System.err.println("Error generating full report: " + e.getMessage());
        }
    }
    
    public static long measureExecutionTime(Runnable algorithm) {
        long startTime = System.nanoTime();
        algorithm.run();
        return System.nanoTime() - startTime;
    }
}
