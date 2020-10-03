package com.example.writerz;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;


public class NameFragment extends Fragment {

    EditText name;
    //EditText location;
    FirebaseUser fuser;
    DatabaseReference reference;
    String phoneNumber;
    ProgressBar progressBar;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_name, container, false);

        phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().trim();
        progressBar = getActivity().findViewById(R.id.progress_bar);

        return  view;
    }

    public   void register(String userType){
        progressBar.setVisibility(View.VISIBLE);
        name = view.findViewById(R.id.name_txt);
        //location = view.findViewById(R.id.location_txt);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        String name1 = name.getText().toString().trim();
        //String location1 = location.getText().toString().trim();

        HashMap<String,Object> hashMap = new HashMap<>();

        if (userType.equals("User")){
            hashMap.put("ut","u");
            hashMap.put("un",name1);
            hashMap.put("no",phoneNumber);
            hashMap.put("pURL","p");
           // hashMap.put("loc",location1);
            }

        else if (userType.equals("Writer")){
            hashMap.put("ut","w");
            hashMap.put("un",name1);
            hashMap.put("no",phoneNumber);
            hashMap.put("pURL","p");
          //  hashMap.put("loc",location1);
            hashMap.put("h1URL","h1");
            hashMap.put("h2URL","h2");}
        else
            Toast.makeText(getContext(), "UserType Unknown", Toast.LENGTH_SHORT).show();

        reference.child(phoneNumber).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful());
                else
                    Toast.makeText(getContext(), "Phone Number Already Exists", Toast.LENGTH_SHORT).show();

            }
        });


    }
        }

