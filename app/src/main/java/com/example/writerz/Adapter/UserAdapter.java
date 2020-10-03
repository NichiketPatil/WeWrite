package com.example.writerz.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.writerz.MessageActivity;
import com.example.writerz.Model.Writer;
import com.example.writerz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private Context mContext;
    private List<Writer> mUser;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String username = "";

    public  UserAdapter(Context mContext,List<Writer> mUser){

        this.mContext = mContext;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Writer writer = mUser.get(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (!(firebaseUser.getPhoneNumber().equals(mContext.getResources().getString(R.string.nichiket)) || firebaseUser.getPhoneNumber().equals(mContext.getResources().getString(R.string.anikesh))))

            holder.username.setText("Admin");
        else
            holder.username.setText(writer.getUn());

        Glide.with(mContext)
                .load(writer.getpURL())
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(holder.profile_img);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getPhoneNumber());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Writer writer = dataSnapshot.getValue(Writer.class);
                username = writer.getUn();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MessageActivity.class);
//                intent.putExtra("userid", writer.getNo());


//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_TEXT,"Hi WEWRITE");
//                intent.setType("text/plain");
//                intent.setPackage("com.whatsapp");
//                mContext.startActivity(intent);
                boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
            if (isWhatsappInstalled){
                String phoneNumberWithCountryCode = writer.getNo();
                String message = "From "+username+":\n";
                mContext.startActivity(
                        new Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                        String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phoneNumberWithCountryCode, message)
                                )
                        )
                );}

            else {
                Toast.makeText(mContext, "WhatsApp Not Installed", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(mContext, MessageActivity.class);
                 intent.putExtra("userid", writer.getNo());
                 mContext.startActivity(intent);
            }
            }

        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_img;
        private  ImageView img_on;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_img = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
        }
    }
    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = mContext.getPackageManager();
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
