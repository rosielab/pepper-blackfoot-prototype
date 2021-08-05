package com.example.blackfootprojectdemo;

import java.util.HashMap;
import java.util.Map;


public interface PepperConstants
{
    // **Introductory Constants**
    String[] feelingWellConstant = new String[]{"good", "nice", "well", "fine", "okay", "better", "so so", "great", "yes", "hello", "hi"};
    String[] feelingBadConstant = new String[]{"not", "bad", "not good", "cry", "sad", "no", "worry", "terrible"};

    // **PhraseText Constants**
    String[] playTextConstant = new String[]{"play", "game", "first"};
    String[] storyTextConstant = new String[]{"hear", "story", "hear story", "listen"};
    String[] learnTextConstant = new String[]{"learn", "flashcards", "teach", "second", "middle"};
    String[] testTextConstant = new String[]{"test", "exam", "third", "last", "final"};
    String[] scoreTextConstant = new String[]{"score", "grade", "number"};
    String[] exitTextConstant = new String[]{"no", "nope", "exit", "stop", "done", "break", "not anymore", "no more"};
    String[] anyTextConstant = new String[]{"anything", "you", "something", "matter", "any", "eh", "not sure", "don't know", "uh", "choose", "pick"};

    // **Story Menu Constants**
    String[] welcomeStoryConstant = new String[]{"welcome", "message", "first", "one"};
    String[] smallNumberStoryConstant = new String[]{"small", "number", "counts", "counting", "hundred", "second", "two"};
    String[] pauseStoryConstant = new String[]{"pause", "wait"};
    String[] endStoryConstant = new String[]{"stop", "done", "finish", "end"};
    String[] continueStoryConstant = new String[]{"yes", "sure", "yep", "go", "okay", "continue", "story", "proceed"};
    String[] restartStoryConstant = new String[]{"restart", "reset", "beginning", "front"};
    String[] welcomeStorySubtitles = new String[]{"Hello my friends.", "My name is Yellowhorn.", "I am very glad that you are able to visit here.", "Come on in.", "Stay for a while.", "Read the articles we have here.", "Come again."};

    // **Learn Menu Constants**
    String[] learnGreetingConstant = new String[]{"greeting", "first", "greetings"};
    String[] learnFoodConstant = new String[]{"food", "second"};
    String[] continueConstant = new String[]{"yes", "sure", "yep", "go", "okay", "continue"};

    // **Miscellaneous Constants**
    String[] affirmationConstant = new String[]{"great", "awesome", "cool", "sweet", "nice"};
    String[] correctFeedbackConstant = new String[]{"good job", "nice work", "right on", "keep it up", "way to go", "you nailed it", "I'm impressed", "you got it"};
    String[] incorrectFeedbackConstant = new String[]{"almost got it", "so close", "not quite", "good try", "I don't think that's correct"};
    String[] yesConstant = new String[]{"yes", "okay", "sure"};
    String[] noConstant = new String[]{"no", "nah", "nope"};


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
        //put("meat", "i'ksisako");

    }};
    String[] foodWordsList = new String[]{"egg", "fish", "bread", "water", "apples", "oranges", "burger", "fries", "desert", "tea", "coffee", "salt", "sugar"}; //need to add "meat"

    // **HashMap of People Words**
    Map<String, String> peopleWords = new HashMap<String, String>()
    {{

        put("woman", "aakii");
        put("man", "ninaa");
        put("girl", "aakiikoan");
        put("boy", "saahkomaapi");
        put("mother", "iksisst");
        put("father", "inn");
        put("child", "pookaa");

    }};
    String[] peopleWordsList = new String[]{"woman", "man", "girl", "boy", "mother", "father", "child"};

    // **HashMap of House Words**
    Map<String, String> houseWords = new HashMap<String, String>()
    {{

        put("bathroom", "makapoiyiss");
        put("kitchen", "itoiyo'soap");
        put("car", "aiksistomatomahka");
        put("door", "kitsim");
        put("window", "ksisstsikomstan");
        put("elevator", "aisspaohpii");
        put("dog", "imitaa");

    }};
    String[] houseWordsList = new String[]{"bathroom", "kitchen", "car", "door", "window", "elevator", "dog"};

    // **HashMap of Location Words**
    Map<String, String> locationWords = new HashMap<String, String>()
    {{

        put("cafe", "itoiyo'pii");
        put("house", "naapoiyiss");
        put("store", "itaohpomoapii");
        put("cinema", "aisaksittoo");
        put("tipi", "niitoiyiss");
        put("night club", "itaisimmioapii");
        put("movie", "aisaiksisttoo");

    }};
    String[] locationWordsList = new String[]{"cafe", "house", "store", "cinema", "tipi", "night club", "movie"};

    // **HashMap of Sentence Words**
    Map<String, String> sentenceWords = new HashMap<String, String>()
    {{

        put("today", "annohk");
        put("this morning", "ksisskanaotonni");
        put("tomorrow", "aapinakos");
        put("i will go", "nitaakitapoo");
        put("i will eat", "nitaaksoyi");
        put("i went", "nichitapoo");
        put("please pass the", "kippohkokkit");
        put("and", "ki");
        put("evening", "otako");
        put("where", "tsima");
        put("who", "tahkaa");
        put("yesterday", "matonni");

    }};
    String[] sentenceWordsList = new String[]{"today", "this morning", "tomorrow", "i will go", "i will eat", "i went", "please pass the", "and", "evening", "where", "who", "yesterday"};
}
