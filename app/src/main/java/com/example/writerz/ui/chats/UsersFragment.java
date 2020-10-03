package com.example.writerz.ui.chats;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.writerz.Adapter.UserAdapter;
import com.example.writerz.LoginActivity;
import com.example.writerz.Model.Writer;
import com.example.writerz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

     RecyclerView recyclerView;
     UserAdapter userAdapter;
     List<Writer> mUser;
    private Button login;
    String userType;
    DatabaseReference reference;
    Writer writer;
    FirebaseUser firebaseUser;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

   //     userType = getArguments().getString("userType");
//        scrollingActivity activity = (scrollingActivity) getActivity();
//        Bundle results = activity.getMyData();
//         userType = results.getString("userType");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");


        View view;
        if(!FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
            view = inflater.inflate(R.layout.fragment_chats, container, false);
            recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mUser = new ArrayList<>();
            if (!(firebaseUser.getPhoneNumber().equals(getResources().getString(R.string.nichiket)) || firebaseUser.getPhoneNumber().equals(getResources().getString(R.string.anikesh))))
                readAdmin();
            else
                readUser();
        }

        else {
            view = inflater.inflate(R.layout.fragment_guest_chat, container, false);
            login = view.findViewById(R.id.btn_login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
        return view;
    }

    private void readAdmin() {

        DatabaseReference Adminreference = reference.child(getResources().getString(R.string.anikesh));
        Adminreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                writer = dataSnapshot.getValue(Writer.class);
                    assert  writer!= null;
                    assert  firebaseUser!=null;
                    mUser.add(writer);
                userAdapter = new UserAdapter(getContext(),mUser);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void readUser(){
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUser.clear();
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        writer = snapshot.getValue(Writer.class);
                        assert writer!=null;
                        if (!(writer.getNo().equals(getResources().getString(R.string.nichiket)) || writer.getNo().equals(getResources().getString(R.string.anikesh))))
                            mUser.add(writer);
                    }
                    userAdapter = new UserAdapter(getContext(),mUser);
                    recyclerView.setAdapter(userAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }




}