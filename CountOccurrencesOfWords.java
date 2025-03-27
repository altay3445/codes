import java.util.Map; // Java's own map interface
import java.util.TreeMap; // one of Java's own map implementations

/*
 * A program to count the occurrences of words in a string and print the words
 * in alphabetical order with the number of the occurrences for each word.
 * 
 * It uses one of Java's own map implementations (java.util.TreeMap) which is 
 * based on a balanced search tree. (TreeMap is efficient for traversing the keys 
 * in the map in a sorted order.)
 */
public class CountOccurrencesOfWords {
   public static void main(String[] args) {
      // the text is given as a string
      String text = "Good morning. Have a good day. Have a good class. Have fun!";

      // create a tree map to hold the words as the keys (String) and their counts
      // as the values (Integer) sorted in an alphabetical order of the keys
      Map<String, Integer> map = new TreeMap<>();

      // replace each punctuation character in the text with a space
      text = text.replaceAll("\\p{P}", " ");

      // split the text into words
      // by using one or more whitespaces (\s+) as a delimiter
      String[] words = text.split("\s+");

      // for each word
      for (int i = 0; i < words.length; i++) {
         // the key for the current word is the word itself in lowercase
         // (this way "Good" is treated the same as "good")
         String key = words[i].toLowerCase();
         // if the key is not in the map
         if (!map.containsKey(key))
            // put an entry for the key with a value = 1 (count) into the map
            map.put(key, 1);
         // otherwise (the key is already in the map)
         else {
            // get the value (count) stored in the map for the key
            int value = map.get(key);
            // increase the value (the count for the current word) by 1
            value++;
            // update the corresponding entry in the map
            map.put(key, value);
         }
      }

      // print the key (word) and the value (count) for each entry in the map
      // in alphabetical order of the words (as the used map is a tree map)
      // by using the forEach method of the Map interface
      map.forEach((k, v) -> System.out.println(k + "\t" + v));
   }
}