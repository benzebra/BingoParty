package com.example.serverapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextParams;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.renderscript.RenderScript;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    public static class PlayerViewHolder extends RecyclerView.ViewHolder{
        private final TextView NameTV;
        private final TextView flagTV;
        private final Context ctx;

        public PlayerViewHolder(View view){
            super(view);

            this.NameTV = view.findViewById(R.id.nameTV);
            this.flagTV = view.findViewById(R.id.colorTV);
            this.ctx = NameTV.getContext();
        }
    }

    private final ArrayList<Player> dataset;

    public PlayerAdapter(ArrayList<Player> dataset){
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_player, parent, false);

        return new PlayerViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        String playerName = dataset.get(position).getName();
        String playerAddress = dataset.get(position).getAddress();

        Resources res = holder.ctx.getResources();
        Drawable green = ResourcesCompat.getDrawable(res, R.drawable.fullgreen, null);
        Drawable yellow = ResourcesCompat.getDrawable(res, R.drawable.fullyellow, null);

        if(playerName != null){
            holder.flagTV.setBackground(green);
            holder.NameTV.setText(playerName);
        }else{
            holder.flagTV.setBackground(yellow);
            holder.NameTV.setText(playerAddress);
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
