import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

public class StringBenchmarkRunner {
  private static final int SIZE = 100_000;
  private static final int LOOKUPS = 1_000;
  private static final int TRIALS = 10;

  private static volatile long checksum;

  public static void main(String[] args) {
    System.out.println("String list benchmarks");

    CustomArrayList<String> customArray = new CustomArrayList<>();
    CustomLinkedList<String> customLinked = new CustomLinkedList<>();
    ArrayList<String> javaArray = new ArrayList<>();
    LinkedList<String> javaLinked = new LinkedList<>();

    for (int i = 0; i < SIZE; i++) {
      String item = "Item " + i;
      customArray.add(item);
      customLinked.add(item);
      javaArray.add(item);
      javaLinked.add(item);
    }

    Random random = new Random(42);
    int[] indexes = new int[LOOKUPS];
    for (int i = 0; i < indexes.length; i++) {
      indexes[i] = random.nextInt(SIZE);
    }

    benchmarkRandomAccess("Custom ArrayList", customArray::get, indexes);
    benchmarkRandomAccess("Custom LinkedList", customLinked::get, indexes);
    benchmarkRandomAccess("Java ArrayList", javaArray::get, indexes);
    benchmarkRandomAccess("Java LinkedList", javaLinked::get, indexes);

    System.out.println("\nSequential traversal");

    benchmarkTraversal("Custom ArrayList", customArray::forEach, customArray.size());
    benchmarkTraversal("Custom LinkedList", customLinked::forEach, customLinked.size());
    benchmarkTraversal("Java ArrayList", javaArray::forEach, javaArray.size());
    benchmarkTraversal("Java LinkedList", javaLinked::forEach, javaLinked.size());

    System.out.println("\nInsert front");
    benchmarkInsert("Custom ArrayList", "front", 0, customArray::add, customArray::remove);
    benchmarkInsert("Custom LinkedList", "front", 0, customLinked::add, customLinked::remove);
    benchmarkInsert("Java ArrayList", "front", 0, javaArray::add, javaArray::remove);
    benchmarkInsert("Java LinkedList", "front", 0, javaLinked::add, javaLinked::remove);

    System.out.println("\nInsert middle");
    benchmarkInsert("Custom ArrayList", "middle", customArray.size() / 2, customArray::add, customArray::remove);
    benchmarkInsert("Custom LinkedList", "middle", customLinked.size() / 2, customLinked::add, customLinked::remove);
    benchmarkInsert("Java ArrayList", "middle", javaArray.size() / 2, javaArray::add, javaArray::remove);
    benchmarkInsert("Java LinkedList", "middle", javaLinked.size() / 2, javaLinked::add, javaLinked::remove);

    System.out.println("\nInsert end");
    benchmarkInsert("Custom ArrayList", "end", customArray.size(), customArray::add, customArray::remove);
    benchmarkInsert("Custom LinkedList", "end", customLinked.size(), customLinked::add, customLinked::remove);
    benchmarkInsert("Java ArrayList", "end", javaArray.size(), javaArray::add, javaArray::remove);
    benchmarkInsert("Java LinkedList", "end", javaLinked.size(), javaLinked::add, javaLinked::remove);

    System.out.println("\nRemove front");
    benchmarkRemove("Custom ArrayList", "front", 0, customArray::remove, customArray::add);
    benchmarkRemove("Custom LinkedList", "front", 0, customLinked::remove, customLinked::add);
    benchmarkRemove("Java ArrayList", "front", 0, javaArray::remove, javaArray::add);
    benchmarkRemove("Java LinkedList", "front", 0, javaLinked::remove, javaLinked::add);

    System.out.println("\nRemove middle");
    benchmarkRemove("Custom ArrayList", "middle", customArray.size() / 2, customArray::remove, customArray::add);
    benchmarkRemove("Custom LinkedList", "middle", customLinked.size() / 2, customLinked::remove, customLinked::add);
    benchmarkRemove("Java ArrayList", "middle", javaArray.size() / 2, javaArray::remove, javaArray::add);
    benchmarkRemove("Java LinkedList", "middle", javaLinked.size() / 2, javaLinked::remove, javaLinked::add);

    System.out.println("\nRemove end");
    benchmarkRemove("Custom ArrayList", "end", customArray.size() - 1, customArray::remove, customArray::add);
    benchmarkRemove("Custom LinkedList", "end", customLinked.size() - 1, customLinked::remove, customLinked::add);
    benchmarkRemove("Java ArrayList", "end", javaArray.size() - 1, javaArray::remove, javaArray::add);
    benchmarkRemove("Java LinkedList", "end", javaLinked.size() - 1, javaLinked::remove, javaLinked::add);

  }

