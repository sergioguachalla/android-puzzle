package com.example.puzzle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LevelOneFragment.
     */
    // TODO: Rename and change types and number of parameters
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
                        // Implementa la lógica para intercambiar piezas o moverlas
                        // Usando finalI y finalJ como índices
                    }
                });
            }
        }

        return view;
    }

    private boolean isValidMove(int row, int col) {
        // Verifica si la pieza se puede mover a la posición vacía
        return (Math.abs(row - emptyRow) + Math.abs(col - emptyCol)) == 1;
    }

    private void swapTiles(int row, int col) {
        // Intercambia las posiciones de la pieza seleccionada y la vacía
        String text = textViews[row][col].getText().toString();
        textViews[emptyRow][emptyCol].setText(text);
        textViews[row][col].setText("");

        emptyRow = row;
        emptyCol = col;
    }

}