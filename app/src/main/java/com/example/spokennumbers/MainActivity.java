package com.example.spokennumbers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int CONTENT_VIEW_ID = 10101010;

    private FragmentManager fragmentManager;
    /* when I started out with this project, spoken numbers
    * was the only game that I intended to add to this app.
    *  So mainFragment is for the Spoken numbers module */
    private spoken_numbers_main_fragment mainFragment;
    private spoken_numbers_ingame_fragment ingameFragment;
    private spoken_numbers_recall_fragment recallFragment;
    private EvaluationFragment evaluationFragment;

    private FlashAnzan flashAnzanFragment;
    private GamesMenu gamesMenuFragment;

    public static PrefConfig prefConfig;
    public static boolean evaluationMode = true;

    // specific to spoken numbers game
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String DELAYTIME = "delayTime";
    public static final String INCTIME = "incTime";
    public static final String ISFEMALE = "isFemale";
    public static final String ISDECIMAL = "isDecimal";
    public static final String EVALMODE = "isEvalMode";
    public static final String HIGHSCORE = "highScore";

    public void switchToSpokenNumbersGameFragment(){
        mainFragment = new spoken_numbers_main_fragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        fragmentTransaction.replace(gamesMenuFragment.getId(), mainFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void switchToFlashAnzanGameFragment(){
        flashAnzanFragment = new FlashAnzan();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        fragmentTransaction.replace(gamesMenuFragment.getId(), flashAnzanFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    /* Return from the game module fragment to the games menu fragment */
    public void switchToGamesMenuFragment(String currentFragmentName){
        gamesMenuFragment = new GamesMenu();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        switch(currentFragmentName){
            case "SpokenNumbers":
                fragmentTransaction.replace(mainFragment.getId(), gamesMenuFragment);
                break;
            case "FlashAnzan":
                fragmentTransaction.replace(flashAnzanFragment.getId(), gamesMenuFragment);
                break;
        }
        fragmentTransaction.replace(flashAnzanFragment.getId(), gamesMenuFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void switchToInGameFragment(float timeDelay, float timeInc, boolean isFemale, boolean isDec){
        ingameFragment = new spoken_numbers_ingame_fragment(timeDelay, timeInc, isFemale, isDec);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        fragmentTransaction.replace(mainFragment.getId(), ingameFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }
    public void switchToRecallFragment(ArrayList<Integer> al){
        recallFragment = new spoken_numbers_recall_fragment(al);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        fragmentTransaction.replace(ingameFragment.getId(), recallFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void switchToEvalFragment(ArrayList<Integer> al){
        evaluationFragment = new EvaluationFragment(al);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(ingameFragment.getId(), evaluationFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void switchToMainFragment(String mode){
        mainFragment = new spoken_numbers_main_fragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        //fragmentTransaction.addToBackStack(null);
        switch (mode) {
            case "recall":
                fragmentTransaction.replace(recallFragment.getId(), mainFragment);
                break;
            case "eval":
                fragmentTransaction.replace(evaluationFragment.getId(), mainFragment);
                break;
            case "ingame":
                fragmentTransaction.replace(ingameFragment.getId(), mainFragment);
                break;
            default:
                return;
        }
        fragmentTransaction.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);

       // setContentView(R.layout.activity_main);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        prefConfig = new PrefConfig(getApplicationContext());

        mainFragment = new spoken_numbers_main_fragment();

        //flashAnzanFragment = new FlashAnzan();
        //flashAnzanFragment.setArguments(getIntent().getExtras());

        gamesMenuFragment = new GamesMenu();
        gamesMenuFragment.setArguments(getIntent().getExtras());

        //mainFragment.setArguments(getIntent().getExtras());
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.add(CONTENT_VIEW_ID, mainFragment);
        fragmentTransaction.add(CONTENT_VIEW_ID, gamesMenuFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (ingameFragment != null) {
            ingameFragment.stopCountDownTimer();
        }
        super.onBackPressed();
    }
}