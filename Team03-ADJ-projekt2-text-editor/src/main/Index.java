import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * This class create all the index with the paragraphnumber, which belongs to the words.
 * The words must have a minimum of four paragraphnumbers.
 */
public class Index {

    private static final int FIRST_INDEX = 0;
    private final static int REAL_INDEX = 1;
    private final static int MIN_NUMBER = 4;
    private final static String WHITESPACE = " ";

    /**
     * @param linkedList is the list, containing all the strings that the user is editing.
     * @return a Hashmap containing indexes of words, being used more than 3 times. Used to print the index.
     */
    protected HashMap<String, HashSet<Integer>> getIndex(LinkedList<String> linkedList){
        HashMap<String, HashSet<Integer>> indexMap = new HashMap<>();
        for(int listIndex = FIRST_INDEX; listIndex < linkedList.size(); listIndex++){
            String [] value = linkedList.get(listIndex).split(WHITESPACE);
            for (int i = FIRST_INDEX; i < value.length; i++){
                if(Character.isUpperCase(value[i].charAt(FIRST_INDEX))){
                    HashSet<Integer> indexValue = indexMap.get(value[i]);
                    if(indexValue == null){
                        indexValue = new HashSet<>();
                    }
                    indexValue.add(listIndex + REAL_INDEX);
                    indexMap.put(value[i], indexValue);
                }
            }
        }

        ArrayList <String> filterList = new ArrayList<>();
        for (String key : indexMap.keySet()){
            if(indexMap.get(key).size() < MIN_NUMBER){
                filterList.add(key);
            }
        }

        for (String text : filterList) {
            indexMap.remove(text);
        }

        return indexMap;
    }
}