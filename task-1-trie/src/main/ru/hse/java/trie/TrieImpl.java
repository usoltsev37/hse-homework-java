package ru.hse.java.trie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.String;
import java.util.Map;

public class TrieImpl implements Trie {

    private static class Vertex {
        private int size;
        private boolean isFinal;
        private final HashMap<Character, Vertex> nextVertex = new HashMap<>();


        private Vertex getOrCreate(Character elem) {
            if (!nextVertex.containsKey(elem)) {
                nextVertex.put(elem, new Vertex());
            }
            return nextVertex.get(elem);
        }

        private Vertex move(Character elem) {
            return nextVertex.get(elem);
        }

    }

    private final Vertex root;

    public TrieImpl() {
        root = new Vertex();
    }

    private static void checkCorrectString(String string) throws IllegalArgumentException {
        if (string == null || !string.matches("^[A-Za-z]*$")) {
            throw new IllegalArgumentException("Trie only supports Latin letters in both registers(with empty string)");
        }
    }

    @Override
    public boolean add(@NotNull String element) {
        try {
            checkCorrectString(element);
            if (contains(element)) {
                return false;
            }
            Vertex currVertex = root;
            currVertex.size++;
            for (char el : element.toCharArray()) {
                currVertex = currVertex.getOrCreate(el);
                currVertex.size++;
            }
            currVertex.isFinal = true;
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }

    }

    @Override
    public boolean contains(@NotNull String element) {
        Vertex currVertex = root;
        for (char el : element.toCharArray()) {
            currVertex = currVertex.move(el);
            if (currVertex == null) {
                return false;
            }
        }
        return currVertex.isFinal;
    }

    @Override
    public boolean remove(@NotNull String element) {
        if (!contains(element)) {
            return false;
        }
        Vertex currVertex = root;
        currVertex.size--;
        for (char el : element.toCharArray()) {
            if(currVertex.move(el).size == 1) {
                currVertex.nextVertex.remove(el);
                return true;
            }
            currVertex = currVertex.move(el);
            currVertex.size--;
        }
        currVertex.isFinal = false;
        return true;
    }

    @Override
    public int size() {
        return root.size;
    }

    @Override
    public int howManyStartsWithPrefix(@NotNull String prefix) {
        Vertex currVertex = root;
        for (char c : prefix.toCharArray()) {
            currVertex = currVertex.move(c);
            if(currVertex == null) {
                return 0;
            }
        }
        return currVertex.size;
    }

    @Override @Nullable
    public String nextString(String element, int k) {
        if(k < 0) {
            throw new IllegalArgumentException("Only not negative k!!");
        }
        if (!contains(element) && k == 0) {
            return null;
        } else if (k == 0) {
            return element;
        } else if (contains(element)) {
            int n = findNumString(element);
            return kStatistic(n + k);
        } else {
            add(element);
            int n = findNumString(element);
            String result = kStatistic(n + k);
            remove(element);
            return result;
        }
    }


    private String kStatistic(int k) {
        StringBuilder resultStrBuilder = new StringBuilder();
        Vertex currVertex = root;
        if (this.contains("")) {
            k--;
        }
        while (k != 0) {
            char newSymbol = '\0';

            for (Map.Entry<Character, Vertex> elem : currVertex.nextVertex.entrySet()) {
                Vertex childVertex = elem.getValue();
                if (childVertex.size < k) {
                    k -= childVertex.size;
                } else {
                    if (childVertex.isFinal) {
                        k--;
                    }
                    newSymbol = elem.getKey();
                    break;
                }
            }
            if (newSymbol == '\0') {
                break;
            }
            resultStrBuilder.append(newSymbol);
            currVertex = currVertex.move(newSymbol);
        }
        return resultStrBuilder.toString();
    }


    private int findNumString(String element) {
        if (!contains(element)) {
            return 0;
        }
        int num = 1;
        Vertex currVertex = root;
        for (char c : element.toCharArray()) {
            if (currVertex.isFinal) {
                num++;
            }
            for (Map.Entry<Character, Vertex> elem : currVertex.nextVertex.entrySet()) {
                char cLower = elem.getKey();
                if (cLower >= c) {
                    break;
                }
                if (currVertex.move(cLower) == null) {
                    continue;
                }
                num += currVertex.move(cLower).size;
            }

            currVertex = currVertex.move(c);
        }
        return num;
    }
}
