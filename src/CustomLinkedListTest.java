import java.util.Random;

/**
 * Sanity tests for CustomLinkedList. Run with:
 *     javac CustomLinkedList.java CustomLinkedListTest.java
 *     java CustomLinkedListTest
 * Every check compares CustomLinkedList's behavior to java.util.LinkedList.
 */
public class CustomLinkedListTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testIntegers();
        testStrings();
        testDoubles();
        testEdgeCases();
        testRandomAgainstJavaApi();

        System.out.println("\nPassed: " + passed + "  Failed: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testIntegers() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        check("Integer size after 100 adds", list.size() == 100);
        check("Integer get(57)", list.get(57) == 57);

        list.addFirst(-1);
        list.add(50, 999);
        list.addLast(1000);
        check("addFirst", list.get(0) == -1 && list.getFirst() == -1);
        check("add middle", list.get(50) == 999);
        check("addLast", list.getLast() == 1000);

        check("removeFirst", list.removeFirst() == -1);
        check("remove middle", list.remove(49) == 999);
        check("removeLast", list.removeLast() == 1000);
        check("tail updated after removeLast", list.getLast() == 99);
        check("back to original", list.size() == 100 && list.get(0) == 0 && list.get(99) == 99);

        long sum = 0;
        for (int x : list) {
            sum += x;
        }
        check("for-each traversal sum", sum == 4950);
    }

    private static void testStrings() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.add("b");
        list.addFirst("a");
        list.add("c");
        check("String toString", list.toString().equals("[a, b, c]"));
        check("String contains", list.contains("b"));
        check("String indexOf", list.indexOf("c") == 2);
        check("String removeValue middle", list.removeValue("b") && list.toString().equals("[a, c]"));
        check("String removeValue last updates tail",
                list.removeValue("c") && list.getLast().equals("a"));
        list.add("d");
        check("add after removing tail", list.toString().equals("[a, d]"));
        check("String set", list.set(1, "z").equals("d") && list.get(1).equals("z"));
    }

    private static void testDoubles() {
        CustomLinkedList<Double> list = new CustomLinkedList<>();
        list.add(1.5);
        list.add(2.5);
        list.add(1, 2.0);
        check("Double middle insert", list.get(1) == 2.0 && list.size() == 3);
    }

    private static void testEdgeCases() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        check("get on empty throws", throwsException(() -> list.get(0)));
        check("removeFirst on empty throws", throwsException(list::removeFirst));
        check("removeLast on empty throws", throwsException(list::removeLast));

        list.add(1);
        check("removeLast on 1 element", list.removeLast() == 1 && list.isEmpty());
        list.addLast(5); // would crash if tail wasn't reset to null
        check("addLast after emptying", list.getFirst() == 5 && list.getLast() == 5);

        check("get negative throws", throwsException(() -> list.get(-1)));
        check("add past size throws", throwsException(() -> list.add(3, 7)));

        list.clear();
        check("clear empties list", list.isEmpty() && list.toString().equals("[]"));
    }

    /** 10,000 random operations, checked step by step against java.util.LinkedList. */
    private static void testRandomAgainstJavaApi() {
        CustomLinkedList<Integer> mine = new CustomLinkedList<>();
        java.util.LinkedList<Integer> theirs = new java.util.LinkedList<>();
        Random rng = new Random(42);
        boolean ok = true;

        for (int step = 0; step < 10_000 && ok; step++) {
            int op = rng.nextInt(6);
            if (op <= 1 || theirs.isEmpty()) {
                int idx = rng.nextInt(theirs.size() + 1);
                int val = rng.nextInt();
                mine.add(idx, val);
                theirs.add(idx, val);
            } else if (op == 2) {
                int idx = rng.nextInt(theirs.size());
                ok = mine.remove(idx).equals(theirs.remove(idx));
            } else if (op == 3) {
                ok = mine.removeLast().equals(theirs.removeLast());
            } else if (op == 4) {
                ok = mine.removeFirst().equals(theirs.removeFirst());
            } else {
                int idx = rng.nextInt(theirs.size());
                ok = mine.get(idx).equals(theirs.get(idx));
            }
            ok = ok && mine.size() == theirs.size()
                    && (theirs.isEmpty() || mine.getLast().equals(theirs.getLast()));
        }
        check("10,000 random ops match java.util.LinkedList", ok);
    }

    private static boolean throwsException(Runnable r) {
        try {
            r.run();
            return false;
        } catch (RuntimeException e) {
            return true;
        }
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            failed++;
            System.out.println("FAIL  " + name);
        }
    }
}
