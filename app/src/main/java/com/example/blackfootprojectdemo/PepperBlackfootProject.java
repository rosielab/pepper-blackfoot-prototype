package com.example.blackfootprojectdemo;

import android.os.Bundle;
import android.util.Log;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.util.PhraseSetUtil;

public class PepperBlackfootProject extends RobotActivity implements RobotLifecycleCallbacks
{
    private static String TAG = "MainActivity";
    private static boolean continue_looping = true;
    private QiContext pepper_context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Hide speech bar from primary view (only shown when Pepper's talked to).
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.TOP);
        setContentView(R.layout.activity_main);

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
        while (continue_looping == true)
        {
            updateTabletImage("activity_main");
            menuSystem();
        }
        // Translation: See you later, have a good day.
        sayText("Kitatama'sino! Have a good day.");
    }

    // Initial introduction by Pepper
    private void introduction()
    {
        sayText("Oki! I'm Pepper! How are you doing today?");
        PhraseSet feeling_well = PhraseText("good", "nice", "well", "fine", "okay", "better", "so so");
        PhraseSet not_feeling_well = PhraseText("not", "bad", "not good", "cry", "sad");

        ListenText(feeling_well, not_feeling_well);

        sayText("Let's learn some Blackfoot together!");
    }

    // Menu system with the various options
    private void menuSystem()
    {
        sayText("Would you like to play a game, learn some words, do a little test, or exit?");

        PhraseSet play_text = PhraseText("play", "game", "first");
        PhraseSet learn_text = PhraseText("learn", "flashcards", "teach", "second", "middle");
        PhraseSet test_text = PhraseText("test", "exam", "third", "last", "final");
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

    // Learn menu function
    private void learnMenu()
    {
        updateTabletImage("main_menu");
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

    @Override
    public void onRobotFocusLost()
    {
        this.pepper_context = null;
    }

    @Override
    public void onResume()
    {
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
    protected void onDestroy()
    {
        // Unregister the RobotLifecycleCallbacks for this Activity.
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
    */