import java.io.*;
import java.util.Scanner;
import java.util.Collections;

public class DictionaryManagement extends Dictionary {

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

        try {
            Scanner s = new Scanner(new File("./src/dictionaries.txt"));
            String line = s.nextLine();
            BufferedReader r = new BufferedReader(new FileReader("./src/dictionaries.txt"));

            while ((line = r.readLine()) != null) {
                String[] parts = line.split("\t", 2);
                if (parts.length >= 2) {
                    Word newWord = new Word(parts[0].trim(), parts[1].trim());
                    words.add(newWord);
                }
            }
            Collections.sort(words);//Sort words in ascending order
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dictionaryLookup() {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the word you want to look up: ");
        String lookUpWord = s.nextLine();

        //NEED TO UPDATE THIS PART BY USING BINARY SEARCH 
        for (Word w : words) {
            if (w.getWordTarget().equals(lookUpWord)) {
                System.out.println(w.getWordExplain());
                return;
            }
        }
        System.out.println("Word not found!");
        //


    }

    public static void addWord() {

        Scanner s = new Scanner(System.in);

        System.out.println("Enter the word you want to add: ");
        String newWordTarget = s.nextLine();

        System.out.println("Enter the word explanation: ");
        String newWordExplain = s.nextLine();

        Word newWord = new Word(newWordTarget, newWordExplain);

        //NEED TO UPDATE THIS PART BY FINDING THE POSITION TO ADD WORD
        words.add(newWord);
        Collections.sort(words);
        //

        System.out.println("Word added!");

    }

    public static void updateWord() {

        Scanner s = new Scanner(System.in);

        System.out.println("Enter the word you want to update: ");
        String updateWordTarget = s.nextLine();

        //NEED TO UPDATE THIS PART USING BINARY SEARCH
        for (Word w : words) {
            if (w.getWordTarget().equals(updateWordTarget)) {

                System.out.println("Enter the update explanation: ");
                String updateWordExplain = s.nextLine();

                w.setWordExplain(updateWordExplain);
                System.out.println("Word updated!");

                return;
            }
        }
        System.out.println("Word not found!");
        //

    }

    public static void deleteWord() {

        Scanner s = new Scanner(System.in);

        System.out.println("Enter the word you want to delete: ");
        String deleteWordTarget = s.nextLine();

        //NEED TO UPDATE THIS PART USING BINARY SEARCH
        for (Word w : words) {
            if (w.getWordTarget().equals(deleteWordTarget)) {

                words.remove(w);
                System.out.println("Word deleted!");

                s.close();
                return;
            }
        }
        System.out.println("Word doesn't exist!");
        //

        //s.close();
    }

    public static void dictionaryExportToFile() {
        File f = new File("./src/dictionaries.txt");

        try {
            FileOutputStream output = new FileOutputStream(f);
            for (Word w : words) {
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
