package com.example.writerz;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;


public class EducationFragment extends Fragment {
    Spinner classSpinner;
    Spinner branchSpinner;
    String[] branch;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_education, container, false);

        classSpinner = view.findViewById(R.id.class_spinner);
        branchSpinner = view.findViewById(R.id.branch_spinner);

        String[] class2 = {"10th or Below 10th", "11th or 12th","After 12th"};
        ArrayAdapter<String> classes = new ArrayAdapter<>(getContext(),R.layout.spinner_white, class2);
        classes.setDropDownViewResource(R.layout.spinner_dropdown);
        classSpinner.setAdapter(classes);



        final String[] brach10 = {"CBSE", "ISCE","State"};
        final String[] branch11 = {"Science","Commerce","Arts"};
        final String[] branch12 = {"Engineering","Medical","Commerce Fields","Law","Architecture","Diploma Course"};


        classes.setDropDownViewResource(R.layout.spinner_dropdown);

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              switch (position){
                  case 0: branch = brach10;break;
                  case 1:branch = branch11;break;
                  case 2:branch = branch12;break;
              }
              ArrayAdapter<String> branches = new ArrayAdapter<>(getContext(),R.layout.spinner_white, branch);
              branches.setDropDownViewResource(R.layout.spinner_dropdown);
              branchSpinner.setAdapter(branches);
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}});

        return view;
    }

    public void register(String phone){

        String class1 = classSpinner.getSelectedItem().toString();
        String branch1 = branchSpinner.getSelectedItem().toString();
        reference = FirebaseDatabase.getInstance().getReference("Info").child(phone);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("c",class1);
        hashMap.put("b",branch1);
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful());
            else
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();}
        });
    }

}
