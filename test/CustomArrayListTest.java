public class CustomArrayListTest {
  public static void main(String[] args) {
    testEmptyAndAppend();
    testResize();
    testIndexedInsertionAndRemoval();
    testSet();
    testInvalidIndexes();
    System.out.println("All ArrayList checks passed.");
  }

  private static void testEmptyAndAppend() {
    CustomArrayList<Integer> list = new CustomArrayList<>();

    check(list.isEmpty(), "A new list should be empty");
    check(list.size() == 0, "A new list should have size 0");

    list.add(42);
    check(!list.isEmpty(), "The list should no longer be empty");
    check(list.size() == 1, "Adding one value should make the size 1");
    check(Integer.valueOf(42).equals(list.get(0)),
        "Index 0 should contain 42");
  }

  private static void testResize() {
    CustomArrayList<Integer> listResize = new CustomArrayList<>();

    for (int i = 0; i <= 10; i++) {
      listResize.add(i);
    }

    for (int i = 0; i <= 10; i++) {
      check(Integer.valueOf(i).equals(listResize.get(i)),
          "Index " + i + " should contain " + i);
    }

    check(listResize.size() == 11, "Adding 11 values should make size 11");
  }

  private static void testIndexedInsertionAndRemoval() {
    CustomArrayList<Integer> list = new CustomArrayList<>();
    list.add(0, 20);
    list.add(0, 10);
    list.add(1, 15);
    list.add(list.size(), 30);
    checkContents(list, 10, 15, 20, 30);

    check(Integer.valueOf(15).equals(list.remove(1)), "Remove should return 15");
    checkContents(list, 10, 20, 30);
    check(Integer.valueOf(10).equals(list.remove(0)), "Remove should return 10");
    checkContents(list, 20, 30);
    check(Integer.valueOf(30).equals(list.remove(list.size() - 1)), "Remove should return 30");
    checkContents(list, 20);
    check(Integer.valueOf(20).equals(list.remove(0)), "Remove should return 20");
    checkContents(list);
    check(list.isEmpty(), "Removing the only element should empty the list");
  }

  private static void testSet() {
    CustomArrayList<String> list = new CustomArrayList<>();
    list.add("old");
    check("old".equals(list.set(0, "new")), "Set should return the previous value");
    check("new".equals(list.get(0)), "Set should store the replacement value");
    check(list.size() == 1, "Set should leave the size unchanged");
  }

  private static void testInvalidIndexes() {
    CustomArrayList<Integer> list = new CustomArrayList<>();
    expectIndexError(() -> list.get(0), "Get from an empty list");
    list.add(42);
    expectIndexError(() -> list.get(-1), "Get at a negative index");
    expectIndexError(() -> list.get(list.size()), "Get at size");
    expectIndexError(() -> list.set(list.size(), 99), "Set at size");
    expectIndexError(() -> list.remove(list.size()), "Remove at size");
    expectIndexError(() -> list.add(-1, 99), "Insert at a negative index");
    expectIndexError(() -> list.add(list.size() + 1, 99), "Insert past size");
    checkContents(list, 42);
  }

  private static void checkContents(CustomArrayList<Integer> list, int... expected) {
    check(list.size() == expected.length, "Expected size " + expected.length);
    for (int i = 0; i < expected.length; i++) {
      check(Integer.valueOf(expected[i]).equals(list.get(i)),
          "Index " + i + " should contain " + expected[i]);
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
