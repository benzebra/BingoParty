package com.example.serverapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_STD extends Fragment {

    public Fragment_STD() {
        // Required empty public constructor
    }

    public static Fragment_STD newInstance() {
        Fragment_STD fragment = new Fragment_STD();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__s_t_d, container, false);
        // Inflate the layout for this fragment
        return rootView;
    }
}