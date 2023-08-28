package com.itoasis.callingapp.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.modal.HistoryModal;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<HistoryModal> HistoryModalArrayList;

    // creating a constructor for our variables.
    public HistoryAdapter(ArrayList<HistoryModal> HistoryModalArrayList, Context context) {
        this.HistoryModalArrayList = HistoryModalArrayList;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<HistoryModal> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        HistoryModalArrayList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_recyclar_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        HistoryModal model = HistoryModalArrayList.get(position);
       holder.callerNameTV.setText(model.getCallerName());
       holder.receiverNameTV.setText(model.getReceiverName());
       holder.timeTV.setText(model.getCallTime());
       holder.dateTV.setText(model.getCallDate());

    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return HistoryModalArrayList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private final TextView callerNameTV;
        private final TextView receiverNameTV;
        private final TextView timeTV;
        private final TextView dateTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            callerNameTV = itemView.findViewById(R.id.callerName);
            receiverNameTV = itemView.findViewById(R.id.ReceuiverName);
            timeTV = itemView.findViewById(R.id.time);
             dateTV= itemView.findViewById(R.id.Date);

        }
    }

}


