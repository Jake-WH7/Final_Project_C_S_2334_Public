import java.util.Iterator;
import java.util.NoSuchElementException;


public class CustomLinkedList<T> implements Iterable<T> {

    
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

   
    private Node<T> head;

    
    private Node<T> tail;

    
    private int size;

    
    public CustomLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }


   
    public int size() {
        return size;
    }

    
    public boolean isEmpty() {
        return size == 0;
    }


    public T get(int index) {
        return nodeAt(index).data;
    }

    
    public T set(int index, T element) {
        Node<T> node = nodeAt(index);
        T old = node.data;
        node.data = element;
        return old;
    }

    
    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return head.data;
    }

    
    public T getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.data;
    }


   
    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);
        newNode.next = head;
        head = newNode;
        if (tail == null) { // list was empty, so the new node is also the last
            tail = newNode;
        }
        size++;
    }

  
    public void addLast(T element) {
        Node<T> newNode = new Node<>(element);
        if (tail == null) { // empty list
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    
    public void add(T element) {
        addLast(element);
    }

    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == 0) {
            addFirst(element);
        } else if (index == size) {
            addLast(element);
        } else {
            Node<T> prev = nodeAt(index - 1);
            Node<T> newNode = new Node<>(element);
            newNode.next = prev.next; 
            prev.next = newNode;      
            size++;
        }
    }


   
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        T removed = head.data;
        head = head.next;
        if (head == null) { // list is now empty
            tail = null;
        }
        size--;
        return removed;
    }

   
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        if (size == 1) {
            return removeFirst();
        }
        Node<T> prev = nodeAt(size - 2);
        T removed = tail.data;
        prev.next = null;
        tail = prev;
        size--;
        return removed;
    }

   
    public T remove(int index) {
        checkIndex(index);
        if (index == 0) {
            return removeFirst();
        }
        if (index == size - 1) {
            return removeLast();
        }
        Node<T> prev = nodeAt(index - 1);
        Node<T> target = prev.next;
        prev.next = target.next; 
        size--;
        return target.data;
    }

    
    public boolean removeValue(Object element) {
        Node<T> prev = null;
        Node<T> current = head;
        while (current != null) {
            if (element == null ? current.data == null : element.equals(current.data)) {
                if (prev == null) {
                    removeFirst();
                } else {
                    prev.next = current.next;
                    if (current == tail) {
                        tail = prev;
                    }
                    size--;
                }
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }


    
    public int indexOf(Object element) {
        int i = 0;
        for (Node<T> current = head; current != null; current = current.next) {
            if (element == null ? current.data == null : element.equals(current.data)) {
                return i;
            }
            i++;
        }
        return -1;
    }

   
    public boolean contains(Object element) {
        return indexOf(element) != -1;
    }

    

    
    private Node<T> nodeAt(int index) {
        checkIndex(index);
        if (index == size - 1) {
            return tail; 
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            public boolean hasNext() {
                return current != null;
            }

            
            public T next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                T value = current.data;
                current = current.next;
                return value;
            }
        };
    }

   
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<T> current = head; current != null; current = current.next) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }
}
