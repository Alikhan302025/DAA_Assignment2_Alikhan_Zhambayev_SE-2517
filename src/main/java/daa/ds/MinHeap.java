package daa.ds;

import daa.metrics.Metrics;

public class MinHeap {
    private int[] data;
    private int size;
    private final Metrics metrics;

    public MinHeap(Metrics metrics) {
        this.metrics = metrics;
        this.data = new int[10];
        this.size = 0;
    }

    public MinHeap() {
        this(new Metrics());
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int left(int i) {
        return 2 * i + 1;
    }

    private int right(int i) {
        return 2 * i + 2;
    }

    private void swap(int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
        metrics.move();
    }


    private void grow() {
        int[] bigger = new int[data.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = data[i];
            metrics.move();
        }
        data = bigger;
    }

    public boolean isValidHeap() {
        for (int i = 1; i < size; i++) {
            if (data[parent(i)] > data[i]) {
                return false;
            }
        }
        return true;
    }



    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("heap is empty");
        }
        return data[0];
    }

    private void bubleUp(int i){
        while(i > 0){
            metrics.step();
            metrics.compare();
            int p = parent(i);
            if (data[p] > data[i]) {
                swap(p,i);
                i = p;
            }
            else{
                break;
            }
        }
    }

    public void insert(int x) {
        if (size == data.length) {
            grow();
        }

        data[size] = x;
        size++;
        bubleUp(size - 1);
    }



    public int extractMin() {
        throw new UnsupportedOperationException();
    }
}