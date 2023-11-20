package CommandVersion;

import java.security.SecureRandom;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;


public class DictionaryCommandline extends Dictionary{

    private static int getRandomWithExclusion(SecureRandom rand, int upperBound, ArrayList<Integer> exclude) {
        ArrayList<Integer> excludeBackup = new ArrayList<>(exclude);
        excludeBackup.sort(Comparator.naturalOrder());
        int random = rand.nextInt(upperBound - exclude.size());
        for (int ex : excludeBackup) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
    }

    public static void showAllWords(){
        System.out.println("No\t| English\t| Vietnamese");
        for(int i = 1; i <= words.size(); i++){
            System.out.println(i + "\t| " + words.get(i - 1).getWordTarget() + "\t| " + words.get(i - 1).getWordExplain());
        }
    }

    // public static void dictionaryBasic(){
    //     DictionaryManagement.insertFromCommandline();
    //     showAllWords();
    // }

    public static void dictionarySearcher(){

        Scanner s = new Scanner(System.in);

        System.out.println("Search word: ");
        String search = s.nextLine();

        boolean isFound = false;//Check if can search

        for(Word w: words){
            String target = w.getWordTarget();

            /* Condition: 
            - Length of search word is not greater than length of target word.
            - Search word is found at the beginning of the target word.
            */
            if(search.length() <= target.length() 
                && target.substring(0, search.length()).equals(search)){
                System.out.println(target);
                isFound = true;
            } else {
                //Iteration stop condition
                if(isFound) {
                    return;
                }

            }
        }

        if(!isFound){
            System.out.println("No word found!");
        }

    }

    //UPDATE DICTIONARY GAME
    public static void dictionaryGame(){

        final int NUMBER_OF_ANSWER = 6;
        final int NUMBER_OF_QUESTION = 10;

        if (words.size() >= NUMBER_OF_ANSWER) {

            SecureRandom rand = new SecureRandom();
            int upperBound = words.size();
            int point = 0;

            for (int q = 1; q <= NUMBER_OF_QUESTION; q++) {

                ArrayList<Integer> randomWordIndex = new ArrayList<Integer>();

                for (int i = 0; i < NUMBER_OF_ANSWER; i++) {
                    randomWordIndex.add(getRandomWithExclusion(rand, upperBound, randomWordIndex));
                }
                int answerIndex = rand.nextInt(upperBound + NUMBER_OF_ANSWER - upperBound % NUMBER_OF_ANSWER) % NUMBER_OF_ANSWER;

                System.out.println("Question " + q + ": Find the meaning of the word: " + words.get(randomWordIndex.get(answerIndex)).getWordTarget());
                for (int i = 1; i <= NUMBER_OF_ANSWER; i++) {
                    String tmp = Integer.toString(i) + ". " + words.get(randomWordIndex.get(i - 1)).getWordExplain();
                    System.out.println(tmp);
                }

                System.out.print("Your answer is: ");
                Scanner s = new Scanner(System.in);
                int playerChoose = s.nextInt();
                if (playerChoose - 1 == answerIndex) {
                    System.out.println("You are correct!");
                    ++point;
                } else {
                    System.out.println("You are wrong! The right answer is " + Integer.toString(answerIndex + 1));
                }
            }
            System.out.println("End game: " + point + "/" + NUMBER_OF_QUESTION);

        } else {
            System.out.println("Not enough words for game!");
        }
    }


    public static void dictionaryMenu() {
        System.out.println("Welcome to My Application!");
        System.out.println("[0] Exit");
        System.out.println("[1] Add");
        System.out.println("[2] Remove");
        System.out.println("[3] Update");
        System.out.println("[4] Display");
        System.out.println("[5] Lookup");
        System.out.println("[6] Search");
        System.out.println("[7] Game");
        System.out.println("[8] Import from file");
        System.out.println("[9] Export to file");
        System.out.print("Your action: ");
    }

    public static void dictionaryFunction(){
        dictionaryMenu();
        Scanner s = new Scanner(System.in);
        int choice = s.nextInt();
        switch(choice) {
            case 0:
                System.exit(0);
                break;
            case 1:
                DictionaryManagement.addWord();
                break;
            case 2:
                DictionaryManagement.deleteWord();
                break;
            case 3:
                DictionaryManagement.updateWord();
                break;
            case 4:
                showAllWords();
                break;
            case 5:
                DictionaryManagement.dictionaryLookup();
                break;
            case 6:
                dictionarySearcher();
                break;
            case 7:
                dictionaryGame();
                break;
            case 8:
                DictionaryManagement.insertFromFile();
                break;
            case 9:
                DictionaryManagement.dictionaryExportToFile();
                break;
            default:
                System.out.println("Action not supported!");
        }
    }

    public static void dictionaryContinue() {
        Scanner s = new Scanner(System.in);
        System.out.print("Do you want to continue? (Y/N): ");
        String action = s.nextLine();
        if(action.toUpperCase().equalsIgnoreCase("N")){
            System.exit(0);
        }
    }

    public static void dictionaryAdvanced() {
        DictionaryManagement.insertFromFile();
        dictionaryFunction();
        while(true){
            dictionaryContinue();
            dictionaryFunction();
        }
    }
}
