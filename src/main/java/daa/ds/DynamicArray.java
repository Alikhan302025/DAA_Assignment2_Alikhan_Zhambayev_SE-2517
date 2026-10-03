package daa.ds;
import daa.metrics.Metrics;

public class DynamicArray implements IntList {
    private int[] data; //внутренний массив
    private int size;
    private final Metrics metrics;

    public DynamicArray(Metrics metrics){
        this.metrics = metrics;
        this.data = new int[10];
        this.size = 0;
    }

    public DynamicArray(){
        this(new Metrics());
    }

    private void grow() {
        int[] bigger = new int[data.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = data[i];
            metrics.move();
        }
        data = bigger;
    }

    @Override
    public int size(){
        return size;
    }
    @Override
    public int get(int index){
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException("index" + index);
        }
        metrics.step();
        return data[index];
    }

    @Override
    public void add(int x) {
        if(size == data.length){
            grow();}
        data[size] = x;
        size++;
    }

    @Override
    public void add(int index, int x) {
        if (index < 0 || index > size){
            throw new IndexOutOfBoundsException("index: " + index);
        }
        if (size == data.length) {
            grow();}

        for (int i = size; i > index; i--){// [1,2,3] size 3 add(1,9)
            data[i] = data[i - 1];
            metrics.move();

        }
        data[index] = x;
        size++;
    }

    @Override
    public int remove(int index) { // [1,2,3] size 3 remove(1) removed = 2
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException("index: " + index);
        }

        int removed = data[index];

        for (int i = index; i < size - 1; i++){
            data[i] = data[i + 1];
            metrics.move();
        }
        size--;
        return removed;
    }

    @Override
    public boolean contains(int x) {
        throw new UnsupportedOperationException();
    }


}
