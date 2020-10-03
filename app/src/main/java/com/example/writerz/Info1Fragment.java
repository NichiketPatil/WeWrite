package com.example.writerz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class Info1Fragment extends Fragment {
    Spinner spinner;
    DatabaseReference reference;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_info1, container, false);
        spinner = view.findViewById(R.id.order);

        String[] order = {"25 pages", "50 pages","75 pages","100 pages"};
        ArrayAdapter<String> orders = new ArrayAdapter<>(getContext(),R.layout.spinner_white, order);
        orders.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(orders);





        return view;
    }

    public void register(String phone){
        String selected = spinner.getSelectedItem().toString();
        reference = FirebaseDatabase.getInstance().getReference("Info").child(phone);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("o",selected);
        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful());
                else
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
