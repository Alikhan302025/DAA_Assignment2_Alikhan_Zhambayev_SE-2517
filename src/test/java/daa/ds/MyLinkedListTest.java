package daa.ds;

import daa.metrics.Metrics;
import org.junit.jupiter.api.Test;

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
}