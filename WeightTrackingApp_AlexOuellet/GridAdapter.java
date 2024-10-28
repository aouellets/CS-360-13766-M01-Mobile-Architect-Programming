package com.example.weighttrackingapp_alexouellet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private List<WeightEntry> weightEntries;

    public GridAdapter(List<WeightEntry> weightEntries) {
        this.weightEntries = weightEntries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weight_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeightEntry entry = weightEntries.get(position);
        holder.weightTextView.setText(entry.getWeight() + " kg");
        holder.dateTextView.setText(entry.getDate());
    }

    @Override
    public int getItemCount() {
        return weightEntries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView weightTextView, dateTextView;

        ViewHolder(View itemView) {
            super(itemView);
            weightTextView = itemView.findViewById(R.id.weightTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
