package com.mysungrussian.mysungrussian;

import java.util.StringTokenizer;

/**
 * Created by He on 3/2/2016.
 */
public class Rule_Tester {
    static String test_words =  "Здравствуйте мир\n" +
            "меня:mʲiˈɲɑ\n" +
            "придётся:prʲiˈdjo.tsɑ\n" +
            "хоронить:xʌ.rɑˈɲitʲ\n" +
            "-\n" +
            "Иль:ilʲ\n" +
            "мне:mʲɲɛ\n" +
            "тебя:tʲiˈbʲɑ\n" +
            "не:ɲɪ\n" +
            "знаю:ˈznɑ.ju\n" +
            "друг:druk\n" +
            "мой:moj\n" +
            "милый:ˈmʲi.ɫɨj\n" +
            "-\n" +
            "Но:no\n" +
            "судьбы:suˈdʲbɨ\n" +
            "твоей:tvɑˈjej\n" +
            "прервётся:prʲɪ.ˈrvʲo.tsɑ\n" +
            "нить:ɲitʲ\n" +
            "-\n";

    public static void main(String[] args){
        //хоронить:xʌ.rɑˈɲitʲ
        //String word2 = "Здравствуйте, мир!";
        String word2 = "Здравствуйте мир";
        String ipa2 = new String();
        ipa2 = RuleEngine.Transcribe(word2);


        // Try to write a test frame here.
        // The data is just stored in a string called test_word
        StringTokenizer st = new StringTokenizer(test_words, "\n");
        while (st.hasMoreTokens()) {
            String line = st.nextToken();

            if (line.contains("-")){
                continue;
            }

            StringTokenizer st2 = new StringTokenizer(line, ":");
            String word = st2.nextToken();
            String true_ipa = st2.nextToken();
            String ipa = "(n/a)";
            System.out.println("\n+++++++++++++"+word+"+++++++++++++");

            try {
                ipa = RuleEngine.Transcribe(word);
                System.out.println(":D word = "+word+", true_ipa = "+true_ipa+", ipa = "+ipa);

            } catch (ArrayIndexOutOfBoundsException e){
                System.out.println("D: word = "+word+", true_ipa = "+true_ipa+", ipa failed");
                e.printStackTrace();

            }

        }
    }
}
