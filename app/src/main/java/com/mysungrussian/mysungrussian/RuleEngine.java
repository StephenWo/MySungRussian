package com.mysungrussian.mysungrussian;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by hedai on 2016-02-24.
 */
public class RuleEngine {
    public static HashMap<String, String[]> cyrillicLib = new HashMap<String, String[]>();

    /*tmp_syllables is in the form of {word} {aaa.bbb.ccc.}  this is used for calling different process on the ipas
    *the first process is palatalizaiont
    */
    private static String[] unvoiced  = {"f","k","p","s","ʃ","t","x"};
    private static String[] voiced  = {"v","ɡ","b","z","ʒ","d","ɣ"};
    private static HashSet<String> vowel_set = new HashSet<String>(Arrays.asList("а", "э", "ы", "у", "о", "я", "е", "ё", "ю", "и","А", "Э", "Ы", "У", "О", "Я", "Е", "Ё", "Ю", "И"));
    private static HashSet<String> pal_consant_set = new HashSet<String>(Arrays.asList("p","b","t","d","k","ɡ","f","s","ʃ","x","v","z","r","m","n","l"));
    private static HashSet<String> voiced_set = new HashSet<String>(Arrays.asList("v","ɡ","b","z","ʒ","d","ɣ"));
    private static HashSet<String> unvoiced_set = new HashSet<String>(Arrays.asList("f","k","p","s","ʃ","t","x"));
    public static String voiced_string = "vɡbzʒdɣ";
    public static String unvoiced_string = "fkpsʃtx";
    public static String sonorant_string = "rmnl";
    private static String vowel_string ="ɑaʌɛiɪɨou";
    private static String vowel_letter_string = "аэыуояеёюи";
    public static String consonants_letters = "бвгджзйклмнпрстфхцчшщ";
    //public static HashMap<Integer, String[]> tmp_syllables = new HashMap<Integer, String[]>();

