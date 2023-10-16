package CommandVersion;

import java.util.Scanner;

public class DictionaryCommandline extends Dictionary{

    public static void showAllWords(){
        System.out.println("No\t| English\t| Vietnamese");
        for(int i = 0; i < words.size(); i++){
            System.out.println(i + "\t| " + words.get(i).getWordTarget() + "\t| " + words.get(i).getWordExplain());
        }
    }

    public static void dictionaryBasic(){
        DictionaryManagement.insertFromCommandline();
        showAllWords();
    }

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
                && target.substring(0, search.length()).equals(target)){
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

    public static void main(String[] args) {
        dictionaryAdvanced();
    }
}
