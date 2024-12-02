package com.example.bakelink.bakers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bakelink.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class WeightsBottomSheetFragment extends BottomSheetDialogFragment {
    private EditText editTextWeights;
    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add_weight, container, false);

        editTextWeights = view.findViewById(R.id.edit_speciality_service);
        saveButton = view.findViewById(R.id.btn_save);

        saveButton.setOnClickListener(v -> {
            String weight = editTextWeights.getText().toString().trim();
            if (!weight.isEmpty()) {

                ((B_AddNewCakeActivity) getActivity()).addWeightsToList(weight);
                dismiss();
            }
        });

        return view;
    }
}
