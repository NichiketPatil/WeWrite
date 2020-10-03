package com.example.writerz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.writerz.Model.Writer;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HandwritingActivity extends AppCompatActivity {

    private static  final  int IMAGE_REQUEST = 1;
    private StorageReference storageReference;
    private Uri imageUri;
    private StorageTask uploadTask;
    FirebaseUser fuser;
    DatabaseReference reference;
    String imageNo;
    ImageView image1;
    ImageView image2;
    CircleImageView profile_img;
    Button btn1;
    Button btn2;
    Button profileBtn;
    StorageReference fileReference;
    String condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handwriting);

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        profileBtn = findViewById(R.id.btn_profile);
        profile_img = findViewById(R.id.profile_image);
        condition = "success";

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getPhoneNumber());
        storageReference = FirebaseStorage.getInstance().getReference("handwritings").child(fuser.getPhoneNumber());

        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Writer writer = dataSnapshot.getValue(Writer.class);

                Glide.with(HandwritingActivity.this)
                        .load(writer.getH1URL())
                        .into(image1);

                Glide.with(HandwritingActivity.this)
                        .load(writer.getH2URL())
                        .into(image2);

                Glide.with(HandwritingActivity.this)
                        .load(writer.getpURL())
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round)
                        .into(profile_img);
                condition = "success";
                if (writer.getpURL().equals("p") &&writer.getH1URL().equals("h1") && writer.getH2URL().equals("H2"))
                    condition = "fail";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = "image1";
                openImage();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = "image1";
                openImage();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = "image2";
                openImage();
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = "image2";
                openImage();
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = "profile";
                openImage();
            }
        });

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = "profile";
                openImage();
            }
        });

        findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HandwritingActivity.this,TermsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                if (condition.equals("success"))
                    startActivity(intent);
                else
                    Toast.makeText(HandwritingActivity.this, "All Uploads are Necessary", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openImage() {
    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("storage","Permission is granted");
                startActivityForResult(intent,IMAGE_REQUEST);
            } else {
                Log.v("storage","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v("storage","Permission is granted");
                    startActivityForResult(intent,IMAGE_REQUEST);}
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("storage","Permission is granted");
            startActivityForResult(intent,IMAGE_REQUEST);
        }
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(HandwritingActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            }
            else
                uploadImage();
        }}


    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri!=null){
            if(imageNo.equals("image1"))
             fileReference = storageReference.child("h1");
            else if(imageNo.equals("image2"))
                fileReference = storageReference.child("h2");
            else if(imageNo.equals("profile"))
                fileReference = storageReference.child("p");


            //path converted from Uri
            String filepath = getRealPathFromURI(imageUri);

            Compress compress = new Compress();
            filepath = compress.resizeAndCompressImageBeforeSend(this,filepath,"p");

            //Uri convert back again from path
            Uri uri = Uri.fromFile(new File(filepath));


            uploadTask = fileReference.putFile(uri);


            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();




                        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getPhoneNumber());
                        HashMap<String,Object> map = new HashMap<>();
                       if (imageNo.equals("image1"))
                        map.put("h1URL", mUri);
                       else if(imageNo.equals("image2"))
                        map.put("h2URL",mUri);
                       else if (imageNo.equals("profile"))
                         map.put("pURL",mUri);
                       else
                           Toast.makeText(HandwritingActivity.this, "No image Selected", Toast.LENGTH_SHORT).show();

                       reference.updateChildren(map);
                       pd.dismiss(); }
                    else{
                        Toast.makeText(HandwritingActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HandwritingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });


        }
        else {
            Toast.makeText(HandwritingActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };

        //This method was deprecated in API level 11
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        CursorLoader cursorLoader = new CursorLoader(
                this,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


}
