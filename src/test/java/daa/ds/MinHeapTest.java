package daa.ds;

import daa.metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {

    // ---------- insert и peekMin ----------

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
            heap.insert(i);              // каждый новый элемент меньше всех, идёт до корня
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

    // ---------- счётчики ----------

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

        heap.insert(10);                       // родитель 5 не больше 10: одна проверка, обмена нет
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

        heap.insert(1);                        // 1 поднимается через 5 и 2 до корня
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
            heap.insert(i);                    // возрастающие: обменов нет
        }
        assertEquals(0, m.getMoves());

        heap.insert(10);                       // 11-й элемент: grow копирует 10
        assertEquals(10, m.getMoves());
        assertEquals(11, heap.size());
        assertTrue(heap.isValidHeap());
    }
}