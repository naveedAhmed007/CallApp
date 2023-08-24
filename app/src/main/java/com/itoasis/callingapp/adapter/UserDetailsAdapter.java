package com.itoasis.callingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.modal.UserDetailsModal;

import java.util.ArrayList;

public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.ViewHolder> {

    private ArrayList<UserDetailsModal> UserDetailsModalArrayList;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userDetailsCollection = db.collection("userDetails");

    public UserDetailsAdapter(ArrayList<UserDetailsModal> UserDetailsModalArrayList, Context context) {
        this.UserDetailsModalArrayList = UserDetailsModalArrayList;
        this.context=context;
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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user ID of the item to be deleted
                String userId = model.getUserId(); // Assuming this is the unique identifier for the user

                // Call a method to delete the user from Firestore
                deleteDocumentById(userId);

                // Remove the item from the RecyclerView locally
                removeAt(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return UserDetailsModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, id, creditstv;
        private final ImageButton edit, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_value);
            id = itemView.findViewById(R.id.id_value);
            edit = itemView.findViewById(R.id.imageButtonEdit);
            creditstv = itemView.findViewById(R.id.creditstv);
            delete = itemView.findViewById(R.id.imageButtonDelete);
        }
    }

    private void removeAt(int position) {
        UserDetailsModalArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, UserDetailsModalArrayList.size());
    }
    private void deleteDocumentById(String documentId) {
        userDetailsCollection.document(documentId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document deleted successfully
                        Toast.makeText(context, "Document deleted from Firestore", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle deletion failure
                        Toast.makeText(context, "Error deleting document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Firestore Delete Error", "Error deleting document: " + e.getMessage());
                    }
                });
    }
}
