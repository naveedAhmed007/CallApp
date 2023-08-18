package com.itoasis.callingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.modal.HistoryModal;
import com.itoasis.callingapp.modal.SetPricesModal;

import java.util.ArrayList;

public class SetPricesAdapter extends RecyclerView.Adapter<SetPricesAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<SetPricesModal> SetPricesModalArrayList;

    // creating a constructor for our variables.
    public SetPricesAdapter(ArrayList<SetPricesModal> SetPricesModalArrayList, Context context) {
        this.SetPricesModalArrayList = SetPricesModalArrayList;
    }

    public void filterList(ArrayList<SetPricesModal> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        SetPricesModalArrayList= filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
    // method for filtering our recyclerview items.


    @NonNull
    @Override
    public SetPricesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_price_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetPricesAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        SetPricesModal model = SetPricesModalArrayList.get(position);
        holder.country.setText(model.getCountryName());

        holder.c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.c2.getVisibility()==View.GONE)  holder.c2.setVisibility(View.VISIBLE);
                else if(holder.c2.getVisibility()==View.VISIBLE)  holder.c2.setVisibility(View.GONE);


            }
        });



    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return SetPricesModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private final TextView country;
        CardView c1,c2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            country = itemView.findViewById(R.id.country);
            c1 = itemView.findViewById(R.id.c1);
            c2 = itemView.findViewById(R.id.c2);


        }
    }
}

