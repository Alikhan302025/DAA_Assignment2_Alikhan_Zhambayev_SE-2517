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
        Node node = new Node(x);

        if(head == null){
            head = node;
            tail = node;
            metrics.move();
            metrics.move();
        }
        else{
            tail.next = node;
            tail = node;
            metrics.move();
            metrics.move();

        }
        size++;
    }

    @Override
    public void add(int index, int x) {
        if (index < 0 || index > size){
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        if (index == size){
            add(x);
            return;
        }
        Node node = new Node(x);

        if (index == 0){
            node.next = head;
            head = node;
            metrics.move();
            metrics.move();
        }
        else{
            Node prev = nodeAt(index - 1);
            node.next =  prev.next;
            prev.next = node;
            metrics.move();
            metrics.move();

        }
        size++;
    }

    @Override
    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        int removed;
        if (index == 0) {
            removed = head.value;
            head = head.next;
            metrics.move();
            if (head == null) {
                tail = null;
                metrics.move();
            }
        } else {
            Node prev = nodeAt(index - 1);
            Node target = prev.next;
            removed = target.value;
            prev.next = target.next;
            metrics.move();
            if (target == tail) {
                tail = prev;
                metrics.move();
            }
        }
        size--;
        return removed;
    }

    @Override
    public boolean contains(int x) {
        Node current = head;
        while (current != null) {
            metrics.step();
            metrics.compare();
            if (current.value == x) {
                return true;
            }
            current = current.next;
        }
        return false;
    }


}
