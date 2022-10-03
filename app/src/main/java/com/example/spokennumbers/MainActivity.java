package com.example.spokennumbers;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int CONTENT_VIEW_ID = 10101010;

    private FragmentManager fragmentManager;
    private spoken_numbers_main_fragment mainFragment;
    private spoken_numbers_ingame_fragment ingameFragment;
    private spoken_numbers_recall_fragment recallFragment;
    private EvaluationFragment evaluationFragment;

    private Boolean is_night_mode = false;

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
    public void switchToGamesMenuFragment(){
        gamesMenuFragment = new GamesMenu();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);

        /* lord, please forgive me for the sin I'm about to commit */
        int numberOfFragments = getSupportFragmentManager().getFragments().size();
        for (Fragment fragment: getSupportFragmentManager().getFragments()){
            if(numberOfFragments == 1){
                fragmentTransaction.replace(fragment.getId(), gamesMenuFragment);
                break;
            }
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            --numberOfFragments;
        }
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
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dekadico_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.toggle_dark_mode:
                if(!is_night_mode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    is_night_mode = true;
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    is_night_mode = false;
                }
                return true;
            case R.id.github_link:
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://github.com/VivekThazhathattil/dekadico"));
                startActivity(viewIntent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);

        is_night_mode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;

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

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                switchToGamesMenuFragment();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    @Override
    public void onBackPressed() {
        if (ingameFragment != null) {
            ingameFragment.stopCountDownTimer();
        }
        super.onBackPressed();
    }

    @Override
    public void recreate(){
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        startActivity(getIntent());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}