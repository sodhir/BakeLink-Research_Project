package com.example.bakelink.bakers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bakelink.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FillingsBottomSheetFragment extends BottomSheetDialogFragment {
    private EditText editTextFillings;
    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add_filling, container, false);

        editTextFillings = view.findViewById(R.id.edit_speciality_service);
        saveButton = view.findViewById(R.id.btn_save);

        saveButton.setOnClickListener(v -> {
            String fillings = editTextFillings.getText().toString().trim();
            if (!fillings.isEmpty()) {
                // Pass data back to activity or fragment
                ((B_AddNewCakeActivity) getActivity()).addFillingsToList(fillings);
                dismiss(); // Dismiss bottom sheet
            }
        });

        return view;
    }
}
