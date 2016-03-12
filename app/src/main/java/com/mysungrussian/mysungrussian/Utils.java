package com.mysungrussian.mysungrussian;

/**
 * Created by hedai on 2016-03-10.
 */
public class Utils {
    /*this helper function compares two strings and gives out the char
    *that exists in both strings
    */
    public static String pickSameChar(String word, String list){
        String sameChar = new String();
        if(word.length()>0) {
            for (int i = 0; i < word.length(); i++) {
                String tmp = String.valueOf(word.charAt(i));
                if(list.contains(tmp)){
                    return tmp;
                }
            }
        }
        return  sameChar;
    }

    public static String stress(Integer dis, String vowel, String word_syl, String ipa_syl){
        if(vowel.equals("а")){return stress_а(dis, word_syl, ipa_syl);}

        return "No Vowel";
    }


    /*
    * This helper function helps change the ipa for а
    * cond_y stands for "ча", "ща"
    * cond_p stands for followed by palatalized consonants or й
    *
    * */

    public static String stress_а(Integer dis, String word_syl, String ipa_syl){
        String replacement = "ɑ";
        boolean cond_y = word_syl.contains("ча")||word_syl.contains("ща");
        boolean cond_p = ipa_syl.matches(".*ɑ\\wʲ.*")||ipa_syl.matches(".*ɑtʃʲ.*")||ipa_syl.matches(".*ɑj.*");
        if(dis == -100){}
        else if(!cond_y){replacement = (Math.abs(dis) == 1 )? "ɑ" : "ʌ";}
        else if(cond_y && cond_p && dis ==0){replacement = "a";}
        else if(cond_y && cond_p && dis !=0){replacement = (dis > 0 )? "ɪ" : "i";}
        else if(cond_y && !cond_p && dis<0){replacement = "ɪ";}
        System.out.println("Replacement " + replacement);
        ipa_syl = ipa_syl.replace("ɑ", replacement);
        return ipa_syl;
    }


}
