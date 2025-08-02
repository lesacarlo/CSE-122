// Carlo Lesaca
// 07/31/25
// CSE 123
// P2: Absurdle
// TA:  Nicole Ham
// This program runs a game called "Absurdle," a word guessing game similar to Wordle.
// Unlike Wordle, where the answer is fixed, Absurdle changes the target word based on 
// the user's guesses, trying to
// prolong the game as much as possible.
// in this program, user also inputs the length of the word they would like to guess,
// as well as a dictionary file
// that cointains the words that can be used in the game. 

import java.util.*;
import java.io.*;

public class Absurdle  {
    public static final String GREEN = "🟩";
    public static final String YELLOW = "🟨";
    public static final String GRAY = "⬜";

    // [[ ALL OF MAIN PROVIDED ]]
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to the game of Absurdle.");

        System.out.print("What dictionary would you like to use? ");
        String dictName = console.next();

        System.out.print("What length word would you like to guess? ");
        int wordLength = console.nextInt();

        List<String> contents = loadFile(new Scanner(new File(dictName)));
        Set<String> dictionary = pruneDictionary(contents, wordLength);

        List<String> guessedPatterns = new ArrayList<>();
        while (!isFinished(guessedPatterns)) {
            System.out.print("> ");
            String guess = console.next();
            String pattern = recordGuess(guess, dictionary, wordLength);
            guessedPatterns.add(pattern);
            System.out.println(": " + pattern);
            System.out.println();
        }
        System.out.println("Absurdle " + guessedPatterns.size() + "/∞");
        System.out.println();
        printPatterns(guessedPatterns);
    }

    //B: This method prints out all the patterns of the user's guesses 
    //   after they complete the game.
    //P: A List<String> named patterns, which contains all the patterns
    //   of the words that the user has guessed
    public static void printPatterns(List<String> patterns) {
        for (String pattern : patterns) {
            System.out.println(pattern);
        }
    }

    //B: This method determines if the User has completed the game. 
    //   The method checks if any patterns have 
    //   been displayed (indicating the start of a game), or if the last pattern 
    //   is contains white or yellow (indicating 
    //   that the last guess was not correct).
    //R: Returns true if the game is finished, meaning the user guessed the word. Returns
    //   false otherwise.
    //P: A List<String>  named patterns, which contains all the 
    //   patterns of the words that the user has guessed
    public static boolean isFinished(List<String> patterns) {
        if (patterns.isEmpty()) {
            return false;
        }
        String lastPattern = patterns.get(patterns.size() - 1);
        return !lastPattern.contains("⬜") && !lastPattern.contains("🟨");
    }

    //B: This method reads a given file, and adds it into a List<String>
    //R: Returns the List<String> after we read the entire file
    //P: Scanner dictScan: contains file contents
    public static List<String> loadFile(Scanner dictScan) {
        List<String> contents = new ArrayList<>();
        while (dictScan.hasNext()) {
            contents.add(dictScan.next());
        }
        return contents;
    }

    //B: This method edits the list of words in the List<String> contents to a set of 
    //   words with a specified word length
    //E: If the word length is zero, we throw an IllegalArgumentException
    //R: Returns the edited set of words into a Set<String>
    //P: List<String> contents to obtain the original set of words, 
    //   int wordLength to obtain the word length we want
    //   to prune our dictionary to. 
    public static Set<String> pruneDictionary(List<String> contents, int wordLength) {
        if(wordLength < 1){
            throw new IllegalArgumentException();
        }
        Set<String> pruneList = new TreeSet<String>();

        for(String word: contents){
            if (word.length()==(wordLength)){
                pruneList.add(word);
            }
        }
        return pruneList;
    }

    //B: This method generates all possible patterns for the guess against 
    //   all remaining words in the dictionary,
    //   then selects the pattern that has the largest remaining words
    //   The dictionary is then modified to contain only the words that have that pattern.
    //   In case of ties, the alphabetically first pattern is chosen.
    //E: If the given dictionary is empty, or if the users guess 
    //   is not consistent with their chosen word length, we 
    //   throw a IllegalArgumentException
    //R: Returns a String pattern thhat clues the user towards an answer
    //   contained in the modified dictionary
    //P: A string guess, which is the user's attempt at guessing the word;
    //   A Set<String> dictionary,
    //   which is used when we call patternFor, 
    //   int wordlength for the conditional to throw the exception
    public static String recordGuess(String guess, Set<String> dictionary, int wordLength) { 
        if (dictionary.isEmpty() || (guess.length() != wordLength)){
            throw new IllegalArgumentException ();
        }

        Map<String,Set<String>> patternMap = createPatternMap(dictionary, guess);

        String greatestPatternSet = findGreatestSet(patternMap);

        dictionary.clear();
        dictionary.addAll(patternMap.get(greatestPatternSet));

        return greatestPatternSet;

    }

    //B: This helper method creates a pattern with the User's guess vs a word in the dictionary
    //R: Returns a String pattern of colored squares (🟩🟨⬜) representing clues
    //   towards the correct word.
    //P: String word, which is the word we compare the users guess to,
    //   and String guess which is the User's guess
    public static String patternFor(String word, String guess) {
        List<String> pattern = new ArrayList<>();

        for (int i = 0 ; i < guess.length(); i++){
            pattern.add("" + guess.charAt(i));
        }
        
        Map<Character,Integer> countChar = countCharacters(word);
        
        //green boxes
        for(int i = 0 ; i < pattern.size() ; i ++){
            if(guess.charAt(i)==word.charAt(i)){
                pattern.set(i, GREEN);
                countChar.put(word.charAt(i), countChar.get(word.charAt(i)) -1);
            }
        }
    
        //yellow boxes
        for (int i = 0; i < pattern.size(); i++) {
            // Only process if not already GREEN
            if (!pattern.get(i).equals(GREEN)) {
                // Check if this character is contained in the word, and if the character can still be used
                if (countChar.containsKey(guess.charAt(i)) && countChar.get(guess.charAt(i)) > 0) {
                    pattern.set(i, YELLOW);
                    countChar.put(guess.charAt(i), countChar.get(guess.charAt(i)) - 1);
                }
            }
        }

        //we replace all values without a green or yellow box with grey boxes
        for(int i = 0 ; i < pattern.size() ; i ++){
            if(!pattern.get(i).equals(GREEN) && !pattern.get(i).equals(YELLOW)){
                pattern.set(i, GRAY);
            }
        }

        String patternToString = "";
        for(String color : pattern){
            patternToString += color;
        }

        return patternToString;
    }


    //B: This helper method creates a map of each pattern, where each pattern
    //   holds words that match said pattern
    //R: Returns a Map<String,Set<String>> to be used in our recordGuess method,
    //   where we then find the pattern key with the 
    //   largest set of word values
    //P: Set<String> called dictionary, where we pull each word and create a
    //   pattern for with the user's guess, 
    //   and String guess, which is the user's guess,
    //   where it is compared to a word in the dictionary to create a pattern
    public static Map<String,Set<String>> createPatternMap(Set<String> dictionary, String guess){
        Map<String,Set<String>> patternMap = new TreeMap<>();

        // create a pattern for each word in the dictionary, store the pattern key in the map as a string, 
        // then add the word to the setOfWords set of createdPattern key
        for(String word : dictionary){
            String createdPattern = patternFor(word, guess);
            //everytime there is not a pattern in the map, we create the pattern
            if(!patternMap.containsKey(createdPattern)){
                Set<String> setOfWords = new HashSet<>();
                patternMap.put(createdPattern, setOfWords);
            }
            patternMap.get(createdPattern).add(word);
        }
    
        return patternMap;
    }


    //B: This helper method finds the pattern key with the most amount of word values
    //R: Returns String pattern key with the largest set of word values
    //P: Map<String,Set<String>> where we obtain the pattern key and set values
    public static String findGreatestSet(Map<String,Set<String>> patternMap){
        String greatestSet = "";
        int startingSize = 0;

        for(String patternSet: patternMap.keySet()){
            int setSize = patternMap.get(patternSet).size();
            if(setSize>startingSize){
                startingSize = setSize;
                greatestSet = patternSet;
            }
        }
        return greatestSet;
    }

    //B: This helper method counts the amount of times a character is shown up in a word,
    //   to attact available characters in pattern tracking
    //R: Returns a Map<Character,Integer>  containing character keys, and integer values
    //P: String word that we count characters from
    public static Map<Character,Integer> countCharacters (String word){
        Map<Character, Integer> countChar = new TreeMap<>();
        for(int i = 0 ; i < word.length() ; i++){
            char letter = word.charAt(i);

            if(countChar.containsKey(letter)){ 
                countChar.put(letter, countChar.get(letter)+1);
            }
            else{ 
                countChar.put(letter, 1);
            }
        }
        return countChar;
    }
}