  private static void benchmarkRandomAccess(
      String name, IntFunction<String> get, int[] indexes) {
    for (int i = 0; i < 5; i++) {
      timeLookups(get, indexes); // Warm up before recording trials.
    }

    long totalNanos = 0;
    for (int trial = 0; trial < TRIALS; trial++) {
      totalNanos += timeLookups(get, indexes);
    }

    double nanosPerLookup = (double) totalNanos / (TRIALS * indexes.length);
    System.out.printf("%-18s %10.1f ns per lookup%n", name, nanosPerLookup);
  }

  private static long timeLookups(IntFunction<String> get, int[] indexes) {
    long sum = 0;
    long start = System.nanoTime();

    for (int index : indexes) {
      sum += get.apply(index).length();
    }

    long elapsed = System.nanoTime() - start;
    checksum = sum; // Keep the retrieved values observable.
    return elapsed;
  }

  private static void benchmarkTraversal(
      String name, Consumer<Consumer<String>> traverse, int size) {
    for (int i = 0; i < 5; i++) {
      timeTraversal(traverse);
    }

    long totalNanos = 0;
    for (int trial = 0; trial < TRIALS; trial++) {
      totalNanos += timeTraversal(traverse);
    }

    double nanosPerElement = (double) totalNanos / ((long) TRIALS * size);
    System.out.printf("%-18s %10.1f ns per element%n", name, nanosPerElement);
  }

  private static long timeTraversal(Consumer<Consumer<String>> traverse) {
    long[] sum = { 0 };
    Consumer<String> addToSum = value -> sum[0] += value.length();

    long start = System.nanoTime();
    traverse.accept(addToSum);
    long elapsed = System.nanoTime() - start;

    checksum = sum[0];
    return elapsed;
  }

  private static void benchmarkInsert(
      String name, String position, int index,
      BiConsumer<Integer, String> insert, IntConsumer remove) {
    String value = "Inserted item";

    for (int i = 0; i < 5; i++) {
      insert.accept(index, value);
      remove.accept(index);
    }

    long totalNanos = 0;
    for (int trial = 0; trial < TRIALS; trial++) {
      long start = System.nanoTime();
      insert.accept(index, value);
      totalNanos += System.nanoTime() - start;

      remove.accept(index);
    }

    double nanosPerInsert = (double) totalNanos / TRIALS;
    System.out.printf("%s %s insert: %10.1f ns per insertion%n",
        name, position, nanosPerInsert);
  }

  private static void benchmarkRemove(
      String name, String position, int index,
      IntFunction<String> remove, BiConsumer<Integer, String> insert) {
    for (int i = 0; i < 5; i++) {
      String removed = remove.apply(index);
      insert.accept(index, removed);
    }

    long totalNanos = 0;
    for (int trial = 0; trial < TRIALS; trial++) {
      long start = System.nanoTime();
      String removed = remove.apply(index);
      totalNanos += System.nanoTime() - start;

      insert.accept(index, removed);
    }

    double nanosPerRemoval = (double) totalNanos / TRIALS;
    System.out.printf("%s %s remove: %10.1f ns per removal%n",
        name, position, nanosPerRemoval);
  }
}
