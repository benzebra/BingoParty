package com.example.serverapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MatrixAdapter extends RecyclerView.Adapter<MatrixAdapter.MatrixViewHolder> {

    //ViewHolder class
    public static class MatrixViewHolder extends RecyclerView.ViewHolder{

        private TextView[] TVList = new TextView[9];
        private Context ctx;

        public MatrixViewHolder(View view){
            super(view);

            TVList[0] = view.findViewById(R.id.TV1);
            TVList[1] = view.findViewById(R.id.TV2);
            TVList[2] = view.findViewById(R.id.TV3);
            TVList[3] = view.findViewById(R.id.TV4);
            TVList[4] = view.findViewById(R.id.TV5);
            TVList[5] = view.findViewById(R.id.TV6);
            TVList[6] = view.findViewById(R.id.TV7);
            TVList[7] = view.findViewById(R.id.TV8);
            TVList[8] = view.findViewById(R.id.TV9);

            this.ctx = TVList[0].getContext();
        }
    }

    private ArrayList<int[]> dataset;

    public MatrixAdapter(ArrayList<int[]> dataset){
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public MatrixViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_fields, parent, false);
        MatrixViewHolder matrixViewHolder = new MatrixViewHolder(view);

        return matrixViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MatrixViewHolder holder, int position) {
        TextView toModify;
        String toPut;

        int[] myMatrix = dataset.get(position);
        //System.out.println("DEBUG ADAPTER:65, matrix: " + dataset.get(position));

        for(int i=0; i<myMatrix.length; i++){
            System.out.println("adapter: " + myMatrix[i]);
            toModify = holder.TVList[i];
            toPut = Integer.toString(myMatrix[i]);
            toModify.setText(toPut);
        }
        if(myMatrix.length<9){
            for(int i=myMatrix.length; i<9; i++){
                toModify = holder.TVList[i];
                toModify.setText(null);
            }
        }
    }

    @Override
    public int getItemCount() {
        // 9
        return dataset.size();
    }
}