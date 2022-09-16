package com.example.spokennumbers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesMenu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Button spoken_numbers_game_button;
    private Button flash_anzan_game_button;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GamesMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GamesMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static GamesMenu newInstance(String param1, String param2) {
        GamesMenu fragment = new GamesMenu();
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
        return inflater.inflate(R.layout.fragment_games_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        spoken_numbers_game_button = getView().findViewById(R.id.spoken_numbers_game_button);
        flash_anzan_game_button = getView().findViewById(R.id.flash_anzan_game_button);

        /* Replace games menu fragment witih spoken numbers fragment
         * on pressing spoken numbers button
         */
        spoken_numbers_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).switchToSpokenNumbersGameFragment();
            }
        });

        /* Replace games menu fragment witih flash anzan fragment
         * on pressing flash anzan button
         */

        flash_anzan_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).switchToFlashAnzanGameFragment();
            }
        });

    }
}