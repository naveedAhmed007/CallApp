package com.itoasis.callingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.modal.PaymentModal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<PaymentModal> PaymentModalArrayList;

    // creating a constructor for our variables.
    public PaymentAdapter(ArrayList<PaymentModal> PaymentModalArrayList, Context context) {
        this.PaymentModalArrayList = PaymentModalArrayList;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<PaymentModal> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        PaymentModalArrayList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        PaymentModal model = PaymentModalArrayList.get(position);
        holder.name.setText(model.getName());
        holder.date.setText(model.getDate());
        holder.price.setText(model.getPrice());
        holder.iteration.setText(model.getIteration());

//        Picasso
//                .get()
//                .load(model.getUri())
//                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return PaymentModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private final TextView name,date,price,iteration;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            name = itemView.findViewById(R.id.paymentName);
            date = itemView.findViewById(R.id.time);
            price = itemView.findViewById(R.id.price);
            iteration = itemView.findViewById(R.id.iteration);
            imageView = itemView.findViewById(R.id.profile_image);


        }
    }
}
