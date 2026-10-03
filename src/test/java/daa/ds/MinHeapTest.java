package daa.ds;

import daa.metrics.Metrics;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {


    @Test
    void insertAndPeekMin() {
        MinHeap heap = new MinHeap();
        heap.insert(5);
        heap.insert(3);
        heap.insert(8);

        assertEquals(3, heap.size());
        assertEquals(3, heap.peekMin());
        assertTrue(heap.isValidHeap());
    }

    @Test
    void peekMinOnEmptyThrows() {
        MinHeap heap = new MinHeap();
        assertThrows(IllegalStateException.class, heap::peekMin);
    }

    @Test
    void insertSingleElement() {
        MinHeap heap = new MinHeap();
        heap.insert(42);
        assertEquals(1, heap.size());
        assertEquals(42, heap.peekMin());
        assertTrue(heap.isValidHeap());
    }

    @Test
    void insertDuplicates() {
        MinHeap heap = new MinHeap();
        for (int i = 0; i < 5; i++) {
            heap.insert(7);
        }
        assertEquals(5, heap.size());
        assertEquals(7, heap.peekMin());
        assertTrue(heap.isValidHeap());
    }

    @Test
    void insertDescendingBubblesToRoot() {
        MinHeap heap = new MinHeap();
        for (int i = 100; i >= 1; i--) {
            heap.insert(i);
            assertEquals(i, heap.peekMin());
            assertTrue(heap.isValidHeap());
        }
        assertEquals(100, heap.size());
    }

    @Test
    void insertAscendingKeepsMinAtRoot() {
        MinHeap heap = new MinHeap();
        for (int i = 0; i < 100; i++) {
            heap.insert(i);
            assertEquals(0, heap.peekMin());
            assertTrue(heap.isValidHeap());
        }
    }

    @Test
    void heapPropertyHoldsAfterEveryRandomInsert() {
        Random rnd = new Random(42);
        MinHeap heap = new MinHeap();
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < 5000; i++) {
            int v = rnd.nextInt(1000);
            heap.insert(v);
            min = Math.min(min, v);

            assertTrue(heap.isValidHeap());
            assertEquals(min, heap.peekMin());
            assertEquals(i + 1, heap.size());
        }
    }


    @Test
    void insertIntoEmptyCountsNothing() {
        Metrics m = new Metrics();
        MinHeap heap = new MinHeap(m);
        heap.insert(5);

        assertEquals(0, m.getSteps());
        assertEquals(0, m.getComparisons());
        assertEquals(0, m.getMoves());
    }

    @Test
    void insertLargeValueStopsImmediately() {
        Metrics m = new Metrics();
        MinHeap heap = new MinHeap(m);
        heap.insert(1);
        heap.insert(5);
        heap.insert(3);
        m.reset();

        heap.insert(10);
        assertEquals(1, m.getSteps());
        assertEquals(1, m.getComparisons());
        assertEquals(0, m.getMoves());
    }

    @Test
    void insertSmallestBubblesAllTheWayUp() {
        Metrics m = new Metrics();
        MinHeap heap = new MinHeap(m);
        heap.insert(2);
        heap.insert(5);
        heap.insert(3);
        m.reset();

        heap.insert(1);
        assertEquals(1, heap.peekMin());
        assertEquals(2, m.getSteps());
        assertEquals(2, m.getComparisons());
        assertEquals(2, m.getMoves());
    }

    @Test
    void growthCopiesAllElements() {
        Metrics m = new Metrics();
        MinHeap heap = new MinHeap(m);
        for (int i = 0; i < 10; i++) {
            heap.insert(i);
        }
        assertEquals(0, m.getMoves());

        heap.insert(10);
        assertEquals(10, m.getMoves());
        assertEquals(11, heap.size());
        assertTrue(heap.isValidHeap());
    }


    @Test
    void extractMinOnEmptyThrows() {
        MinHeap heap = new MinHeap();
        assertThrows(IllegalStateException.class, heap::extractMin);
    }

    @Test
    void extractMinFromSingleElement() {
        MinHeap heap = new MinHeap();
        heap.insert(42);
        assertEquals(42, heap.extractMin());
        assertEquals(0, heap.size());
        assertTrue(heap.isEmpty());

        assertThrows(IllegalStateException.class, heap::peekMin);
    }

    @Test
    void extractMinReturnsInOrder() {
        MinHeap heap = new MinHeap();
        int[] values = {5, 3, 8, 1, 9, 2, 7};
        for (int v : values) {
            heap.insert(v);
        }

        int[] expected = {1, 2, 3, 5, 7, 8, 9};
        for (int e : expected) {
            assertEquals(e, heap.extractMin());
            assertTrue(heap.isValidHeap());
        }
        assertEquals(0, heap.size());
    }

    @Test
    void extractMinWithDuplicates() {
        MinHeap heap = new MinHeap();
        int[] values = {3, 1, 3, 1, 2, 2};
        for (int v : values) {
            heap.insert(v);
        }
        int[] expected = {1, 1, 2, 2, 3, 3};
        for (int e : expected) {
            assertEquals(e, heap.extractMin());
            assertTrue(heap.isValidHeap());
        }
    }

    @Test
    void sortedOutputOnRandomData() {
        Random rnd = new Random(42);
        int n = 5000;
        int[] values = new int[n];
        MinHeap heap = new MinHeap();
        for (int i = 0; i < n; i++) {
            values[i] = rnd.nextInt(1000);
            heap.insert(values[i]);
        }

        int[] expected = values.clone();
        Arrays.sort(expected);

        for (int i = 0; i < n; i++) {
            assertEquals(expected[i], heap.extractMin());
            assertTrue(heap.isValidHeap());
        }
        assertEquals(0, heap.size());
    }

    @Test
    void randomInsertAndExtractMatchPriorityQueue() {
        Random rnd = new Random(7);
        MinHeap heap = new MinHeap();
        PriorityQueue<Integer> expected = new PriorityQueue<>();

        for (int step = 0; step < 5000; step++) {
            if (expected.isEmpty() || rnd.nextInt(3) != 0) {
                int v = rnd.nextInt(1000);
                heap.insert(v);
                expected.add(v);
            } else {
                assertEquals((int) expected.poll(), heap.extractMin());
            }
            assertTrue(heap.isValidHeap());
            assertEquals(expected.size(), heap.size());
            if (!expected.isEmpty()) {
                assertEquals((int) expected.peek(), heap.peekMin());
            }
        }
    }


    @Test
    void extractMinFromSingleElementCountsNothing() {
        Metrics m = new Metrics();
        MinHeap heap = new MinHeap(m);
        heap.insert(5);
        m.reset();

        heap.extractMin();
        assertEquals(0, m.getSteps());
        assertEquals(0, m.getComparisons());
        assertEquals(0, m.getMoves());
    }

    @Test
    void extractMinStopsImmediately() {
        Metrics m = new Metrics();
        MinHeap heap = new MinHeap(m);
        heap.insert(1);
        heap.insert(5);
        heap.insert(2);                        // куча [1, 5, 2]
        m.reset();

        assertEquals(1, heap.extractMin());    // 2 идёт в корень, куча [2, 5]
        assertEquals(1, m.getSteps());         // одна итерация
        assertEquals(1, m.getComparisons());   // только 5 < 2? (правого ребёнка нет)
        assertEquals(1, m.getMoves());         // только постановка 2 в корень
    }

    @Test
    void extractMinOneSwapCounts() {
        Metrics m = new Metrics();
        MinHeap heap = new MinHeap(m);
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        heap.insert(4);                        // куча [1, 2, 3, 4]
        m.reset();

        assertEquals(1, heap.extractMin());    // [4, 2, 3] → обмен с 2 → [2, 4, 3]
        assertEquals(1, m.getSteps());
        assertEquals(2, m.getComparisons());   // 3 < 2? и 2 < 4?
        assertEquals(2, m.getMoves());         // постановка в корень + один обмен
        assertEquals(2, heap.peekMin());
    }
}