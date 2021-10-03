package com.example.spokennumbers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link spoken_numbers_main_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class spoken_numbers_main_fragment extends Fragment {

    private ImageButton startButton;
    private EditText timeDelayInput;
    private EditText timeIncInput;
    private RadioButton femaleVoiceButton;
    private RadioButton maleVoiceButton;
    private float defaultTimeDelay;
    private float defaultTimeInc;

    public spoken_numbers_main_fragment() {
        // Required empty public constructor
    }
    public static spoken_numbers_main_fragment newInstance() {
        spoken_numbers_main_fragment fragment = new spoken_numbers_main_fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.spoken_numbers_main_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        startButton = getView().findViewById(R.id.start_button);
        timeDelayInput = getView().findViewById(R.id.time_delay_input);
        timeIncInput = getView().findViewById(R.id.time_inc_num);
        defaultTimeDelay = (float)1.00;
        defaultTimeInc = (float)0.25;
        timeDelayInput.setText(Float.toString(defaultTimeDelay));
        timeIncInput.setText(Float.toString(defaultTimeInc));

        femaleVoiceButton = getView().findViewById(R.id.radio_button_female);
        maleVoiceButton = getView().findViewById(R.id.radio_button_male);

        startButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String timeDelayStr = timeDelayInput.getText().toString();
                String timeIncStr = timeIncInput.getText().toString();

                Float timeDelayNum;
                Float timeIncNum;

                if(!timeDelayStr.matches("\\d+(?:\\.\\d+)?")){ //not a number
                    timeDelayNum = (float) defaultTimeDelay;
                }
                else{
                    timeDelayNum = Float.valueOf(timeDelayStr);
                    if(timeDelayNum <= 0.1)
                        timeDelayNum = (float)defaultTimeDelay;
                }

                if(!timeIncStr.matches("\\d+(?:\\.\\d+)?")){ //not a number
                    timeIncNum = (float) defaultTimeInc;
                }
                else{
                    timeIncNum = Float.valueOf(timeIncStr);
                    if(timeIncNum <= 0.1)
                        timeIncNum = (float)defaultTimeInc;
                }
                boolean isFemaleVoice = true;
                if(femaleVoiceButton.isChecked()){
                    isFemaleVoice = true;
                }
                else if(maleVoiceButton.isChecked()){
                    isFemaleVoice = false;
                }

                ((MainActivity)getActivity()).switchToInGameFragment(timeDelayNum, timeIncNum, isFemaleVoice);
            }
        });
    }
}
