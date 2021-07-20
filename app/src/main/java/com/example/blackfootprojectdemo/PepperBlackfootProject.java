package com.example.blackfootprojectdemo;

import android.content.res.Resources;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RawRes;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ApproachHumanBuilder;
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
import com.aldebaran.qi.sdk.object.human.Human;
import com.aldebaran.qi.sdk.object.humanawareness.ApproachHuman;
import com.aldebaran.qi.sdk.object.humanawareness.HumanAwareness;
import com.aldebaran.qi.sdk.util.PhraseSetUtil;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class PepperBlackfootProject extends RobotActivity implements RobotLifecycleCallbacks, PepperConstants
{
    MediaPlayer mediaPlayer;
    private static final String TAG = "MainActivity";
    private static boolean continue_looping;
    private QiContext pepper_context = null;
    private double totalUserScore = 0;
    private double foodScore = 0;
    private double greetingScore = 0;
    private int totalWordsTested = 0;
    private int totalFoodWordsTested = 0;
    private int totalGreetingWordsTested = 0;
    private int testingWordsSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Hide speech bar from primary view (only shown when Pepper's talked to).
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.TOP);

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);
        Log.i(TAG, "OnCreate function succeeded.");
    }

    @Override
    // The robot focus is gained.
    public void onRobotFocusGained(QiContext qiContext)
    {
        this.pepper_context = qiContext;
        continue_looping = true;
        updateTabletImage("splashscreen");
        introduction();
        do
        {
            updateTabletImage("main_menu");
            menuSystem();
        } while (continue_looping);
    }

    // Initial introduction by Pepper
    private void introduction()
    {
        // Pepper waves while introducing himself
        runAnimation(R.raw.hello_a001);
        /*
        playMedia("greeting"); // length is 13s, +1s delay on Pepper
        pausePepper(14);
        */
        sayText("Oki!");
        playMedia("learn_hello");
        sayText("I'm Pepper! How are you doing today?");
        PhraseSet feelingWell = PhraseText(feelingWellConstant);
        PhraseSet feelingBad = PhraseText(feelingBadConstant);

        ListenText(feelingWell, feelingBad);
        runAnimation(R.raw.affirmation_a011);
        sayText("Let's learn some Blackfoot together!");
    }

    // Menu system outlining the various options
    private void menuSystem() {
        runAnimation(R.raw.show_tablet_a002);
        sayText("Would you like to play a game, hear a story, learn new words, test your knowledge, check your score or stop?");

        PhraseSet playText = PhraseText(playTextConstant);
        PhraseSet storyText = PhraseText(storyTextConstant);
        PhraseSet learnText = PhraseText(learnTextConstant);
        PhraseSet testText = PhraseText(testTextConstant);
        PhraseSet scoreText = PhraseText(scoreTextConstant);
        PhraseSet exitTablet = PhraseText(exitTextConstant);
        PhraseSet anyText = PhraseText(anyTextConstant);

        PhraseSet matchedMenuOption = ListenText(playText, storyText, learnText, testText, scoreText, anyText, exitTablet);

        if (PhraseSetUtil.equals(matchedMenuOption, playText)) {
            sayText("Let's play!");
        } else if (PhraseSetUtil.equals(matchedMenuOption, storyText)) {
            sayText("Let's hear a story!");
            storyMenu();
        } else if (PhraseSetUtil.equals(matchedMenuOption, learnText)) {
            sayText("Let's learn!");
            learnMenu();
        } else if (PhraseSetUtil.equals(matchedMenuOption, testText)) {
            sayText("Let's do a test!");
            testMenu();
        } else if ((PhraseSetUtil.equals(matchedMenuOption, scoreText))) {
            checkScore();
        } else if (PhraseSetUtil.equals(matchedMenuOption, anyText)) {
            sayText("Sure! I'm happy to choose.");
        } else if (PhraseSetUtil.equals(matchedMenuOption, exitTablet)) {
            // Kitatama'sino: See you later.
            sayText("Kitatama'sino! Have a good day.");

            continue_looping = false;
            onRobotFocusGained(pepper_context);
        }
    }

    //Pepper's Story Function to hear stories in Blackfoot
    private void storyMenu()
    {
        updateTabletImage("storysplashscreen");
        sayText("Would you like to hear a welcome message or the small number story?");
        runAnimation(R.raw.nicereaction_a002);

        // Ask the user which story they want to hear
        PhraseSet welcomeStoryCategory = PhraseText(welcomeStoryConstant);
        PhraseSet smallNumberStoryCategory = PhraseText(smallNumberStoryConstant);
        PhraseSet anyText = PhraseText(anyTextConstant);

        PhraseSet matchedMenuOption = ListenText(welcomeStoryCategory, smallNumberStoryCategory, anyText);
        String chosenStory = "";
        int audioFileDuration = 0;

        Map<String, String> currentHashSet = null;
        if (PhraseSetUtil.equals(matchedMenuOption,welcomeStoryCategory))
        {
            chosenStory = "welcome_message";

        }
        else if (PhraseSetUtil.equals(matchedMenuOption,smallNumberStoryCategory))
        {
            chosenStory = "small_number_counts";

        }
        else if (PhraseSetUtil.equals(matchedMenuOption,anyText))
        {
            sayText("Sure! I'm happy to choose.");

            // Get a random integer and choose the appropriate category.
            int randomNum = ThreadLocalRandom.current().nextInt(1, 3);
            switch (randomNum)
            {
                case 1:
                    chosenStory = "welcome_message";
                    audioFileDuration = 14;
                    break;
                case 2:
                    chosenStory = "small_number_counts";
                    audioFileDuration = 197;
                    break;
            }
        }
        updateTabletImage("story");
        sayText("Say done if you want to end the story.");
        playMedia(chosenStory);
        PhraseSet pauseStory = PhraseText(pauseStoryConstant);
        PhraseSet matchedPauseOption = ListenText(pauseStory);


        //pausePepper(audioFileDuration);
        // add time delay based on length of audio file

        if (PhraseSetUtil.equals(matchedPauseOption, pauseStory)) {
            mediaPlayer.stop();
            sayText("Returning to main menu.");
            return;
        }


    }

    // Pepper's Learn Function to teach words
    private void learnMenu()
    {
        updateTabletImage("vocabularysplashscreen");
        sayText("I can teach you greetings and food names! What would you like to start with?");
        runAnimation(R.raw.nicereaction_a002);

        // Ask the user which word categories they would like to learn first.
        PhraseSet greetingCategory = PhraseText(learnGreetingConstant);
        PhraseSet foodCategory = PhraseText(learnFoodConstant);
        PhraseSet anyText = PhraseText(anyTextConstant);

        PhraseSet matchedMenuOption = ListenText(greetingCategory, foodCategory, anyText);

        Map<String, String> currentHashSet = null;
        if (PhraseSetUtil.equals(matchedMenuOption,greetingCategory))
        {
            currentHashSet = greetingWords;
        }
        else if (PhraseSetUtil.equals(matchedMenuOption,foodCategory))
        {
            currentHashSet = foodWords;
        }
        else if (PhraseSetUtil.equals(matchedMenuOption,anyText))
        {
            sayText("Sure! I'm happy to choose.");

            // Get a random integer and choose the appropriate category.
            int randomNum = ThreadLocalRandom.current().nextInt(1, 3);
            switch (randomNum)
            {
                case 1:
                    currentHashSet = greetingWords;
                    break;
                case 2:
                    currentHashSet = foodWords;
                    break;
            }
        }
        assert currentHashSet != null;

        // Iterate through each word until the user wants to stop
        for (String word : currentHashSet.keySet())
        {

            // Translation of English word
            String blackfootWord = currentHashSet.get(word);

            // Remove whitespace and characters in word to accompany file name.
            String wordWithoutSpaces = word.replaceAll("[\\s .?']","");
            String learnWordAudioFile = "learn_" + wordWithoutSpaces;
            updateTabletImage(wordWithoutSpaces);
            runAnimation(R.raw.show_tablet_a002);

            // Ask the user to speak the English translation
            sayText(word + " in Blackfoot is " + blackfootWord);
            playMedia(learnWordAudioFile);
            pausePepper(1);

            sayText("Can you now tell me what " + blackfootWord);
            playMedia(learnWordAudioFile);
            pausePepper(1);
            sayText(" is in English?");

            // Ask end user to repeat the translation
            PhraseSet word_text = PhraseText(word);
            PhraseSet matchedFlashcardOption = ListenText(word_text);

            if (PhraseSetUtil.equals(matchedFlashcardOption,word_text))
            {
                sayText(randomString(correctFeedbackConstant) + "! " + word + " is " + blackfootWord + ".");
                playMedia(learnWordAudioFile);
                pausePepper(1);
                runAnimation(R.raw.affirmation_a011);
            }

            boolean continueListeningBlackfoot = true;
            PhraseSet continueWithWords = PhraseText(continueConstant);
            PhraseSet exitLearning = PhraseText(exitTextConstant);

            // Ask the user to speak the Blackfoot word
            do
            {
                sayText("Now, repeat after me. " + blackfootWord);
                playMedia(learnWordAudioFile);

                // Pause Pepper for 5 seconds.
                pausePepper(5);
                sayText(randomString(affirmationConstant) + "! Would you like to hear that again?");
                runAnimation(R.raw.nicereaction_a002);

                PhraseSet listenAgain = ListenText(continueWithWords, exitLearning);

                if (PhraseSetUtil.equals(listenAgain,exitLearning))
                {
                    continueListeningBlackfoot = false;
                }

            } while (continueListeningBlackfoot);

            // Ask the user if they would like to continue to exit.

            sayText("You're doing great, shall I continue with another word?");
            PhraseSet continueLearning = ListenText(continueWithWords, exitLearning);

            if (PhraseSetUtil.equals(continueLearning,exitLearning))
            {
                sayText("Oh okay, for sure!");
                break;
            }
        }
    }

    // When user chooses to test. Opens/runs test module
    private void testMenu()
    {
        updateTabletImage("vocabularysplashscreen");
        sayText("I can test you on greetings and food names! What would you like to start with?");
        runAnimation(R.raw.nicereaction_a002);

        // Ask the user which word categories they would like to learn first.
        PhraseSet greetingCategory = PhraseText(learnGreetingConstant);
        PhraseSet foodCategory = PhraseText(learnFoodConstant);
        PhraseSet anyText = PhraseText(anyTextConstant);
        PhraseSet continueWithWords = PhraseText(continueConstant);
        PhraseSet exitLearning = PhraseText(exitTextConstant);

        PhraseSet matchedMenuOption = ListenText(greetingCategory, foodCategory, anyText);

        // Initialize testing HashMap and corresponding matching string key array
        Map<String, String> currentHashSet = null;
        String[] wordList = null;

        // Listen for vocabulary test user would like, set matching vocabulary set
        if (PhraseSetUtil.equals(matchedMenuOption,greetingCategory))
        {
            currentHashSet = greetingWords;
            wordList = greetingWordsList;
        }
        else if (PhraseSetUtil.equals(matchedMenuOption,foodCategory))
        {
            currentHashSet = foodWords;
            wordList = foodWordsList;
        }
        // Randomize vocab test choice for user
        else if (PhraseSetUtil.equals(matchedMenuOption,anyText))
        {
            sayText("Sure! I'm happy to choose.");

            // Get a random integer and choose the appropriate category.
            // reset vocabulary word scores
            int randomNum = ThreadLocalRandom.current().nextInt(1, 3);
            switch (randomNum)
            {
                case 1:
                    currentHashSet = greetingWords;
                    wordList = greetingWordsList;
                    greetingScore = 0;
                    totalGreetingWordsTested = 0;
                    break;
                case 2:
                    currentHashSet = foodWords;
                    wordList = foodWordsList;
                    foodScore = 0;
                    totalFoodWordsTested = 0;
                    break;
            }
        }
        assert currentHashSet != null;

        sayText("Alright, let's begin!");
        runAnimation(R.raw.nicereaction_a002);
        totalUserScore = 0;
        totalWordsTested = 0;
        testingWordsSize = currentHashSet.size();

        // randomize vocabulary so each test is in different order
        shuffleVocabulary(wordList);

        // Asks question for each vocab work in wordList and thus HashMap
        for (String listWord: wordList)
        {
            totalWordsTested++;
            String englishWord = null;
            String blackfootWord = null;

            // Match randomized question to HashMap, set english/blackfoot answers
            for (String word: currentHashSet.keySet())
            {
                if (listWord.equals(word))
                {
                    englishWord = word;
                    blackfootWord = currentHashSet.get(englishWord);

                }
            }

            // Reset number of tries and variable controlling when to move to next question
            boolean correctAnswer = false;
            double numberTries = 0;

            // Set vocab question's corresponding image and Blackfoot audio file
            assert englishWord != null;
            String wordWithoutSpaces = englishWord.replaceAll("[\\s .?']","");
            String testWordAudioFile = "learn_" + wordWithoutSpaces;
            updateTabletImage(wordWithoutSpaces + "testing");

            // Get/set incorrect answers from HashMap
            String incorrectEnglishWordsString = "";
            int keyCounter = 0;
            for (String keyName: currentHashSet.keySet()) {
                keyCounter++;
                if (!keyName.equals(englishWord)) {
                    incorrectEnglishWordsString += keyName;
                }
                if (keyCounter != testingWordsSize)
                {
                    incorrectEnglishWordsString += ",";
                }
            }
            // Set correct/incorrect answers for Pepper to listen to
            PhraseSet correctWord = PhraseText(englishWord);
            PhraseSet incorrectEnglishWords = PhraseText(incorrectEnglishWordsString.split(","));

            // Ask question and listen until correct answer received. 3 tries per question.
            while (!correctAnswer && numberTries < 3)
            {
                sayText("What is " + blackfootWord + " in English?");
                playMedia(testWordAudioFile);
                pausePepper(1);
                Listen listen = ListenBuilder.with(pepper_context).withPhraseSets(correctWord, incorrectEnglishWords, anyText).build();
                ListenResult listenResult = listen.run();
                PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();

                // Check for correct answer
                if (PhraseSetUtil.equals(matchedPhraseSet,correctWord))
                {
                    sayText( "Yes, " + randomString(correctFeedbackConstant) + "! " + englishWord + " is " + blackfootWord + ".");
                    playMedia(testWordAudioFile);
                    pausePepper(1);
                    runAnimation(R.raw.affirmation_a002);
                    correctAnswer = true;
                }
                else if (PhraseSetUtil.equals(matchedPhraseSet, incorrectEnglishWords))
                {
                    sayText("Hmm, " + randomString(incorrectFeedbackConstant) + ". Try again!");
                    runAnimation(R.raw.thinking_a001);
                    numberTries++;
                }
                else if (PhraseSetUtil.equals(matchedPhraseSet, anyText))
                {
                    sayText("Don't worry it's okay, here I'll help you!");
                    numberTries = 3;
                }

                // Too many wrong tries, review answer
                if (numberTries == 3)
                {
                    sayText(blackfootWord + " is " + englishWord + " in English. Repeat after me: " + englishWord + ".");
                    playMedia(testWordAudioFile);
                    runAnimation(R.raw.show_tablet_a002);
                    boolean continueBool = false;
                    while (!continueBool)
                    {
                        PhraseSet matchedCorrectionOption = ListenText(correctWord);
                        if (PhraseSetUtil.equals(matchedCorrectionOption,correctWord))
                        {
                            sayText(randomString(correctFeedbackConstant) + "!");
                            runAnimation(R.raw.affirmation_a011);
                            continueBool = true;
                        }
                    }
                }
            } // end while loop: got correct answer

            //update score if number_tries is high enough: 2 tries or less needed. 3 or more tries allocates 0 points
            if (numberTries <= 2)
            {
                totalUserScore += 1-(0.25 * numberTries);
            }

            // Save scores
            if (currentHashSet == greetingWords)
            {
                greetingScore = totalUserScore;
                totalGreetingWordsTested = totalWordsTested;

            } else if (currentHashSet == foodWords)
            {
                foodScore = totalUserScore;
                totalFoodWordsTested = totalWordsTested;
            }

            sayText("Would you like to get tested on another word?");
            PhraseSet continueLearning = ListenText(continueWithWords, exitLearning);

            if (PhraseSetUtil.equals(continueLearning,exitLearning))
            {
                sayText("Oh okay, no worries!");
                break;
            }

        } // end of all questions, return to main menu

        sayText("Your final score is " + totalUserScore + "/" + totalWordsTested +  ".0 or " + totalUserScore/totalWordsTested*100 + "%. Good job!");
        runAnimation(R.raw.affirmation_a011);
    } // end testMenu()


    private void checkScore()
    {
        updateTabletImage("vocabularysplashscreen");
        sayText("Would you like to check greetings or food scores?");
        // Ask the user which word categories they would like to learn first.
        PhraseSet greetingCategory = PhraseText(learnGreetingConstant);
        PhraseSet foodCategory = PhraseText(learnFoodConstant);

        PhraseSet matchedMenuOption = ListenText(greetingCategory, foodCategory);

        // set score/words tested to matching vocabulary; these reset to 0 when testmenu() is called and are reset.
        if (PhraseSetUtil.equals(matchedMenuOption,greetingCategory))
        {
            totalUserScore = greetingScore;
            totalWordsTested = totalGreetingWordsTested;
        }
        else if (PhraseSetUtil.equals(matchedMenuOption,foodCategory))
        {
            totalUserScore = foodScore;
            totalWordsTested = totalFoodWordsTested;
        }

        // if scores are 0, not taken yet, can't display score
        if (totalUserScore == 0 && totalWordsTested == 0)
        {
            sayText("Sorry, you haven't taken this test yet.");
        } else
            {
            sayText("Your last score was " + totalUserScore + "/" + totalWordsTested +  ".0 or " + totalUserScore/totalWordsTested*100 + "%. Feel free to test your knowledge again!");
        }

    }

    /*
    Pre: songName parameter should be the "exact" title of the song file
    Post: The audio file is played
    */
    private void playMedia(String songName)
    {
        // Get the audio file ID
        Resources res = getResources();
        int soundId = res.getIdentifier(songName, "raw", getPackageName());

        mediaPlayer = new MediaPlayer();
        try
        {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), soundId);
            mediaPlayer.start();
        }
        catch (Exception error)
        {
            Log.i(TAG, "Audio Error: " + error);
            sayText("I couldn't say the word at this time.");
        }
    }

    // Pepper will pause and wait for the specified number of seconds
    private void pausePepper(int time)
    {
        try
        {
            // Convert seconds to milliseconds
            Thread.sleep(time * 1000);
        }
        catch (Exception e)
        {
            Log.i(TAG, "Sleep Thread Failed.");
        }
    }

    // Doesn't work in emulator - might need Physical Pepper
    // Pepper approaches the human before engaging in a conversation
    private void approachHuman()
    {
        HumanAwareness humanAwareness = pepper_context.getHumanAwareness();
        Human recommendedHuman = humanAwareness.getRecommendedHumanToApproach();

        ApproachHuman approachHuman = ApproachHumanBuilder.with(pepper_context)
                .withHuman(recommendedHuman)
                .build();

        approachHuman.addOnHumanIsTemporarilyUnreachableListener(() -> {
            Say say = SayBuilder.with(pepper_context)
                    .withText("I have troubles reaching you, please come closer!")
                    .build();
            say.run();
        });

        approachHuman.async().run();
    }

    // Param: String array filled with elements to randomize
    // Post: Random string from String array
    private String randomString(String[] stringArray)
    {
        Random random = new Random();
        int index = random.nextInt(stringArray.length);
        return stringArray[index];
    }

    // Param: String array
    // Post: Shuffled String array, no modifications done to size or strings themselves
    // Use for looping through random strings to randomize vocabulary in HashMap(learning,testing questions)
    private String[] shuffleVocabulary(String[] stringArray)
    {
        // Create list of array, shuffle, then set array as randomized list
        List<String> strList = Arrays.asList(stringArray);
        Collections.shuffle(strList);

        stringArray = strList.toArray(new String[strList.size()]);
        return stringArray;
    }

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
        return PhraseSetBuilder.with(pepper_context)
                .withTexts(textToSpeech)
                .build();
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

        // Return the phrase Pepper recognized
        return listenResult.getMatchedPhraseSet();
    }

    /*
    Pre: Proper XML (tablet image) file name
    Param: String representing the file name (without the .xml extension)
    Post: Updates the tablet image
    */
    private void updateTabletImage(String file_name)
    {
        // Need to run on UI thread to change tablet image
        runOnUiThread(() -> {
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
        QiSDK.unregister(this);
        mediaPlayer.stop();
        Log.d("onPause", "QiSDK.unregister pause");
        super.onPause();
    }

    @Override
    public void onRobotFocusRefused(String reason)
    {
        // The robot focus is refused.
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        this.pepper_context = null;
        QiSDK.unregister(this, this);
        mediaPlayer.release();
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
    //Attributions:
    //Sound effects obtained from https://www.zapsplat.com
    //Additional sound effects from https://www.zapsplat.com
    */