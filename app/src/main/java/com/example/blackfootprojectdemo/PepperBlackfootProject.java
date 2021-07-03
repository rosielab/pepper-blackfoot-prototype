package com.example.blackfootprojectdemo;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RawRes;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.util.PhraseSetUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class PepperBlackfootProject extends RobotActivity implements RobotLifecycleCallbacks
{
    private static final String TAG = "MainActivity";
    private static boolean continue_looping = true;
    private QiContext pepper_context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide speech bar from primary view (only shown when Pepper's talked to).
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.TOP);
        setContentView(R.layout.main_menu);

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);
        Log.i(TAG, "OnCreate function succeeded.");
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext)
    {
        // The robot focus is gained.
        this.pepper_context = qiContext;
        introduction();
        while (continue_looping)
        {
            updateTabletImage("main_menu");
            menuSystem();
        }
        updateTabletImage("main_menu");
        // Translation: See you later, have a good day.
        sayText("Kitatama'sino! Have a good day.");
    }

    // Initial introduction by Pepper
    private void introduction()
    {
        // Pepper waves while introducing himself
        runAnimation(R.raw.hello_a001);
        sayText("Oki! I'm Pepper! How are you doing today?");
        PhraseSet feeling_well = PhraseText("good", "nice", "well", "fine", "okay", "better", "so so", "great", "yes", "hello");
        PhraseSet not_feeling_well = PhraseText("not", "bad", "not good", "cry", "sad", "no");

        ListenText(feeling_well, not_feeling_well);
        runAnimation(R.raw.affirmation_a011);
        sayText("Let's learn some Blackfoot together!");
    }

    // Menu system with the various options
    private void menuSystem()
    {
        runAnimation(R.raw.show_tablet_a002);
        sayText("Would you like to play a game, learn new words, or test your Blackfoot knowledge?");

        PhraseSet play_text = PhraseText("play", "game", "first");
        PhraseSet learn_text = PhraseText("learn", "layout/flashcards", "teach", "second", "middle");
        PhraseSet test_text = PhraseText("test", "exam", "third", "last", "final");
        PhraseSet score_text = PhraseText("score", "grade", "number");
        PhraseSet any_text = PhraseText("anything", "you", "something", "matter", "any");
//        PhraseSet exit_tablet = PhraseText("exit", "stop", "done", "break");

        PhraseSet matchedMenuOption = ListenText(play_text, learn_text, test_text, any_text);

        if (PhraseSetUtil.equals(matchedMenuOption,play_text))
        {
            sayText("Let's play!");
        }
        else if (PhraseSetUtil.equals(matchedMenuOption,learn_text))
        {
            sayText("Let's learn!");
            learnMenu();
        }
        else if (PhraseSetUtil.equals(matchedMenuOption,test_text))
        {
            sayText("Let's do a test!");
            testMenu();
        }
        else if ((PhraseSetUtil.equals(matchedMenuOption,score_text)))
        {
            //String score = checkScore();
            //sayText("Your last score was " + score + ". Feel free to test your knowledge again!");

        }
        else if (PhraseSetUtil.equals(matchedMenuOption,any_text))
        {
            sayText("Sure! I'm happy to choose.");
        }
//        else if (PhraseSetUtil.equals(matchedMenuOption,exit_tablet))
//        {
//            continue_looping = false;
//        }
    }

    // When user chooses to learn
    private void learnMenu()
    {
        // Hashmap (dictionary) for the flashcard words
        Map<String, String> greetingWords = new HashMap<String, String>()
        {{
            // Make sure the English word matches the XML filename (for tablet preview)
            put("hello", "oki");
            put("how are you","tsa niitapiiwa");
            put("not too bad","matohkwiikii");
        }};

        Map<String, String> foodWords = new HashMap<String, String>()
        {{
            put("egg", "owa");
        }};

        sayText("I can teach you greetings and food names! What would you like to start with?");
        runAnimation(R.raw.nicereaction_a002);
        PhraseSet greeting_category = PhraseText("greeting", "first");
        PhraseSet food_category = PhraseText("food", "second");
        PhraseSet any_text = PhraseText("anything", "you", "something", "matter", "any");

        PhraseSet matchedMenuOption = ListenText(greeting_category, food_category, any_text);

        Map<String, String> current_hash_set = null;
        boolean greetingOptionChosen = false;
        boolean foodOptionChosen = false;
        if (PhraseSetUtil.equals(matchedMenuOption,greeting_category))
        {
            greetingOptionChosen = true;
            current_hash_set = greetingWords;
        }
        else if (PhraseSetUtil.equals(matchedMenuOption,food_category))
        {
            foodOptionChosen = true;
            current_hash_set = foodWords;
        }
        else if (PhraseSetUtil.equals(matchedMenuOption,any_text))
        {
            sayText("Sure! I'm happy to choose.");

            // Get a random integer and choose the appropriate category.
            int randomNum = ThreadLocalRandom.current().nextInt(1, 3);
            switch (randomNum)
            {
                case 1:
                    current_hash_set = greetingWords;
                    break;
                case 2:
                    current_hash_set = foodWords;
                    break;
            }
        }
        for (String word : current_hash_set.keySet())
        {
            // Translation of English word
            String blackfootWord = current_hash_set.get(word);

            // Remove whitespace in word to accompany file name.
            updateTabletImage(word.replaceAll("\\s+",""));
            runAnimation(R.raw.show_tablet_a002);
            sayText(word + " in Blackfoot is " + blackfootWord + ". Can you now tell me what " + blackfootWord + " is in English?");

            // Ask end user to repeat the translation
            PhraseSet word_text = PhraseText(word);
            PhraseSet matchedFlashcardOption = ListenText(word_text);

            if (PhraseSetUtil.equals(matchedFlashcardOption,word_text))
            {
                sayText("Yes! " + word + " is " + blackfootWord + ".");
                runAnimation(R.raw.affirmation_a011);
            }

            sayText("Would you like to learn another word?");
            PhraseSet continueWithWords = PhraseText("yes", "sure", "yep", "go");
            PhraseSet exitLearning = PhraseText("no", "nope", "exit", "stop", "done", "break", "not anymore", "no more");
            PhraseSet continueLearning = ListenText(continueWithWords, exitLearning);

            if (PhraseSetUtil.equals(continueLearning,exitLearning))
            {
                break;
            }
        }
    }


    // When user chooses to test. Opens/runs test module
    private void testMenu()
    {
        // HashMap of all words testing
        Map<String, String> testingWords = new HashMap<String, String>()
        {{
            // Add elements of all m/c
            put("egg", "owa");
            put("fish", "mamii");
            put("bread", "napayin");
            put("water", "aohkii");
        }};

        sayText("Let's begin!");
        runAnimation(R.raw.nicereaction_a002);
        double totalScore = 0;

        // Asks four questions for each word in HashMap
        for (String englishWord: testingWords.keySet())
        {
            boolean correctAnswer = false;
            double numberTries = 0;

            //set correct word's corresponding image, get string correct answer from HashMap
            updateTabletImage(englishWord.concat("testing"));
            String blackfootWord = testingWords.get(englishWord);


            // loop through HashMap keys, adds all but correct to string of incorrect words, separate by commas.
            String incorrectEnglishWordsString = "";
            int keyCounter = 0;
            for (String keyName: testingWords.keySet()) {
                keyCounter++;
                if (!keyName.equals(englishWord)) {
                    incorrectEnglishWordsString += keyName.toString();
                }
                if (keyCounter != testingWords.size())
                {
                    incorrectEnglishWordsString += ",";
                }
            }
            // Get correct/incorrect/unknown answers. split incorrect word string into separate word strings
            PhraseSet correctWord = PhraseText(englishWord);
            PhraseSet incorrectEnglishWords = PhraseText(incorrectEnglishWordsString.split(","));
            PhraseSet dontKnow = PhraseText("eh", "not sure", "I don't know", "don't know", "uh");

            // Listen to user and ask question until answer is correct
            while (!correctAnswer && numberTries < 3)
            {
                sayText("What is " + blackfootWord + " in English?");
                Listen listen = ListenBuilder.with(pepper_context).withPhraseSets(correctWord, incorrectEnglishWords, dontKnow).build();
                ListenResult listenResult = listen.run();
                PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();

                // Check for correct answer
                if (PhraseSetUtil.equals(matchedPhraseSet,correctWord))
                {
                    sayText("Yes! " + englishWord + " is " + blackfootWord + ". You got it!");
                    runAnimation(R.raw.affirmation_a002);
                    correctAnswer = true;
                }
                else if (PhraseSetUtil.equals(matchedPhraseSet, incorrectEnglishWords))
                {
                    sayText("Hmm, I don't think that is correct. Try again!");
                    runAnimation(R.raw.thinking_a001);
                    numberTries++;
                }
                else if (PhraseSetUtil.equals(matchedPhraseSet, dontKnow))
                {
                    sayText("Don't worry it's okay, here I'll help you!");
                    numberTries = 3;
                }

                // Too many wrong tries, review answer
                if (numberTries == 3)
                {
                    sayText(blackfootWord + " is " + englishWord + " in English. Repeat after me: " + englishWord + ".");
                    runAnimation(R.raw.show_tablet_a002);
                    boolean continueBool = false;
                    while (!continueBool)
                    {
                        PhraseSet matchedCorrectionOption = ListenText(correctWord);
                        if (PhraseSetUtil.equals(matchedCorrectionOption,correctWord))
                        {
                            sayText("You got it! Let's continue.");
                            runAnimation(R.raw.affirmation_a011);
                            continueBool = true;
                        }
                    }
                }
            } // end while loop: got correct answer

            //update score if number_tries is high enough: 2 tries or less needed. 3 or more tries allocates 0 points
            if (numberTries <= 2)
            {
                totalScore += 1-(0.25 * numberTries);
            }
        } // end of all questions, return to main menu
        sayText("Your final score is " + totalScore + "/" + testingWords.size() +  ".0 or " + totalScore/testingWords.size()*100 + "%. Good job!");
        runAnimation(R.raw.affirmation_a011);
    } // end testMenu()


    /*
    Param: String text that Pepper should say
    Post: Pepper speaks the text provided
    */
    private void sayText(String textToSpeech)
    {
        Say say = SayBuilder.with(pepper_context)
                .withText(textToSpeech)
                .build();
        say.run();
    }

    /*
    Param: Strings separated by commas
    Post: Returns the phrase that Pepper should recognize or listen to
    */
    // The "..." notation represents Varargs in Java (accepting parameters of any length)
    private PhraseSet PhraseText(String ... textToSpeech)
    {
        PhraseSet phraseSetPlay = PhraseSetBuilder.with(pepper_context)
                .withTexts(textToSpeech)
                .build();
        return phraseSetPlay;
    }

    /*
    Param: PhraseSet(s) returned by the PhraseSet function
    Post: Returns the phrase that Pepper has recognized
    */
    private PhraseSet ListenText(PhraseSet ... phrase_set)
    {
        Listen listen = ListenBuilder.with(pepper_context)
                .withPhraseSets(phrase_set)
                .build();

        ListenResult listenResult = listen.run();

        // Log what Pepper has heard in console
        Log.i(TAG, "Heard phrase: " + listenResult.getHeardPhrase().getText());
        PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();

        // Return the phrase Pepper recognized
        return matchedPhraseSet;
    }

    /*
    Pre: Proper XML (tablet image) file name
    Param: String representing the file name (without the .xml extension)
    Post: Updates the tablet image
    */
    private void updateTabletImage(String file_name)
    {
        // Need to run on UI thread to change tablet image
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    int file_id = getResources().getIdentifier(file_name, "layout", getPackageName());
                    setContentView(file_id);
                }
                catch (Exception incorrectFileName)
                {
                    Log.e(TAG, "File not found: " + incorrectFileName);
                    setContentView(R.layout.activity_main);
                }
            }
        });
    }

    /*
    Param: Animation title with the R.raw prefix (e.g. R.raw.elephant_a001)
    Post: Pepper plays the relevant animation
    */
    private void runAnimation(@RawRes int animation)
    {
        Animation build_animation = AnimationBuilder.with(pepper_context)
                .withResources(animation)
                .build();

        Animate animate = AnimateBuilder.with(pepper_context)
                .withAnimation(build_animation)
                .build();

        animate.async().run();
    }

    @Override
    public void onRobotFocusLost()
    {
        this.pepper_context = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        QiSDK.register(this, this);
        super.onResume();
        Log.d("onResume", "QiSDK.register");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        QiSDK.unregister(this);
        Log.d("onPause", "QiSDK.unregister pause");
    }

    @Override
    public void onRobotFocusRefused(String reason)
    {
        // The robot focus is refused.
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        super.onDestroy();
        this.pepper_context = null;
        QiSDK.unregister(this, this);
        super.onDestroy();
    }
}

    /*
    // Future Reference: OnClickMethods (Button) and Changing Text
    public void playButtonClick (View playButtonView) {
        // Find "resource by ID" called WelcomeTextBox and modify it
        TextView welcomeText = findViewById(R.id.welcomeTextBox);
        welcomeText.setText("Hello, " + user_name + "!");
    }
    // Easy, medium, difficult. Some say/show the word and m/c the english words. Harder don't have m/c english and are audio only.

    */