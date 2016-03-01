package com.mysungrussian.mysungrussian;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by chengchenliu on 2016-03-01.
 * Use the jsoup to crawl online Russian library to get the stress syllable.
 */
public class StressHelper {

    // This function looks up the "word" and get its ipas (somehow there may be more than 1, so used a list)
    public static List<String> getIPAFromWikitionary(String word){
        Document doc;
        List<String> ipa_strings = new ArrayList<String>();

        try {

            // need http protocol
            String url = "https://ru.wiktionary.org/wiki/" + word;
            doc = Jsoup.connect(url).get();

            Elements E = doc.select("span");
            for (Element e : E) {

                //System.out.println("\n class? "+e.attr("class"));
                if (e.attr("class").equals("IPA")) {
                    System.out.println("Found an IPA : " + e.text());
                    ipa_strings.add(e.text());
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ipa_strings;

    }


    public static void main(String[] args) {
        String word = "покой";
        List<String> ipas = getIPAFromWikitionary(word);

    }
}
