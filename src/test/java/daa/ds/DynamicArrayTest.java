package daa.ds;

import daa.metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicArrayTest {


    @Test
    void addAndGet() {
        DynamicArray arr = new DynamicArray();
        arr.add(5);
        arr.add(6);
        arr.add(7);

        assertEquals(3, arr.size());
        assertEquals(5, arr.get(0));
        assertEquals(6, arr.get(1));
        assertEquals(7, arr.get(2));
    }

    @Test
    void addManyElementsTriggersGrowth() {
        DynamicArray arr = new DynamicArray();
        for (int i = 0; i < 100; i++) {
            arr.add(i * 2);
        }
        assertEquals(100, arr.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i * 2, arr.get(i));
        }
    }

    @Test
    void getInvalidIndexThrows() {
        DynamicArray arr = new DynamicArray();
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(0)); // пустой
        arr.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(-1));
    }


    @Test
    void addAtIndexInMiddle() {
        DynamicArray arr = new DynamicArray();
        arr.add(5);
        arr.add(6);
        arr.add(7);
        arr.add(1, 9);

        assertEquals(4, arr.size());
        assertEquals(5, arr.get(0));
        assertEquals(9, arr.get(1));
        assertEquals(6, arr.get(2));
        assertEquals(7, arr.get(3));
    }

    @Test
    void addAtIndexHeadAndTail() {
        DynamicArray arr = new DynamicArray();
        arr.add(0, 1);          // в пустой массив
        arr.add(0, 0);          // в начало
        arr.add(2, 2);          // в конец (index == size)

        assertEquals(3, arr.size());
        assertEquals(0, arr.get(0));
        assertEquals(1, arr.get(1));
        assertEquals(2, arr.get(2));
    }

    @Test
    void addAtIndexInvalidThrows() {
        DynamicArray arr = new DynamicArray();
        arr.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> arr.add(-1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.add(2, 5)); // size = 1, значит 2 нельзя
    }

    @Test
    void addAtIndexWhenFullGrows() {
        DynamicArray arr = new DynamicArray();
        for (int i = 0; i < 10; i++) {
            arr.add(i);          // массив ровно полон (ёмкость 10)
        }
        arr.add(0, 100);         // нужен рост + сдвиг

        assertEquals(11, arr.size());
        assertEquals(100, arr.get(0));
        assertEquals(0, arr.get(1));
        assertEquals(9, arr.get(10));
    }


    @Test
    void removeFromMiddleReturnsValue() {
        DynamicArray arr = new DynamicArray();
        arr.add(5);
        arr.add(6);
        arr.add(7);
        arr.add(8);

        assertEquals(6, arr.remove(1));
        assertEquals(3, arr.size());
        assertEquals(5, arr.get(0));
        assertEquals(7, arr.get(1));
        assertEquals(8, arr.get(2));
    }

    @Test
    void removeFirstAndLast() {
        DynamicArray arr = new DynamicArray();
        arr.add(1);
        arr.add(2);
        arr.add(3);

        assertEquals(1, arr.remove(0));
        assertEquals(3, arr.remove(1));   // теперь [2, 3], индекс 1 это последний
        assertEquals(1, arr.size());
        assertEquals(2, arr.get(0));
    }

    @Test
    void removeSingleElement() {
        DynamicArray arr = new DynamicArray();
        arr.add(42);
        assertEquals(42, arr.remove(0));
        assertEquals(0, arr.size());
    }

    @Test
    void removeInvalidThrows() {
        DynamicArray arr = new DynamicArray();
        assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(0)); // пустой
        arr.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(-1));
    }


    @Test
    void containsFindsPresentAndMissing() {
        DynamicArray arr = new DynamicArray();
        arr.add(5);
        arr.add(6);
        arr.add(7);

        assertTrue(arr.contains(5));
        assertTrue(arr.contains(7));
        assertFalse(arr.contains(99));
    }

    @Test
    void containsOnEmptyAndDuplicates() {
        DynamicArray arr = new DynamicArray();
        assertFalse(arr.contains(1));

        arr.add(3);
        arr.add(3);
        arr.add(3);
        assertTrue(arr.contains(3));
    }

    @Test
    void containsIgnoresRemovedElements() {
        DynamicArray arr = new DynamicArray();
        arr.add(1);
        arr.add(2);
        arr.remove(1);
        assertFalse(arr.contains(2));   // после remove 2 не должно находиться
    }


    @Test
    void getCountsOneStep() {
        Metrics m = new Metrics();
        DynamicArray arr = new DynamicArray(m);
        arr.add(5);
        m.reset();

        arr.get(0);
        assertEquals(1, m.getSteps());
    }

    @Test
    void containsCountsStepsAndComparisons() {
        Metrics m = new Metrics();
        DynamicArray arr = new DynamicArray(m);
        arr.add(5);
        arr.add(6);
        arr.add(7);
        m.reset();

        arr.contains(7);
        assertEquals(3, m.getSteps());
        assertEquals(3, m.getComparisons());
    }

    @Test
    void removeFirstCountsShifts() {
        Metrics m = new Metrics();
        DynamicArray arr = new DynamicArray(m);
        for (int i = 0; i < 5; i++) {
            arr.add(i);
        }
        m.reset();

        arr.remove(0);
        assertEquals(4, m.getMoves());
    }

    @Test
    void addAtHeadCountsShifts() {
        Metrics m = new Metrics();
        DynamicArray arr = new DynamicArray(m);
        arr.add(1);
        arr.add(2);
        arr.add(3);
        m.reset();

        arr.add(0, 9);
        assertEquals(3, m.getMoves());
    }

    @Test
    void growthCopiesAllElements() {
        Metrics m = new Metrics();
        DynamicArray arr = new DynamicArray(m);
        for (int i = 0; i < 10; i++) {
            arr.add(i);
        }
        assertEquals(0, m.getMoves());

        arr.add(10);
        assertEquals(10, m.getMoves());
    }


    @Test
    void randomOperationsMatchArrayList() {
        Random rnd = new Random(42);
        DynamicArray arr = new DynamicArray();
        List<Integer> expected = new ArrayList<>();

        for (int step = 0; step < 5000; step++) {
            int op = rnd.nextInt(4);
            if (op == 0) {
                int v = rnd.nextInt(100);
                arr.add(v);
                expected.add(v);
            } else if (op == 1) {
                int idx = rnd.nextInt(expected.size() + 1);
                int v = rnd.nextInt(100);
                arr.add(idx, v);
                expected.add(idx, v);
            } else if (op == 2 && !expected.isEmpty()) {
                int idx = rnd.nextInt(expected.size());
                int exp = expected.remove(idx);
                int act = arr.remove(idx);
                assertEquals(exp, act);
            } else {
                int v = rnd.nextInt(100);
                assertEquals(expected.contains(v), arr.contains(v));
            }
            assertEquals(expected.size(), arr.size());
        }

        for (int i = 0; i < expected.size(); i++) {
            assertEquals((int) expected.get(i), arr.get(i));
        }
    }
}