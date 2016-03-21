package com.mysungrussian.mysungrussian;

/**
 * Created by hedai on 2016-03-10.
 */
public class Utils {
    public static String consonant_ipas = RuleEngine.voiced_string + RuleEngine.unvoiced_string + RuleEngine.sonorant_string;
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

    public static String stress(String IMF, Integer stresspos, Integer dis, String vowel, String pre_ws, String word_syl, String ipa_syl){
        if(vowel.equals("а")){return stress_а(dis, word_syl, ipa_syl);}
        else if(vowel.equals("о")){return stress_о(stresspos, dis, word_syl, ipa_syl);}
        else if(vowel.equals("е")){return stress_е(IMF, stresspos, dis, pre_ws, word_syl, ipa_syl);}
        else if(vowel.equals("я")){return stress_я(IMF, stresspos, dis, pre_ws, word_syl, ipa_syl);}
        else{
            return ipa_syl;
        }
    }


    /*
    * This helper function helps change the ipa for а
    * cond_y stands for "ча", "ща"
    * cond_p stands for followed by palatalized consonants or й
    * case to be handled, initial letter gives ɑ not initial syllable
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
        ipa_syl = ipa_syl.replace("ɑ", replacement);
        return ipa_syl;
    }



    /*
    * stress o
    * when stress, "o" , (unstressed in certain foreign words, case is ignored for now )
    * immediate pre stress, initial letter "ɑ"
    * case not handled, -100, 0, should be "ɑ", test case "бояться"
    * other wise "ʌ"
    * */
    public static String stress_о(Integer stresspos, Integer dis, String word_syl, String ipa_syl){
        String replacement = "o";
        if(dis == 0 ||(dis == -100 && stresspos == 0)){}
        else if(dis == -100 && word_syl.substring(0,1).equals("о")){replacement = "ɑ";}
        else if(dis == -1){replacement = "ɑ";}
        else {replacement = "ʌ"; }
        ipa_syl = ipa_syl.replace("o",replacement);
        return ipa_syl;
    }

    /*
    * stress "е", {"jɛ", "ɛ", "ɪ", "jɪ", "je", "e", "ji", "i", "ɨ"});
    * four conditions
    * stressed:unstressed, initial/following a vowel:following a cons
    * followed by unpalatalized cons/final:NOT, interpalatalized
    * */
    public static String stress_е(String IMF, Integer stresspos, Integer dis, String pre_ws, String word_syl, String ipa_syl) {
        System.out.println("prews + word_syl " + (pre_ws + word_syl));
        String replacement  = "jɛ";
        String pattern_iv = "(.*ае.*)|(.*эе.*)|(.*ые.*)|(.*уе.*)|(.*ое.*)|(.*яе.*)|(.*ее.*)|(.*ёе.*)|(.*юе.*)|(.*ие.*)";
        boolean cond_iv = (dis == -100 && word_syl.substring(0,1).equals("е"))||((pre_ws+word_syl).matches(pattern_iv));
        boolean cond_p = ipa_syl.matches(".*ɛ\\wʲ.*")||ipa_syl.matches(".*ɛtʃʲ.*")||ipa_syl.matches(".*ɛj.*");
        boolean cond_final = (IMF == "F")&&((RuleEngine.consonants_letters).contains(word_syl.substring(word_syl.length()-1)))&&(word_syl.substring(word_syl.length()-2).equals("е"));
        boolean cond_post_notP = false;
        boolean cond_pre_cons = false;
        try {
            cond_post_notP = (!cond_p) && (consonant_ipas.contains(ipa_syl.substring(ipa_syl.indexOf("ɛ") + 1)));
        }catch (IndexOutOfBoundsException e){
            cond_post_notP =false;
        }
        try{
            cond_pre_cons = RuleEngine.consonants_letters.contains(word_syl.substring(word_syl.indexOf("е")-1));
        }catch (IndexOutOfBoundsException e){
            cond_pre_cons =false;
        }
        String pattern_interPal = "(.*ʲɛ\\wʲ.*)|(.*ʲɛtʃʲ.*)|(.*ʲɛj.*)";
        boolean cond_interPal = ipa_syl.matches(pattern_interPal);
        if(dis == 0 || (dis == -100 && stresspos == 0)){
            if (cond_pre_cons){
                if(cond_final || cond_post_notP){
                    replacement = "ɛ";
                }else if (cond_p){
                    replacement = "e";
                }
            }else if(cond_iv){
                if(cond_post_notP){
                    replacement = "jɛ";
                }else if(cond_p){
                    replacement = "je";
                }
            }
        }else{
            if(word_syl.matches(".*же.*")||word_syl.matches(".*же.*")||word_syl.matches(".*же.*")){
                replacement = "ɨ";
            }else if(ipa_syl.matches(".*ʲɛ.*") && (cond_final || cond_post_notP)){
                replacement = "ɪ";
            }else if (cond_interPal){
                replacement = "i";
            }else if (cond_iv){
                if (cond_p){
                    replacement = "ji";
                }else if (cond_final || cond_post_notP){
                    replacement = "jɪ";
                }
            }
        }
        ipa_syl = ipa_syl.replace("jɛ", replacement);
        return ipa_syl;
    }

