package com.itoasis.callingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.modal.MessageModal;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<MessageModal> MessageModalArrayList;

    // creating a constructor for our variables.
    public MessageAdapter(ArrayList<MessageModal> MessageModalArrayList, Context context) {
        this.MessageModalArrayList = MessageModalArrayList;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<MessageModal> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        MessageModalArrayList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        MessageModal model = MessageModalArrayList.get(position);
        holder.nameChat.setText(model.getName());
        holder.notificationTV.setText(model.getMessage());
        holder.time.setText(model.getTime());
    }


    @Override
    public int getItemCount() {
        // returning the size of array list.
        return MessageModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private final TextView nameChat;
        private final TextView notificationTV;
        private final TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            nameChat = itemView.findViewById(R.id.NameChat);
            notificationTV = itemView.findViewById(R.id.notificationTV);
            time = itemView.findViewById(R.id.timeText);
        }
    }
}
