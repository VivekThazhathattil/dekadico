package com.example.spokennumbers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button recall_button;
    private ImageButton speaker_button;
    private TextView recall_view;
    private CountDownTimer timer;
    private Boolean app_running;
    private ArrayList<Integer> rand_num_list = new ArrayList<Integer>(); // Create an ArrayList object
    private String recall_string;
    private Button next_button;
    private Integer counter;
    private Float time_delay;
    private Button enter_button;
    private EditText time_delay_input;
    private TextView time_delay_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recall_button = findViewById(R.id.recall_button);
        speaker_button = findViewById(R.id.speaker_button);
        recall_view = findViewById(R.id.recall_view);
        next_button = findViewById(R.id.next_button);
        enter_button = findViewById(R.id.enter_button);
        time_delay_input = findViewById(R.id.time_delay_input);
        time_delay_text = findViewById(R.id.time_delay_text);

        recall_button.setVisibility(View.INVISIBLE);
        speaker_button.setVisibility(View.VISIBLE);
        recall_view.setVisibility(View.INVISIBLE);
        next_button.setVisibility(View.INVISIBLE);
        time_delay_input.setVisibility(View.VISIBLE);
        time_delay_text.setVisibility(View.VISIBLE);
        enter_button.setVisibility(View.VISIBLE);

        recall_view.setMovementMethod(new ScrollingMovementMethod());
        app_running = Boolean.FALSE;

        time_delay = (float) 1;
        enter_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String time_delay_str = time_delay_input.getText().toString();
                if(time_delay_str.isEmpty()){
                    time_delay = (float) 0.01;
                }
                else {
                    time_delay = Float.valueOf(time_delay_str);
                    if (time_delay == 0.0)
                        time_delay = (float) 1;
                }
            }
        });

        recall_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                app_running = Boolean.FALSE;
                recall_view.setVisibility(View.VISIBLE);
                recall_button.setVisibility(View.INVISIBLE);
                next_button.setVisibility(View.VISIBLE);

                time_delay_input.setVisibility(View.VISIBLE);
                time_delay_text.setVisibility(View.VISIBLE);
                enter_button.setVisibility(View.VISIBLE);

                ImageView img_button = findViewById(R.id.speaker_button);
                img_button.setImageResource(R.drawable.ic_speaker);

                timer.cancel();
                recall_string = "";
                counter = 0;
                next_button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v){
                        if(counter < rand_num_list.size()) {
                            recall_string = recall_string + rand_num_list.get(counter) + "   ";
                            recall_view.setText(recall_string);
                            counter = counter + 1;
                        }
                    }
                });
            }
        });

        speaker_button.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN && !app_running) {
                    // scale your value
                    float reducedvalue = (float)0.9;
                    v.setScaleX(reducedvalue);
                    v.setScaleY(reducedvalue);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP && !app_running) {
                    v.setScaleX(1);
                    v.setScaleY(1);
                }
                if (app_running == Boolean.FALSE) {
                    rand_num_list.clear();
                    app_running = Boolean.TRUE;
                    recall_view.setText("");
                    ImageView img_button = (ImageView) findViewById(R.id.speaker_button);
                    img_button.setImageResource(R.drawable.ic_speaker_stop);
                    recall_button.setVisibility(View.VISIBLE);
                    recall_view.setVisibility(View.INVISIBLE);
                    next_button.setVisibility(View.INVISIBLE);

                    time_delay_input.setVisibility(View.INVISIBLE);
                    time_delay_text.setVisibility(View.INVISIBLE);
                    enter_button.setVisibility(View.INVISIBLE);

                    timer = new CountDownTimer(3600000, (int)(time_delay*1000)) {

                        @Override
                        public void onTick(long millisinFuture) {
                            Random r = new Random();
                            int rand_num = r.nextInt(10);
                            rand_num_list.add(rand_num);
                            String sound_file_name = "a" + rand_num;
                            int sound_id = getResources().getIdentifier(sound_file_name, "raw", getPackageName());
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), sound_id);
                            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mp.start();
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                public void onCompletion(MediaPlayer mp) {
                                    mp.release();

                                }

                                ;
                            });
                        }

                        @Override
                        public void onFinish() {
                            try {
                                app_running = Boolean.FALSE;
                                ImageView img_button = (ImageView) findViewById(R.id.speaker_button);
                                img_button.setImageResource(R.drawable.ic_speaker);
                            } catch (Exception e) {
                                Log.e("Error", "Error: " + e.toString());
                            }
                        }
                    }.start();
                }
                return false;
            }
        });

    }
    }
