package com.example.puzzle;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LevelOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelOneFragment extends Fragment {

    public static String username;
    private int moves = 0;
    private TextView[][] textViews = new TextView[3][3];

    Button buttonReset, buttonExit;

    private TextView textViewMoves,textViewUsername;
    private static final String[][] SOLVED_ARRANGEMENT = {
            {"1", "2", "3"},
            {"4", "5", "6"},
            {"7", "8", ""}
    };
    Chronometer chronometer;

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

        chronometer = view.findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        initializeTiles(view);
        shuffleTiles();
        textViewMoves = view.findViewById(R.id.tvMoves);
        buttonReset = view.findViewById(R.id.btReset);
        buttonExit = view.findViewById(R.id.btExit);
        textViewUsername = view.findViewById(R.id.etUsername);
        textViewUsername.setText(username);
        textViewMoves.setText("Movimientos : o");
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
                //go to home fragment
                HomeFragment homeFragment = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

            }
        });

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
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
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
                    }
                }
            }
        });
    }


    private void shuffleTiles() {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < 3 * 3; i++) {
            positions.add(i);
        }

        int emptyPosition = emptyRow * 3 + emptyCol;

        // Realiza la mezcla aleatoria, pero limita la cantidad de intentos para evitar bloqueos
        int attempts = 0;
        final int MAX_ATTEMPTS = 1000;
        do {
            Collections.shuffle(positions);
            attempts++;
        } while ((!isSolvable(positions) || positions.indexOf(emptyPosition) != positions.size() - 1) && attempts < MAX_ATTEMPTS);

        if (attempts >= MAX_ATTEMPTS) {
            // Manejar aquí la situación si la mezcla no puede generar una solución después de muchos intentos
            // Por ejemplo, podrías mostrar un mensaje de error al usuario
            return;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int newPosition = positions.get(i * 3 + j);
                int newRow = newPosition / 3;
                int newCol = newPosition % 3;

                String currentText = textViews[i][j].getText().toString();
                Drawable currentBackground = textViews[i][j].getBackground();

                // Intercambia el texto y el fondo entre las casillas
                textViews[i][j].setText(textViews[newRow][newCol].getText());
                textViews[i][j].setBackground(textViews[newRow][newCol].getBackground());

                textViews[newRow][newCol].setText(currentText);
                textViews[newRow][newCol].setBackground(currentBackground);

                if (currentText.equals("")) {
                    emptyRow = newRow;
                    emptyCol = newCol;
                }
            }
        }
    }







    private boolean isSolved() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
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
        int blankRowFromBottom = 3 - emptyRow; // Blank cell's row from the bottom

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








