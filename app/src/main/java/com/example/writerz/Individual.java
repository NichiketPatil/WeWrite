package com.example.writerz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.writerz.Model.Writer;
import com.example.writerz.Model.WriterInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Individual extends AppCompatActivity {
     DatabaseReference reference;
     DatabaseReference reference2;
   //  FirebaseUser fuser;

     TextView username;
     TextView location;
     TextView expertise;
     TextView minOrder;
     TextView minTime;
     CircleImageView profile_image;
     ImageView writing1;
     ImageView writing2;
//    RatingBar ratingBar;
     String h1URL = "";
     String h2URL = "";
     String pURL = "";
     String expertise1 = "";
    Toolbar toolbar;
    LinearLayout chat;
    String userID = "";
    String name = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual);

        Intent intent = getIntent();
        userID =  intent.getStringExtra("userID");

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        //fuser = FirebaseAuth.getInstance().getCurrentUser();
        username = findViewById(R.id.username);
        profile_image = findViewById(R.id.profile_image);
        writing1 = findViewById(R.id.writing1);
        writing2 = findViewById(R.id.writing2);
        toolbar = findViewById(R.id.toolbar);
        chat = findViewById(R.id.chat);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
     //   ratingBar = findViewById(R.id.ratingBar);

        location = findViewById(R.id.location);
        expertise= findViewById(R.id.expertise);
        minOrder= findViewById(R.id.min_order);
        minTime= findViewById(R.id.min_time);

//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                Toast.makeText(Individual.this, "Rating: "  + rating, Toast.LENGTH_SHORT).show();
//            }
//        });


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Writer writer = dataSnapshot.getValue(Writer.class);
               h1URL = writer.getH1URL();
               h2URL = writer.getH2URL();
               pURL = writer.getpURL();
                name = writer.getUn();
               username.setText(name);
               getSupportActionBar().setTitle(writer.getUn());
               location.setText(writer.getLoc(Individual.this));

               Glide.with(Individual.this)
                               .load(pURL)
                               .placeholder(R.mipmap.ic_launcher_round)
                               .error(R.mipmap.ic_launcher_round)
                               .into(profile_image);

               Glide.with(Individual.this)
                               .load(h1URL)
                               .into(writing1);

               Glide.with(Individual.this)
                       .load(h2URL)
                       .into(writing2);
           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {}
       });

        reference2 = FirebaseDatabase.getInstance().getReference("Info").child(userID);

        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               try {

                WriterInfo writer = dataSnapshot.getValue(WriterInfo.class);
                minOrder.setText(writer.getO());
                minTime.setText(writer.getT());
                if (writer.getF().equals("yes"))
                    expertise1="Fast and Efficient with low price\n";

                if (writer.getN().equals("yes"))
                    expertise1 = expertise1.concat("Can write clean with steady time\n");

                if (writer.getE().equals("yes"))
                    expertise1 = expertise1.concat("Available for Emergency Orders");

                expertise.setText(expertise1);}
               catch (Exception e){
                   Toast.makeText(Individual.this, e.getMessage(), Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

       writing1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(Individual.this,ViewImage.class);
               intent.putExtra("handwritingNo","h1");
               intent.putExtra("hURL",h1URL);
               startActivity(intent);
           }
       });
       writing2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(Individual.this,ViewImage.class);
               intent.putExtra("handwritingNo","h2");
               intent.putExtra("hURL",h2URL);
               startActivity(intent);
           }
       });

       chat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openChat();
           }
       });

    }

    private void openChat() {
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");

        if (isWhatsappInstalled){
        String message = "By "+"Through "+name + "("+userID +") :\n";
        startActivity(
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s",getResources().getString(R.string.anikesh), message)
                        )
                )
        );}
        else {
            Toast.makeText(this, "WhatsApp Not Installed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Individual.this, MessageActivity.class);
            intent.putExtra("userid", getResources().getString(R.string.anikesh));
            startActivity(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }



}
