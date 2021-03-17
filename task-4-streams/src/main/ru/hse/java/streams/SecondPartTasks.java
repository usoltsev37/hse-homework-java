package ru.hse.java.streams;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) throws IOException {
        try{
            return paths.stream().
                    flatMap(
                            path -> {
                                try {
                                    return Files.lines(Paths.get(path));
                                } catch (IOException e) {
                                    throw new UncheckedIOException(e);
                                }
                            }
                    ).
                    filter(s -> s.contains(sequence)).
                    collect(Collectors.toList());
        } catch (UncheckedIOException e) {
            throw new IOException(e);
        }

    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        return piDividedBy4(10000);
    }

    public static double piDividedBy4(int n) {
        double diameter = 1.0;
        Random rand = new Random(37);
        return Stream.iterate(1, x -> x + 1).
                limit(n).
                filter(temp -> {
                            double x = ((rand.nextDouble() % diameter) + diameter) % diameter;
                            double y = ((rand.nextDouble() % diameter) + diameter) % diameter;
                            assert(x >= 0);
                            assert(y >= 0);
                            return (x - diameter / 2) * (x - diameter / 2) + (y - diameter / 2) * (y - diameter / 2) < diameter / 4;
                        }
                        ).
                count() / (double)n;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream().
                map(entry ->
                    new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().size())
                ).
                max(Comparator.comparing(Map.Entry::getValue)).
                map(Map.Entry::getKey).
                orElse(null);
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().
                flatMap(m -> m.entrySet().stream()).
                collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                ));
    }
}