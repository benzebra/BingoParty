package com.example.myapplication;

import com.example.myapplication.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
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

    private ArrayList<Matrix> dataset;

    public MatrixAdapter(ArrayList<Matrix> dataset){
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public MatrixViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_matrix, parent, false);
        MatrixViewHolder matrixViewHolder = new MatrixViewHolder(view);

        return matrixViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MatrixViewHolder holder, int position) {

        TextView toModify;
        String toPut;

        Resources res = holder.ctx.getResources();
        Drawable extracted = ResourcesCompat.getDrawable(res, R.drawable.selected_ball, null);
        Drawable toExtract = ResourcesCompat.getDrawable(res, R.drawable.to_select_ball, null);

        Matrix.MatrixNumbers[] myMatrix = dataset.get(position).getMyMatrix();
        //System.out.println("DEBUG ADAPTER:65, matrix: " + dataset.get(position));

        for(int i=0; i<myMatrix.length; i++){

            toModify = holder.TVList[i];
            toPut = myMatrix[i].getNumToString();
            toModify.setText(toPut);


            //System.out.println("Adapter:81 - " + myMatrix[i].getFlag());
            if(myMatrix[i].getFlag()){
                toModify.setBackground(extracted);
                //toModify.setBackground(null);
                //((TextView) toModify).setText(toPut);
            }else{
                toModify.setBackground(toExtract);
                //((TextView) toModify).setText(toPut);
            }
        }
    }

    @Override
    public int getItemCount() {
        // 9
        return dataset.size();
    }
}
