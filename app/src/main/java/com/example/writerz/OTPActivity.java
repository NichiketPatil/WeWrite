package com.example.writerz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
    private EditText code_txt;
    private String verificationID;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    String  phone;

    DatabaseReference reference;
    ProgressBar midProgress;
    LinearLayout translucentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        code_txt = findViewById(R.id.code_txt);
        phone = getIntent().getStringExtra("phoneNumber");
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        midProgress = findViewById(R.id.mid_progress_bar);
        translucentLayout = findViewById(R.id.translucent_layout);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        sendVerificationCode(phone);



        findViewById(R.id.sign_in_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code  = code_txt.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                if (code.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    code_txt.setError("Enter code...");
                    code_txt.requestFocus();
                    return;
                }
                verifycode(code);

            }
        });
    }

    private void verifycode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code);
        signInWithCredentials(credential);
    }

    private void signInWithCredentials(PhoneAuthCredential credential) {
            mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        translucentLayout.setBackgroundResource(R.color.translucent);
                        midProgress.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        updateActivity();
                    }
                    else {
                        code_txt.setError("Incorrect OTP");
                        code_txt.setText("");}
                }
            });
    }

    private void  sendVerificationCode(String number){
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID = s;

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code!=null)
                verifycode(code);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(OTPActivity.this,RegisterPhone.class));
        }
    };

    private void updateActivity() {
        phone = getIntent().getStringExtra("phoneNumber");

        reference = FirebaseDatabase.getInstance().getReference("Users").child(phone);


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("un")){
                    Intent intent = new Intent(OTPActivity.this, scrollingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);}
                else {
                    Intent intent = new Intent(OTPActivity.this, UserWriterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("phoneNumber", phone);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
