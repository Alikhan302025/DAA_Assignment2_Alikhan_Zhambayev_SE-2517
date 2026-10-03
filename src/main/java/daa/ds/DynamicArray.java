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
