package com.example.bakelink.bakers;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bakelink.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BakerProfileBottomSheetFragment extends BottomSheetDialogFragment {
    private EditText bakeryTitle, description;
    private Button saveButton;
    private String title, bakerDesc, currentUserId;
    private DatabaseReference databaseReference;

    public BakerProfileBottomSheetFragment() {
    }

    public BakerProfileBottomSheetFragment(String currentUserId, String bakeryTitle, String description) {
        this.currentUserId = currentUserId;
        this.title = bakeryTitle;
        this.bakerDesc = description;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_edit_baker_profile, container, false);

        // Initialize Views
        bakeryTitle = view.findViewById(R.id.bakeryName);
        description = view.findViewById(R.id.bakeryDescription);
        saveButton = view.findViewById(R.id.bakerybtnSaveChanges);

        // Pre-fill data
        bakeryTitle.setText(title);
        description.setText(bakerDesc);

        // Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("bakers").child(currentUserId);

        // Save button listener
        saveButton.setOnClickListener(v -> {
            String updatedTitle = bakeryTitle.getText().toString().trim();
            String updatedDescription = description.getText().toString().trim();

            if (TextUtils.isEmpty(updatedTitle) || TextUtils.isEmpty(updatedDescription)) {
                Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update data in Firebase
            databaseReference.child("bakeryTitle").setValue(updatedTitle);
            databaseReference.child("description").setValue(updatedDescription)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                            dismiss(); // Close the bottom sheet
                        } else {
                            Toast.makeText(getContext(), "Failed to update. Try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return view;
    }


}
