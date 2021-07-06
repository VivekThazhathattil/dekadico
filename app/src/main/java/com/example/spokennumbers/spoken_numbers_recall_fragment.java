package com.example.spokennumbers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class spoken_numbers_recall_fragment extends Fragment {
    private ArrayList<Integer> num_list;
    private int counter;
    private TextView answerView;
    private String recallString;
    public spoken_numbers_recall_fragment(ArrayList<Integer> al) {
        num_list = al;
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
        return inflater.inflate(R.layout.spoken_numbers_recall_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        final ImageButton oneSolnButton = getView().findViewById(R.id.one_soln_button);
        final ImageButton allSolnButton = getView().findViewById(R.id.all_soln_button);
        final ImageButton noSolnButton = getView().findViewById(R.id.no_soln_button);
        final ImageButton returnButton = getView().findViewById(R.id.exit_to_menu_button);
        answerView = getView().findViewById(R.id.answer_text);
        recallString = "";
        counter = 0;
        oneSolnButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(counter < num_list.size()){
                    recallString += num_list.get(counter) + "    ";
                    answerView.setText(recallString);
                }
                counter++;
            }
        });
        allSolnButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                recallString = "";
                for(int i = 0; i < num_list.size(); i++)
                   recallString += num_list.get(i) + "    ";
                answerView.setText(recallString);
                counter = num_list.size();
            }
        });
        noSolnButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                recallString = "";
                answerView.setText(recallString);
                counter = 0;
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                ((MainActivity)getActivity()).switchToMainFragment();
            }
        });
    }
}
