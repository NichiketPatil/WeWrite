package com.example.writerz;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;


public class NameLocationActivity extends AppCompatActivity {
    String userType;
    String phone;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_location);

        Fragment fragment = new NameFragment();
        replaceFragment(fragment);
        userType = getIntent().getStringExtra("userType");
        phone = getIntent().getStringExtra("phoneNumber");
        progressBar = findViewById(R.id.progress_bar);

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();

            }
        });
    }

    private void next() {
        Fragment fragment;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_register);
        if (currentFragment instanceof NameFragment) {
            NameFragment nameFragment = (NameFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_register);
            getSupportFragmentManager().executePendingTransactions();
            nameFragment.register(userType);
            fragment = new EducationFragment();
            replaceFragment(fragment);
            progressBar.setVisibility(View.INVISIBLE);
        }
        else if (currentFragment instanceof EducationFragment){
            progressBar.setVisibility(View.VISIBLE);
            EducationFragment educationFragment = (EducationFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_register);
            educationFragment.register(phone);
            if (userType.equals("Writer")){
            Intent intent = new Intent(NameLocationActivity.this,InfoActivity.class);
            intent.putExtra("userType",userType);
            startActivity(intent);}
            else {
                Intent intent = new Intent(NameLocationActivity.this,scrollingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }


    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.fragment_register, someFragment);
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }
}
