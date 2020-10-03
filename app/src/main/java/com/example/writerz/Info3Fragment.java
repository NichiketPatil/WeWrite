package com.example.writerz;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;


public class Info3Fragment extends Fragment {
    RadioGroup que;
    DatabaseReference reference;
    String selected = "";

    //Emergency Program
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_info3, container, false);
        que = view.findViewById(R.id.que);

        que.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.yes: selected = "yes";break;
                    case R.id.no: selected = "no";break;
                }
            }
        });

        return view;}


    public void register(String phone){
        reference = FirebaseDatabase.getInstance().getReference("Info").child(phone);
        HashMap<String,Object> hashMap = new HashMap<>();
        if (!selected.equals("")) {
            hashMap.put("e", selected);

            reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) ;
                    else
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            Toast.makeText(getContext(), "Please select something", Toast.LENGTH_SHORT).show();
    }

}
