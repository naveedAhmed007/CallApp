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

    private ArrayList<MessageModal> messageModalArrayList;
    private Context context;

    private ItemClickListener itemClickListener;

    public MessageAdapter(ArrayList<MessageModal> messageModalArrayList, Context context, ItemClickListener listener) {
        this.messageModalArrayList = messageModalArrayList;
        this.context = context;
        this.itemClickListener = listener;
    }

    public void filterList(ArrayList<MessageModal> filterlist) {
        messageModalArrayList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageModal model = messageModalArrayList.get(holder.getAdapterPosition());
        holder.nameChat.setText(model.getRoomName());

        // Set an OnClickListener on the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Trigger the onItemClick method of the ItemClickListener
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameChat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameChat = itemView.findViewById(R.id.NameChat);
        }
    }

    // Define an interface for item click events
    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
