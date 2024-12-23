package com.example.bakelink.bakers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bakelink.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SpecialitiesBottomSheetFragment extends BottomSheetDialogFragment {

    private EditText editTextSpeciality;
    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add_baker_speciality, container, false);

        editTextSpeciality = view.findViewById(R.id.edit_speciality_service);
        saveButton = view.findViewById(R.id.btn_save);

        saveButton.setOnClickListener(v -> {
            String speciality = editTextSpeciality.getText().toString().trim();
            if (!speciality.isEmpty()) {

                ((B_ProfileSetupActivity) getActivity()).addSpecialityToList(speciality);

                dismiss();
            }
        });

        return view;
    }

}

