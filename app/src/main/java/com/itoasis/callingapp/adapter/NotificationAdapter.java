package com.itoasis.callingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.modal.HistoryModal;
import com.itoasis.callingapp.modal.NotificationModal;

import java.util.ArrayList;

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.ViewHolder>  {
    private ArrayList<NotificationModal> notilist;

    // creating a constructor for our variables.
    public NotificationAdapter(ArrayList<NotificationModal> courseModelArrayList, Context context) {
        this.notilist = courseModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModal model = notilist.get(position);
        holder.notiDes.setText(model.getNotificationDescription());
        holder.time.setText(model.getNotificationTime());

    }

    @Override
    public int getItemCount() {
        return notilist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private final TextView notiDes;
        private final TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            notiDes = itemView.findViewById(R.id.notificationTV);
            time = itemView.findViewById(R.id.timeText);
        }
    }
}
