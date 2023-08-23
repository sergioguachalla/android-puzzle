package com.example.puzzle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    Button buttonLevelOne, buttonLevelTwo;

    EditText editTextName;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        editTextName = view.findViewById(R.id.etName);

        buttonLevelOne = view.findViewById(R.id.btnToLevel1);
        buttonLevelTwo = view.findViewById(R.id.btnToLevel2);
        buttonLevelOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextName.getText().toString();
                LevelOneFragment.username = username;
                if(!username.isEmpty()) {
                    LevelOneFragment levelOneFragment = new LevelOneFragment();

                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, levelOneFragment).commit();
                }else{
                    showAlertDialog();
                }
            }
        });

        buttonLevelTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LevelTwoFragment levelTwoFragment = new LevelTwoFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, levelTwoFragment).commit();
            }
        });

        return view;
    }
    private void showAlertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                .setTitle("Alerta")
                .setMessage("El campo de nombre está vacío. Por favor, ingresa tu nombre.")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aquí puedes agregar acciones adicionales si es necesario
                    }
                })
                .create();

        alertDialog.show();
    }

}