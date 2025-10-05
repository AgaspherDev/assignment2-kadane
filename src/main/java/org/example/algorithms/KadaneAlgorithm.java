package org.example.algorithms;

import org.example.metrics.PerformanceTracker;

public class KadaneAlgorithm {
    
    public static class Result {
        private final int maxSum;
        private final int startIndex;
        private final int endIndex;
        private final PerformanceTracker.Metrics metrics;
        
        public Result(int maxSum, int startIndex, int endIndex, PerformanceTracker.Metrics metrics) {
            this.maxSum = maxSum;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.metrics = metrics;
        }
        
        public int getMaxSum() { return maxSum; }
        public int getStartIndex() { return startIndex; }
        public int getEndIndex() { return endIndex; }
        public PerformanceTracker.Metrics getMetrics() { return metrics; }
        
        @Override
        public String toString() {
            return String.format("Max Sum: %d, Range: [%d, %d], %s", 
                maxSum, startIndex, endIndex, metrics);
        }
    }
    
    public static Result findMaxSubarray(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        
        if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        }
        
        PerformanceTracker.Metrics metrics = new PerformanceTracker.Metrics();
        metrics.incrementMemoryAllocations();
        
        int maxSum = array[0];
        metrics.incrementArrayAccesses();
        metrics.incrementAssignments();
        
        int currentSum = array[0];
        metrics.incrementArrayAccesses();
        metrics.incrementAssignments();
        
        int startIndex = 0;
        int endIndex = 0;
        int tempStart = 0;
        
        metrics.incrementAssignments();
        metrics.incrementAssignments();
        metrics.incrementAssignments();
        
        for (int i = 1; i < array.length; i++) {
            metrics.incrementComparisons();
            
            int currentElement = array[i];
            metrics.incrementArrayAccesses();
            metrics.incrementAssignments();
            
            if (currentSum < 0) {
                metrics.incrementComparisons();
                currentSum = currentElement;
                tempStart = i;
                metrics.incrementAssignments();
                metrics.incrementAssignments();
            } else {
                currentSum += currentElement;
                metrics.incrementAssignments();
            }
            
            if (currentSum > maxSum) {
                metrics.incrementComparisons();
                maxSum = currentSum;
                startIndex = tempStart;
                endIndex = i;
                metrics.incrementAssignments();
                metrics.incrementAssignments();
                metrics.incrementAssignments();
            }
        }
        
        Result result = new Result(maxSum, startIndex, endIndex, metrics);
        metrics.incrementMemoryAllocations();
        return result;
    }
    
    public static void validateInput(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }
        
        if (array.length == 0) {
            throw new IllegalArgumentException("Input array cannot be empty");
        }
        
        if (array.length > 1000000) {
            throw new IllegalArgumentException("Array size exceeds maximum limit of 1,000,000 elements");
        }
    }
}
