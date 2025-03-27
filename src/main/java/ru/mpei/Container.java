package ru.mpei;

/*Данный класс реализует узел, который содержит массив данных по умолчанию 5
 * и ссылки на правый, левый узел.
 * @param <T>
 */

public class Container<T> {

    private T[] data;
    private T prev;
    private T next;
    private int containerSizeMax = 5;



    /*Т.к в методах идет работа со свободными ячейками слева (справа), то нужна переменная
     * Например, добавляем элемент и при каждом движении увеличивая index.
     * Если containerSizeMax - index ==0, то значит нужно создать новый узел.
     */
    private int index;


    //Конструктор
    public Container(T next, T prev) {
        this.next = next;
        this.prev = prev;
        this.data = (T[]) new Object[containerSizeMax];
        this.index = 0;

    }

    //Методы доступа (геттеры и сеттеры)

    //Методы для работы со ссылками на следующий и предыдущий элементы
    //get возвращают ссылки на следующий и предыдущий элементы
    //set устанавливают ссылки на следующий и предыдущий элементы

    public T getNext() {
        return next;
    }

    public void setNext(T next) {
        this.next = next;
    }

    public T getPrev() {
        return prev;
    }

    public void setPrev(T prev) {
        this.prev = prev;
    }


    //Метод для работы с массивом данных
    //get возвращает сам массив
    //setData(...) устанавливает значение в массив данных по индексу i для добавления элемента в контейнер, используя индекс
    public T[] getData() {
        return data;
    }

    public void setData(int i, T set) {
        data[i] = set;

    }

    //Методы для работы с размером контейнера и индексом

    public int getContainerSizeMax() {
        return containerSizeMax;
    } //возвращает макс. размер контейнера

    public int getIndex() {
        return index;
    } //возвращает текущий индекс (кол-во добавленных элементов)

    public void setIndex(int index) {
        this.index = index;
    } //устанавливает значение индекса


}
