package com.example.writerz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.writerz.Model.Writer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserWriterActivity extends AppCompatActivity {


     String user_type;
     Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_writer);

        intent = new Intent(UserWriterActivity.this,LocationActivity.class);


        findViewById(R.id.user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_type = "User";
                intent.putExtra("userType",user_type);
                Intent i = getIntent();
                intent.putExtra("phoneNumber",i.getStringExtra("phoneNumber"));
                startActivity(intent);
            }
        });

        findViewById(R.id.writer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_type = "Writer";
                Intent i = getIntent();
                intent.putExtra("userType",user_type);
                intent.putExtra("phoneNumber",i.getStringExtra("phoneNumber"));
                startActivity(intent);
            }
        });
    }
}
