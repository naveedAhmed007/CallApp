package com.itoasis.callingapp.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itoasis.callingapp.R;
import com.itoasis.callingapp.utils.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static android.app.Activity.RESULT_OK;

public class profile_call extends Fragment {
    androidx.appcompat.widget.AppCompatImageView notification;

    String phoneNUmber, credits,userEmail;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_REQUEST = 2;
    private ImageView selectedImageView;

    private Uri selectedImageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        View notification = v.findViewById(R.id.postfixIcon);
        userEmail =Singleton.getInstance().getUserEmail();
        String userName = Singleton.getInstance().getUserName();
        char Fristchar = Singleton.getInstance().getChar();
        credits = Singleton.getInstance().getCredits();
        phoneNUmber = Singleton.getInstance().getPhoneNumberr();
        // Set the user's name in your TextView
        TextView userNameTextView = v.findViewById(R.id.User_Name);
        TextView firstChar = v.findViewById(R.id.name_profle_alphabet);
        TextView userCredits = v.findViewById(R.id.credits);
        userCredits.setText(credits + " / 240");
        TextView userNumber = v.findViewById(R.id.phoneNumber);
        userNumber.setText(phoneNUmber);
        userNameTextView.setText(userName);
        String Alpha = Character.toString(Fristchar);
        firstChar.setText(Alpha);
        Toast.makeText(requireContext(), userEmail, Toast.LENGTH_SHORT).show();
        selectedImageView = v.findViewById(R.id.selectedImageView);

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Add an OnClickListener to the circularViewLayout

        selectedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog();
            }
        });

        // Check and request permissions if needed
        checkPermissionsAndRequest();

        // Rest of your code...

        return v;
    }

    // Handle the result of image selection or camera capture
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                if (data != null && data.getData() != null) {
                    selectedImageUri = data.getData();
                    displaySelectedImage(selectedImageUri);
                    uploadImageToFirebaseStorage(selectedImageUri);
                }
            } else if (requestCode == CAMERA_CAPTURE_REQUEST) {
                if (data != null && data.getExtras() != null) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    selectedImageView.setImageBitmap(bitmap);

                    // Convert the bitmap to a Uri
                    selectedImageUri = getImageUriFromBitmap(bitmap);
                    uploadImageToFirebaseStorage(selectedImageUri);
                }
            }
        }
    }

    // Display the selected image in the ImageView
    private void displaySelectedImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            selectedImageView.setImageBitmap(bitmap);
            selectedImageView.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Upload the selected image to Firebase Storage
    private void uploadImageToFirebaseStorage(Uri imageUri) {
        if (imageUri != null) {
            StorageReference imageRef = storageReference.child("profile_images/" + userEmail+".jpg");

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = imageRef.putBytes(data);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image upload successful
                        Toast.makeText(getActivity(), "Profile Changed", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Log.e("FirebaseStorage", "Image upload failed: " + e.getMessage());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Show a dialog to let the user choose between gallery and camera
    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Image Source");
        builder.setItems(new CharSequence[]{"Choose from Gallery", "Take Photo"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Choose from Gallery
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                                break;
                            case 1:
                                // Take Photo
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_CAPTURE_REQUEST);
                                break;
                        }
                    }
                });
        builder.show();
    }

    // Convert a Bitmap to a Uri
    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    // Check and request permissions if needed (for Android 6.0 and above)
    private void checkPermissionsAndRequest() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }
}
