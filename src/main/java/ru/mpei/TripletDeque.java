package ru.mpei;

//Импорты
import java.util.Collection; //базовый интерфейс всех коллекций
import java.util.Deque; //интерфейс для двусторонней очереди
import java.util.NoSuchElementException; /*исключение, которое выбрасывается, если операция с элементом
не может быть выполнена*/

public class TripletDeque<T> implements Deque<T>, Containerable { //объявление класса, реализующего интерфейсы

    private static final int ORDER_VOLUME = 1000; //Максимальный размер очереди

    private Container<T> first; //первый контейнер в очереди
    private Container<T> last; //последний контейнер в очереди

    private int length;    //текущая заполненность очереди
    private int orderVolume; //максимальная вместимость очереди



    public TripletDeque() { //конструктор по умолчанию (без параметров), который вызывает другой конструктор
        this(ORDER_VOLUME);
    }

    //Создаем очередь, где первый и последний узел вместе null
    public TripletDeque(int orderVolume) { //конструктор с параметром
        this.orderVolume = orderVolume; //устанавливает макс. вместимость очереди
        this.length = 0; //текущая заполненность очереди
        Container<T> container = new Container<T>(null, null); //создание объекта - контейнера без данных
        this.first = container; //ссылка на первый элемент
        this.last = container; //ссылка на последний элемент
    }

    /*
     * Добавляем элемент в первый узел
     * назначаем t в массив в левую сторону. Если узел заполнен,
     * то создаем новый узел - он становится первым
     *
     */

    @Override //аннотация для переопределения метода интерфейса
    //Метод, который добавляет элемент в начало очереди
    public void addFirst(T t) {
        if (t == null) { //проверка на равенство null предотвращает добавление пустых элементов
            throw new NullPointerException(); //выбрасывается исключение
        }

        if (this.length < orderVolume) { //проверка на переполнение очереди

            //Если узел переполнен, то создаем новый
            if (first.getContainerSizeMax() == first.getIndex()) {
                Container<T> cont = new Container<T>((T) first, null);
                this.first.setPrev((T) cont);
                this.first = cont;
            }
            //индексация в массиве с 0, поэтому надо вычитать 1
            first.setData(first.getContainerSizeMax() - first.getIndex() - 1, t); //добавление элемента в контейнер
            first.setIndex(first.getIndex() + 1); //увеличиваем индекс на 1
            this.length++; //увеличиваем длину очереди
        } else {
            throw new IllegalStateException(); //выбрасывается исключение - очередь переполнена
        }

    }

    /*
     * Добавляем новые элементы, рост идет вправо с 0 индекса, если узел заполнен, то создаем новый узел
     * и добавляем ссылку next
     * @param t the element to be added.
     */

    @Override
    public void addLast(T t) { //добавляет элемент в конец очереди
        if (t == null) {
            throw new NullPointerException();
        }

        if (this.length < orderVolume) {

            //Если переполнен, то создаем новый узел
            if (last.getContainerSizeMax() == last.getIndex()) {
                Container<T> cont = new Container<T>(null, (T) last);
                this.last.setNext((T) cont);
                this.last = cont;
            }
            last.setData(last.getIndex(), t);
            last.setIndex(last.getIndex() + 1);
            this.length++;
        } else {
            throw new IllegalStateException();
        }
    }

    /*
     * По сути метод такой же как и addFirst только возвращает буль, а не бросает исключение
     *
     * @param t the element to add
     * @return
     */

