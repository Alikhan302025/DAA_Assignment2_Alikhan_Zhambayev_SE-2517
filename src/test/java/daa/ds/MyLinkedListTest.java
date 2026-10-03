package daa.ds;

import daa.metrics.Metrics;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedListTest {

    @Test
    void addAndGet() {
        MyLinkedList list = new MyLinkedList();
        list.add(5);
        list.add(6);
        list.add(7);

        assertEquals(3, list.size());
        assertEquals(5, list.get(0));
        assertEquals(6, list.get(1));
        assertEquals(7, list.get(2));
    }

    @Test
    void addManyKeepsOrder() {
        MyLinkedList list = new MyLinkedList();
        for (int i = 0; i < 1000; i++) {
            list.add(i * 3);
        }
        assertEquals(1000, list.size());
        for (int i = 0; i < 1000; i++) {
            assertEquals(i * 3, list.get(i));
        }
    }

    @Test
    void getInvalidIndexThrows() {
        MyLinkedList list = new MyLinkedList();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0)); // пустой
        list.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    void addCountsTwoMoves() {
        Metrics m = new Metrics();
        MyLinkedList list = new MyLinkedList(m);
        list.add(5);                           // в пустой: head и tail
        assertEquals(2, m.getMoves());

        m.reset();
        list.add(6);                           // в непустой: tail.next и tail
        assertEquals(2, m.getMoves());
        assertEquals(0, m.getSteps());         // обхода нет, шагов нет
    }

    @Test
    void getCountsStepsEqualToIndexPlusOne() {
        Metrics m = new Metrics();
        MyLinkedList list = new MyLinkedList(m);
        list.add(5);
        list.add(6);
        list.add(7);
        m.reset();

        list.get(2);                           // посетили 3 узла
        assertEquals(3, m.getSteps());

        m.reset();
        list.get(0);                           // посетили 1 узел
        assertEquals(1, m.getSteps());
    }

    @Test
    void addAtIndexInMiddle() {
        MyLinkedList list = new MyLinkedList();
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(1, 9);

        assertEquals(4, list.size());
        assertEquals(5, list.get(0));
        assertEquals(9, list.get(1));
        assertEquals(6, list.get(2));
        assertEquals(7, list.get(3));
    }

    @Test
    void addAtIndexHeadAndTail() {
        MyLinkedList list = new MyLinkedList();
        list.add(0, 1);          // в пустой список
        list.add(0, 0);          // в начало
        list.add(2, 2);          // в конец (index == size)
        list.add(3);             // обычный add после вставки: проверка tail

        assertEquals(4, list.size());
        assertEquals(0, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(2, list.get(2));
        assertEquals(3, list.get(3));
    }

    @Test
    void addAtIndexInvalidThrows() {
        MyLinkedList list = new MyLinkedList();
        list.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(2, 5)); // size = 1, значит 2 нельзя
    }

    @Test
    void addAtHeadCountsOnlyMoves() {
        Metrics m = new Metrics();
        MyLinkedList list = new MyLinkedList(m);
        list.add(1);
        list.add(2);
        list.add(3);
        m.reset();

        list.add(0, 9);
        assertEquals(2, m.getMoves());         // две ссылки
        assertEquals(0, m.getSteps());         // обхода нет
    }

    @Test
    void addInMiddleCountsStepsAndMoves() {
        Metrics m = new Metrics();
        MyLinkedList list = new MyLinkedList(m);
        for (int i = 0; i < 4; i++) {
            list.add(i);
        }
        m.reset();

        list.add(2, 9);                        // prev на позиции 1: посещаем 2 узла
        assertEquals(2, m.getSteps());
        assertEquals(2, m.getMoves());
    }

    @Test
    void removeFromMiddleReturnsValue() {
        MyLinkedList list = new MyLinkedList();
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);

        assertEquals(6, list.remove(1));
        assertEquals(3, list.size());
        assertEquals(5, list.get(0));
        assertEquals(7, list.get(1));
        assertEquals(8, list.get(2));
    }

    @Test
    void removeFirstAndLast() {
        MyLinkedList list = new MyLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);

        assertEquals(1, list.remove(0));
        assertEquals(3, list.remove(1));   // теперь [2, 3], индекс 1 это последний
        assertEquals(1, list.size());
        assertEquals(2, list.get(0));
    }

    @Test
    void removeSingleElementLeavesEmptyList() {
        MyLinkedList list = new MyLinkedList();
        list.add(42);
        assertEquals(42, list.remove(0));
        assertEquals(0, list.size());

        list.add(7);                       // список снова работает
        assertEquals(1, list.size());
        assertEquals(7, list.get(0));
    }

    @Test
    void addAfterRemovingLastKeepsTailCorrect() {
        MyLinkedList list = new MyLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(2);                    // удалили последний

        list.add(99);                      // должен встать после 2
        assertEquals(3, list.size());
        assertEquals(2, list.get(1));
        assertEquals(99, list.get(2));
    }

    @Test
    void removeInvalidThrows() {
        MyLinkedList list = new MyLinkedList();
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0)); // пустой
        list.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
    }

    @Test
    void removeFirstCountsOnlyMoves() {
        Metrics m = new Metrics();
        MyLinkedList list = new MyLinkedList(m);
        for (int i = 0; i < 3; i++) {
            list.add(i);
        }
        m.reset();

        list.remove(0);
        assertEquals(1, m.getMoves());         // head = head.next
        assertEquals(0, m.getSteps());         // обхода нет
    }

    @Test
    void removeLastCountsStepsAndTwoMoves() {
        Metrics m = new Metrics();
        MyLinkedList list = new MyLinkedList(m);
        for (int i = 0; i < 4; i++) {
            list.add(i);
        }
        m.reset();

        list.remove(3);                        // prev на позиции 2: посещаем 3 узла
        assertEquals(3, m.getSteps());
        assertEquals(2, m.getMoves());         // prev.next и tail
    }

    @Test
    void containsFindsPresentAndMissing() {
        MyLinkedList list = new MyLinkedList();
        list.add(5);
        list.add(6);
        list.add(7);

        assertTrue(list.contains(5));
        assertTrue(list.contains(7));
        assertFalse(list.contains(99));
    }

    @Test
    void containsOnEmptyAndDuplicates() {
        MyLinkedList list = new MyLinkedList();
        assertFalse(list.contains(1));

        list.add(3);
        list.add(3);
        list.add(3);
        assertTrue(list.contains(3));
    }

    @Test
    void containsIgnoresRemovedElements() {
        MyLinkedList list = new MyLinkedList();
        list.add(1);
        list.add(2);
        list.remove(1);
        assertFalse(list.contains(2));
    }

    @Test
    void containsCountsStepsAndComparisons() {
        Metrics m = new Metrics();
        MyLinkedList list = new MyLinkedList(m);
        list.add(5);
        list.add(6);
        list.add(7);
        m.reset();

        list.contains(7);                      // найдёт на третьем узле
        assertEquals(3, m.getSteps());
        assertEquals(3, m.getComparisons());

        m.reset();
        list.contains(99);                     // не найдёт: пройдёт все 3
        assertEquals(3, m.getSteps());
        assertEquals(3, m.getComparisons());
    }

    @Test
    void randomOperationsMatchArrayList() {
        Random rnd = new Random(42);
        MyLinkedList list = new MyLinkedList();
        List<Integer> expected = new ArrayList<>();

        for (int step = 0; step < 5000; step++) {
            int op = rnd.nextInt(4);
            if (op == 0) {
                int v = rnd.nextInt(100);
                list.add(v);
                expected.add(v);
            } else if (op == 1) {
                int idx = rnd.nextInt(expected.size() + 1);
                int v = rnd.nextInt(100);
                list.add(idx, v);
                expected.add(idx, v);
            } else if (op == 2 && !expected.isEmpty()) {
                int idx = rnd.nextInt(expected.size());
                int exp = expected.remove(idx);
                int act = list.remove(idx);
                assertEquals(exp, act);
            } else {
                int v = rnd.nextInt(100);
                assertEquals(expected.contains(v), list.contains(v));
            }
            assertEquals(expected.size(), list.size());
        }

        for (int i = 0; i < expected.size(); i++) {
            assertEquals((int) expected.get(i), list.get(i));
        }
    }
}