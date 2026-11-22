public class CustomLinkedListTest {
  public static void main(String[] args) {
    testIndexedInsertionAndRemoval();
    testAppendAfterRemoval();
    testInvalidIndexes();
    System.out.println("All LinkedList checks passed.");
  }

  private static void testIndexedInsertionAndRemoval() {
    CustomLinkedList<String> list = new CustomLinkedList<>();
    check(list.isEmpty(), "A new list should be empty");
    checkContents(list);

    list.add(0, "C");
    list.add(0, "A");
    list.add(1, "B");
    list.add(list.size(), "D");
    check(!list.isEmpty(), "The list should no longer be empty");
    checkContents(list, "A", "B", "C", "D");

    check("B".equals(list.remove(1)), "Remove should return B");
    checkContents(list, "A", "C", "D");
    check("A".equals(list.remove(0)), "Remove should return A");
    checkContents(list, "C", "D");
  }

  private static void testAppendAfterRemoval() {
    CustomLinkedList<String> list = new CustomLinkedList<>();
    check(list.add("A"), "Append should return true");
    list.add("B");
    check("B".equals(list.remove(list.size() - 1)), "Remove should return the last value");
    checkContents(list, "A");
    list.add("C");
    checkContents(list, "A", "C");

    check("A".equals(list.remove(0)), "Remove should return the first value");
    checkContents(list, "C");
    check("C".equals(list.remove(0)), "Remove should return the only value");
    checkContents(list);
    check(list.isEmpty(), "Removing the only element should empty the list");
    list.add("D");
    list.add("E");
    checkContents(list, "D", "E");
  }

  private static void testInvalidIndexes() {
    CustomLinkedList<String> list = new CustomLinkedList<>();
    expectIndexError(() -> list.remove(0), "Remove from an empty list");
    list.add("A");
    expectIndexError(() -> list.get(-1), "Get at a negative index");
    expectIndexError(() -> list.get(list.size()), "Get at size");
    expectIndexError(() -> list.remove(list.size()), "Remove at size");
    expectIndexError(() -> list.add(-1, "B"), "Insert at a negative index");
    expectIndexError(() -> list.add(list.size() + 1, "B"), "Insert past size");
    checkContents(list, "A");
  }

  private static void checkContents(CustomLinkedList<String> list, String... expected) {
    check(list.size() == expected.length, "Expected size " + expected.length);
    for (int i = 0; i < expected.length; i++) {
      check(expected[i].equals(list.get(i)), "Index " + i + " should contain " + expected[i]);
    }
  }

  private static void expectIndexError(Runnable operation, String description) {
    try {
      operation.run();
    } catch (IndexOutOfBoundsException expected) {
      return;
    }
    throw new AssertionError(description + " should throw IndexOutOfBoundsException");
  }

  private static void check(boolean condition, String message) {
    if (!condition) {
      throw new AssertionError(message);
    }
  }
}
