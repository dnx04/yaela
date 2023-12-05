package Sound;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s1 = sc.next();
        String ans = "select distinct * from 'av' where word like '"
                + s1 + "%' and length(pronounce) > 0 order by id limit 5;";
        Sound.wordSearch(ans);
    }
}