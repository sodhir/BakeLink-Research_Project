package com.example.bakelink.customers;

import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BottomSheetEditProfile extends BottomSheetDialogFragment {

    private EditText fullNameEditText, addressEditText, dobEditText;
    private Button saveButton;
    private FirebaseAuth auth;
    private DatabaseReference customersReference;

    public BottomSheetEditProfile() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_edit_profile, container, false);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        customersReference = FirebaseDatabase.getInstance().getReference("Customers");

        // Initialize UI components
        fullNameEditText = view.findViewById(R.id.et_full_name);
        addressEditText = view.findViewById(R.id.et_address);
        dobEditText = view.findViewById(R.id.et_dob);
        saveButton = view.findViewById(R.id.btn_save);

        // Save button click listener
        saveButton.setOnClickListener(v -> {
            String userId = auth.getCurrentUser().getUid();
            String fullName = fullNameEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String dob = dobEditText.getText().toString().trim();

            if (!fullName.isEmpty() && !address.isEmpty() && !dob.isEmpty()) {
                customersReference.child(userId).child("fullName").setValue(fullName);
                customersReference.child(userId).child("address").setValue(address);
                customersReference.child(userId).child("dob").setValue(dob)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            dismiss();
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
