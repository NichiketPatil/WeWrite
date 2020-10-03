package com.example.writerz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button register;
    Button guest_signin;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    private ProgressBar progressBar;


    @Override
    protected void onStart(){
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser!= null){
            Intent intent = new Intent(LoginActivity.this,scrollingActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        register = findViewById(R.id.btn_enter);
        FirebaseApp.initializeApp(this);
        guest_signin = findViewById(R.id.btn_guest);

        progressBar = findViewById(R.id.progressBar);

        ViewPager pager =findViewById(R.id.photos_viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
        ImageAdapter adapter = new ImageAdapter(this);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterPhone.class);
                startActivity(intent);
            }
        });

        guest_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                auth = FirebaseAuth.getInstance();
                auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, scrollingActivity.class));
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Sign in Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
