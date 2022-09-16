package com.example.spokennumbers;

import android.app.Activity;
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

    private static Integer flash_timeout = 200;
    private static Integer num_timeout = 200;
    private static Integer num_digits = 3;
    private static Integer num_rows = 5;

    private static long final_answer = 0;

    private static final Integer default_flash_timeout = 200;
    private static final Integer default_num_timeout = 200;
    private static final Integer default_num_rows = 5;
    private static final Integer default_num_digits = 3;

    private static boolean continuous_mode = false;
    private static boolean speech_synthesis = false;
    private static boolean subtractions_mode = false;
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
        if(num_digits <= 0)  num_digits = 1;
        else if(num_digits > 17) num_digits = 17;
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
                    num_display_textview.setText(elements_to_display.get(element_counter[0]).toString());
                    element_counter[0]++;
                    if(element_counter[0] >= elements_to_display.size()){
                        Activity myActivity = getActivity();
                        myActivity.runOnUiThread(new Runnable() {
                                                     @Override
                                                     public void run() {
                                                         show_answer_button.setVisibility(View.VISIBLE);
                                                     }
                                                 }
                        );
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

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
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

        num_display_textview.setText("Ready");
        show_answer_button.setVisibility(View.GONE);


        begin_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
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