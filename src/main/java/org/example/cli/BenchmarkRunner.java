package org.example.cli;

import org.example.algorithms.KadaneAlgorithm;
import org.example.metrics.PerformanceTracker;

import java.util.Random;
import java.util.Scanner;

public class BenchmarkRunner {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private final PerformanceTracker tracker;
    
    public BenchmarkRunner() {
        this.tracker = new PerformanceTracker();
    }
    
    public void run() {        
        while (true) {
            displayMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1 -> testWithCustomArray();
                case 2 -> testWithRandomArray();
                case 3 -> performanceComparison();
                case 4 -> runEdgeCaseTests();
                case 5 -> runComprehensiveBenchmark();
                case 6 -> viewBenchmarkResults();
                case 7 -> exportResultsToCsv();
                case 10 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    private void displayMenu() {
        System.out.println("Choose an option:");
        System.out.println("1. Test with custom array");
        System.out.println("2. Test with random array");
        System.out.println("3. Performance comparison");
        System.out.println("4. Run edge case tests");
        System.out.println("5. Run comprehensive benchmark");
        System.out.println("6. View benchmark results");
        System.out.println("7. Export results to CSV");
        System.out.println("10. Exit");
        System.out.print("Enter your choice (1-10): ");
    }
    
    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private void testWithCustomArray() {
        System.out.print("Enter array elements separated by spaces: ");
        
        try {
            String[] input = scanner.nextLine().trim().split("\\s+");
            int[] array = new int[input.length];
            
            for (int i = 0; i < input.length; i++) {
                array[i] = Integer.parseInt(input[i]);
            }
            
            runAlgorithmTest(array, "Custom Array");
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid integers.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void testWithRandomArray() {
        System.out.print("Enter array size (1-10000): ");
        
        try {
            int size = Integer.parseInt(scanner.nextLine().trim());
            
            if (size < 1 || size > 10000) {
                System.out.println("Size must be between 1 and 10000.");
                return;
            }
            
            System.out.print("Enter range for random numbers (min max): ");
            String[] range = scanner.nextLine().trim().split("\\s+");
            
            if (range.length != 2) {
                System.out.println("Please enter exactly two numbers for min and max.");
                return;
            }
            
            int min = Integer.parseInt(range[0]);
            int max = Integer.parseInt(range[1]);
            
            if (min >= max) {
                System.out.println("Min must be less than max.");
                return;
            }
            
            int[] array = generateRandomArray(size, min, max);
            
            System.out.print("Generated array: ");
            printArray(array, Math.min(20, array.length));
            if (array.length > 20) {
                System.out.println("... (showing first 20 elements)");
            } else {
                System.out.println();
            }
            
            runAlgorithmTest(array, "Random Array");
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid integers.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void performanceComparison() {        
        int[] sizes = {100, 1000, 5000, 10000};
        
        tracker.clearResults();
        
        for (int size : sizes) {
            System.out.println("\nArray size: " + size);
            int[] array = generateRandomArray(size, -100, 100);
            
            KadaneAlgorithm.Result standardResult = KadaneAlgorithm.findMaxSubarray(array);
            long standardTime = PerformanceTracker.measureExecutionTime(() -> {
                KadaneAlgorithm.findMaxSubarray(array);
            });
            tracker.addResult("Standard Kadane", size, standardTime, standardResult.getMetrics(), standardResult.getMaxSum());

            KadaneAlgorithm.Result optimizedResult = KadaneAlgorithm.findMaxSubarrayOptimized(array);
            long optimizedTime = PerformanceTracker.measureExecutionTime(() -> {
                KadaneAlgorithm.findMaxSubarrayOptimized(array);
            });
            tracker.addResult("Optimized Kadane", size, optimizedTime, optimizedResult.getMetrics(), optimizedResult.getMaxSum());
            
            System.out.printf("Standard Algorithm: %d ns%n", standardTime);
            System.out.printf("Optimized Algorithm: %d ns%n", optimizedTime);
            System.out.printf("Speedup: %.2fx%n", (double) standardTime / optimizedTime);
        }
        
        tracker.printComparison();
    }
    
    private void runEdgeCaseTests() {
        System.out.println("\n--- Edge Case Tests ---");
        
        int[][] testCases = {
            {5},
            {-3},
            {-5, -2, -8, -1},
            {1, 2, 3, 4, 5},
            {-1, -2, -3, -4, -5},
            {-2, 1, -3, 4, -1, 2, 1, -5, 4},
            {0, 0, 0, 0},
            {-1, 0, -2, 3, 0, -1, 2},
            {Integer.MAX_VALUE, -1, Integer.MAX_VALUE},
            {Integer.MIN_VALUE + 1, Integer.MAX_VALUE, Integer.MIN_VALUE + 1}
        };
        
        String[] descriptions = {
            "Single positive element",
            "Single negative element", 
            "All negative elements",
            "All positive elements",
            "All negative elements (descending)",
            "Mixed positive/negative",
            "All zeros",
            "Mixed with zeros",
            "Large positive values",
            "Extreme values"
        };
        
        for (int i = 0; i < testCases.length; i++) {
            System.out.println("\nTest case " + (i + 1) + ": " + descriptions[i]);
            runAlgorithmTest(testCases[i], descriptions[i]);
        }
    }
    
    private void runComprehensiveBenchmark() {
        System.out.println("\n--- Comprehensive Benchmark ---");
        System.out.println("Running benchmark with multiple array sizes and configurations...");
        
        tracker.clearResults();
        
        int[] sizes = {10, 50, 100, 500, 1000, 5000, 10000};
        int[][] ranges = {{-10, 10}, {-100, 100}, {-1000, 1000}};
        
        int totalTests = sizes.length * ranges.length * 2;
        int currentTest = 0;
        
        for (int size : sizes) {
            for (int[] range : ranges) {
                int[] array = generateRandomArray(size, range[0], range[1]);
                
                currentTest++;
                System.out.printf("Progress: %d/%d - Testing Standard Algorithm (size=%d, range=[%d,%d])%n", 
                    currentTest, totalTests, size, range[0], range[1]);
                
                KadaneAlgorithm.Result standardResult = KadaneAlgorithm.findMaxSubarray(array);
                long standardTime = PerformanceTracker.measureExecutionTime(() -> {
                    KadaneAlgorithm.findMaxSubarray(array);
                });
                tracker.addResult("Standard", size, standardTime, standardResult.getMetrics(), standardResult.getMaxSum());
                
                currentTest++;
                System.out.printf("Progress: %d/%d - Testing Optimized Algorithm (size=%d, range=[%d,%d])%n", 
                    currentTest, totalTests, size, range[0], range[1]);
                
                KadaneAlgorithm.Result optimizedResult = KadaneAlgorithm.findMaxSubarrayOptimized(array);
                long optimizedTime = PerformanceTracker.measureExecutionTime(() -> {
                    KadaneAlgorithm.findMaxSubarrayOptimized(array);
                });
                tracker.addResult("Optimized", size, optimizedTime, optimizedResult.getMetrics(), optimizedResult.getMaxSum());
            }
        }
        
        System.out.println("\nBenchmark completed!");
        System.out.printf("Total tests run: %d%n", tracker.getResults().size());
        tracker.printSummary();
    }

    private void exportResultsToCsv() {
        System.out.println("\n--- Export Results to CSV ---");
        if (tracker.getResults().isEmpty()) {
            System.out.println("No benchmark results available. Run a benchmark first.");
            return;
        }

        try {
            tracker.exportToCsvWithTimestamp();
        } catch (Exception e) {
            System.err.println("Error exporting to CSV: " + e.getMessage());
        }
    }

    private void viewBenchmarkResults() {
        System.out.println("\n--- Benchmark Results ---");
        if (tracker.getResults().isEmpty()) {
            System.out.println("No benchmark results available. Run a benchmark first.");
        } else {
            tracker.printSummary();
            tracker.printComparison();
        }
    }
    
    private void runAlgorithmTest(int[] array, String description) {
        try {
            KadaneAlgorithm.validateInput(array);
            
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            System.out.println("Result: " + result);
            
            if (array.length <= 20) {
                System.out.print("Subarray: [");
                for (int i = result.getStartIndex(); i <= result.getEndIndex(); i++) {
                    System.out.print(array[i]);
                    if (i < result.getEndIndex()) System.out.print(", ");
                }
                System.out.println("]");
            }
            
        } catch (Exception e) {
            System.out.println("Error processing " + description + ": " + e.getMessage());
        }
    }
    
    private int[] generateRandomArray(int size, int min, int max) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(max - min + 1) + min;
        }
        return array;
    }
    
    private void printArray(int[] array, int limit) {
        System.out.print("[");
        for (int i = 0; i < limit; i++) {
            System.out.print(array[i]);
            if (i < limit - 1) System.out.print(", ");
        }
        System.out.print("]");
    }
    
    public static void main(String[] args) {
        new BenchmarkRunner().run();
    }
}
