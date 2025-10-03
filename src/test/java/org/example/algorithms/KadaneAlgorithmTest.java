package org.example.algorithms;

import org.example.algorithms.KadaneAlgorithm;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Kadane Algorithm Tests")
public class KadaneAlgorithmTest {

    @Nested
    @DisplayName("Input Validation Tests")
    class InputValidationTests {
        
        @Test
        @DisplayName("Should throw exception for null array")
        void testNullArray() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> KadaneAlgorithm.findMaxSubarray(null)
            );
            assertEquals("Array cannot be null", exception.getMessage());
        }
        
        @Test
        @DisplayName("Should throw exception for empty array")
        void testEmptyArray() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> KadaneAlgorithm.findMaxSubarray(new int[0])
            );
            assertEquals("Array cannot be empty", exception.getMessage());
        }
        
        @Test
        @DisplayName("Should validate large array size")
        void testLargeArrayValidation() {
            int[] largeArray = new int[1000001];
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> KadaneAlgorithm.validateInput(largeArray)
            );
            assertEquals("Array size exceeds maximum limit of 1,000,000 elements", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Single Element Tests")
    class SingleElementTests {
        
        @Test
        @DisplayName("Should handle single positive element")
        void testSinglePositiveElement() {
            int[] array = {42};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertEquals(42, result.getMaxSum());
            assertEquals(0, result.getStartIndex());
            assertEquals(0, result.getEndIndex());
            assertNotNull(result.getMetrics());
        }
        
        @Test
        @DisplayName("Should handle single negative element")
        void testSingleNegativeElement() {
            int[] array = {-15};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertEquals(-15, result.getMaxSum());
            assertEquals(0, result.getStartIndex());
            assertEquals(0, result.getEndIndex());
        }
        
        @Test
        @DisplayName("Should handle single zero element")
        void testSingleZeroElement() {
            int[] array = {0};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertEquals(0, result.getMaxSum());
            assertEquals(0, result.getStartIndex());
            assertEquals(0, result.getEndIndex());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {
        
        @Test
        @DisplayName("Should handle all negative elements")
        void testAllNegativeElements() {
            int[] array = {-5, -2, -8, -1, -4};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertEquals(-1, result.getMaxSum());
            assertEquals(3, result.getStartIndex());
            assertEquals(3, result.getEndIndex());
        }
        
        @Test
        @DisplayName("Should handle all positive elements")
        void testAllPositiveElements() {
            int[] array = {1, 2, 3, 4, 5};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertEquals(15, result.getMaxSum());
            assertEquals(0, result.getStartIndex());
            assertEquals(4, result.getEndIndex());
        }
        
        @Test
        @DisplayName("Should handle all zero elements")
        void testAllZeroElements() {
            int[] array = {0, 0, 0, 0};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertEquals(0, result.getMaxSum());
            assertEquals(0, result.getStartIndex());
            assertEquals(0, result.getEndIndex());
        }
        
        @Test
        @DisplayName("Should handle mixed elements with zeros")
        void testMixedElementsWithZeros() {
            int[] array = {-1, 0, -2, 3, 0, -1, 2, 0};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertEquals(4, result.getMaxSum());
            assertEquals(3, result.getStartIndex());
            assertEquals(6, result.getEndIndex());
        }
    }

    @Nested
    @DisplayName("Basic Functionality Tests")
    class BasicFunctionalityTests {
        
        @Test
        @DisplayName("Should find maximum subarray in classic example")
        void testClassicExample() {
            int[] array = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertEquals(6, result.getMaxSum());
            assertEquals(3, result.getStartIndex());
            assertEquals(6, result.getEndIndex());
        }
        
        @Test
        @DisplayName("Should handle alternating positive negative")
        void testAlternatingPositiveNegative() {
            int[] array = {5, -3, 2, -1, 4};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertEquals(7, result.getMaxSum());
            assertEquals(0, result.getStartIndex());
            assertEquals(4, result.getEndIndex());
        }
        
        @Test
        @DisplayName("Should handle maximum at beginning")
        void testMaximumAtBeginning() {
            int[] array = {10, -5, -3, -1};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertEquals(10, result.getMaxSum());
            assertEquals(0, result.getStartIndex());
            assertEquals(0, result.getEndIndex());
        }
        
        @Test
        @DisplayName("Should handle maximum at end")
        void testMaximumAtEnd() {
            int[] array = {-5, -3, -1, 10};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertEquals(10, result.getMaxSum());
            assertEquals(3, result.getStartIndex());
            assertEquals(3, result.getEndIndex());
        }
    }

    @Nested
    @DisplayName("Metrics Tests")
    class MetricsTests {
        
        @Test
        @DisplayName("Should track array accesses correctly")
        void testArrayAccessTracking() {
            int[] array = {1, 2, 3};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertTrue(result.getMetrics().getArrayAccesses() > 0);
            assertTrue(result.getMetrics().getComparisons() > 0);
            assertTrue(result.getMetrics().getAssignments() > 0);
        }
        
        @Test
        @DisplayName("Should track memory allocations")
        void testMemoryAllocationTracking() {
            int[] array = {1, 2, 3};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertTrue(result.getMetrics().getMemoryAllocations() > 0);
        }
        
        @Test
        @DisplayName("Metrics should scale with array size")
        void testMetricsScaling() {
            int[] smallArray = {1, 2};
            int[] largeArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            
            KadaneAlgorithm.Result smallResult = KadaneAlgorithm.findMaxSubarray(smallArray);
            KadaneAlgorithm.Result largeResult = KadaneAlgorithm.findMaxSubarray(largeArray);
            
            assertTrue(largeResult.getMetrics().getArrayAccesses() > 
                      smallResult.getMetrics().getArrayAccesses());
            assertTrue(largeResult.getMetrics().getComparisons() > 
                      smallResult.getMetrics().getComparisons());
        }
    }

    @Nested
    @DisplayName("Result Class Tests")
    class ResultClassTests {
        
        @Test
        @DisplayName("Result should contain all required information")
        void testResultCompleteness() {
            int[] array = {1, -2, 3};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            assertNotNull(result);
            assertNotNull(result.getMetrics());
            assertTrue(result.getStartIndex() >= 0);
            assertTrue(result.getEndIndex() >= result.getStartIndex());
            assertTrue(result.getEndIndex() < array.length);
        }
        
        @Test
        @DisplayName("Result toString should be informative")
        void testResultToString() {
            int[] array = {1, -2, 3};
            KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
            
            String resultString = result.toString();
            assertTrue(resultString.contains("Max Sum"));
            assertTrue(resultString.contains("Range"));
            assertTrue(resultString.contains("Metrics"));
        }
    }

    @Test
    @DisplayName("Should handle extreme values")
    void testExtremeValues() {
        int[] array = {Integer.MAX_VALUE, -1, Integer.MAX_VALUE};
        KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
        
        assertTrue(result.getMaxSum() > 0);
        assertNotNull(result.getMetrics());
    }

    @Test
    @DisplayName("Should handle duplicate elements")
    void testDuplicateElements() {
        int[] array = {3, 3, -5, 3, 3};
        KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(array);
        
        assertEquals(7, result.getMaxSum());
        assertTrue(result.getStartIndex() >= 0);
        assertTrue(result.getEndIndex() < array.length);
    }

    @Test
    @DisplayName("Should handle large arrays efficiently")
    void testLargeArrayPerformance() {
        int[] largeArray = new int[10000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = (i % 3 == 0) ? -1 : 1;
        }
        
        long startTime = System.nanoTime();
        KadaneAlgorithm.Result result = KadaneAlgorithm.findMaxSubarray(largeArray);
        long endTime = System.nanoTime();
        
        assertNotNull(result);
        assertTrue((endTime - startTime) < 100_000_000);
        assertTrue(result.getMaxSum() > 0);
    }
}
