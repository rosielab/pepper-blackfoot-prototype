package com.example.blackfootprojectdemo;

import android.content.res.Resources;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.ArrayList;
//import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.Math;


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
    private int correctTestingButton;
    private boolean correctAnswerChosen = false;
    private boolean incorrectAnswerChosen = false;
    private ArrayList<Integer> incorrectTestingButtons = new ArrayList<Integer>();

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
        PhraseSet matchedFeelings = ListenText(feelingWell, feelingBad);

        if (PhraseSetUtil.equals(matchedFeelings,feelingWell))
        {
            sayText("Oh wow, that's great to hear! I'm glad you're doing well.");
        }
        else
        {
            sayText("Oh, well you know what cheers me up? The feeling you get after learning something new!");
        }
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
        sayText("Say pause to pause or stop to end story if you would like! Otherwise, enjoy the show.");

        PhraseSet pauseStory = PhraseText(pauseStoryConstant);
        PhraseSet endStory = PhraseText(endStoryConstant);
        PhraseSet continueStory = PhraseText(continueStoryConstant);

        boolean endStoryFunction = false;
        boolean continueStoryFunction = true;
        int audioPosition = 0;
        playMedia(chosenStory);
        while (!endStoryFunction)
        {

            mediaPlayer.seekTo(audioPosition);
            mediaPlayer.start();

            PhraseSet matchedStoryOption = ListenText(pauseStory, endStory, continueStory);

            // End story, return to main menu
            if (PhraseSetUtil.equals(matchedStoryOption, endStory)) {
                mediaPlayer.stop();
                sayText("Returning to main menu.");
                return;
            }
            // pause story, option to continue (re-loops) or exit/end story
            // **Note: currently the if/else doesn't wait for the user very long, only a few seconds. if no response, story continues but can be repaused/stopped **
            else if (PhraseSetUtil.equals(matchedStoryOption, pauseStory)) {
                mediaPlayer.pause();
                audioPosition = mediaPlayer.getCurrentPosition();
                sayText("I've paused the story for you. Would you like to continue or stop?");
                if (PhraseSetUtil.equals(matchedStoryOption, endStory)) {
                    mediaPlayer.stop();
                    sayText("Returning to main menu.");
                    return;
                } else if (PhraseSetUtil.equals(matchedStoryOption, continueStory)) {
                    mediaPlayer.seekTo(audioPosition);
                    mediaPlayer.start();
                }

                // = mediaPlayer.getCurrentPosition();
            }
        } //endStoryFunction

    } // end StoryMenu()

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

            // Reset number of tries
            double numberTries = 0;

            // Set vocab question's corresponding image and Blackfoot audio file
            assert englishWord != null;
            String wordWithoutSpaces = englishWord.replaceAll("[\\s .?']","");
            String testWordAudioFile = "learn_" + wordWithoutSpaces;

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
            String[] incorrectEnglishWordsList = incorrectEnglishWordsString.split(",");
            PhraseSet incorrectEnglishWords = PhraseText(incorrectEnglishWordsList);

            String finalBlackfootWord = blackfootWord;
            String finalEnglishWord = englishWord;

            // Since we're changing the tablet image elements, we need to run on the main thread.
            // Assigns each tablet button on the template with a random option for the current word
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    updateTabletImage("initialtesttemplate");

                    // Update the test template with the corresponding Blackfoot word and its English options
                    TextView blackfootTestWord = (TextView)findViewById(R.id.blackfoottestword);
                    // Set the slide text title to the Blackfoot work (first letter capitalized)
                    blackfootTestWord.setText(finalBlackfootWord.substring(0, 1).toUpperCase() + finalBlackfootWord.substring(1));

                    // User can choose between EnglishTestWord 1 and EnglishTestWord 4 (4 options)
                    int lowOption = 1;
                    int highOption = 4;

                    ArrayList<Integer> chosenButtonOptions = new ArrayList<Integer>();
                    ArrayList<String> availableEnglishButtonWords = new ArrayList<String>(Arrays.asList(incorrectEnglishWordsList));
                    availableEnglishButtonWords.removeAll(Arrays.asList("", " ", null));

                    // Clear the past incorrect Button IDs
                    incorrectTestingButtons.clear();

                    // Get a button to store the correct English answer
                    int correctRandomOption = ThreadLocalRandom.current().nextInt(lowOption, highOption+1);
                    String correctButtonText = "englishtestword" + correctRandomOption;
                    int correctButtonID = getResources().getIdentifier(correctButtonText, "id", getPackageName());
                    correctTestingButton = correctButtonID;

                    // Set the correct button's text to the answer
                    Button correctButton = findViewById(correctButtonID);
                    correctButton.setText(finalEnglishWord);
                    chosenButtonOptions.add(correctRandomOption);

                    // Remove the correct text from the available options
                    availableEnglishButtonWords.remove(finalEnglishWord);

                    // Iterate through the rest of the buttons
                    Button[] initialTestButtons = new Button[highOption];
                    for (int i = 1; i < highOption; i++)
                    {
                        // Get another button that has not already been chosen/modified
                        Collections.sort(chosenButtonOptions);
                        int incorrectRandomOption = getRandomNumberWithExclusion(lowOption, highOption, chosenButtonOptions);
                        String incorrectOptionButton = "englishtestword" + incorrectRandomOption;

                        // Get the button's ID
                        int incorrectButtonID = getResources().getIdentifier(incorrectOptionButton, "id", getPackageName());

                        // Add the button ID to exclusion list so that we don't change the button again.
                        chosenButtonOptions.add(incorrectRandomOption);
                        Button currentButton = findViewById(incorrectButtonID);

                        // Add the incorrect Button IDs into an array to "hide" them after if the user has incorrect responses
                        incorrectTestingButtons.add(incorrectButtonID);

                        // Assign a random incorrect English word (not previously used) to the button
                        int randomEnglishWordIndex = ThreadLocalRandom.current().nextInt(0, availableEnglishButtonWords.size());

                        String chosenEnglishWord  = availableEnglishButtonWords.get(randomEnglishWordIndex);
                        currentButton.setText(chosenEnglishWord);

                        // Remove the correct text from the available options
                        availableEnglishButtonWords.remove(chosenEnglishWord);
                    }

                }
            });

            // Ask question and listen until correct answer received. 3 tries per question.
            while (numberTries < 3)
            {
                sayText("What is " + blackfootWord + " in English?");
                playMedia(testWordAudioFile);
                pausePepper(1);

                // Currently not working (onButtonClick instead of just Pepper listening as well)
                /*
                Button correctButton = findViewById(correctTestingButton);
                correctButton.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View view)
                    {
                        correctAnswerChosen = true;
                    }
                });
                */

                Listen listenForCorrectAnswer = ListenBuilder.with(pepper_context).withPhraseSets(correctWord, incorrectEnglishWords, anyText).build();
                ListenResult listenResult = listenForCorrectAnswer.run();
                PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();

                // Check for correct answer
                if (PhraseSetUtil.equals(matchedPhraseSet,correctWord) || correctAnswerChosen)
                {
                    sayText( "Yes, " + randomString(correctFeedbackConstant) + "! " + englishWord + " is " + blackfootWord + ".");
                    playMedia(testWordAudioFile);
                    pausePepper(1);
                    runAnimation(R.raw.affirmation_a002);
                    break;
                }
                else if (PhraseSetUtil.equals(matchedPhraseSet, incorrectEnglishWords))
                {
                    sayText("Hmm, " + randomString(incorrectFeedbackConstant));
                    if (numberTries < 2)
                    {
                        sayText("Please try again!");
                    }
                    runAnimation(R.raw.thinking_a001);
                    numberTries++;
                }
                else if (PhraseSetUtil.equals(matchedPhraseSet, anyText))
                {
                    sayText("Don't worry it's okay, here I'll help you!");
                    numberTries = 3;
                }

                // If the user has reached 2 tries, hide 2 of the incorrect buttons
                if (numberTries == 2)
                {
                    for (int i = 0; i < 2; i++)
                    {
                        // Get a button ID with the incorrect answer
                        int randomHiddenButtonIndex = ThreadLocalRandom.current().nextInt(0, incorrectTestingButtons.size());
                        int incorrectButtonID = incorrectTestingButtons.get(randomHiddenButtonIndex);
                        incorrectTestingButtons.remove(randomHiddenButtonIndex);

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                // Make the button invisible
                                Button hiddenButton = findViewById(incorrectButtonID);
                                hiddenButton.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
                // Too many wrong tries, review answer
                if (numberTries == 3)
                {
                    sayText(blackfootWord + " is " + englishWord + " in English. Repeat after me: " + englishWord + ".");
                    playMedia(testWordAudioFile);
                    runAnimation(R.raw.show_tablet_a002);

                    PhraseSet matchedCorrectionOption = ListenText(correctWord);
                    if (PhraseSetUtil.equals(matchedCorrectionOption,correctWord))
                    {
                        sayText(randomString(correctFeedbackConstant) + "!");
                        runAnimation(R.raw.affirmation_a011);
                    }
                }
            }

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

        sayText("Your final score is " + totalUserScore + "/" + totalWordsTested +  ".0 or " + Math.round(totalUserScore/totalWordsTested*100) + "%. Good job!");
        runAnimation(R.raw.affirmation_a011);
    } // end testMenu()


    private void checkScore()
    {
        updateTabletImage("vocabularysplashscreen");
        boolean returnToMainMenu = false;
        while (!returnToMainMenu)
        {
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
                sayText("Your last score was " + totalUserScore + "/" + totalWordsTested +  ".0 or " + Math.round(totalUserScore/totalWordsTested*100) + "%. Feel free to test your knowledge again!");
            }

            // Ask to check another score
            sayText("Would you like to check another score?");
            PhraseSet continueScore = PhraseText(continueConstant);
            PhraseSet exitScore = PhraseText(exitTextConstant);

            PhraseSet matchedLeaveScoreOption = ListenText(continueScore, exitScore);


            // Listen for vocabulary test user would like, set matching vocabulary set
            if (PhraseSetUtil.equals(matchedLeaveScoreOption,continueScore))
            {
                sayText("Okay!");
            }
            else if (PhraseSetUtil.equals(matchedLeaveScoreOption,exitScore))
            {
                sayText("Taking you back to the main menu.");
                returnToMainMenu = true;
            }
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

    // https://stackoverflow.com/questions/6443176/how-can-i-generate-a-random-number-within-a-range-but-exclude-some
    // Get a random number in a given range, but exclude a few numbers from getting picked
    private static int getRandomNumberWithExclusion(int start, int end, ArrayList<Integer> exclude) {
        Random rnd = new Random();
        int random = start + rnd.nextInt(end - start + 1 - exclude.size());
        for (int ex : exclude) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
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