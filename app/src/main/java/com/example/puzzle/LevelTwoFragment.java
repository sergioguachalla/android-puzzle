package com.example.puzzle;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.PopupWindow;
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
    public static String username;

    private TextView[][] textViews = new TextView[4][4];
    Chronometer chronometer;
    private int emptyRow = 3;
    private int emptyCol = 3;

    private int moves = 0;

    Button buttonReset, buttonExit, buttonSolve;
    private TextView textViewMoves,textViewUsername;


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

        buttonExit = view.findViewById(R.id.btExit);
        buttonReset = view.findViewById(R.id.btReset);
        buttonSolve = view.findViewById(R.id.btSolve);
        textViewUsername = view.findViewById(R.id.etUsername);
        textViewMoves = view.findViewById(R.id.tvMoves);
        textViewUsername.setText(username);
        textViewMoves.setText("Movimientos : 0");
        chronometer = view.findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moves = 0;
                textViewMoves.setText("Movimientos: 0");
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                shuffleTiles();
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }
        });


        buttonSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solvePuzzle();
                chronometer.stop();

            }
        });

        initializeTiles(view);
        shuffleTiles();

        return view;
    }

    private void swapTiles(int row, int col) {
        if (isValidMove(row, col)) {
            String text = textViews[row][col].getText().toString();
            Drawable background = textViews[row][col].getBackground();
            Drawable backgroundEmpty = textViews[emptyRow][emptyCol].getBackground();
            textViews[emptyRow][emptyCol].setText(text);
            textViews[row][col].setText(null);
            textViews[emptyRow][emptyCol].setBackground(background);
            textViews[row][col].setBackground(backgroundEmpty);
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
                    textViewMoves.setText(move);
                    if (isSolved()) {
                        chronometer.stop();
                        showWinToast(SystemClock.elapsedRealtime() - chronometer.getBase(), moves);
                        showPopup();
                    }
                }
            }
        });
    }

    private void solvePuzzle() {
        // Initialize the tiles in order.
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                char letter = (char)(i * 4 + j + 65);
                textViews[i][j].setText(String.valueOf(letter));
            }
        }
        textViews[3][3].setText("");
    }


    private void showPopup(){

        View popupView = LayoutInflater.from(getContext() ).inflate(R.layout.popup_layout, null);

        TextView timeTextView = popupView.findViewById(R.id.timeTextView);
        TextView movesTextView = popupView.findViewById(R.id.movesTextView);
        TextView scoreTextView = popupView.findViewById(R.id.scoreTextView);

        long elapsedTime = SystemClock.elapsedRealtime() - chronometer.getBase();
        String formattedTime = "Tiempo: " +  elapsedTime / 1000 + " segundos";

        String formattedMoves = "Movimientos: " + moves;

        String score = "Puntaje: " + getScore(moves);

        timeTextView.setText(formattedTime);
        movesTextView.setText(formattedMoves);
        scoreTextView.setText(score);
        View rootView = getActivity().getWindow().getDecorView();


        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
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

    private Double getScore(int moves){
        //the score is between 0 and 100

        return 100.0 - (moves * 2.5);


    }

}