    @Override
    public boolean offerFirst(T t) { //пытается добавить элемент в начало
        if (t == null) {
            throw new NullPointerException();
        }

        if (this.length < orderVolume) {

            //Если узел переполнен, то создаем новый
            if (first.getContainerSizeMax() == first.getIndex()) {
                Container<T> cont = new Container<T>((T) first, null);
                this.first.setPrev((T) cont);
                this.first = cont;
            }
            //индексация в массиве с 0, поэтому надо вычитать 1
            first.setData(first.getContainerSizeMax() - first.getIndex() - 1, t);
            first.setIndex(first.getIndex() + 1);
            this.length++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean offerLast(T t) { //добавление элемента в конец очереди, true если успешно, false если нет
        if (t == null) {
            throw new NullPointerException();
        }

        if (this.length < orderVolume) {

            //Если переполнен, то создаем новый узел
            if (last.getContainerSizeMax() == last.getIndex()) {
                Container<T> cont = new Container<T>(null, (T) last);
                this.last.setNext((T) cont);
                this.last = cont;
            }
            last.setData(last.getIndex(), t);
            last.setIndex(last.getIndex() + 1);
            this.length++;
            return true;
        } else {
            return false;
        }
    }


    /*
     * Возвращает и удаляет первый элемент очереди (самый левый)
     * Если индекс == 0 то нужно перевязать ссылки на контейнеры и убрать пустой
     */

    @Override
    //Удаляет и возвращает первый элемент в очереди
    public T removeFirst() {
        // Вначале могут быть дырки, т.к. рост влево, ищем первый !=null
        for (int i = 0; i < first.getContainerSizeMax(); i++) { //поиск первого ненулевого элемента
            if (first.getData()[i] != null) {
                T retVal = first.getData()[i]; //первый ненулевой элемент
                //Т.к добавление элемента идет через (getContainerSizeMax - getIndex), то при удалении элемента нужно индекс уменьшить
                first.setIndex(first.getIndex() - 1);
                first.setData(first.getContainerSizeMax() - first.getIndex() - 1, null); //устанавливаем  null
                //Если возникла ситуация что мы удалили единственный элемент в узле, и у него есть сосед next, то first двигается вправо
                //когда контейнер стал пустым
                if (first.getIndex() == 0 && first.getNext() != null) { //переходим к следующему контейнеру
                    first = (Container<T>) first.getNext(); //обновляем ссылку
                    first.setPrev(null); //удаляем ссылку на предыдущий контейнер
                }
                this.length--;
                return retVal;
            }
        }
        //Если очередь пуста, то исключение
        throw new NoSuchElementException();
    }

    //Удаляет и возвращает последний элемент в очереди
    @Override
    public T removeLast() {

        for (int i = last.getContainerSizeMax() - 1; i >= 0; i--) {
            if (last.getData()[i] != null) {
                T retVal = last.getData()[i];

                if (last.getData()[0] == null) {

                    last.setData(last.getIndex(), null);
                    last.setIndex(last.getIndex() - 1);

                } else {
                    last.setIndex(last.getIndex() - 1);
                    last.setData(last.getIndex(), null);
                }


                if (last.getIndex() == 0 && last.getPrev() != null) {
                    last = (Container<T>) last.getPrev();
                    last.setNext(null);
                }
                this.length--;

                // Если текущая длина очереди ноль, то нужно все ссылки на first и last перепривязать на начальные
                if (length == 0) {
                    Container<T> container = new Container<T>(null, null);
                    this.first = container;
                    this.last = container;
                }

                return retVal;
            }

        }
        throw new NoSuchElementException();
    }


    //Извлечение и удаление первого элемента из очереди без исключения
    @Override
    public T pollFirst() {

        // Вначале могут быть дырки, т.к. рост влево, ищем первый !=null
        for (int i = 0; i < first.getContainerSizeMax(); i++) {
            if (first.getData()[i] != null) {
                T retVal = first.getData()[i];
                //Т.к добавление элемента идет через (getContainerSizeMax - getIndex), то при удалении элемента нужно индекс ументшить
                first.setIndex(first.getIndex() - 1);
                first.setData(first.getContainerSizeMax() - first.getIndex() - 1, null);
                //Если возникла ситуация что мы удалили единственный элемент в узле, и у него есть сосед next, то first двигается вправо
                if (first.getIndex() == 0 && first.getNext() != null) {
                    first = (Container<T>) first.getNext();
                    first.setPrev(null);
                }
                this.length--;
                return retVal;
            }
        }
        //Если очередь пуста, то null
        return null;
    }


    //Извлекает и возвращает последний элемент в очереди без исключения
    @Override
    public T pollLast() {
        for (int i = last.getContainerSizeMax() - 1; i >= 0; i--) {
            if (last.getData()[i] != null) {
                T retVal = last.getData()[i];
                last.setIndex(last.getIndex() - 1);
                last.setData(last.getIndex(), null);
                if (last.getIndex() == 0 && last.getPrev() != null) {
                    last = (Container<T>) last.getPrev();
                    last.setNext(null);
                }
                this.length--;
                return retVal;
            }
        }
        return null;
    }

    /*
     * Извлекает голову очереди, но не удаляет
     * По сути берем первый узел и идем с 0 индекса пока не встретим данные
     */

    //Возвращает но не удаляет
    @Override
    public T getFirst() {
        T[] data = first.getData();
        for (int i = 0; i < first.getContainerSizeMax(); i++) {
            if (data[i] != null) {
                return data[i];
            }
        }
        throw new NoSuchElementException(); //Если очередь пуста
    }

    /*
     * Извлекает хвост очереди - самый правый элемент
     */

    @Override
    public T getLast() {
        T[] data = last.getData();
        for (int i = first.getContainerSizeMax() - 1; i >= 0; i--) {
            if (data[i] != null) {
                return data[i];
            }
        }
        throw new NoSuchElementException();
    }

    //Извлекает первый элемент очереди но не удаляет его, если очередь пуста - null
    @Override
    public T peekFirst() {
        T[] data = first.getData();
        for (int i = 0; i < first.getContainerSizeMax(); i++) {
            if (data[i] != null) {
                return data[i];
            }
        }

        return null;
    }

    //Извлекает последний элемент очереди но не удаляет его, если очередь пуста - null
    @Override
    public T peekLast() {
        T[] data = last.getData();
        for (int i = first.getContainerSizeMax() - 1; i >= 0; i--) {
            if (data[i] != null) {
                return data[i];
            }
        }

        return null;
    }


    /*Удаляет первое вхождение указанного элемента из очереди
    Метод removeFirstOccurrence(Object o) удаляет первое вхождение элемента из очереди. Для этого он:
    Проходит по всем контейнерам.
    В каждом контейнере ищет элемент.
    Если элемент найден, сдвигает все последующие элементы влево и уменьшает длину очереди.
    Если элемент был найден и удалён, возвращает true, иначе — false.*/

    @Override
    public boolean removeFirstOccurrence(Object o) { //о - элемент который нужно удалить

        Container<T> cont = first; //начинаем с первого контейнера
        //Нужно для случая когда только один узел
        int i = -1; //не ищем внутри контейнера
        boolean findFirstEl = false;

        if (o == null) { //проверка на null
            throw new NullPointerException();
        }

        while (cont.getNext() != null || i == -1) {
            //Если в первом узле не нашли, то идем в следующий
            if (i != -1) {
                cont = (Container<T>) cont.getNext();
            }

            T[] data = cont.getData();
            //Поиск в контейнере
            for (i = 0; i < cont.getContainerSizeMax(); i++) {
                //нашли первый такой элемент и сразу уменьшили очередь
                if (!findFirstEl) {
                    findFirstEl = data[i].equals(o);
                    length--;
                }
                //нашли первое совпадение, то просто смещаем все на 1 влево. Так делаем до последней ячейки
                if (findFirstEl && i < cont.getContainerSizeMax() - 1) {
                    data[i] = data[i + 1];
                }
                //Если у нашего контейнера есть следующий, то нужно из него перетащить 0 ячейку, иначе наша последняя будет null
                if (findFirstEl && i == cont.getContainerSizeMax() - 1) {
                    if (cont.getNext() != null) {
                        data[i] = ((Container<T>) cont.getNext()).getData()[0];
                    } else {
                        data[i] = null;
                    }
                }
            }
        }

        //Берем реальный индекс куда был записан последний элемент
        last.setIndex(last.getIndex() - 1);
        if (last.getIndex() == 0 && last.getPrev() != null) {
            last = (Container<T>) last.getPrev();
            last.setNext(null);
        }

        return findFirstEl;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {

        return true;
    }

    /*
     *
     * Метод из коллекции ОЧЕРЕДЬ. Вставляет элемент в хвост очереди (самое правое место)
     *
     * @param t the element to add
     * @return {@code true} (as specified by {@link Collection#add})
     *
     * @throws NullPointerException if the specified element is null and this
     *         deque does not permit null elements
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this deque
     */

    //Добавляет элемент в конец очереди (в хвост)
    @Override
    public boolean add(T t) {
        if (t == null) {
            throw new NullPointerException();
        }

        if (this.length < orderVolume) {

            //Если переполнен, то создаем новый узел
            if (last.getContainerSizeMax() == last.getIndex()) {
                Container<T> cont = new Container<T>(null, (T) last);
                this.last.setNext((T) cont);
                this.last = cont;
            }
            last.setData(last.getIndex(), t);
            last.setIndex(last.getIndex() + 1);
            this.length++;
            return true;
        } else {
            throw new IllegalStateException();
        }
    }


    /*
     * Метод аналогичен public boolean add(T t), только возвращает true если смогли добавить и false если не смогли
     *
     * @param t the element to add
     * @return
     */

    //Добавляет элемент в конец очереди без исключения, а возвращает буль
    @Override
    public boolean offer(T t) {
        if (t == null) {
            throw new NullPointerException();
        }

        if (this.length < orderVolume) {

            //Если переполнен, то создаем новый узел
            if (last.getContainerSizeMax() == last.getIndex()) {
                Container<T> cont = new Container<T>(null, (T) last);
                this.last.setNext((T) cont);
                this.last = cont;
            }
            last.setData(last.getIndex(), t);
            last.setIndex(last.getIndex() + 1);
            this.length++;
            return true;
        } else {
            return false;
        }
    }

    //Удаляет и возвращает первый элемент в очереди
    @Override
    public T remove() {

        // Вначале могут быть дырки, т.к. рост влево, ищем первый !=null
        for (int i = 0; i < first.getContainerSizeMax(); i++) {
            if (first.getData()[i] != null) {
                T retVal = first.getData()[i];
                //Т.к добавление элемента идет через (getContainerSizeMax - getIndex), то при удалении элемента нужно индекс ументшить
                first.setIndex(first.getIndex() - 1);
                first.setData(first.getContainerSizeMax() - first.getIndex() - 1, null);
                //Если возникла ситуация что мы удалили единственный элемент в payload узла, и у него есть сосед next, то first двигается вправо
                if (first.getIndex() == 0 && first.getNext() != null) {
                    first = (Container<T>) first.getNext();
                    first.setPrev(null);
                }
                this.length--;
                return retVal;
            }
        }
        //Если очередь пуста, то исключение
        throw new NoSuchElementException();

    }

    /**
     * Retrieves and removes the head of the queue represented by this deque
     * (in other words, the first element of this deque), or returns
     * {@code null} if this deque is empty.
     *
     * <p>This method is equivalent to {@link #pollFirst()}.
     *
     * @return the first element of this deque, or {@code null} if
     *         this deque is empty
     */

    //Удаляет и возвращает первый элемент очереди без исключений, возвращает null

    @Override
    public T poll() {

        // Вначале могут быть дырки, т.к. рост влево, ищем первый !=null
        for (int i = 0; i < first.getContainerSizeMax(); i++) {
            if (first.getData()[i] != null) {
                T retVal = first.getData()[i];
                //Т.к добавление элемента идет через (getContainerSizeMax - getIndex), то при удалении элемента нужно индекс ументшить
                first.setIndex(first.getIndex() - 1);
                first.setData(first.getContainerSizeMax() - first.getIndex() - 1, null);
                //Если возникла ситуация что мы удалили единственный элемент узла, и у него есть сосед next, то first двигается вправо
                if (first.getIndex() == 0 && first.getNext() != null) {
                    first = (Container<T>) first.getNext();
                    first.setPrev(null);
                }
                this.length--;
                return retVal;
            }
        }

        return null;
    }


    /**
     *  Извлекает, но не удаляет голову очереди (самый правый элемент)
     * This method differs from {@link #peek peek} only in that it throws an
     * exception if this deque is empty.
     *
     * <p>This method is equivalent to {@link #getFirst()}.
     *
     * @return the head of the queue represented by this deque
     * @throws NoSuchElementException if this deque is empty
     */


    //Извлекает, но не удаляет первый элемент из  очереди
    @Override
    public T element() {
        // Вначале могут быть дырки, т.к. рост влево, ищем первый !=null
        for (int i = 0; i < first.getContainerSizeMax(); i++) {
            if (first.getData()[i] != null) {
                T retVal = first.getData()[i];

                return retVal;
            }
        }
        //Если очередь пуста, то исключение
        throw new NoSuchElementException();
    }


    //Извлекает первый элемент, но не выбрасывает исключение, а возвращает null
    @Override
    public T peek() {
        // Вначале могут быть дырки, т.к. рост влево, ищем первый !=null
        for (int i = 0; i < first.getContainerSizeMax(); i++) {
            if (first.getData()[i] != null) {
                T retVal = first.getData()[i];

                return retVal;
            }
        }

        return null;
    }


    //Реализует добавление всех элементов из переданной коллекции в очередь
    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c.isEmpty()) {
            throw new NullPointerException();
        }
        //будет знаком того, удалось ли поместить всею коллекцию в нашу очередь
        boolean flg = false;
        for (Object data : c) {
            if (data == null) {
                throw new NullPointerException();
            }
            if (data != null && offer((T) data)) {
                flg = true;
            } else {
                throw new IllegalStateException();
            }
        }
        return flg;
    }

    //Очищает очередь, устанавливая новый пустой контейнер
    @Override
    public void clear() {
        Container<T> container = new Container<T>(null, null);
        this.first = container;
        this.last = container;
    }

    //Оставляет в очереди только те элементы, которые содержатся в переданной коллекции
    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    //Удаляет все элементы из очереди, которые содержатся в коллекции
    @Override
    public boolean removeAll(Collection c) {
        return false;
    }


    //Добавляет элемент в начало очереди в контексте стека
    //LIFO - Last In First Out
    @Override
    public void push(T t) {
        if (t == null) {
            throw new NullPointerException();
        }

        if (this.length < orderVolume) {

            //Если узел переполнен, то создаем новый
            if (first.getContainerSizeMax() == first.getIndex()) {
                Container<T> cont = new Container<T>((T) first, null);
                this.first.setPrev((T) cont);
                this.first = cont;
            }
            //индексация в массиве с 0, поэтому надо вычитать 1
            first.setData(first.getContainerSizeMax() - first.getIndex() - 1, t);
            //first.setData(first.getIndex(), t);
            first.setIndex(first.getIndex() + 1);
            this.length++;
        } else {
            throw new IllegalStateException();
        }
    }

    //Как push только удаляет
    @Override
    public T pop() {
        // Вначале могут быть дырки, т.к. рост влево, ищем первый !=null
        for (int i = 0; i < first.getContainerSizeMax(); i++) {
            if (first.getData()[i] != null) {
                T retVal = first.getData()[i];
                //Т.к добавление элемента идет через (getContainerSizeMax - getIndex), то при удалении элемента нужно индекс ументшить
                first.setIndex(first.getIndex() - 1);
                first.setData(first.getContainerSizeMax() - first.getIndex() - 1, null);
                //Если возникла ситуация что мы удалили единственный элемент в payload узла, и у него есть сосед next, то first двигается вправо
                if (first.getIndex() == 0 && first.getNext() != null) {
                    first = (Container<T>) first.getNext();
                    first.setPrev(null);
                }
                this.length--;
                return retVal;
            }
        }
        //Если очередь пуста, то исключение
        throw new NoSuchElementException();
    }

    //Удаляет первое вхождение
    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    //
    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }


