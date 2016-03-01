package com.mysungrussian.mysungrussian;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * Created by chengchenliu on 2016-03-01.
 * Use the jsoup to crawl online Russian library to get the stress syllable.
 */
public class StressHelper {

    // This function looks up the "word" and get its ipas (somehow there may be more than 1, so used a list)
    public static List<String> getIPAFromWikitionary(String word){
        Locale.setDefault(new Locale("ru"));
        word = word.toLowerCase();

        Document doc;
        List<String> ipa_strings = new ArrayList<String>();

        try {

            // need http protocol
            String url = "https://en.wiktionary.org/wiki/" + word;
            doc = Jsoup.connect(url).get();

            Elements E = doc.select("span.IPA");    //span with class=IPA
            for (Element e : E) {
                System.out.println("Found an IPA : " + e.text());
                ipa_strings.add(e.text());
            }

        } catch (IOException e) {
            System.out.println("!!!!This word is not found on wikitionary: "+word);
            //e.printStackTrace();
        }
        return ipa_strings;

    }

    static String test_words =  "меня:mʲiˈɲɑ\n" +
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
    public static void main(String[] args) {
        /*String word = "Иль";

        List<String> ipas = getIPAFromWikitionary(word);
        if (!ipas.isEmpty()) {
            System.out.println(":D word = " + word +", wiki_ipa = " + ipas.get(0));
        } else {
            System.out.println(":( word = "+word+",  no IPA from wiki!");
        }*/

        List<String> ipas = new ArrayList<String>();


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
                ipas = getIPAFromWikitionary(word);
                if (!ipas.isEmpty()) {
                    System.out.println(":D word = " + word + " (" + true_ipa + "), wiki_ipa = " + ipas.get(0));
                } else {
                    System.out.println(":( word = "+word+" ("+true_ipa+"),  no IPA from wiki!");
                }

            } catch (ArrayIndexOutOfBoundsException e){
                System.out.println("D: word = "+word+" ("+true_ipa+"),  failed");
                e.printStackTrace();

            }

        }
    }
}
