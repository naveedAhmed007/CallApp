package com.itoasis.callingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.modal.ChatMessage;
import com.itoasis.callingapp.utils.Singleton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SENDER_VIEW = 1;
    private static final int RECEIVER_VIEW = 2;
    private RecyclerView recyclerView;
    private String clientEmail;
    private List<ChatMessage> chatMessages;
    private SimpleDateFormat dateFormat;

    // Constructor that takes a List<ChatMessage> as a parameter
    public ChatAdapter(List<ChatMessage> chatMessages, String clientEmail, RecyclerView recyclerView) {
        this.chatMessages = chatMessages;
        this.clientEmail = clientEmail;
        this.recyclerView = recyclerView;
        this.dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == SENDER_VIEW) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sender_chat_item, parent, false);
            return new SenderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reciever_chat_item, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);

        if (holder.getItemViewType() == SENDER_VIEW) {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.senderMessageText.setText(chatMessage.getMessage());
            senderViewHolder.senderMessageTime.setText(formatTimestamp(chatMessage.getTimestamp()));
        } else {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
            receiverViewHolder.receiverMessageText.setText(chatMessage.getMessage());
            receiverViewHolder.receiverMessageTime.setText(formatTimestamp(chatMessage.getTimestamp()));
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = chatMessages.get(position);
        String loggedInUserEmail = Singleton.getInstance().getUserEmail();

        if (message != null && loggedInUserEmail != null && loggedInUserEmail.equals(message.getSender())) {
            return SENDER_VIEW; // Message sent by the logged-in user
        } else {
            return RECEIVER_VIEW; // Message received from the other party
        }
    }

    // Add this method to format the timestamp
    private String formatTimestamp(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }

    // ViewHolder for sender's chat message
    private static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessageText;
        TextView senderMessageTime;

        SenderViewHolder(View itemView) {
            super(itemView);
            senderMessageText = itemView.findViewById(R.id.sender_message_text);
            senderMessageTime = itemView.findViewById(R.id.sender_message_time);
        }
    }

    // ViewHolder for receiver's chat message
    private static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMessageText;
        TextView receiverMessageTime;

        ReceiverViewHolder(View itemView) {
            super(itemView);
            receiverMessageText = itemView.findViewById(R.id.receiver_message_text);
            receiverMessageTime = itemView.findViewById(R.id.reciever_message_time);
        }
    }
}
