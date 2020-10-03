package com.example.writerz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.writerz.Adapter.MessageAdapter;
import com.example.writerz.Model.AdminChat;
import com.example.writerz.Model.Writer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    ImageButton send_btn;
    EditText message;

    ImageView profile_image;
    TextView username;
    MessageAdapter messageAdapter;
    List<AdminChat> mChat;
    FirebaseUser fuser;
    DatabaseReference userReference;
    DatabaseReference chatReference;
    RecyclerView recyclerView;
    String userid;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        send_btn = findViewById(R.id.send_btn);
        message = findViewById(R.id.message);
        recyclerView = findViewById(R.id.recycler_view);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        intent = getIntent();
        userid = intent.getStringExtra("userid");
        if (!(fuser.getPhoneNumber().equals(getResources().getString(R.string.nichiket)) || fuser.getPhoneNumber().equals(getResources().getString(R.string.anikesh))))
            chatReference = FirebaseDatabase.getInstance().getReference("Chats").child(fuser.getPhoneNumber());
        else
            chatReference = FirebaseDatabase.getInstance().getReference("Chats").child(userid);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String msg = message.getText().toString();
               if (!msg.equals("")){
                   sendMessage(msg );
               }
               else
                   Toast.makeText(MessageActivity.this,"You can't send empty message",Toast.LENGTH_SHORT).show();
               message.setText("");
            }
        });

       Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        profile_image = findViewById(R.id.profile_img);
        username = findViewById(R.id.username_txt);
//Here
        userReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Writer user = dataSnapshot.getValue(Writer.class);
                if (user.getNo().equals(getResources().getString(R.string.nichiket)) || user.getNo().equals(getResources().getString(R.string.anikesh)))
                    username.setText("Admin");
                else
                    username.setText(user.getUn());
                Glide.with(MessageActivity.this)
                        .load(user.getpURL())
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round)
                        .into(profile_image);

                readMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }

    private  void sendMessage(String message) {

        HashMap<String, Object> hashMap = new HashMap<>();

        if (!(fuser.getPhoneNumber().equals(getResources().getString(R.string.nichiket)) || fuser.getPhoneNumber().equals(getResources().getString(R.string.anikesh))))
            hashMap.put("mt", "u");
        else
            hashMap.put("mt","a");

        hashMap.put("m", message);

        chatReference.push().setValue(hashMap);

    }

    private  void readMessage(){
        mChat = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AdminChat chat = snapshot.getValue(AdminChat.class);
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat);
                    recyclerView.setAdapter(messageAdapter);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
