package com.example.blackfootprojectdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.Say;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private QiContext pepper_context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide speech bar from primary view (only shown when Pepper's talked to).
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.TOP);
        setContentView(R.layout.activity_main);
        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // The robot focus is gained.
        // Create a new say action.
        this.pepper_context = qiContext;
        sayText("Oki! I'm Pepper! Let's learn some Blackfoot. How would you like to begin?\n" +
                        "We can learn some words, play a game, or have a fun test to see how well you do!");
//        Say say = SayBuilder.with(qiContext) // Create the builder with the context.
//                .withText("Oki! I'm Pepper! Let's learn some Blackfoot. How would you like to begin?" +
//                        "We can learn some words, play a game, or have a fun test to see how well you do!") // Set the text to say.
//                .build(); // Build the say action.
//
//        // Execute the action.
//        say.run();
    }

    @Override
    public void onRobotFocusLost() {
        // The robot focus is lost.
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }

    private void sayText(String textToSpeech) {
        Say say = SayBuilder.with(pepper_context) // Create the builder with the context.
                .withText(textToSpeech) // Set the text to say.
                .build(); // Build the say action.
        say.run();
    }

    // When the play button on the main menu is clicked
    public void playButtonClick (View playButtonView) {
        String user_name = "Garry";
        // Find "resource by ID" called WelcomeTextBox and modify it
        TextView welcomeText = findViewById(R.id.welcomeTextBox);
        welcomeText.setText("Hello, " + user_name + "!");
        sayText("Hi " + user_name + "! Let's play a game and learn Blackfoot!");
    }
}