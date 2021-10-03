package com.example.spokennumbers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int CONTENT_VIEW_ID = 10101010;

    private FragmentManager fragmentManager;
    private spoken_numbers_main_fragment mainFragment;
    private spoken_numbers_ingame_fragment ingameFragment;
    private spoken_numbers_recall_fragment recallFragment;

    public void switchToInGameFragment(float timeDelay, float timeInc, boolean isFemale, boolean isDec){
        ingameFragment = new spoken_numbers_ingame_fragment(timeDelay, timeInc, isFemale, isDec);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(mainFragment.getId(), ingameFragment);
        fragmentTransaction.commit();
    }
    public void switchToRecallFragment(ArrayList<Integer> al){
        recallFragment = new spoken_numbers_recall_fragment(al);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(ingameFragment.getId(), recallFragment);
        fragmentTransaction.commit();
    }
    public void switchToMainFragment(){
        mainFragment = new spoken_numbers_main_fragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(recallFragment.getId(), mainFragment);
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

        mainFragment = new spoken_numbers_main_fragment();
        mainFragment.setArguments(getIntent().getExtras());
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(CONTENT_VIEW_ID, mainFragment);
        fragmentTransaction.commit();
    }

}
