package com.example.puzzle;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LevelOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelOneFragment extends Fragment {

    private TextView[][] textViews = new TextView[3][3];
    private int emptyRow = 2; // Fila vacía inicial
    private int emptyCol = 2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LevelOneFragment() {
        // Required empty public constructor
    }


    public static LevelOneFragment newInstance(String param1, String param2) {
        LevelOneFragment fragment = new LevelOneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_level_one, container, false);
        initializeTiles(view);
        shuffleTiles();
        return view;
    }

    private boolean isValidMove(int row, int col) {
        return (Math.abs(row - emptyRow) + Math.abs(col - emptyCol)) == 1;
    }

    private void swapTiles(int row, int col) {
        String text = textViews[row][col].getText().toString();
        Drawable background = textViews[row][col].getBackground();
        Drawable emptyBackground = textViews[emptyRow][emptyCol].getBackground();
        textViews[emptyRow][emptyCol].setText(text);
        textViews[row][col].setText("X");
        textViews[row][col].setBackground(emptyBackground);
        textViews[emptyRow][emptyCol].setBackground(background);

        emptyRow = row;
        emptyCol = col;
    }

    private void initializeTiles(View view){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String textViewId = "textView" + i + j;
                int resId = getResources().getIdentifier(textViewId, "id", requireActivity().getPackageName());
                textViews[i][j] = view.findViewById(resId);

                final int finalI = i;
                final int finalJ = j;
                textViews[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isValidMove(finalI, finalJ)) {
                            swapTiles(finalI, finalJ);
                        }
                    }
                });

            }
        }

    }

    private void shuffleTiles() {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < 3 * 3; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int newPosition = positions.get(i * 3 + j);
                int newRow = newPosition / 3;
                int newCol = newPosition % 3;

                String currentText = textViews[i][j].getText().toString();
                Drawable currentBackground = textViews[i][j].getBackground();

                textViews[i][j].setText(textViews[newRow][newCol].getText());
                textViews[i][j].setBackground(textViews[newRow][newCol].getBackground());

                textViews[newRow][newCol].setText(currentText);
                textViews[newRow][newCol].setBackground(currentBackground);

                if (currentText.equals("X")) {
                    emptyRow = newRow;
                    emptyCol = newCol;
                }
            }
        }
    }








}