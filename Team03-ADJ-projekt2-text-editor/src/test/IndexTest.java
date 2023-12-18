import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * This class tests the Index to see if it works correctly!
 */
class IndexTest {

    private Index index = new Index();
    private HashMap<String, HashSet<Integer>> expectedIndex = new HashMap<>();
    private HashSet<Integer> listHallo = new HashSet<>();
    private HashSet<Integer> listJuvan = new HashSet<>();
    private HashSet<Integer> listFifa = new HashSet<>();
    private LinkedList<String> linkedList = new LinkedList<>();

    /**
     * This method initialize all the lists that will be used.
     * The linked-list will be edited with the method index.getIndex() and returns the index.
     */
    @BeforeEach
    void setUp() {
        listHallo.add(1);
        listHallo.add(2);
        listHallo.add(4);
        listHallo.add(5);
        listHallo.add(8);
        expectedIndex.put("Hallo", listHallo);

        listFifa.add(2);
        listFifa.add(4);
        listFifa.add(7);
        listFifa.add(9);
        expectedIndex.put("Fifa", listFifa);

        listJuvan.add(1);
        listJuvan.add(3);
        listJuvan.add(7);
        listJuvan.add(8);
        listJuvan.add(9);
        expectedIndex.put("Juvan", listJuvan);

        linkedList.add(0, "Hallo fifa ich heisse Juvan");
        linkedList.add(1, "Fifa Stefan Hallo");
        linkedList.add(2, "Juvan hey deutsch");
        linkedList.add(3, "Hallo stefan Fifa");
        linkedList.add(4, "Hallo FiFa");
        linkedList.add(5, "FIFA");
        linkedList.add(6, "HeisSE Aleks Juvan Fifa");
        linkedList.add(7, "Dominique Fiffa Juvan Hallo");
        linkedList.add(8, "Juvan Juvan Fifa");
    }

    /**
     * Valid equivalence class 1: Valid Index!
     * expectedIndex and linkedList: Hallo: 1, 2, 4, 5, 8
     * expectedIndex and linkedList: Fifa: 2, 4, 7, 9
     * expectedIndex and linkedList: Juvan: 1, 3, 7, 8, 9
     */
    @Test
    void testGetIndex(){
        assertEquals(expectedIndex, index.getIndex(linkedList));
    }

    /**
     * Valid equivalence class 2: Tests, if linkedList and the expectedIndex are empty!
     * linkedList = empty
     * expectedIndex = empty
     */
    @Test
    void emptyList(){
        expectedIndex.clear();
        linkedList.clear();
        assertEquals(expectedIndex, index.getIndex(linkedList));
    }

    /**
     *  Valid equivalence class 3: the size must be equal.
     *  linkedList.size = 3
     *  expectedIndex = 3;
     */
    @Test
    void testIndexSize(){
        assertEquals(expectedIndex.size(), index.getIndex(linkedList).size());
    }

    /**
     * Valid equivalence class 4: Index should not have two same rows, even if the word exists twice in this row.
     * expectedIndex Juvan: 1, 3, 7, 8, 9
     * linkedList Juvan: 1, 3, 7, 8, 9
     */
    @Test
    void invalidTwoWords(){
        listJuvan.add(9);
        expectedIndex.put("Juvan", listJuvan);
        assertEquals(expectedIndex, index.getIndex(linkedList));
    }

    /**
     * Invalid equivalence class 5: "Fiffa" is not the same word as "Fifa", so it's not an issue for the index, which belongs to "Fifa".
     * expectedIndex Fifa: 2, 4, 7, 8, 9
     * linkedList Fifa: 2, 4, 7, 9
     */
    @Test
    void invalidGetIndexFiffa(){
        listFifa.add(8);
        assertNotEquals(expectedIndex, index.getIndex(linkedList));
    }

    /**
     * Invalid equivalence class 6: Index only uses word that occur more than three times.
     * expectedIndex Stefan: 2, 3
     * linkedList Stefan = null
     */
    @Test
    void invalidWord(){
        HashSet<Integer> listStefan = new HashSet<>();
        listStefan.add(2);
        listStefan.add(3);
        expectedIndex.put("Stefan", listStefan);
        assertNotEquals(expectedIndex, index.getIndex(linkedList));
    }

    /**
     * Invalid equivalence class 7: Index only contains words starting with uppercase letters. Fifa in the first row begins with a lowercase "f"!
     * expectedIndex Fifa: 1, 2, 4, 7, 9
     * linkedList Fifa: 2, 4, 7, 9
     */
    @Test
    void wordUppercase(){
        listFifa.add(1);
        assertNotEquals(expectedIndex, index.getIndex(linkedList));
    }
}