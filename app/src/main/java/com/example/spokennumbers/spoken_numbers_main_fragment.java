package com.example.spokennumbers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;


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
    private RadioButton syntheticButton;
    private RadioButton decimalButton;
    private RadioButton binaryButton;
    private String defaultTimeDelay;
    private String defaultTimeInc;

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

        defaultTimeDelay = ((MainActivity)getActivity()).loadDataDelayTime();
        defaultTimeInc = ((MainActivity)getActivity()).loadDataIncTime();
        timeDelayInput.setText(defaultTimeDelay);
        timeIncInput.setText(defaultTimeInc);

        femaleVoiceButton = getView().findViewById(R.id.radio_button_female);
        maleVoiceButton = getView().findViewById(R.id.radio_button_male);
        syntheticButton = getView().findViewById(R.id.radio_button_synthetic_male);
        syntheticButton.setEnabled(false);

        femaleVoiceButton.setChecked(((MainActivity)getActivity()).loadDataFemaleChecked());
        maleVoiceButton.setChecked(!((MainActivity)getActivity()).loadDataFemaleChecked());

        decimalButton = getView().findViewById(R.id.decimal_radio);
        binaryButton = getView().findViewById(R.id.binary_radio);
        decimalButton.setChecked(((MainActivity)getActivity()).loadDataDecimalChecked());
        binaryButton.setChecked(!((MainActivity)getActivity()).loadDataDecimalChecked());

        startButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String timeDelayStr = timeDelayInput.getText().toString();
                String timeIncStr = timeIncInput.getText().toString();

                float timeDelayNum;
                float timeIncNum;

                if(!timeDelayStr.matches("\\d+(?:\\.\\d+)?")){ //not a number
                    timeDelayNum = Float.parseFloat(defaultTimeDelay);
                }
                else{
                    timeDelayNum = Float.valueOf(timeDelayStr);
                    if(timeDelayNum <= 0.1)
                        timeDelayNum = Float.parseFloat(defaultTimeDelay);
                }

                if(!timeIncStr.matches("\\d+(?:\\.\\d+)?")){ //not a number
                    timeIncNum = Float.parseFloat(defaultTimeInc);
                }
                else{
                    timeIncNum = Float.valueOf(timeIncStr);
                    if(timeIncNum <= 0.1)
                        timeIncNum = Float.parseFloat(defaultTimeInc);
                }
                boolean isFemaleVoice = ((MainActivity)getActivity()).loadDataFemaleChecked();
                if(femaleVoiceButton.isChecked()){
                    isFemaleVoice = true;
                }
                else if(maleVoiceButton.isChecked()){
                    isFemaleVoice = false;
                }

                boolean isDecimal = ((MainActivity)getActivity()).loadDataDecimalChecked();
                if(decimalButton.isChecked()){
                    isDecimal = true;
                }
                else if(binaryButton.isChecked()){
                    isDecimal = false;
                }

                ((MainActivity)getActivity()).saveData(Float.toString(timeDelayNum),
                        Float.toString(timeIncNum), isFemaleVoice, isDecimal);
                ((MainActivity)getActivity()).switchToInGameFragment(timeDelayNum, timeIncNum, isFemaleVoice, isDecimal);
            }
        });
    }
}
