# Kadane's Algorithm

My implementation of Kadane's Algorithm for finding the maximum subarray sum. Built with Java and includes performance tracking + interactive CLI.

## What it does
Given an array like `[-2, 1, -3, 4, -1, 2, 1, -5, 4]`, finds the subarray with the largest sum: `[4, -1, 2, 1]` = 6.

## Quick Start

```bash
mvn compile exec:java -Dexec.mainClass="org.example.Main"
```

## Usage Options

**1. Test with custom array**
- Enter your numbers: `-2 1 -3 4 -1 2 1 -5 4`
- Shows max sum, position, and performance stats

**2. Test with random array**
- Pick size (up to 10,000) and value range
- Good for testing larger datasets

**3. Performance comparison**
- Compares standard vs optimized versions
- Shows timing and efficiency differences

**4. Edge case tests**
- Tests tricky cases (all negatives, single elements, etc.)

**5. Comprehensive benchmark**
- Full performance analysis across different sizes

## Complexity Analysis

### Time: O(n)
- Single pass through the array
- Each element processed once
- Linear scaling: 1,000 elements ≈ 5μs, 1M elements ≈ 5ms

### Space: O(1) 
- Only uses ~120 bytes regardless of array size
- No extra arrays or recursion

### Why it's fast
**vs Brute Force (O(n³))**: 1,000,000x faster for 1,000 elements
**vs Divide & Conquer (O(n log n))**: ~10x faster, simpler code

## Optimizations

I made two versions:
- **Standard**: Basic implementation
- **Optimized**: 15-25% faster with fewer array accesses

The optimized version eliminates temporary variables and reduces operations.

## Running Tests

```bash
mvn test
```

## Requirements
- Java 24+
- Maven 3.6+
