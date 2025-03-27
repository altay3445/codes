import java.util.*;
import java.io.*;

/*
 * A program that counts all the reserved words in a Java source code file and
 * prints the reserved words with their counts in alphabetical order. Java's
 * hash set and tree map implementations are used for efficiency.
 */
public class CountReservedWords {
   public static void main(String[] args) throws Exception {
      // read the name of the Java code file from the user (console)
      Scanner input = new Scanner(System.in);
      System.out.print("Enter the Java code file (e.g., HelloWorld.java): ");
      String filename = input.next().trim();
      input.close();

      // create a file by using the filename read from the user
      File file = new File(filename);
      // if the file exists
      if (file.exists()) {
         // get the map of reserved words and their counts
         TreeMap<String, Integer> reservedWordCounts = countReservedWords(file);

         // print the results
         reservedWordCounts.forEach((word, count) ->
                 System.out.println(word + " " + count));
      }
      // otherwise
      else {
         // notify the user that a file with a given filename does not exist
         System.out.println("The file " + filename + " does not exist.");
      }
   }

   // A method that counts all the reserved words in a given Java code file
   // while ignoring comments and string literals
   public static TreeMap<String, Integer> countReservedWords(File file) throws Exception {
      // Java reserved words
      String[] reservedWordArray = { "true", "false", "null", "abstract", "assert",
              "boolean", "break", "byte", "case", "catch", "char", "class", "continue",
              "default", "do", "double", "else", "enum", "extends", "final", "finally",
              "float", "for", "if", "implements", "import", "instanceof", "int",
              "interface", "long", "native", "new", "package", "private", "protected",
              "public", "return", "short", "static", "strictfp", "super", "switch",
              "synchronized", "this", "throw", "throws", "transient", "try", "void",
              "volatile", "while" };

      // Create a HashSet for quick lookup of reserved words
      Set<String> reservedWordSet = new HashSet<>(Arrays.asList(reservedWordArray));

      // Preprocess the file to remove comments and string literals
      String preprocessedCode = preprocessJavaCodeFile(file);

      // Split the preprocessed code into words
      String[] words = preprocessedCode.split("\\s+");

      // Create a TreeMap to store the occurrences of reserved words
      TreeMap<String, Integer> reservedWordCounts = new TreeMap<>();

      // Count occurrences of each reserved word
      for (String word : words) {
         if (reservedWordSet.contains(word)) {
            reservedWordCounts.put(word, reservedWordCounts.getOrDefault(word, 0) + 1);
         }
      }

      // Return the TreeMap containing the counts
      return reservedWordCounts;
   }

   // A helper method to preprocess the Java code file by removing comments and string literals
   public static String preprocessJavaCodeFile(File file) throws Exception {
      StringBuilder codeWithoutComments = new StringBuilder();
      Scanner input = new Scanner(file);
      boolean isBlockComment = false;

      while (input.hasNextLine()) {
         String line = input.nextLine();

         // Remove single-line comments
         int singleLineCommentIndex = line.indexOf("//");
         if (singleLineCommentIndex != -1) {
            line = line.substring(0, singleLineCommentIndex);
         }

         // Handle block comments
         if (isBlockComment) {
            int blockCommentEnd = line.indexOf("*/");
            if (blockCommentEnd != -1) {
               line = line.substring(blockCommentEnd + 2);
               isBlockComment = false;
            } else {
               continue; // Skip the entire line if still inside a block comment
            }
         }

         int blockCommentStart = line.indexOf("/*");
         if (blockCommentStart != -1) {
            isBlockComment = true;
            line = line.substring(0, blockCommentStart);
         }

         // Remove string literals
         line = line.replaceAll("\"(\\\\.|[^\"])*\"", "");

         // Append the cleaned line
         codeWithoutComments.append(line).append(" ");
      }

      input.close();
      return codeWithoutComments.toString();
   }
}
