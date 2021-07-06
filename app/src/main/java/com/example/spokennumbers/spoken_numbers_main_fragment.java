package com.example.spokennumbers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link spoken_numbers_main_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class spoken_numbers_main_fragment extends Fragment {

    private static Button startButton;
//    private static Button okButton;
    private static EditText timeDelayInput;

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
//        okButton = getView().findViewById(R.id.time_delay_button);
        timeDelayInput = getView().findViewById(R.id.time_delay_input);
        timeDelayInput.setText("1.00");
        startButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String timeDelayStr = timeDelayInput.getText().toString();
                Float timeDelayNum;
                if(!timeDelayStr.matches("\\d+(?:\\.\\d+)?")){ //not a number
                    timeDelayNum = (float) 1.0;
                }
                else{
                    timeDelayNum = Float.valueOf(timeDelayStr);
                    if(timeDelayNum == 0)
                        timeDelayNum = (float)1.0;
                }
                ((MainActivity)getActivity()).switchToInGameFragment(timeDelayNum);
            }
        });
    }
}
