package CommandVersion;

import java.io.*;
import java.util.Scanner;
import java.util.Collections;

public class DictionaryManagement extends Dictionary{

    /** Find the position to insert in array. */
    private static int findInsertIndex(Word w) {
        int start = 0;
        int end = words.size() - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            int cmp = words.get(mid).getWordTarget().compareTo(w.getWordTarget());
            if (cmp == 0) {
                return -1;
            } else if (cmp < 0) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return end + 1;
    }

    /** Find the position of a word in an array, return -1 if not exist. */
    private static int binarySearch(Word w) {
        int start = 0;
        int end = words.size() - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            int cmp = words.get(mid).getWordTarget().compareTo(w.getWordTarget());
            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return -1;
    }

    public static void insertFromCommandline() {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        for (int i = 0; i < n; i++) {
            String wordTarget = s.nextLine();
            String wordExplain = s.nextLine();
            Word input = new Word(wordTarget, wordExplain);
            words.add(input);
        }
        Collections.sort(words);//Sort words in ascending order
    }

    public static void insertFromFile() {

        try{
            BufferedReader r = new BufferedReader(new FileReader(System.getProperty("user.dir") + "./src/main/java/CommandVersion/dictionaries.txt"));
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split("\t", 2);
                if(parts.length >= 2){
                    Word newWord = new Word(parts[0].trim(), parts[1].trim());
                    
                    int insertIndex = binarySearch(newWord);
                    if (insertIndex != -1) {
                        words.get(insertIndex).setWordExplain(newWord.getWordExplain());
                    } else {
                        insertIndex = findInsertIndex(newWord);
                        words.add(insertIndex, newWord);
                    }
                }
            }
            Collections.sort(words);//Sort words in ascending order
            r.close();
            System.out.println("Imported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dictionaryLookup(){
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the word you want to look up: ");
        String lookUpWord = s.nextLine();
        Word word = new Word(lookUpWord, null);


        int index = binarySearch(word);
        if (index != -1) {
            System.out.println(words.get(index).getWordExplain());
        } else {
            System.out.println("Word not found!");
        }



    }

    public static void addWord(){

        Scanner s = new Scanner(System.in);

        System.out.println("Enter the word you want to add: ");
        String newWordTarget = s.nextLine();

        

        System.out.println("Enter the word explanation: ");
        String newWordExplain = s.nextLine();

        Word newWord = new Word(newWordTarget, newWordExplain);

        int insertIndex = findInsertIndex(newWord);

        if (insertIndex == -1) {
            System.out.println("Cannot add word: word has existed!");
        } else {
            words.add(insertIndex, newWord);
            System.out.println("Word added!");
        }
        


        
        
    }

    public static void updateWord(){

        Scanner s = new Scanner(System.in);

        System.out.println("Enter the word you want to update: ");
        String updateWordTarget = s.nextLine();
        Word word = new Word(updateWordTarget, null);

        int index = binarySearch(word);
        if (index != -1) {
            System.out.println("Enter the update explanation: ");
            String updateWordExplain = s.nextLine();

            words.get(index).setWordExplain(updateWordExplain);

            System.out.println("Word updated!");
        } else {
            System.out.println("Word not found!");
        }


    }

    public static void deleteWord(){

        Scanner s = new Scanner(System.in);

        System.out.println("Enter the word you want to delete: ");
        String deleteWordTarget = s.nextLine();
        Word word = new Word(deleteWordTarget, null);

        int index = binarySearch(word);
        if (index != -1) {
            words.remove(index);
            System.out.println("Word deleted!");
        } else {
            System.out.println("Word doesn't exist!");
        }
    }

    public static void dictionaryExportToFile() {
        File f = new File(System.getProperty("user.dir") + "./src/main/java/CommandVersion/dictionaries.txt");

        try {
            FileOutputStream output = new FileOutputStream(f);
            for(Word w: words){
                String tmp = w.getWordTarget() + "\t" + w.getWordExplain() + "\n";
                output.write(tmp.getBytes());
            }
            System.out.println("Exported successfully!");
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
