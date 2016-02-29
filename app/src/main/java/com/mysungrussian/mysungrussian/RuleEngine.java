package com.mysungrussian.mysungrussian;

import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by hedai on 2016-02-24.
 */
public class RuleEngine {
    public static HashMap<String, String[]> cyrillicLib = new HashMap<String, String[]>();
    public static HashMap<Integer, String[]> tmp_syllables = new HashMap<Integer, String[]>();

    static {
        /*ʲ
        * Using ^ to cover for small j, need to be fixed
        * Hello World Example: Здравствуйте, мир!  IPA:ˈzdrɑ.stvuj.tʲɪ mʲir*/
        //hashmap structure char, V/C/S, {IPAs}
        cyrillicLib.put("А", new String[]{"V", "ɑ", "ɑ", "a"});
        cyrillicLib.put("а", new String[]{"V", "ɑ", "ɑ", "a"});
        cyrillicLib.put("Б", new String[]{"C", "bɛ", "b", "p"});
        cyrillicLib.put("б", new String[]{"C", "bɛ", "b", "p"});
        cyrillicLib.put("В", new String[]{"C", "vɛ", "v", "f"});
        cyrillicLib.put("в", new String[]{"C", "vɛ", "v", "f"});
        cyrillicLib.put("Г", new String[]{"C", "ɡɛ", "ɡ", "k", "v", "x"});
        cyrillicLib.put("г", new String[]{"C", "ɡɛ", "ɡ", "k", "v", "x"});
        cyrillicLib.put("Д", new String[]{"C", "dɛ", "d", "t"});
        cyrillicLib.put("д", new String[]{"C", "dɛ", "d", "t"});
        cyrillicLib.put("Е", new String[]{"VI", "jɛ", "jɛ", "ɛ", "ɪ"});
        cyrillicLib.put("е", new String[]{"VI", "jɛ", "jɛ", "ɛ", "ɪ"});
        cyrillicLib.put("Ё", new String[]{"VI", "jo", "jo"});
        cyrillicLib.put("ё", new String[]{"VI", "jo", "jo"});
        cyrillicLib.put("Ж", new String[]{"C", "ʒɛ", "ʒ", "tʃ"});
        cyrillicLib.put("ж", new String[]{"C", "ʒɛ", "ʒ", "tʃ"});
        cyrillicLib.put("З", new String[]{"C", "zɛ", "z", "s"});
        cyrillicLib.put("з", new String[]{"C", "zɛ", "z", "s"});
        cyrillicLib.put("И", new String[]{"VI", "i", "i", "ɨ"});
        cyrillicLib.put("и", new String[]{"VI", "i", "i", "ɨ"});
        cyrillicLib.put("Й", new String[]{"C", "i ˈkrɑt.kɑ.jɛ", "j"});
        cyrillicLib.put("й", new String[]{"C", "i ˈkrɑt.kɑ.jɛ", "j"});
        cyrillicLib.put("К", new String[]{"C", "kɑ", "k", "ɡ"});
        cyrillicLib.put("к", new String[]{"C", "kɑ", "k", "ɡ"});
        cyrillicLib.put("Л", new String[]{"C", "ɛl", "l", "ɫ"});
        cyrillicLib.put("л", new String[]{"C", "ɛl", "l", "ɫ"});
        cyrillicLib.put("М", new String[]{"C", "ɛm", "m"});
        cyrillicLib.put("м", new String[]{"C", "ɛm", "m"});
        cyrillicLib.put("Н", new String[]{"C", "ɛn", "n", "ɲ"});
        cyrillicLib.put("н", new String[]{"C", "ɛn", "n", "ɲ"});
        cyrillicLib.put("О", new String[]{"V", "o", "o", "ɑ", "ʌ"});
        cyrillicLib.put("о", new String[]{"V", "o", "o", "ɑ", "ʌ"});
        cyrillicLib.put("П", new String[]{"C", "pɛ", "p", "b"});
        cyrillicLib.put("п", new String[]{"C", "pɛ", "p", "b"});
        cyrillicLib.put("Р", new String[]{"C", "ɛr", "r"});
        cyrillicLib.put("р", new String[]{"C", "ɛr", "r"});
        cyrillicLib.put("С", new String[]{"C", "ɛs", "s", "z"});
        cyrillicLib.put("с", new String[]{"C", "ɛs", "s", "z"});
        cyrillicLib.put("Т", new String[]{"C", "tɛ", "t", "d"});
        cyrillicLib.put("т", new String[]{"C", "tɛ", "t", "d"});
        cyrillicLib.put("У", new String[]{"V", "u", "u"});
        cyrillicLib.put("у", new String[]{"V", "u", "u"});
        cyrillicLib.put("Ф", new String[]{"C", "fɛ", "f", "v"});
        cyrillicLib.put("ф", new String[]{"C", "fɛ", "f", "v"});
        cyrillicLib.put("Х", new String[]{"C", "xɑ", "x", "ɣ"});
        cyrillicLib.put("х", new String[]{"C", "xɑ", "x", "ɣ"});
        cyrillicLib.put("Ц", new String[]{"C", "tsɛ", "ts"});
        cyrillicLib.put("ц", new String[]{"C", "tsɛ", "ts"});
        cyrillicLib.put("Ч", new String[]{"C", "tʃɛ", "tʃj", "ʃ"});
        cyrillicLib.put("ч", new String[]{"C", "tʃɛ", "tʃj", "ʃ"});
        cyrillicLib.put("Ш", new String[]{"C", "ʃɑ", "ʃ", "ʒ"});
        cyrillicLib.put("ш", new String[]{"C", "ʃɑ", "ʃ", "ʒ"});
        cyrillicLib.put("Щ", new String[]{"C", "ʃtʃɑ", "ʃtʃ"});
        cyrillicLib.put("щ", new String[]{"C", "ʃtʃɑ", "ʃtʃ"});
        cyrillicLib.put("Ъ", new String[]{"S", "ˈtvʲor.dɨj znɑk",""});
        cyrillicLib.put("ъ", new String[]{"S", "ˈtvʲor.dɨj znɑk",""});
        cyrillicLib.put("Ы", new String[]{"V", "ɨ", "ɨ"});
        cyrillicLib.put("ы", new String[]{"V", "ɨ", "ɨ"});
        cyrillicLib.put("Ь", new String[]{"S", "ˈmʲax_͜kʲij znɑk","->"});
        cyrillicLib.put("ь", new String[]{"S", "ˈmʲax_͜kʲij znɑk","->"});
        cyrillicLib.put("Э", new String[]{"V", "ɛ", "ɛ", "e"});
        cyrillicLib.put("э", new String[]{"V", "ɛ", "ɛ", "e"});
        cyrillicLib.put("Ю", new String[]{"VI", "ju", "ju"});
        cyrillicLib.put("ю", new String[]{"VI", "ju", "ju"});
        cyrillicLib.put("Я", new String[]{"VI", "jɑ", "jɑ", "jɑ", "jɪ"});
        cyrillicLib.put("я", new String[]{"VI", "jɑ", "jɑ", "jɑ", "jɪ"});
        //the following cyrillic vanished after 1918
        cyrillicLib.put("Ѣ", new String[]{"V", "jatʲ", "ɛ", "e", "ɪ"});
        cyrillicLib.put("ѣ", new String[]{"V", "jatʲ", "ɛ", "e", "ɪ"});
        cyrillicLib.put("І", new String[]{"V", "i", "i", "ɨ"});
        cyrillicLib.put("і", new String[]{"V", "i", "i", "ɨ"});
        cyrillicLib.put("Ѵ", new String[]{"V", "ˈi.ʒɨ.tsʌ", "i", "ɨ"});
        cyrillicLib.put("ѵ", new String[]{"V", "ˈi.ʒɨ.tsʌ", "i", "ɨ"});
        cyrillicLib.put("Ѳ", new String[]{"C", "ˈfʲi.tɑ", "f", "v"});
        cyrillicLib.put("ѳ", new String[]{"C", "ˈfʲi.tɑ", "f", "v"});
    }


