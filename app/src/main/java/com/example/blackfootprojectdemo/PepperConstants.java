package com.example.blackfootprojectdemo;

import java.util.HashMap;
import java.util.Map;


public interface PepperConstants
{
    // **Introductory Constants**
    String[] feelingWellConstant = new String[]{"good", "nice", "well", "fine", "okay", "better", "so so", "great", "yes", "hello", "hi"};
    String[] feelingBadConstant = new String[]{"not", "bad", "not good", "cry", "sad", "no", "worry"};

    // **PhraseText Constants**
    String[] playTextConstant = new String[]{"play", "game", "first"};
    String[] learnTextConstant = new String[]{"learn", "flashcards", "teach", "second", "middle"};
    String[] testTextConstant = new String[]{"test", "exam", "third", "last", "final"};
    String[] scoreTextConstant = new String[]{"score", "grade", "number"};
    String[] exitTextConstant = new String[]{"no", "nope", "exit", "stop", "done", "break", "not anymore", "no more"};
    String[] anyTextConstant = new String[]{"anything", "you", "something", "matter", "any", "eh", "not sure", "don't know", "uh"};

    // **Learn Menu Constants**
    String[] learnGreetingConstant = new String[]{"greeting", "first"};
    String[] learnFoodConstant = new String[]{"food", "second"};
    String[] continueConstant = new String[]{"yes", "sure", "yep", "go", "okay"};

    // **Miscellaneous Constants**
    String[] affirmationConstant = new String[]{"great", "awesome", "cool", "sweet", "nice"};
    String[] correctFeedbackConstant = new String[]{"good job", "nice work", "right on", "keep it up", "way to go", "you nailed it", "I'm impressed", "you got it"};
    String[] incorrectFeedbackConstant = new String[]{"almost got it", "so close", "not quite", "good try", "I don't think that's correct"};


    // **HashMap of Greeting Words**
    Map<String, String> greetingWords = new HashMap<String, String>()
    {{
        // Make sure the English word matches the XML filename (for tablet preview)
        put("hello", "oki");
        put("how are you","tsa niita'piiwa");
        put("not too bad","matohkwiikii");
        put("let's go","okí");
        put("yes","aa");
        put("no","saa");
        put("hello friend","oki napi");
        put("i'm doing good. you?","tsikohssokopii. kistoo?");
    }};
    String[] greetingWordsList = new String[]{"hello", "how are you", "not too bad", "let's go", "yes", "no", "hello friend", "i'm doing good. you?"};


    // **HashMap of Food Words**
    Map<String, String> foodWords = new HashMap<String, String>()
    {{
        put("egg", "owa");
        put("fish", "mamii");
        put("bread", "napayin");
        put("water", "aohkii");
        put("apples", "aipasstaamiinammiksi");
        put("oranges", "aotahkoinammiksi");
        put("burger", "pikkiaaksin");
        put("fries", "paataakistsi");
        put("desert", "pisatsoyiikan");
        put("tea", "siksikimmii");
        put("coffee", "iitapsiksikimmii");
        put("salt", "isttsiksipoko");
        put("sugar", "naapiiniiwan");

    }};
    String[] foodWordsList = new String[]{"egg", "fish", "bread", "water", "apples", "oranges", "burger", "fries", "desert", "tea", "coffee", "salt", "sugar"};
}
