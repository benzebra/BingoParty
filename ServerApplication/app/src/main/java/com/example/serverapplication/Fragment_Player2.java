package com.example.serverapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_Player2 extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    //private adapter myadp;

    /**
     * PUBLIC EMPTY CONSTRUCTOR
     */
    public Fragment_Player2() { }

    /**
     * FACTORY METHOD
     */
    public static Fragment_Player2 newInstance() {
        Fragment_Player2 fragment = new Fragment_Player2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = this.getView().findViewById(R.id.recycler_field2);
        layoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        //adapter = new adapter(dataset)
        //recyclerview.setadapter(adapter)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__player2, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        //adapter.notifydatasetchanged();
    }
}