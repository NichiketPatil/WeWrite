package com.example.writerz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    EditText name;
    EditText location;
    CircleImageView profile;
    TextView changeProfile;
    Spinner classSpinner;
    Spinner branchSpinner;
    ImageView check;
    DatabaseReference reference;
    DatabaseReference reference1;
    FirebaseUser fuser;
    static  final  int IMAGE_REQUEST = 1;
    StorageReference storageReference;
    Uri imageUri;
    StorageTask uploadTask;
    ProgressBar progressBar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        profile = findViewById(R.id.profile_image);
        changeProfile = findViewById(R.id.change_profile_image);
        classSpinner = findViewById(R.id.class_spinner);
        branchSpinner = findViewById(R.id.branch_spinner);
        check = findViewById(R.id.check);
        progressBar = findViewById(R.id.progress_bar);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("handwritings").child(fuser.getPhoneNumber());
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getPhoneNumber());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Writer writer = dataSnapshot.getValue(Writer.class);
                name.setText(writer.getUn());
                location.setText(writer.getLoc(EditProfileActivity.this));
                Glide.with(getApplicationContext())
                        .load(writer.getpURL())
                        .into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });



        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                editProfile();
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openImage();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openImage();
            }
        });

    }

    private  void editProfile(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("un",name.getText().toString());
        map.put("loc",location.getText().toString());
        reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.INVISIBLE);
                finish();
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
    }


    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri!=null){
            final StorageReference fileReference = storageReference.child("p.jpg");

            //path converted from Uri
            String filepath = getRealPathFromURI(imageUri);

            Compress compress = new Compress();
            filepath = compress.resizeAndCompressImageBeforeSend(EditProfileActivity.this,filepath,"p");

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
                        map.put("pURL", mUri);
                        reference.updateChildren(map);

                        pd.dismiss(); }
                    else{
                        Toast.makeText(EditProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });


        }
        else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(this, "Upload in progress", Toast.LENGTH_SHORT).show();
            }
            else
                uploadImage();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
