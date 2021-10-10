package com.example.spokennumbers;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;


public class spoken_numbers_ingame_fragment extends Fragment {
    private float timeDelay;
    private final float timeInc;
    private final boolean isFemale;
    private final boolean isDecimal;
    private final ArrayList<Integer> rand_num_list = new ArrayList<>();
    private Boolean appRunning;
    private long defaultMillisLeft;
    private long millisLeft;
    private CountDownTimer timer;

    public spoken_numbers_ingame_fragment(float td, float ti, boolean isFemale, boolean isDecimal) {
        this.timeDelay = td;
        this.timeInc = ti;
        this.isFemale = isFemale;
        this.isDecimal = isDecimal;
    }

    public CountDownTimer setupTimer(long timeLeft, float interval){
        return new CountDownTimer(timeLeft,(int)(interval*1000) ) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisLeft = millisUntilFinished;
                if(millisLeft >= defaultMillisLeft - timeDelay * 1000){
                    return;
                }
                Random r = new Random();
                int rand_num;
                if(isDecimal){
                    rand_num = r.nextInt(10);
                }
                else{
                    rand_num = r.nextInt(2);
                }
                rand_num_list.add(rand_num);
                String female = "a";
                if(!isFemale){
                    female = "b";
                }
                String sound_file_name = female + rand_num;
                int sound_id = getResources().getIdentifier(sound_file_name, "raw", Objects.requireNonNull(getActivity()).getPackageName());
                MediaPlayer mp = MediaPlayer.create(getActivity().getApplicationContext(), sound_id);
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
            }

            @Override
            public void onFinish() {
            }
        };
    }

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spoken_numbers_ingame_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        final ImageButton pauseButton = Objects.requireNonNull(getView()).findViewById(R.id.pause_button);
        final Button recallButton = getView().findViewById(R.id.recall_button);
        final Button decrementButton = getView().findViewById(R.id.time_decrement);
        final Button incrementButton = getView().findViewById(R.id.time_increment);
        final TextView currentTimeTextView = getView().findViewById(R.id.current_time_control_text);

        String currTimeText = timeDelay + "s";
        currentTimeTextView.setText(currTimeText);

        String timeIncStr = "+" + timeInc;
        String timeDecStr = "-" + timeInc;
        incrementButton.setText(timeIncStr);
        decrementButton.setText(timeDecStr);

        appRunning = true;
        defaultMillisLeft = 3600000;
        millisLeft = defaultMillisLeft;

        rand_num_list.clear();
        timer = setupTimer(millisLeft, timeDelay);
        timer.start();

        incrementButton.setOnClickListener(v -> {
             timeDelay += timeInc;
            String currTimeText12 = timeDelay + "s";
            currentTimeTextView.setText(currTimeText12);
            timer.cancel();
             timer = setupTimer(defaultMillisLeft, timeDelay);
             timer.start();
         });

        decrementButton.setOnClickListener(v -> {
            if(timeDelay - timeInc < 0.1){
               return;
            }
            timeDelay -= timeInc;
            String currTimeText1 = timeDelay + "s";
            currentTimeTextView.setText(currTimeText1);
            timer.cancel();
            timer = setupTimer(defaultMillisLeft, timeDelay);
            timer.start();
        });

        recallButton.setOnClickListener(v -> {
            timer.cancel();
            if(MainActivity.evaluationMode)
                ((MainActivity) Objects.requireNonNull(getActivity())).switchToEvalFragment(rand_num_list);
            else
            ((MainActivity) Objects.requireNonNull(getActivity())).switchToRecallFragment(rand_num_list);
        });

        pauseButton.setOnClickListener(v -> {
            if(appRunning) {
                timer.cancel();
                pauseButton.setImageResource(android.R.drawable.ic_media_play);
                appRunning = false;
            }
            else {
                timer = setupTimer(millisLeft, timeDelay);
                timer.start();
                pauseButton.setImageResource(android.R.drawable.ic_media_pause);
                appRunning = true;
            }
        });
    }
}
