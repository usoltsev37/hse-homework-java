package ru.hse.java.trie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Trie {

    /**
     * Expected complexity: O(|element|)
     *
     * @return <tt>true</tt> if this set did not already contain the specified
     * element
     */
    boolean add(@NotNull String element);

    /**
     * Expected complexity: O(|element|)
     */
    boolean contains(@NotNull String element);

    /**
     * Expected complexity: O(|element|)
     *
     * @return <tt>true</tt> if this set contained the specified element
     */
    boolean remove(@NotNull String element);

    /**
     * Expected complexity: O(1)
     */
    int size();

    /**
     * Expected complexity: O(|prefix|)
     */
    int howManyStartsWithPrefix(@NotNull String prefix);

    /**
     * Get String in trie, next after [element] up to k elements
     * Expected complexity: O(|trie depth|)
     *
     * @return found String or null
     */
    @Nullable
    String nextString(String element, int k);
}


