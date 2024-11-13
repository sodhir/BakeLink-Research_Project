package com.example.bakelink.bakers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bakelink.R;
import com.example.bakelink.bakers.models.Cake;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditCakeBottomSheet extends BottomSheetDialogFragment {
    private EditText etCakeName, etCakeDescription, etCakePrice;
    private Button btnSaveChanges;
    private Cake cake;
    private OnCakeUpdateListener updateListener;

    public EditCakeBottomSheet(Cake cake, OnCakeUpdateListener updateListener) {
        this.cake = cake;
        this.updateListener = updateListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_edit_cake, container, false);

        etCakeName = view.findViewById(R.id.etCakeName);
        etCakeDescription = view.findViewById(R.id.etCakeDescription);
        etCakePrice = view.findViewById(R.id.etCakePrice);
        btnSaveChanges = view.findViewById(R.id.btnSaveChanges);

        // Prepopulate data
        etCakeName.setText(cake.getCakeName());
        etCakeDescription.setText(cake.getDescription());
        etCakePrice.setText(String.valueOf(cake.getPrice()));

        // Handle save button click
        btnSaveChanges.setOnClickListener(v -> {
            // Update cake data
            cake.setCakeName(etCakeName.getText().toString());
            cake.setDescription(etCakeDescription.getText().toString());
            try {
                cake.setPrice(Double.parseDouble(etCakePrice.getText().toString()));
            } catch (NumberFormatException e) {
                etCakePrice.setError("Invalid price");
                return;
            }

            updateCakeInDatabase(cake);

            // Notify listener about the update
            if (updateListener != null) {
                updateListener.onCakeUpdated(cake);
            }
            dismiss();
        });

        return view;
    }


    private void updateCakeInDatabase(Cake updatedCake) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Assuming 'bakerId' is the current user's (baker's) unique ID
        String bakerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String cakeId = updatedCake.getCakeId();

        // Log to check IDs
        Log.d("Update Cake", "Baker ID: " + bakerId);
        Log.d("Update Cake", "Cake ID: " + cakeId);

        // Check if bakerId and cakeId are not null
        if (bakerId == null || cakeId == null) {
            Toast.makeText(getContext(), "Invalid baker or cake ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reference to the specific cake node within the baker's cakes collection
        DatabaseReference cakeRef = databaseReference.child("bakers").child(bakerId).child("cakes").child(cakeId);

        // Create a map of the fields to update
        Map<String, Object> updates = new HashMap<>();
        updates.put("cakeName", updatedCake.getCakeName());
        updates.put("description", updatedCake.getDescription());
        updates.put("price", updatedCake.getPrice());  // Ensure this is a number
        updates.put("cakeImgUrl", updatedCake.getCakeImgUrl());  // Update other fields as needed

        // Perform the update
        cakeRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
               // Toast.makeText(getContext(), "Cake updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Update Cake", "Update failed: " + task.getException().getMessage());
                Toast.makeText(getContext(), "Failed to update cake: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface OnCakeUpdateListener {
        void onCakeUpdated(Cake updatedCake);
    }


}
