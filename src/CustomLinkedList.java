import java.util.function.Consumer;

public class CustomLinkedList<T> {
  private static class Node<T> {
    T value;
    Node<T> next;

    Node(T value) {
      this.value = value;
    }
  }

  private Node<T> head;
  private Node<T> tail;
  private int size;

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return this.size == 0;
  }

  public boolean add(T element) {
    Node<T> newNode = new Node<>(element);

    if (isEmpty()) {
      head = newNode;
    } else {
      tail.next = newNode;
    }

    tail = newNode;
    size++;
    return true;
  }

  public T get(int index) {
    checkElementIndex(index);
    Node<T> current = head;

    for (int i = 0; i < index; i++) {
      current = current.next;
    }

    return current.value;
  }

  public void forEach(Consumer<? super T> action) {
    Node<T> current = head;
    while (current != null) {
      action.accept(current.value);
      current = current.next;
    }
  }

  public void add(int index, T element) {
    checkPositionIndex(index);
    if (index == size) {
      add(element);
      return;
    } else if (index == 0) {
      Node<T> newNode = new Node<>(element);
      newNode.next = head;
      head = newNode;
      size++;
    } else {
      Node<T> newNode = new Node<>(element);
      Node<T> previousNode = head;

      for (int i = 0; i < index - 1; i++) {
        previousNode = previousNode.next;
      }
      newNode.next = previousNode.next;
      previousNode.next = newNode;
      size++;
    }
  }

  public T remove(int index) {
    checkElementIndex(index);
    Node<T> removed;
    if (index == 0) {
      removed = head;
      head = head.next;
      if (head == null) {
        tail = null;
      }
    } else {
      Node<T> previousNode = head;
      for (int i = 0; i < index - 1; i++) {
        previousNode = previousNode.next;
      }
      removed = previousNode.next;
      previousNode.next = removed.next;
      if (removed == tail) {
        tail = previousNode;
      }
    }
    size--;
    return removed.value;
  }

  private void checkElementIndex(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(
          "Index: " + index + ", Size: " + size);
    }
  }

  private void checkPositionIndex(int index) {
    if (index < 0 || index > size) {
      throw new IndexOutOfBoundsException(
          "Index: " + index + ", Size: " + size);
    }
  }
}
