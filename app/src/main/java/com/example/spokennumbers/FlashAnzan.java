package com.example.spokennumbers;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlashAnzan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlashAnzan extends Fragment {

    private static Integer flash_timeout;
    private static Integer num_timeout;
    private static Integer num_digits;
    private static Integer num_rows;

    private static long final_answer = 0;

    private static final Integer default_flash_timeout = 1000;
    private static final Integer default_num_timeout = 1000;
    private static final Integer default_num_rows = 5;
    private static final Integer default_num_digits = 3;

    private static boolean continuous_mode;
    private static boolean speech_synthesis;
    private static boolean subtractions_mode;
    private static boolean is_running = false;
    private static Timer timer;

    private EditText num_digits_edittext;
    private EditText num_rows_edittext;
    private EditText timeout_edittext;
    private EditText flash_timeout_edittext;
    private Switch continuous_mode_switch;
    private Switch subtractions_switch;
    private Switch speech_synthesis_switch;
    private Button begin_button;
    private Button show_answer_button;
    private TextView num_display_textview;
    private TableLayout settings_table_tablelayout;
    private TableLayout settings_table_tablelayout2;

    public FlashAnzan() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FlashAnzan newInstance(String param1, String param2) {
        FlashAnzan fragment = new FlashAnzan();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void adjustNumDigits(){
        num_digits = (num_digits > 5)
                ? 5
                : ((num_digits <= 0)
                ? 1
                : num_digits);
    }

    private static void playSound(Context context, int id){
        MediaPlayer mediaPlayer = MediaPlayer.create(context, id);
        mediaPlayer.start();
    }

    private ArrayList<Long> generateRandomNumbers(){
        adjustNumDigits();
        ArrayList<Long> result = new ArrayList<Long>();
        for(int i = 0; i < num_rows; ++i){
            long temp_num = (long) (Math.random() * Math.pow(10, num_digits));
            if(subtractions_mode){
               if(Math.random() > 0.5)
                   temp_num *= -1;
            }

            result.add(temp_num);
        }
        return result;
    }

    private long getFinalSum(ArrayList<Long> nums){
        long result = 0;
        for(int i = 0; i < nums.size(); ++i) {
            result += nums.get(i);
        }
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flash_anzan, container, false);
    }

    public void execTaskPeriodic(ArrayList<Long> elements_to_display){
        ArrayList<String> pregame_dots = new ArrayList<String>(Arrays.asList(".", "..", "...", "Go!"));

        final int[] element_counter = {0};
        final int[] dots_counter = {0};

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(dots_counter[0] < pregame_dots.size()){
                    num_display_textview.setText(pregame_dots.get(dots_counter[0]));
                    dots_counter[0]++;
                }
                else{
                    playSound(getContext(), R.raw.number_view);
                    num_display_textview.setText(elements_to_display.get(element_counter[0]).toString());
                    element_counter[0]++;
                    if(element_counter[0] >= elements_to_display.size()){
                        Activity myActivity = getActivity();
                        if(myActivity != null){
                            myActivity.runOnUiThread(new Runnable() {
                                                         @Override
                                                         public void run() {
                                                             if(show_answer_button != null)
                                                                 show_answer_button.setVisibility(View.VISIBLE);
                                                         }
                                                     }
                            );
                        }
                        timer.cancel();
                    }
                }
            }
        }, 0, num_timeout);
    }

    private void update_gui(Boolean running){
        show_answer_button.setVisibility(View.GONE);
        if(running){
            settings_table_tablelayout.setVisibility(View.VISIBLE);
            settings_table_tablelayout2.setVisibility(View.VISIBLE);
            begin_button.setText("Begin");
            timer.cancel();
            num_display_textview.setText("Ready");
        }
        else{
            settings_table_tablelayout.setVisibility((View.GONE));
            settings_table_tablelayout2.setVisibility((View.GONE));
            begin_button.setText("Stop");
        }
    }

    private int get_edittext_value(EditText edit_text){
        String value = edit_text.getText().toString();
        if(value.equals(""))
            return -1;
        return Integer.parseInt(value);
    }

    private int update_params_edittext_helper(EditText edit_text, int original_value, int default_value){
        int temp = get_edittext_value(edit_text);
        return (temp == -1) ?  default_value : temp;
    }

    private Boolean update_params_switch_helper(Switch param_switch){
        return param_switch.isChecked();
    }

    private void update_run_params(){
        flash_timeout = update_params_edittext_helper(flash_timeout_edittext, flash_timeout, default_flash_timeout);
        num_timeout = update_params_edittext_helper(timeout_edittext, num_timeout, default_num_timeout);
        num_rows = update_params_edittext_helper(num_rows_edittext, num_rows, default_num_rows);
        num_digits = update_params_edittext_helper(num_digits_edittext, num_digits, default_num_digits);

        continuous_mode = update_params_switch_helper(continuous_mode_switch);
        speech_synthesis = update_params_switch_helper(speech_synthesis_switch);
        subtractions_mode = update_params_switch_helper(subtractions_switch);
    }

    private void loadPreviousDataFromConfigFile(){
        flash_timeout = Integer.parseInt(MainActivity.prefConfig.loadFlashAnzanDataString(PrefConfig.FLASH_ANZAN_NUM_FLASH));
        num_timeout = Integer.parseInt(MainActivity.prefConfig.loadFlashAnzanDataString(PrefConfig.FLASH_ANZAN_NUM_TIMEOUT));
        num_digits = Integer.parseInt(MainActivity.prefConfig.loadFlashAnzanDataString(PrefConfig.FLASH_ANZAN_NUM_DIGITS));
        num_rows = Integer.parseInt(MainActivity.prefConfig.loadFlashAnzanDataString(PrefConfig.FLASH_ANZAN_NUM_ROWS));

        //Log.i("TAG_VIVEK", "Tag:flash_anzan__ " + flash_timeout.toString() + " " + num_timeout.toString() + "!");

        continuous_mode = MainActivity.prefConfig.loadFlashAnzanDataBoolean(PrefConfig.FLASH_ANZAN_CONTINUOUS_MODE);
        speech_synthesis = MainActivity.prefConfig.loadFlashAnzanDataBoolean(PrefConfig.FLASH_ANZAN_SPEECH_SYNTHESIS);
        subtractions_mode = MainActivity.prefConfig.loadFlashAnzanDataBoolean(PrefConfig.FLASH_ANZAN_SUBTRACTIONS);
    }

    private void initializeViewParamElementsWithLoadedData(){
        num_digits_edittext.setText(num_digits == null ? default_num_digits.toString() : num_digits.toString());
        num_rows_edittext.setText(num_rows == null ? default_num_rows.toString() : num_rows.toString());
        timeout_edittext.setText(num_timeout == null ? default_num_timeout.toString() : num_timeout.toString());
        flash_timeout_edittext.setText(flash_timeout == null ? default_flash_timeout.toString() : flash_timeout.toString());
        continuous_mode_switch.setChecked(continuous_mode);
        subtractions_switch.setChecked(subtractions_mode);
        speech_synthesis_switch.setChecked(speech_synthesis);
    }

    private void initializeViewElements(){
        num_digits_edittext = Objects.requireNonNull(getView()).findViewById(R.id.num_digits_row_edittext);
        num_rows_edittext = getView().findViewById(R.id.num_rows_row_edittext);
        timeout_edittext = getView().findViewById(R.id.timeout_row_edittext);
        flash_timeout_edittext = getView().findViewById(R.id.flash_row_edittext);
        continuous_mode_switch = getView().findViewById(R.id.continuous_mode_row_switch);
        subtractions_switch = getView().findViewById(R.id.subtractions_row_switch);
        speech_synthesis_switch = getView().findViewById(R.id.speech_synthesis_row_switch);
        begin_button = getView().findViewById(R.id.begin_anzan_button);
        show_answer_button = getView().findViewById(R.id.show_answer_button);
        num_display_textview = getView().findViewById(R.id.num_display_textview);
        settings_table_tablelayout = getView().findViewById(R.id.settings_table);
        settings_table_tablelayout2 = getView().findViewById(R.id.settings_table2);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        loadPreviousDataFromConfigFile();
        initializeViewElements();
        initializeViewParamElementsWithLoadedData();

        num_display_textview.setText("Ready");
        show_answer_button.setVisibility(View.GONE);

        speech_synthesis_switch.setEnabled(false);
        continuous_mode_switch.setEnabled(false);


        begin_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                MainActivity.prefConfig.saveDataFlashAnzan(num_rows.toString(), num_digits.toString(),
                        num_timeout.toString(), flash_timeout.toString(), subtractions_mode, speech_synthesis,
                        continuous_mode);
                if(is_running){
                    update_gui(true);
                }
                else{
                    update_run_params();
                    update_gui(false);
                    ArrayList<Long> numbers_to_display= generateRandomNumbers();
                    final_answer = getFinalSum(numbers_to_display);
                    execTaskPeriodic(numbers_to_display);
                }
                is_running = !is_running;
            }
        });

        show_answer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num_display_textview.setText(final_answer + "");
                begin_button.setText("Retry");
                show_answer_button.setVisibility(View.GONE);
            }
        });
    }
}