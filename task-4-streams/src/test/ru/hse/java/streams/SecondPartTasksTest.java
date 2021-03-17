package ru.hse.java.streams;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SecondPartTasksTest {

    String partAstoronomy = "src/test/ru/hse/java/streams/files/Astronomy.txt";
    String partAnyOtherDay = "src/test/ru/hse/java/streams/files/AnyOtherDay.txt";

    @Test()
    public void testFindQuotes() {
        List<String> paths = new ArrayList<>(Arrays.asList(partAstoronomy, partAnyOtherDay));
        try {
            assertEquals(
                    new ArrayList<>(Arrays.asList("Behind the clock back there you know",
                            "The clock strikes twelve and moon drops burst",
                            "Alarm clock rings, it's 6:45,", "Alarm clock rings, it's 6:45,")),
                    SecondPartTasks.findQuotes(paths, "clock")
            );
        } catch (IOException e) {
            assert(false);
        }
    }

    @Test
    public void testPiDividedBy4() {
        assertEquals(Math.PI / 4, SecondPartTasks.piDividedBy4(), 0.01);
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> compositions = new HashMap<>();
        compositions.put("1", Arrays.asList("a", "b"));
        compositions.put("2", Arrays.asList("aa", "bb"));
        compositions.put("3", Arrays.asList("aaa", "bbb", "ccc"));
        assertEquals("3", SecondPartTasks.findPrinter(compositions));
    }

    @Test
    public void testCalculateGlobalOrder() {
        List<Map<String, Integer>> orders = new ArrayList<>(Arrays.asList(new HashMap<>(), new HashMap<>()));
        orders.get(0).put("1", 1);
        orders.get(0).put("3", 111);
        orders.get(0).put("2", 928);
        orders.get(1).put("3", 42);
        orders.get(1).put("1", 1);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("1", 2);
        expected.put("2", 928);
        expected.put("3", 153);
        assertEquals(expected, SecondPartTasks.calculateGlobalOrder(orders));
    }
}