package com.example.serverapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayersFieldAdapter extends RecyclerView.Adapter<PlayersFieldAdapter.PlayerFieldViewHolder> {

    public class PlayerFieldViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView fields;

        public PlayerFieldViewHolder(View view){
            super(view);

            name = view.findViewById(R.id.player_name);
            fields = view.findViewById(R.id.player_field);
        }
    }

    private final ArrayList<Player> dataset;

    public PlayersFieldAdapter(ArrayList<Player> dataset){
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public PlayerFieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_playerfields, parent, false);

        return new PlayerFieldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerFieldViewHolder holder, int position) {

        holder.name.setText(dataset.get(position).getName());
        holder.fields.setText(stringify(dataset.get(position).getMatrixArray()));

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public String stringify(ArrayList<int[]> list){
        String returnString = "";
        for(int i=0; i<list.size(); i++){
            for(int j=0; j<list.get(i).length; j++){
                returnString = returnString + list.get(i)[j] + " ";
            }
        }

        return returnString;
    }
}
