package com.example.serverapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Fragment_Player1 extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public static MatrixAdapter adapter;

    private ArrayList<int[]> myList;

    /**
     * PUBLIC EMPTY CONSTRUCTOR
     * @param playerArrayList
     */
    public Fragment_Player1(ArrayList<int[]> playerArrayList) {
        this.myList = playerArrayList;
    }

    /**
     * FACTORY METHOD
     */
    public static Fragment_Player1 newInstance(ArrayList<int[]> playerArrayList) {
        Fragment_Player1 fragment = new Fragment_Player1(playerArrayList);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__player1, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_field1);
        layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        print(myList);

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

    public void print(ArrayList<int[]> list){
        for(int i=0; i<list.size(); i++){
            System.out.println("print array " + i);
            for(int j=0; j<list.get(i).length; j++){
                System.out.print(list.get(i)[j]);
            }
        }
    }
}