package com.itoasis.callingapp.adapter;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itoasis.callingapp.Fragments.add_user;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.modal.UserDetailsModal;

import java.util.ArrayList;

public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.ViewHolder> {

    private static ArrayList<UserDetailsModal> UserDetailsModalArrayList;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userDetailsCollection = db.collection("users");

    public UserDetailsAdapter(ArrayList<UserDetailsModal> UserDetailsModalArrayList, Context context) {
        this.UserDetailsModalArrayList = UserDetailsModalArrayList;
        this.context = context;
    }

    public void filterList(ArrayList<UserDetailsModal> filterlist) {
        UserDetailsModalArrayList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_details_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        UserDetailsModal model = UserDetailsModalArrayList.get(position);
        holder.name.setText(model.getName());
        holder.id.setText(model.getUserId());
        holder.creditstv.setText(model.getCredit() + "/240");

        // Edit button click listener
        holder.edit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isEditMode", true);
            bundle.putString("userId", model.getUserId());
            add_user editFragment = new add_user();
            editFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = ((AppCompatActivity) context)
                    .getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.flFragment, editFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        // Delete button click listener
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a confirmation dialog
                showDeleteConfirmationDialog(model.getUserId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return UserDetailsModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, id, creditstv;
        private final ImageButton edit, delete,chat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_value);
            id = itemView.findViewById(R.id.id_value);
            edit = itemView.findViewById(R.id.imageButtonEdit);
            creditstv = itemView.findViewById(R.id.creditstv);
            delete = itemView.findViewById(R.id.imageButtonDelete);
            chat =itemView.findViewById(R.id.imageButtonChat);

// Add an OnClickListener for the "Chat" button here
        chat.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get the user's ID from the model
            String userId = UserDetailsModalArrayList.get(getAdapterPosition()).getUserId();

            // Display the user's ID (you can customize this action, e.g., start a chat activity)
            Toast.makeText(itemView.getContext(), "Chat with User ID: " + userId, Toast.LENGTH_SHORT).show();
        }
    });
}}
    private void showDeleteConfirmationDialog(String userId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this user?");

        // Add buttons for Yes and No
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Yes, proceed with deletion
                deleteDocumentById(userId, position);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked No, do nothing
                dialog.dismiss();
            }
        });

        // Show the dialog
        builder.create().show();
    }

    private void removeAt(int position) {
        UserDetailsModalArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, UserDetailsModalArrayList.size());
    }

    // Method to delete a user by their user ID
    private void deleteDocumentById(String userId, int position) {
        Query query = userDetailsCollection.whereEqualTo("email", userId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Assuming there is only one document with the given user ID
                        String documentId = document.getId();
                        userDetailsCollection.document(documentId).delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // User deleted successfully
                                            Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
                                            removeAt(position); // Remove from RecyclerView
                                        } else {
                                            // Handle errors
                                            Toast.makeText(context, "Error deleting user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    // Handle errors
                    Toast.makeText(context, "Error deleting user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
