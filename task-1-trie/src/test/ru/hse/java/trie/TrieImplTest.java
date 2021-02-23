package ru.hse.java.trie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class TrieImplTest {

    private TrieImpl trie;
    private TreeSet<String> corrTrie;
    private final int cntStress = 50000;
    private final int seedStress = 38371;
    Random rand;

    private final char[] alphabet;

    public TrieImplTest() {
        alphabet = new char[52];

        for (char c = 'A'; c <= 'Z'; c++) {
            alphabet[c - 'A'] = c;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            alphabet[c - 'A' - 6] = c;
        }
    }

    private char getElemAlphabet(int i) {
        return alphabet[i];
    }

    @BeforeEach
    void startTest() {
        trie = new TrieImpl();
    }

    @Test
    public void testAddSomeStrings() {
        Assertions.assertTrue(trie.add("Towards"));
        Assertions.assertTrue(trie.add("morning"));
        Assertions.assertTrue(trie.add("Warsaw"));
        Assertions.assertTrue(trie.add("speed"));
        Assertions.assertTrue(trie.add("damp"));
        Assertions.assertTrue(trie.add("difficulty"));
        Assertions.assertTrue(trie.add("distinguish"));
        Assertions.assertTrue(trie.add("anything"));

        Assertions.assertEquals(8, trie.size());
    }

    @Test
    public void testAddEqualsString() {
        Assertions.assertTrue(trie.add("Some"));
        Assertions.assertFalse(trie.add("Some"));
        Assertions.assertTrue(trie.add("fire"));
        Assertions.assertFalse(trie.add("fire"));

        Assertions.assertEquals(2, trie.size());
    }

    @Test
    public void testAddEmpty() {
        Assertions.assertTrue(trie.add(""));
        Assertions.assertEquals(1, trie.size());
    }

    @Test
    public void testContainsTrue() {
        trie.add("passengers");
        trie.add("");

        Assertions.assertTrue(trie.contains("passengers"));
        Assertions.assertTrue(trie.contains(""));
    }

    @Test
    public void testContainsFalse() {
        trie.add("paSsengersSomeThing");

        Assertions.assertFalse(trie.contains("paSsengers"));
        Assertions.assertFalse(trie.contains("nine"));
        Assertions.assertFalse(trie.contains(""));
    }

    @Test
    public void testRemoveTrue() {
        trie.add("paSsengersSomeThing");
        trie.add("Towards");
        trie.add("morning");
        trie.add("Warsaw");
        trie.add("speed");
        trie.add("damp");
        trie.add("difficulty");
        trie.add("distinguish");
        trie.add("");

        Assertions.assertTrue(trie.remove("damp"));
        Assertions.assertTrue(trie.remove("difficulty"));
        Assertions.assertTrue(trie.remove("Towards"));
        Assertions.assertTrue(trie.remove(""));

        Assertions.assertEquals(5, trie.size());
    }

    @Test
    public void testRemoveFalse() {
        trie.add("gogol");
        trie.add("Towards");
        trie.add("morning");
        trie.add("Warsaw");
        trie.add("lermontov");
        trie.add("damp");
        trie.add("difficulty");
        trie.add("distinguish");

        Assertions.assertFalse(trie.remove("noneWord"));
        Assertions.assertFalse(trie.remove("diff"));
        Assertions.assertFalse(trie.remove("T"));
        Assertions.assertFalse(trie.remove(""));

        Assertions.assertEquals(8, trie.size());
    }

    @Test
    public void testHowManyStartsWithPrefix() {
        trie.add("Petersburg");
        trie.add("PetersburgMoscow");
        trie.add("PetersburgMos");
        trie.add("PetersburgLove");
        trie.add("Moscowlove");
        trie.add("Moscow");
        trie.add("MoscowMSK");
        trie.add("PetersburgOl");
        trie.add("PetersburgQWERTY");
        trie.add("Petersburgqwerty");

        Assertions.assertEquals(7, trie.howManyStartsWithPrefix("Petersburg"));
        Assertions.assertEquals(7, trie.howManyStartsWithPrefix("P"));
        Assertions.assertEquals(2, trie.howManyStartsWithPrefix("PetersburgM"));
        Assertions.assertEquals(0, trie.howManyStartsWithPrefix("PetersburgNONE"));
        Assertions.assertEquals(0, trie.howManyStartsWithPrefix("none"));
        Assertions.assertEquals(3, trie.howManyStartsWithPrefix("Moscow"));
        Assertions.assertEquals(10, trie.howManyStartsWithPrefix(""));
    }

    @Test
    public void testNextStringNull() {
        trie.add("paSsengersSomeThing");
        trie.add("Towards");
        trie.add("morning");
        trie.add("Warsaw");
        trie.add("speed");
        trie.add("damp");
        trie.add("difficulty");
        trie.add("distinguish");
        trie.add("");

        Assertions.assertNull(trie.nextString("noneElem", 0));
    }

    @Test
    public void testNextStringCommon() {
        trie.add("aa");
        trie.add("aaab");
        trie.add("aaac");
        trie.add("aab");
        trie.add("");
        trie.add("aac");
        trie.add("aacx");
        trie.add("aacz");

        Assertions.assertEquals("", trie.nextString("", 0));
        Assertions.assertEquals("aa", trie.nextString("", 1));
        Assertions.assertEquals("aaac", trie.nextString("aa", 2));
        Assertions.assertEquals("aac", trie.nextString("aa", 4));
    }

    @Test
    public void testNextStringExistWordByValidStep2() {
        trie.add("a");  // 1
        trie.add("aa"); // 2

        // left part
        trie.add("aab"); // 3

        // right part
        trie.add("aad"); // 4
        trie.add("aae"); // 5
        trie.add("aaf"); // 6

        Assertions.assertEquals("aad", trie.nextString("aac", 1));
        Assertions.assertEquals("aae", trie.nextString("aac", 2));
        Assertions.assertEquals("aad", trie.nextString("", 4));
    }



    @BeforeEach
    void startTestStress() {
        corrTrie = new TreeSet<>();
        rand = new Random(seedStress);
    }

    @Test
    public void testStressAdd() {
        for (int cnt = 0; cnt < cntStress; cnt++) {
            StringBuilder strBuilder = new StringBuilder();
            for (int i = cnt % 20; i < 20; i++) {
                strBuilder.append(getElemAlphabet(rand.nextInt(51)));
            }
            Assertions.assertEquals(corrTrie.add(strBuilder.toString()), trie.add(strBuilder.toString()));
        }
    }

    @Test
    public void testStressContains() {
        for (int cnt = 0; cnt < cntStress; cnt++) {
            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                strBuilder.append(getElemAlphabet(rand.nextInt(51)));
            }
            trie.add(strBuilder.toString());
            corrTrie.add(strBuilder.toString());
        }

        rand = new Random(seedStress + 199);
        for (int cnt = 0; cnt < cntStress; cnt++) {
            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                strBuilder.append(getElemAlphabet(rand.nextInt(51)));
            }
            Assertions.assertEquals(corrTrie.contains(strBuilder.toString()), trie.contains(strBuilder.toString()));
        }
    }

    @Test
    public void testStressSize() {
        for (int cnt = 0; cnt < cntStress; cnt++) {
            StringBuilder strBuilder = new StringBuilder();
            for (int i = cnt % 5; i < 5; i++) {
                strBuilder.append(getElemAlphabet(rand.nextInt(51)));
            }
            trie.add(strBuilder.toString());
            corrTrie.add(strBuilder.toString());
        }
        Assertions.assertEquals(corrTrie.size(), trie.size());
    }

    @Test
    public void testStressFull() {
        Set<String> correct = new TreeSet<>();
        for (int cnt = 0; cnt < cntStress; cnt++) {
            int command = rand.nextInt(4);
            StringBuilder strBuilder = new StringBuilder();
            for (int i = cnt % 5; i < 5; i++) {
                strBuilder.append(getElemAlphabet(rand.nextInt(51)));
            }
            String element = strBuilder.toString();
            System.out.println(command);
            if(command == 0) {
                Assertions.assertEquals(correct.size(), trie.size());
            } else if(command == 1) {
                Assertions.assertEquals(correct.add(element), trie.add(element));
            } else if(command == 2) {
                Assertions.assertEquals(correct.remove(element), trie.remove(element));
            } else {
                Assertions.assertEquals(correct.contains(element), trie.contains(element));
            }
        }
    }
}
