package daa.ds;

public interface IntList {
    void add(int x);                 // в конец
    void add(int index, int x);      // вставка
    int remove(int index);           // возвращает удалённое значение
    int get(int index);
    boolean contains(int x);
    int size();
}