    static {
        /*
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
        cyrillicLib.put("Ч", new String[]{"C", "tʃɛ", "tʃʲ", "ʃ"});
        cyrillicLib.put("ч", new String[]{"C", "tʃɛ", "tʃʲ", "ʃ"});
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

    /*
    * Main function for transcribe. This function gets called from the UI with a string of sentence
    * as input and gets the ipa String[] list as output
    * */
    public static String Transcribe(String sentence) {

        //Get the sentence and chars'ipa, both divide the syllables by "."
        String [] sentence_and_ipa = Syllables_divider(sentence);
        String syllable_sentence = sentence_and_ipa[0];
        String sentence_ipa = sentence_and_ipa[1];
        System.out.println("ipa after divider= " + sentence_ipa);
        sentence_ipa = palatalization(sentence_ipa);
        System.out.println("ipa after pal= " + sentence_ipa);
        sentence_ipa = changeVoiced(sentence_ipa);
        System.out.println("ipa after voice= " + sentence_ipa);
        sentence_ipa = addStress(syllable_sentence,sentence_ipa.trim());
        System.out.println("ipa after stress= " + sentence_ipa);

        return sentence_ipa;
    }

    //This function transcribe single word to ipa
    private static String[] Syllables_divider(String sentence) {
        String[] sentence_and_ipa = new String[2];
        String ipa = new String();
        String[] word_array = sentence.split("(?!^)");
        HashMap<String, String[]> word_hash = new HashMap<String, String[]>();
        String word_VCS = new String();
        String word_part = sentence;
        String[] value = new String[word_array.length];
        String word_by_syllable = new String();
        int deleted  = -1;
        for (int i = 0; i < word_array.length; i++) {
            String key = word_array[i];
            String vcs = new String();
            //Need to make sure space only shows in the middle of a sentence
            if (key.equals(" ")) {
                deleted  = -1;
                ipa = ipa + " ";
                word_by_syllable = word_by_syllable + " ";
                word_part = word_part.substring(1, word_part.length());
            } else {
                    value = (String[]) cyrillicLib.get(key);
                    word_hash.put(key, value);
                    vcs = value[0];
                    //divide into syllables
                    if (key.equals("й") || key.equals("Й")) {
                        String[] pre_N = (String[]) cyrillicLib.get(word_array[i - 1]);
                        if (pre_N[0].equals("V") || pre_N[0].equals("VI")) {
                            word_VCS = word_VCS + value[0] + ".";
                            word_VCS = word_VCS.substring(0, word_VCS.length() - 1);
                            ipa = ipa.substring(0, ipa.length() - 1);
                            word_by_syllable = word_by_syllable.substring(0,word_by_syllable.length()-1);
                            ipa = ipa + value[2] + ".";
                            word_by_syllable = word_by_syllable + key + ".";
                            word_part = word_part.substring(1, word_part.length());
                        }
                    } else if (vcs.equals("V") || vcs.equals("VI")) {
                        word_VCS = word_VCS + value[0] + ".";
                        ipa = ipa + value[2] + ".";
                        word_by_syllable = word_by_syllable + key + ".";
                        word_part = word_part.substring(1, word_part.length());
                    } else {
                        String firstword = "";
                        if(word_part.contains(" ")){
                            firstword = word_part.substring(0, word_part.indexOf(" "));
                            if (!checkContainVowels(firstword) && deleted == -1) {
                                ipa = ipa.substring(0, ipa.length() - 1);
                                word_by_syllable = word_by_syllable.substring(0,word_by_syllable.length()-1);
                                word_VCS = word_VCS.substring(0, word_VCS.length() - 1);
                                deleted = 1;
                            }
                        }else {
                            if (!checkContainVowels(word_part) && deleted == -1) {
                                ipa = ipa.substring(0, ipa.length() - 1);
                                word_by_syllable = word_by_syllable.substring(0,word_by_syllable.length()-1);
                                word_VCS = word_VCS.substring(0, word_VCS.length() - 1);
                                deleted = 1;
                            }
                        }
                        word_VCS = word_VCS + value[0];
                        ipa = ipa + value[2];
                        word_by_syllable = word_by_syllable + key;
                        word_part = word_part.substring(1, word_part.length());
                }
            }
        }
        sentence_and_ipa[1] = ipa;
        sentence_and_ipa[0] = word_by_syllable;
        return sentence_and_ipa;
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
        HashSet<String> set = new HashSet<String>(Arrays.asList("p","b","t","d","k","ɡ","f","s","ʃ","x","v","z","r","m","n","l"));
        for (String word : set) {
            if (words.contains(word)) {
                return true;
            }
        }
        return false;
    }


    //palatalization process
    private static String palatalization(String ipa){
        String[] word_ipa_list = ipa.split("\\s");
        String sentence_ipa = "";
        for(int j = 0; j < word_ipa_list.length; j++) {
            String pal_ipa = new String();
            String[] ipa_list = word_ipa_list[j].split("\\.");
            for (int i = ipa_list.length - 1; i >= 0; i--) {
                String current_ipa = ipa_list[i];
                int index = -1;
                //checks if b is shown, and make is soft indicator-
                if (current_ipa.contains("->")) {
                    int tmp_index = current_ipa.indexOf("->");
                    if (check_palatalization(current_ipa.substring(tmp_index - 1, tmp_index))) {
                        current_ipa = current_ipa.substring(0, tmp_index) + "ʲ" + current_ipa.substring(tmp_index + 2, current_ipa.length());
                    }
                    ipa_list[i] = current_ipa;
                }
                //checks if indicator shows up, each syllable can have only one vowel, use if else structure
                if (current_ipa.contains("jɑ")) {
                    int tmp_index = current_ipa.indexOf("jɑ");
                    if (tmp_index != 0 && check_palatalization(current_ipa.substring(tmp_index - 1, tmp_index))) {
                        current_ipa = current_ipa.substring(0, tmp_index) + "ʲɑ" + current_ipa.substring(tmp_index + 2, current_ipa.length());
                    }
                    ipa_list[i] = current_ipa;
                } else if (current_ipa.contains("jɛ")) {
                    int tmp_index = current_ipa.indexOf("jɛ");
                    if (tmp_index != 0 && check_palatalization(current_ipa.substring(tmp_index - 1, tmp_index))) {
                        current_ipa = current_ipa.substring(0, tmp_index) + "ʲɛ" + current_ipa.substring(tmp_index + 2, current_ipa.length());
                    }
                    ipa_list[i] = current_ipa;
                } else if (current_ipa.contains("i")) {
                    int tmp_index = current_ipa.indexOf("i");
                    if (tmp_index != 0 && check_palatalization(current_ipa.substring(tmp_index - 1, tmp_index))) {
                        current_ipa = current_ipa.substring(0, tmp_index) + "ʲi" + current_ipa.substring(tmp_index + 1, current_ipa.length());
                    }
                    ipa_list[i] = current_ipa;
                } else if (current_ipa.contains("jo")) {
                    int tmp_index = current_ipa.indexOf("jo");
                    if (tmp_index != 0 && check_palatalization(current_ipa.substring(tmp_index - 1, tmp_index))) {
                        current_ipa = current_ipa.substring(0, tmp_index) + "ʲo" + current_ipa.substring(tmp_index + 2, current_ipa.length());
                    }
                    ipa_list[i] = current_ipa;
                } else if (current_ipa.contains("ju")) {
                    int tmp_index = current_ipa.indexOf("ju");
                    if (tmp_index != 0 && check_palatalization(current_ipa.substring(tmp_index - 1, tmp_index))) {
                        current_ipa = current_ipa.substring(0, tmp_index) + "ʲu" + current_ipa.substring(tmp_index + 2, current_ipa.length());
                    }
                    ipa_list[i] = current_ipa;
                }
                //makes nj = specical n
                if (ipa_list[i].contains("nʲ")) {
                    ipa_list[i] = ipa_list[i].replaceAll("nʲ", "ɲ");
                }
                if (ipa_list[i].contains("tsʲ")) {
                    ipa_list[i] = ipa_list[i].replaceAll("tsʲ", "ts");
                }

            }
            //make ipa_list to String
            for (int i = 0; i < ipa_list.length; i++) {
                pal_ipa = pal_ipa + ipa_list[i] + ".";
            }
            pal_ipa = pal_ipa.substring(0, pal_ipa.length() - 1);
            sentence_ipa = sentence_ipa + " " + pal_ipa;

        }

        return sentence_ipa;
    }

    /*
    * Process the ipa voice/unvoice
    * The last char, if vowel/sonorant, ignore, set flag = 0
    * if voiced, change to unvoiced, set flag = 1
    * if unvoiced, set flag =2
    * shift left by one, check char
    * if char = " ", don't change flag
    * Q?  special case for "v"?
    * */
    private static String changeVoiced(String ipa){
        String vol_ipa = ipa;
        int flag = -1;
        for(int i = ipa.length()-1; i>=0; i--){
            String single_ipa = String.valueOf(ipa.charAt(i));
            if(i==ipa.length()-1){
                if(sonorant_string.contains(single_ipa) || vowel_string.contains(single_ipa)){
                    flag = 0;
                }else {
                    if(voiced_string.contains(single_ipa)){
                        int index = voiced_string.indexOf(single_ipa);
                        String replaceChar = String.valueOf(unvoiced_string.charAt(index));
                        vol_ipa = vol_ipa.substring(0, i) + replaceChar;
                    }
                    flag = 2;
                }
            }else{
                if(single_ipa.equals(".")){}
                else if(flag == 0){
                    if(voiced_string.contains(single_ipa)){flag =1;}
                    else if (unvoiced_string.contains(single_ipa)){flag =2;}
                }else if(flag == 1 ){
                    if(sonorant_string.contains(single_ipa) || vowel_string.contains(single_ipa)){
                        flag =0;
                    }else if(unvoiced_string.contains(single_ipa)){
                        int index = unvoiced_string.indexOf(single_ipa);
                        String replaceChar = String.valueOf(voiced_string.charAt(index));
                        vol_ipa = vol_ipa.substring(0, i) + replaceChar + vol_ipa.substring(i+1,vol_ipa.length());
                    }
                }else if(flag == 2){
                    if(sonorant_string.contains(single_ipa) || vowel_string.contains(single_ipa)){
                        flag =0;
                    }else if(voiced_string.contains(single_ipa)){
                        int index = voiced_string.indexOf(single_ipa);
                        String replaceChar = String.valueOf(unvoiced_string.charAt(index));
                        vol_ipa = vol_ipa.substring(0, i) + replaceChar + vol_ipa.substring(i+1,vol_ipa.length());
                    }
                }
            }
        }
        return  vol_ipa;
    }

    //this helper function accepets a char as string and a string[] as input, outputs the char position
    private static int checkVoicePosition(String single_ipa, String[] list){
        int pos =-1 ;
        for(int i = 0; i< list.length; i++){
           if(single_ipa.equals(list[i])){
               pos = i;
               break;
           }
        }
        return pos;
    }
    /*
    * addStress accepts a whole sentence ipa and returns stressed ipa
    * default stress position is 0, when wiki can not give position
    * special cases to be checked first, single syllable,then always stressed
    *
    * syllable_sentence is the sentence as syllable format,need to be divide into words first
    * and then iterate words.
    * for each word, covert word string to hashmap<int, string>
    * key 0 is stressed, key
    * key -1 ~ -9 is pre-stressed
    * key 1 ~ 9 is post-stressed
    * */
    public static String addStress(String syllable_sentence, String ipa){
        String stress_ipa = "";
        String [] word_list = syllable_sentence.split(" ");
        String [] ipa_list = ipa.split(" ");
        for (int i=0; i < word_list.length; i++){
            //get the ipa from wiki
            //List<String> wiki_ipa = getIPAFromWikitionary(word_list[i].replace(".",""));
            int wiki_position = getStressPositionFromWikitionary(word_list[i].replace(".",""));
            stress_ipa = stress_ipa + stress_helper(wiki_position, word_list[i],ipa_list[i]) + " ";
        }
        return stress_ipa;
    }

    /*
    * This helper function accepts stress position and single word ipa
    * returns single word stressed ipa
    * This can be called when user wants to change single word stress
    *
    * dis the int range from -100~0~100
    * 0 is stressed, -100  is initial letter
    * if stress is at 0 position, dis = -100
    * */
    public static String stress_helper(int stress_position, String word, String ipa){
        word = word.toLowerCase();
        String stress_ipa = "";
        String[] ipa_syllables = ipa.split("\\.");
        String[] word_syllables = word.split("\\.");
            for(int i = 0; i<ipa_syllables.length; i++){
                int dis = (i == 0 ) ? -100 : i-stress_position;
                String IMF = (i == ipa_syllables.length-1) ? "M":"F";
                String cur_vowel = Utils.pickSameChar(word_syllables[i], vowel_letter_string);
                String pre_ws = new String();
                pre_ws = (i>0) ? word_syllables[i-1]:"";
                String replace = Utils.stress(IMF,stress_position, dis, cur_vowel, pre_ws, word_syllables[i], ipa_syllables[i]);
                if(dis == 0 || (dis == -100 && stress_position == 0)){
                    stress_ipa = stress_ipa + "'" + replace + ".";
                }
                else{
                    stress_ipa = stress_ipa + replace + ".";
                }
            }
        try {
            stress_ipa = stress_ipa.substring(0, stress_ipa.length() - 1);
        }catch (IndexOutOfBoundsException e){
        }
        stress_ipa = stress_ipa.replace(".'","'");
        return stress_ipa;
    }

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

    public static int getStressPositionFromWikitionary(String word){
        Locale.setDefault(new Locale("ru"));
        word = word.toLowerCase();
        int pos = 0;
        Document doc;
        String ipa_strings = new String();

        try {
            // need http protocol
            String url = "https://en.wiktionary.org/wiki/" + word;
            doc = Jsoup.connect(url).get();

            Elements E = doc.select("span.IPA");    //span with class=IPA
            for (Element e : E) {
                System.out.println("Found an IPA : " + e.text());
                ipa_strings = e.text();
            }

        } catch (IOException e) {
            System.out.println("!!!!This word is not found on wikitionary: "+word);
            pos = 0;
            //e.printStackTrace();
        }

        String[] syllables = ipa_strings.split("ɑ|a|ʌ|ɛ|i|ɪ|ɨ|o|u|ɔ|ɐ|ə");
        for(int i = 0 ; i<syllables.length; i ++){
            if(syllables[i].contains("ˈ")){
                return i;
            }
        }
        return pos;

    }

    public static void main(String[] args){
        //хоронить:xʌ.rɑˈɲitʲ
        //String word2 = "Здравствуйте, мир!";отец бы:ɑˈtʲɛdz bɨ
        //начать nɑ 'tʃʲatʲ
        String word2 = "Здравствуйте";

        String ipa2 = new String();
        ipa2 = Transcribe(word2);

        System.out.print("True ipa is ɑ.ptɑˈtʃʲitʲ ɑ.ˈpxot");
    }

    //This function prints a String[]
    public static void testPrint(String name, String[] words) {
        for (int i = 0; i < words.length; i++) {
            System.out.println("the position is " + name + " word = " + words[i]);
        }

    }
}
