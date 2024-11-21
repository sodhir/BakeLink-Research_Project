package com.example.bakelink.bakers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bakelink.R;
import com.example.bakelink.bakers.models.Cake;
import com.example.bakelink.customers.modal.Baker;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class BakerProfileBottomSheetFragment extends BottomSheetDialogFragment {
    private EditText bakeryTitle, description;
    private Button saveButton;

    private String title;
    private  String bakerDesc;

    private OnBakeryUpdateListener updateListener;

    public BakerProfileBottomSheetFragment() {
    }

    public BakerProfileBottomSheetFragment(String bakertitle, String desc, OnBakeryUpdateListener updateListener) {
       this.title = bakertitle;
       this.bakerDesc = desc;
        this.updateListener = updateListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_edit_baker_profile, container, false);

        bakeryTitle = view.findViewById(R.id.bakeryName);
        description = view.findViewById(R.id.bakeryDescription);
        saveButton = view.findViewById(R.id.bakerybtnSaveChanges);

        // Prepopulate data
//        bakeryTitle.setText(baker.getBakeryTitle());
//        description.setText(baker.getDescription());

        saveButton.setOnClickListener(v -> {
            // Update cake data
//            baker.setBakeryTitle(bakeryTitle.getText().toString());
//            baker.setDescription(description.getText().toString());

            updateBakerInDatabase(bakeryTitle.getText().toString(),description.getText().toString());

            // Notify listener about the update
            if (updateListener != null) {
              //  updateListener.onBakeryUpdated(baker);
            }
            dismiss();
        });

        return view;
    }

    private void updateBakerInDatabase(String bakertitle, String description) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Assuming 'bakerId' is the current user's (baker's) unique ID
        String bakerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Reference to the specific cake node within the baker's cakes collection
        DatabaseReference bakerRef = databaseReference.child("bakers").child(bakerId);

        // Create a map of the fields to update
        Map<String, Object> updates = new HashMap<>();
        updates.put("bakeryTitle", bakertitle);
        updates.put("description",description);

        // Perform the update
        bakerRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Toast.makeText(getContext(), "Cake updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Update Cake", "Update failed: " + task.getException().getMessage());
                //  Toast.makeText(getContext(), "Failed to update cake: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public interface OnBakeryUpdateListener {
        void onBakeryUpdated(Baker updatedBaker);
    }

}
