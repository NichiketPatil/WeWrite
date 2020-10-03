package com.example.writerz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class InfoActivity extends AppCompatActivity {
    DatabaseReference reference;
    ProgressBar progressBar;
    FirebaseUser fuser;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        progressBar = findViewById(R.id.progressBar);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        phone = fuser.getPhoneNumber();
        Fragment fragment = new Info1Fragment();
        replaceFragment(fragment);

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });





    }

    private void next() {
        progressBar.setVisibility(View.VISIBLE);

        Fragment fragment;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_info);

        if (currentFragment instanceof Info1Fragment) {
            Info1Fragment info1Fragment = (Info1Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_info);
            getSupportFragmentManager().executePendingTransactions();
            info1Fragment.register(phone);
            fragment = new Info2Fragment();
            replaceFragment(fragment);
            progressBar.setVisibility(View.INVISIBLE);
        }
        else if (currentFragment instanceof Info2Fragment){
            getSupportFragmentManager().executePendingTransactions();
            Info2Fragment info2Fragment = (Info2Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_info);
            info2Fragment.register(phone);
            replaceFragment(new Info3Fragment());
            progressBar.setVisibility(View.INVISIBLE);

        }
        else if (currentFragment instanceof Info3Fragment){
            getSupportFragmentManager().executePendingTransactions();
            Info3Fragment info3Fragment = (Info3Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_info);
            info3Fragment.register(phone);
            if (!info3Fragment.selected.equals(""))
                replaceFragment(new Info4Fragment());
            progressBar.setVisibility(View.INVISIBLE);

        }
        else if (currentFragment instanceof Info4Fragment){
            getSupportFragmentManager().executePendingTransactions();
            Info4Fragment info4Fragment = (Info4Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_info);
            info4Fragment.register(phone);
            if (!info4Fragment.selected.equals(""))
            replaceFragment(new Info5Fragment());
            progressBar.setVisibility(View.INVISIBLE);

        }
        else if (currentFragment instanceof Info5Fragment){
            getSupportFragmentManager().executePendingTransactions();
            Info5Fragment info5Fragment = (Info5Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_info);
            info5Fragment.register(phone);
            getSupportFragmentManager().executePendingTransactions();
            Intent intent = new Intent(InfoActivity.this,HandwritingActivity.class);
            intent.putExtra("userType",getIntent().getStringExtra("userType"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            if (!info5Fragment.selected.equals(""))
                startActivity(intent);
        }
    }


    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.fragment_info, someFragment);
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }
}
