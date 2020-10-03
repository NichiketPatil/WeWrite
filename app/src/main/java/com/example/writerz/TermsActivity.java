package com.example.writerz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class TermsActivity extends AppCompatActivity {
    CheckBox term1;
    CheckBox term2;
    CheckBox term3;
    CheckBox term4;
    Button agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        term1 = findViewById(R.id.term1);
        term2 = findViewById(R.id.term2);
        term3 = findViewById(R.id.term3);
        term4 = findViewById(R.id.term4);
        agree = findViewById(R.id.btn_agree);



        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (term1.isChecked() && term2.isChecked() && term3.isChecked() && term4.isChecked()){
                    Intent intent = new Intent(TermsActivity.this,scrollingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                    Toast.makeText(TermsActivity.this, "Agree to all terms", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
