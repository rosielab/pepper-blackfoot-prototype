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


    // **HashMap of Greeting Words**
    Map<String, String> greetingWords = new HashMap<String, String>()
    {{
        // Make sure the English word matches the XML filename (for tablet preview)
        put("hello", "oki");
        put("how are you","tsa niitapiiwa");
        put("not too bad","matohkwiikii");
        put("let's go","okí");
        put("yes","aa");
        put("no","saa");
        put("hello friend","oki napi");
        put("i'm doing good. you?","tsikohssokopii. kistoo?");
    }};

    // **HashMap of Food Words**
    Map<String, String> foodWords = new HashMap<String, String>()
    {{
        put("egg", "owa");
    }};

    // **HashMap of Test Function Words**
    Map<String, String> testingWords = new HashMap<String, String>()
    {{
        // Add elements of all m/c
        put("egg", "owa");
        put("fish", "mamii");
        put("bread", "napayin");
        put("water", "aohkii");
    }};
}