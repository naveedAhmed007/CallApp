package com.itoasis.callingapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itoasis.callingapp.Fragments.Summary;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.modal.SummaryModal;

import java.util.ArrayList;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<SummaryModal> SummaryModalArrayList;


    // creating a constructor for our variables.
    public SummaryAdapter(ArrayList<SummaryModal> SummaryModalArrayList, Context context) {
        this.SummaryModalArrayList = SummaryModalArrayList;
    }

    // method for filtering our recyclerview items.
    

    @NonNull
    @Override
    public SummaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        SummaryModal model = SummaryModalArrayList.get(position);
        holder.cname.setText(model.getCName());
        holder.minutes.setText(model.getMinutes());
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return SummaryModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private final TextView cname;
        private final TextView minutes;
        private final ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            cname = itemView.findViewById(R.id.cName);
            minutes = itemView.findViewById(R.id.credit);
            imageView = itemView.findViewById(R.id.cflag);
        }
    }
}