    @Override
    public boolean contains(Object o) {
        java.util.Iterator<T> iterator = iterator();
        boolean found = false;
        while ((iterator.hasNext()) && (!found)) {
            T el = iterator.next();
            if (el.equals(o))
                found = true;
        }
        return found;
    }

    @Override
    public int size() {
        int s = length;
        return s;
    }


    @Override
    public boolean isEmpty() {
        return this.length == 0;
    }


    @Override
    public Iterator<T> iterator() {
        Iterator iterator = new Iterator(first);
        return iterator;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    @Override
    public Iterator descendingIterator() {
        Exception UnsupportedOperationException = new Exception();
        try {
            throw UnsupportedOperationException;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Object[] getContainerByIndex(int cIndex) {
        int count = 0;
        Container<T> cont = first;

        for (int i = 0; i <= cIndex; i++) {
            if (count == cIndex) {
                if (cont == null) {
                    return null;
                }

                return cont.getData();
            }
            count++;
            cont = (Container<T>) cont.getNext();
        }

        throw new RuntimeException();
    }


    class Iterator<T> implements java.util.Iterator<T> {
        private Container<T> container;
        private int firtsElenemt;
        private T retVal;
        private boolean flag = true;

        public Iterator(Container<T> container) {
            this.container = container;
        }

        /**
         * Если первый узел не заполнен на все 5 ячеек, то в начале будут ДЫРКИ, нужно найти
         * первый элемент, Который не null и уже с него проверять наличие next
         * @return
         */
        @Override
        public boolean hasNext() {

            if (this.container.getPrev() == null) {
                for (int i = 0; i < container.getContainerSizeMax() && this.flag == true; i++)
                    if (this.container.getData()[i] != null) {
                        this.firtsElenemt = i;
                        this.flag = false;
                    }
            }

            if (this.firtsElenemt != container.getContainerSizeMax() &&
                    this.container.getData()[this.firtsElenemt] != null) {
                return true;
            }
            return false;
        }

        @Override
        public T next() {
            this.retVal = this.container.getData()[this.firtsElenemt];
            this.firtsElenemt++;

            //Если следующий элемент выходит за границы контейнера, то нужно перейти с следуюий узел
            if (this.firtsElenemt == container.getContainerSizeMax() &&
                    this.container.getNext() != null) {
                this.firtsElenemt = 0;
                this.container = (Container<T>) this.container.getNext();
            }
            if (retVal == null) {
                throw new NoSuchElementException();
            } else {
                return this.retVal;
            }

        }
    }

}
