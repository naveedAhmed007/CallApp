package com.itoasis.callingapp.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.modal.UserDetailsModal;

import java.util.ArrayList;

public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<UserDetailsModal> UserDetailsModalArrayList;
    Context context;


    // creating a constructor for our variables.
    public UserDetailsAdapter(ArrayList<UserDetailsModal> UserDetailsModalArrayList, Context context) {
        this.UserDetailsModalArrayList = UserDetailsModalArrayList;
        this.context=context;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<UserDetailsModal> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        UserDetailsModalArrayList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_details_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // setting data to our views of recycler view.
        UserDetailsModal model = UserDetailsModalArrayList.get(position);
        holder.name.setText(model.getName());
        holder.id.setText(model.getUserId());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return UserDetailsModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private final TextView name,id;
        private final ImageButton edit,delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            name = itemView.findViewById(R.id.name_value);
            id = itemView.findViewById(R.id.id_value);
            edit=itemView.findViewById(R.id.imageButtonEdit);
            delete=itemView.findViewById(R.id.imageButtonDelete);


        }
    }
    private void removeAt(int position) {
        UserDetailsModalArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, UserDetailsModalArrayList.size());
    }
}