    public static String Transcribe(String word) {
        String[] words = word.split("\\P{L}+");
        String ipa = new String();

        for(int i = 0; i< words.length;i++) {
            String tmp_ipa = Transcribe_helper(words[i]);
            tmp_ipa = tmp_ipa.substring(0,tmp_ipa.length()-1);
            ipa = ipa + "/"+ tmp_ipa + "/ " ;
        }

        System.out.println("word = " + word);
        System.out.println("ipa = " + ipa);
        return ipa;

    }

    //This function transcribe single word to ipa
    private static String Transcribe_helper(String word) {
        String ipa = new String();
        String[] word_array = word.split("(?!^)");
        HashMap<String, String[]> word_hash = new HashMap<String, String[]>();
        String word_VCS = new String();
        String word_part = word;

        for (int i = 0; i < word_array.length; i++) {
            String key = word_array[i];
            String[] value = (String[])cyrillicLib.get(key);

            word_hash.put(key, value);
            String vcs = value[0];
            //divide into syllables
            if (key.equals("й") || key.equals("Й")){
                String[] pre_N = (String[])cyrillicLib.get(word_array[i-1]);
                if (pre_N[0].equals("V")||pre_N[0].equals("VI")){
                    word_VCS = word_VCS + value[0] + ".";
                    word_VCS = word_VCS.substring(0,word_VCS.length()-1);
                    ipa = ipa.substring(0,ipa.length()-1);
                    ipa = ipa + value[2] + ".";
                    word_part = word_part.substring(1,word_part.length());
                }
            }else if (vcs.equals("V")|| vcs.equals("VI")){
                word_VCS = word_VCS + value[0] + ".";
                ipa = ipa + value[2] + ".";
                word_part = word_part.substring(1,word_part.length());
            }else{
                String last_char = "";
                if(ipa.length() - 1 >= 0) {
                    last_char = ipa.substring(ipa.length() - 1);
                }
                if(!checkContainVowels(word_part) && last_char.equals(".")){
                    ipa = ipa.substring(0,ipa.length()-1);
                    word_VCS = word_VCS.substring(0,word_VCS.length()-1);
                }
                word_VCS = word_VCS + value[0];
                ipa = ipa + value[2];
                word_part = word_part.substring(1, word_part.length());
            }
        }
        ipa = ipa +".";
        word_VCS = word_VCS + ".";
        ipa = palatalization(ipa, word);
        return ipa;
    }

