package com.example.writerz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.writerz.Model.AdminChat;
import com.example.writerz.Model.Chat;

import com.example.writerz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private  static  final int MSG_TYPE_LEFT = 0;
    private  static  final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    FirebaseUser fuser;


    private List<AdminChat> mChat;


    public  MessageAdapter(Context mContext,List<AdminChat> mChat){

        this.mContext = mContext;
        this.mChat = mChat;

    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT){
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
        return new MessageAdapter.ViewHolder(view);}
        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.ViewHolder(view);}
    }



    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        AdminChat chat = mChat.get(position);
        holder.show_message.setText(chat.getM());
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (!(fuser.getPhoneNumber().equals(mContext.getResources().getString(R.string.nichiket)) || fuser.getPhoneNumber().equals(mContext.getResources().getString(R.string.anikesh)))) {
            if (mChat.get(position).getMt().equals("u"))
                return MSG_TYPE_RIGHT;
            else
                return MSG_TYPE_LEFT;
        }
        else{
            if (mChat.get(position).getMt().equals("a"))
                return MSG_TYPE_RIGHT;
            else
                return MSG_TYPE_LEFT;
        }
    }
}
