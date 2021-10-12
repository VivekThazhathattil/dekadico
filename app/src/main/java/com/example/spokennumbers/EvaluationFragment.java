package com.example.spokennumbers;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class EvaluationFragment extends Fragment {
    private ArrayList<Integer> solutionList;
    public static final String TAG = "EVAL";

    public EvaluationFragment(ArrayList<Integer> solutionList) {
        this.solutionList = solutionList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spoken_numbers_eval_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        Button checkButton = Objects.requireNonNull(getView()).findViewById(R.id.check_button);
        TextView statsView = getView().findViewById(R.id.stats_view);
        EditText usersSolutionText = getView().findViewById(R.id.solution_type_edit_text);
        statsView.setText("");

        checkButton.setOnClickListener(v -> {
            if(statsView.getText() != ""){
                statsView.setText("");
                usersSolutionText.setText("");
                ((MainActivity) requireActivity()).switchToMainFragment("eval");
            }

            int numCorrect = checkCorrectness(usersSolutionText);
            ((MainActivity) Objects.requireNonNull(getActivity())).saveHighScore(numCorrect);
            updateStatsView(statsView, numCorrect);
            usersSolutionText.setFocusable(false);
            usersSolutionText.setFocusableInTouchMode(false);
            usersSolutionText.setClickable(false);
            checkButton.setText(R.string.restart_text);
        });
    }

   public int checkCorrectness(EditText editText){
    int correctAnswers = 0;
    String ans = editText.getText().toString();
    SpannableString ss = new SpannableString(ans);
    ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.rgb(255, 127, 138));
    ss.setSpan(fcsRed, 0, ans.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    int total = solutionList.size();
    try{
        for(int i = 0; i < total; ++i){
            if(Integer.parseInt(ans.substring(i,i+1)) == solutionList.get(i)){
                correctAnswers++;
                ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.rgb(101, 195, 104));
                ss.setSpan(fcsGreen, i, i+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        editText.setText(ss);
    }
    catch(Exception ignored){
    }
    return correctAnswers;
   }

    public void updateStatsView(TextView stats, int numCorrectAnswers){
        String statsString = "Total correct: " + numCorrectAnswers;
        stats.setText(statsString);
    }
}