    private static boolean checkContainVowels(String words){
        HashSet<String> set = new HashSet<String>(Arrays.asList("а", "э", "ы", "у", "о", "я", "е", "ё", "ю", "и","А", "Э", "Ы", "У", "О", "Я", "Е", "Ё", "Ю", "И"));
        for (String word : set) {
            if (words.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private static boolean check_palatalization(String words){
        HashSet<String> set = new HashSet<String>(Arrays.asList("p","b","t","d","k","ɡ","f","s","ʃ","x","v","z","r","m","n"));
        for (String word : set) {
            if (words.contains(word)) {
                return true;
            }
        }
        return false;
    }

    //palatalization process
    /*++++++++++++Need to be fixed
    *Case for b is not contained yet
    */
    private static String palatalization(String ipa, String word){
        String pal_ipa = new String();
        String[] ipa_list = ipa.split("\\.");
        for(int i = ipa_list.length-1; i>=0; i--){
            String current_ipa = ipa_list[i];
            int index =-1;
            //checks if b is shown, and make is soft indicator
            if (current_ipa.contains("->")){
                int tmp_index = current_ipa.indexOf("->");
                if (check_palatalization(current_ipa.substring(tmp_index-1,tmp_index))) {
                    current_ipa = current_ipa.substring(0, tmp_index) + "ʲ" + current_ipa.substring(tmp_index + 2, current_ipa.length());
                }
                ipa_list[i] = current_ipa;
            }
            //checks if indicator shows up, each syllable can have only one vowel, use if else structure
            if(current_ipa.contains("jɑ")){
                int tmp_index = current_ipa.indexOf("jɑ");
                if (check_palatalization(current_ipa.substring(tmp_index-1,tmp_index))) {
                    current_ipa = current_ipa.substring(0, tmp_index) + "ʲɑ" + current_ipa.substring(tmp_index + 2, current_ipa.length());
                }
                ipa_list[i] = current_ipa;
            }else if(current_ipa.contains("jɛ")){
                int tmp_index = current_ipa.indexOf("jɛ");
                if (check_palatalization(current_ipa.substring(tmp_index-1,tmp_index))) {
                    current_ipa = current_ipa.substring(0, tmp_index) + "ʲɪ" + current_ipa.substring(tmp_index + 2, current_ipa.length());
                }
                ipa_list[i] = current_ipa;
            }else if(current_ipa.contains("i")){
                int tmp_index = current_ipa.indexOf("i");
                if (check_palatalization(current_ipa.substring(tmp_index-1,tmp_index))){
                    current_ipa = current_ipa.substring(0, tmp_index) + "ʲi" + current_ipa.substring(tmp_index + 1, current_ipa.length());
                }
                ipa_list[i] = current_ipa;
            }else if (current_ipa.contains("jo")){
                int tmp_index = current_ipa.indexOf("jo");
                if (check_palatalization(current_ipa.substring(tmp_index-1,tmp_index))) {
                    current_ipa = current_ipa.substring(0, tmp_index) + "ʲo" + current_ipa.substring(tmp_index + 2, current_ipa.length());
                }
                ipa_list[i] = current_ipa;
            }else if (current_ipa.contains("ju")){
                int tmp_index = current_ipa.indexOf("ju");
                if (check_palatalization(current_ipa.substring(tmp_index-1,tmp_index))) {
                    current_ipa = current_ipa.substring(0, tmp_index) + "ʲu" + current_ipa.substring(tmp_index + 2, current_ipa.length());
                }
                ipa_list[i] = current_ipa;
            }
            //makes nj = specical n
            if(ipa_list[i].contains("nʲ")){
                System.out.println("nj is detected");
                ipa_list[i]  = ipa_list[i].replaceAll("nʲ", "ɲ");
            }

        }
        //make ipa_list to String
        for(int i =0 ; i<ipa_list.length;i++){
            pal_ipa = pal_ipa + ipa_list[i] + ".";
        }
        return pal_ipa;
    }

    public static void main(String[] args){
        //хоронить:xʌ.rɑˈɲitʲ
        //String word = "Здравствуйте, мир!";
        String word = "хоронить";
        String ipa = new String();
        //RuleEngine rule = new RuleEngine();
        ipa = Transcribe(word);
    }

    //This function prints a String[]
    public static void testPrint(String name, String[] words) {
        for (int i = 0; i < words.length; i++) {
            System.out.println("the position is " + name + " word = " + words[i]);
        }

    }
}
