package daa.ds;
// Так head → [5 | •]→ [6 | •]→ [7 | null]
//                                 ↑
//                                 tail
// head хранит ссылку на первый таил на последний нужен что бы могли к концу сразу обратится

import daa.metrics.Metrics;

public class MyLinkedList implements IntList {

    private static class Node{
        int value;
        Node next;

        Node(int value){
        this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private final Metrics metrics;

    public MyLinkedList(Metrics metrics){
        this.metrics = metrics;
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public MyLinkedList(){
        this(new Metrics());
    }

    private Node nodeAt(int index){
        Node current = head;
        metrics.step();
        for (int i = 0; i < index; i++){
            current = current.next;
            metrics.step();
        }
        return current;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
        return nodeAt(index).value;
    }

    @Override
    public void add(int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(int x) {
        throw new UnsupportedOperationException();
    }


}