    /*
    * Stress я {"ɑ", "jɑ", "a", "ja", "ɪ", "jɪ", "i", "ji" ,"ʌ"})
    * special cases are "ʌ"(not considered yet), "ɑ",ая, яя
    * IMF indicates if it is the last syllable
    *
    * */
    public static String stress_я(String IMF, Integer stresspos, Integer dis, String pre_ws, String word_syl, String ipa_syl){
        String replacement = "ɑ";
        boolean cond_p = ipa_syl.matches(".*ɑ\\wʲ.*")||ipa_syl.matches(".*ɑtʃʲ.*")||ipa_syl.matches(".*ɑj.*");
        String pattern_iv = "(.*ая.*)|(.*эя.*)|(.*ыя.*)|(.*уя.*)|(.*оя.*)|(.*яя.*)|(.*ея.*)|(.*ёя.*)|(.*юя.*)|(.*ия.*)";
        boolean cond_iv = (dis == -100 && word_syl.substring(0,1).equals("я"))||((pre_ws+word_syl).matches(pattern_iv));
        boolean cond_final = (IMF == "F")&&((RuleEngine.consonants_letters).contains(word_syl.substring(word_syl.length()-1)))&&(word_syl.substring(word_syl.length()-2).equals("я"));
        boolean cond_post_notP = false;
        boolean cond_pre_P = ipa_syl.matches(".*ʲɑ.*");
        try {
            cond_post_notP = (!cond_p) && (consonant_ipas.contains(ipa_syl.substring(ipa_syl.indexOf("ɑ") + 1)));
        }catch (IndexOutOfBoundsException e){
            cond_post_notP =false;
        }
        if(dis == 0 || (dis == -100 & stresspos == 0)){
            if(cond_iv){
                if(cond_post_notP || cond_final){
                    replacement = "jɑ";
                }else if(cond_p){
                    replacement = "ja";
                }
            }else{
                replacement = (cond_p)? "a":"ɑ";
            }
        }else{
            String ending_two = "";
            try{
                ending_two = word_syl.substring(word_syl.length()-2,word_syl.length());
            }catch(IndexOutOfBoundsException e){}
            if(IMF == "F" && (ending_two.equals("ая")||ending_two.equals("яя"))){
                replacement = "ɑ";
            }else if(cond_iv){
                if(cond_post_notP || cond_final){
                    replacement = "jɪ";
                }else if(cond_p){
                    replacement = "ji";
                }
            }else if (cond_pre_P){
                replacement = "ɪ";
            }

        }
        ipa_syl = ipa_syl.replace("jɑ", replacement);
        return ipa_syl;
    }


}
