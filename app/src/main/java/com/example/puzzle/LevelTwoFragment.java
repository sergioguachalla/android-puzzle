package com.example.puzzle;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LevelTwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelTwoFragment extends Fragment {

    private TextView[][] textViews = new TextView[4][4];
    private int emptyRow = 3;
    private int emptyCol = 3;

    private int moves = 0;

    //letters
    private static final String[][] SOLVED_ARRANGEMENT = {
            {"A", "B", "C", "D"},
            {"E", "F", "G", "H"},
            {"I", "J", "K", "L"},
            {"M", "N", "O", ""}
    };
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LevelTwoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LevelTwoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LevelTwoFragment newInstance(String param1, String param2) {
        LevelTwoFragment fragment = new LevelTwoFragment();
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
        View view = inflater.inflate(R.layout.fragment_level_two, container, false);

        initializeTiles(view);
        shuffleTiles();
        return view;
    }

    private void swapTiles(int row, int col) {
        if (isValidMove(row, col)) {
            String text = textViews[row][col].getText().toString();
            Drawable background = textViews[row][col].getBackground();
            textViews[emptyRow][emptyCol].setText(text);
            textViews[row][col].setText(null);
            textViews[emptyRow][emptyCol].setBackground(background);
            textViews[row][col].setBackground(null);
            emptyRow = row;
            emptyCol = col;
        }
    }

    private boolean isValidMove(int row, int col) {
        return (Math.abs(row - emptyRow) == 1 && col == emptyCol) ||
                (Math.abs(col - emptyCol) == 1 && row == emptyRow);
    }




    private void initializeTiles(View view) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String textViewId = "textView" + i + j;
                int resId = getResources().getIdentifier(textViewId, "id", requireActivity().getPackageName());
                textViews[i][j] = view.findViewById(resId);

                setClickListenerForTile(i, j); // Set click listener for this tile
            }
        }
    }

    private void setClickListenerForTile(final int row, final int col) {
        textViews[row][col].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click en " + textViews[row][col].getText());

                if (isValidMove(row, col)) {
                    swapTiles(row, col);
                    moves++;
                    String move = "Movimientos: " + moves;

                    if (isSolved()) {
                       Toast.makeText(requireContext(), "Ganaste!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    private void shuffleTiles() {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < 4 * 4; i++) {
            positions.add(i);
        }

        int emptyPosition = emptyRow * 4 + emptyCol; // Step 1

        do {
            Collections.shuffle(positions);
        } while (!isSolvable(positions) || positions.indexOf(emptyPosition) != positions.size() - 1); // Step 2

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int newPosition = positions.get(i * 4 + j);
                int newRow = newPosition / 4;
                int newCol = newPosition % 4;

                String currentText = textViews[i][j].getText().toString();
                Drawable currentBackground = textViews[i][j].getBackground();

                textViews[i][j].setText(textViews[newRow][newCol].getText());
                textViews[i][j].setBackground(textViews[newRow][newCol].getBackground());

                textViews[newRow][newCol].setText(currentText);
                textViews[newRow][newCol].setBackground(currentBackground);

                if (currentText.equals("")) {
                    emptyRow = newRow; // Update emptyRow
                    emptyCol = newCol; // Update emptyCol
                }
            }
        }
    }






    private boolean isSolved() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String currentText = textViews[i][j].getText().toString();

                if (!currentText.equals(SOLVED_ARRANGEMENT[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isSolvable(List<Integer> positions) {
        int inversions = 0;
        int blankRowFromBottom = 4 - emptyRow; // Blank cell's row from the bottom

        for (int i = 0; i < positions.size(); i++) {
            for (int j = i + 1; j < positions.size(); j++) {
                if (positions.get(i) != 0 && positions.get(j) != 0 && positions.get(i) > positions.get(j)) {
                    inversions++;
                }
            }
        }

        if (blankRowFromBottom % 2 == 0) {
            return inversions % 2 == 0;
        } else {
            return inversions % 2 != 0;
        }
    }



    private void showWinToast(long elapsedTime, int moves) {
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        seconds %= 60;

        String message = "Ganaste!  " + minutes + ": " + seconds + " con " + moves + " movimientos";
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

}