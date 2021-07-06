package com.example.spokennumbers;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;


public class spoken_numbers_ingame_fragment extends Fragment {
    private float timeDelay;
    private ArrayList<Integer> rand_num_list = new ArrayList<Integer>();
    private Boolean appRunning;
    private long millisLeft;
    private CountDownTimer timer;

    public spoken_numbers_ingame_fragment(float td) {
        this.timeDelay = td;
    }

    public CountDownTimer setupTimer(long timeLeft, float interval){
        CountDownTimer timer = new CountDownTimer(timeLeft,(int)(interval*1000) ) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisLeft = millisUntilFinished;
                Random r = new Random();
                int rand_num = r.nextInt(10);
                rand_num_list.add(rand_num);
                String sound_file_name = "a" + rand_num;
                int sound_id = getResources().getIdentifier(sound_file_name, "raw", getActivity().getPackageName());
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
        return timer;
    }

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spoken_numbers_ingame_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        final ImageButton pauseButton = getView().findViewById(R.id.pause_button);
        final Button recallButton = getView().findViewById(R.id.recall_button);
        appRunning = true;
        millisLeft = 3600000;

        rand_num_list.clear();
        timer = setupTimer(millisLeft, timeDelay);
        timer.start();

        recallButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                timer.cancel();
                ((MainActivity)getActivity()).switchToRecallFragment(rand_num_list);
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
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
            }
        });
    }
}
