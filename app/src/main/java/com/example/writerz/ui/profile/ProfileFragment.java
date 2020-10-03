package com.example.writerz.ui.profile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.writerz.CustomListAdapter;
import com.example.writerz.EditProfileActivity;
import com.example.writerz.Model.Writer;
import com.example.writerz.Model.WriterInfo;
import com.example.writerz.R;
import com.example.writerz.card;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView profile_img;
    TextView username;
    TextView contact;
    DatabaseReference reference;
    FirebaseUser fuser;
    DatabaseReference reference2;
    //   TextView change_profile;
    TextView location;
    TextView editProfile;
    TextView field;
    String userType = "";
    RecyclerView recyclerView;
    Context context;
    ArrayList<card> list;
    CustomListAdapter adapter;
    TextView label;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_img = root.findViewById(R.id.profile_image);
        username = root.findViewById(R.id.username);
        contact = root.findViewById(R.id.contact);
        location = root.findViewById(R.id.location);
        recyclerView = root.findViewById(R.id.recycler_view2);
      //  change_profile =root.findViewById(R.id.change_profile);
        editProfile = root.findViewById(R.id.edit_profile);
        field = root.findViewById(R.id.field);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        label = root.findViewById(R.id.writer_label);

        context = getContext();

        if (!fuser.isAnonymous()) {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getPhoneNumber());
            reference2 = FirebaseDatabase.getInstance().getReference("Info").child(fuser.getPhoneNumber());

            list = new ArrayList<>();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    if (isAdded()) {
                        Writer user = dataSnapshot.getValue(Writer.class);



                      if (user != null) {
                        username.setText(user.getUn());
                        contact.setText(user.getNo());
                        location.setText(user.getLoc(context));
                        userType = user.getUt();
                        Glide.with(getContext())
                                .load(user.getpURL())
                                .error(R.mipmap.ic_launcher_round)
                                .placeholder(R.mipmap.ic_launcher_round)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(profile_img);
                            if (user.getUt().equals("w")){
                                label.setVisibility(View.VISIBLE);
                                list.add(new card(user.getH1URL(), user.getLoc(context), user.getpURL()));
                                adapter = new CustomListAdapter(context, R.layout.card_layout, list);
                                LinearLayoutManager llm = new LinearLayoutManager(context);
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(llm);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                        else {
                        username.setText("Guest");
                        contact.setText("0000000000");
                        location.setText("Null");
                        profile_img.setImageResource(R.mipmap.ic_launcher_round);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });


        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded()){
                WriterInfo writer = dataSnapshot.getValue(WriterInfo.class);
                if (writer!=null){
                    field.setText(writer.getB());}
                else {
                    field.setText("Writing");
                }
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        if (userType.equals("w")){

        }
        }
        else {
            username.setText("Guest");
            contact.setText("0000000000");
            location.setText("Null");
            profile_img.setImageResource(R.mipmap.ic_launcher_round);
        }

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fuser.isAnonymous())
                    startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });
            return root;
        }

}


