package com.example.serverapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Fragment_Player extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public MatrixAdapter adapter;

    private TextView playerNameTV;

    private ArrayList<int[]> myList;
    private String playerName;

    /**
     * PUBLIC EMPTY CONSTRUCTOR
     * @param playerArrayList
     */
    public Fragment_Player(ArrayList<int[]> playerArrayList, String playerName) {
        this.myList = playerArrayList;
        this.playerName = playerName;
    }

    /**
     * FACTORY METHOD
     * @param playerArrayList
     */
    public static Fragment_Player newInstance(ArrayList<int[]> playerArrayList, String playerName) {
        Fragment_Player fragment = new Fragment_Player(playerArrayList, playerName);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__player, container, false);

        playerNameTV = rootView.findViewById(R.id.playerNameTV);
        playerNameTV.setText(playerName);
        recyclerView = rootView.findViewById(R.id.recycler_field);
        layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //print(myList);

        adapter = new MatrixAdapter(myList);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}