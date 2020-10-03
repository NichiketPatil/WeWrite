package com.example.writerz;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;


public class RegisterPhone extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();

        if (!isOnline()){
            new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("Please Connect to Internet and try again")
                    .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(RegisterPhone.this,SplashScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);

       final EditText contact = findViewById(R.id.contact);
       contact.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(contact.getText().toString().length() == 10)
                        findViewById(R.id.next_btn).setBackgroundResource(R.drawable.gradient2);
                    else
                        findViewById(R.id.next_btn).setBackgroundResource(R.drawable.curved_rectangle);
           }

           @Override
           public void afterTextChanged(Editable s) {}
       });



        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText  contact = findViewById(R.id.contact);
                String contactno = contact.getText().toString().trim();
                if (contactno.length() == 10){
                    Intent intent = new Intent(RegisterPhone.this,OTPActivity.class);
                    intent.putExtra("phoneNumber","+91"+contactno);
                    startActivity(intent);}
                else
                    contact.setError("Enter Valid Phone no.");
            }
        });

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
