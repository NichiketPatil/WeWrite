package com.example.writerz;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.writerz.Adapter.UserAdapter;
import com.example.writerz.Model.Writer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import java.util.ArrayList;
import java.util.List;


public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {

     String TAG = "CustomListAdapter";
     private Context mContext;
     //int mResource;
     int lastPosition = -1;
     DatabaseReference reference;
     FirebaseUser fuser;
     ArrayList<String> mPhoneNumber;
     ArrayList<card> objects;


    public  CustomListAdapter(Context context, int resource, ArrayList<card> objects){

        this.mContext = context;
        this.objects = objects;
     //   this.mResource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

//    public CustomListAdapter extends RecyclerView.ViewHolder (Context context, int resource, ArrayList<card> objects) {
//        super(context, resource, objects);
//        mContext = context;
//        mResource = resource;
//
//        //sets up the image loader library
////        setupImageLoader();
//    }

//    @Override
//    public void onBindViewHolder(@NonNull card holder, int position, @NonNull List<Object> payloads) {
//        super.onBindViewHolder(holder, position, payloads);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull card holder, int position) {
//
//    }
        @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //get the persons information
        String title = objects.get(position).getTitle();
        String imgUrl = objects.get(position).getImgURL();
        String pURL = objects.get(position).getProfile();

     //   final  ViewHolder holder = new ViewHolder();
        try {
            //create the view result for showing the animation

            //ViewHolder object

//            if (convertView == null) {
//                LayoutInflater inflater = LayoutInflater.from(mContext);
//                convertView = inflater.inflate(mResource, parent, false);
//                holder = new ViewHolder();
//                holder.title = convertView.findViewById(R.id.location);
//                holder.image = convertView.findViewById(R.id.cardImage);
//                holder.profile_img = convertView.findViewById(R.id.profile_image);
//                holder.dialog = convertView.findViewById(R.id.cardProgressDialog);
//                holder.card_view = convertView.findViewById(R.id.card_view);
//                holder.ratingBar = convertView.findViewById(R.id.ratingBarsmall);

//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
            lastPosition = position;
            holder.title.setText(title);
            Glide.with(mContext)
                    .load(pURL)
                    .error(R.mipmap.ic_launcher_round)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.profile_img);

            holder.dialog.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(imgUrl)
                    .error(R.drawable.handwriting_sample)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.dialog.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.dialog.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.image);

            //create the imageloader object
//            ImageLoader imageLoader = ImageLoader.getInstance();

//            int defaultImage = mContext.getResources().getIdentifier("@drawable/image_failed", null, mContext.getPackageName());

            //create display options
//            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
//                    .cacheOnDisc(true).resetViewBeforeLoading(true)
//                    .showImageForEmptyUri(defaultImage)
//                    .showImageOnFail(defaultImage)
//                    .showImageOnLoading(defaultImage).build();

            //download and display image from url
//            imageLoader.displayImage(imgUrl, holder.image, options, new ImageLoadingListener() {
////                @Override
////                public void onLoadingStarted(String imageUri, View view) {
////                    holder.dialog.setVisibility(View.VISIBLE);
////                }
////
////                @Override
////                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
////                    holder.dialog.setVisibility(View.GONE);
////                }
////
////                @Override
////                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
////                    holder.dialog.setVisibility(View.GONE);
////                }
////
////                @Override
////                public void onLoadingCancelled(String imageUri, View view) {
////                }
////            });

            holder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fuser = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("Users");
                    mPhoneNumber = new ArrayList<>();

                    reference.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Writer writer = snapshot.getValue(Writer.class);
                                if (writer.getUt().equals("w"))
                                    mPhoneNumber.add(writer.getNo());
                            }

                            Pair[] pair = new Pair[4];
                            pair[0] = new Pair<View, String>(holder.image, "shared_card");
                            pair[1] = new Pair<View, String>(holder.title, "shared_location");
                            pair[2] = new Pair<View, String>(holder.ratingBar, "shared_rating");
                            pair[3] = new Pair<View, String>(holder.profile_img, "shared_prof");

                            Intent intent = new Intent(mContext, Individual.class);

                            ActivityOptions val = ActivityOptions.makeSceneTransitionAnimation(
                                    (Activity) mContext, pair// The transition name to be matched in Activity B.
                            );
                            intent.putExtra("userID", mPhoneNumber.get(position));
                            mContext.startActivity(intent, val.toBundle());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });


                }
            });

        } catch (IllegalArgumentException e) {
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage());

        }
    }
//    @NonNull
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
////        //get the persons information
////        String title = getItem(position).getTitle();
////        String imgUrl = getItem(position).getImgURL();
////        String pURL = getItem(position).getProfile();
////
////        try {
////            //create the view result for showing the animation
////
////            //ViewHolder object
////            final ViewHolder holder;
////
////            if (convertView == null) {
////                LayoutInflater inflater = LayoutInflater.from(mContext);
////                convertView = inflater.inflate(mResource, parent, false);
////                holder = new ViewHolder();
////                holder.title = convertView.findViewById(R.id.location);
////                holder.image = convertView.findViewById(R.id.cardImage);
////                holder.profile_img = convertView.findViewById(R.id.profile_image);
////                holder.dialog = convertView.findViewById(R.id.cardProgressDialog);
////                holder.card_view = convertView.findViewById(R.id.card_view);
////                holder.ratingBar = convertView.findViewById(R.id.ratingBarsmall);
////
////                convertView.setTag(holder);
////            } else {
////                holder = (ViewHolder) convertView.getTag();
////            }
////
////            lastPosition = position;
////
////            holder.title.setText(title);
////            Glide.with(mContext)
////                    .load(pURL)
////                    .error(R.mipmap.ic_launcher_round)
////                    .placeholder(R.mipmap.ic_launcher_round)
////                    .into(holder.profile_img);
////
////            holder.dialog.setVisibility(View.VISIBLE);
////            Glide.with(mContext)
////                    .load(imgUrl)
////                    .error(R.drawable.handwriting_sample)
////                    .listener(new RequestListener<Drawable>() {
////                        @Override
////                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
////                            holder.dialog.setVisibility(View.GONE);
////                            return false;
////                        }
////
////                        @Override
////                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
////                            holder.dialog.setVisibility(View.GONE);
////                            return false;
////                        }
////                    })
////                    .into(holder.image);
////
////            //create the imageloader object
//////            ImageLoader imageLoader = ImageLoader.getInstance();
////
//////            int defaultImage = mContext.getResources().getIdentifier("@drawable/image_failed", null, mContext.getPackageName());
////
////            //create display options
//////            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
//////                    .cacheOnDisc(true).resetViewBeforeLoading(true)
//////                    .showImageForEmptyUri(defaultImage)
//////                    .showImageOnFail(defaultImage)
//////                    .showImageOnLoading(defaultImage).build();
////
////            //download and display image from url
//////            imageLoader.displayImage(imgUrl, holder.image, options, new ImageLoadingListener() {
////////                @Override
////////                public void onLoadingStarted(String imageUri, View view) {
////////                    holder.dialog.setVisibility(View.VISIBLE);
////////                }
////////
////////                @Override
////////                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
////////                    holder.dialog.setVisibility(View.GONE);
////////                }
////////
////////                @Override
////////                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
////////                    holder.dialog.setVisibility(View.GONE);
////////                }
////////
////////                @Override
////////                public void onLoadingCancelled(String imageUri, View view) {
////////                }
////////            });
////
////            holder.card_view.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////
////                    fuser = FirebaseAuth.getInstance().getCurrentUser();
////                    reference = FirebaseDatabase.getInstance().getReference("Users");
////                    mPhoneNumber = new ArrayList<>();
////
////                    reference.addValueEventListener(new ValueEventListener() {
////
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                                Writer writer = snapshot.getValue(Writer.class);
////                                if (writer.getUt().equals("w"))
////                                    mPhoneNumber.add(writer.getNo());
////                            }
////
////                            Pair[] pair = new Pair[4];
////                            pair[0] = new Pair<View, String>(holder.image, "shared_card");
////                            pair[1] = new Pair<View, String>(holder.title, "shared_location");
////                            pair[2] = new Pair<View, String>(holder.ratingBar, "shared_rating");
////                            pair[3] = new Pair<View, String>(holder.profile_img, "shared_prof");
////
////                            Intent intent = new Intent(getContext(), Individual.class);
////
////                            ActivityOptions val = ActivityOptions.makeSceneTransitionAnimation(
////                                    (Activity) mContext, pair// The transition name to be matched in Activity B.
////                            );
////                            intent.putExtra("userID", mPhoneNumber.get(position));
////                            getContext().startActivity(intent, val.toBundle());
////                        }
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError databaseError) {
////                        }
////                    });
////
////
////                }
////            });
////            return convertView;
////        } catch (IllegalArgumentException e) {
////            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage());
////            return convertView;
////        }
//
//
//
//
//    }


//    private void setupImageLoader() {
//        // UNIVERSAL IMAGE LOADER SETUP
//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .cacheOnDisc(true).cacheInMemory(true)
//                .imageScaleType(ImageScaleType.EXACTLY)
//                .displayer(new FadeInBitmapDisplayer(300)).build();
//
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                mContext)
//                .defaultDisplayImageOptions(defaultOptions)
//                .memoryCache(new WeakMemoryCache())
//                .discCacheSize(100 * 1024 * 1024).build();
//
//        ImageLoader.getInstance().init(config);
//        // END - UNIVERSAL IMAGE LOADER SETUP
//    }


    public  class ViewHolder extends  RecyclerView.ViewHolder{
        TextView title;
        ImageView image;
        ProgressBar dialog;
        CardView card_view;
        RatingBar ratingBar;
        ImageView profile_img;

        //        public  ViewHolder(Context context, int resource, ArrayList<card> objects){
//            super(context, resource, objects);
//            mContext = context;
//            mResource = resource;
//        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.location);
            image = itemView.findViewById(R.id.cardImage);
            dialog = itemView.findViewById(R.id.cardProgressDialog);
            card_view = itemView.findViewById(R.id.card_view);
            ratingBar = itemView.findViewById(R.id.ratingBarsmall);
            profile_img = itemView.findViewById(R.id.profile_image);

            // username = itemView.findViewById(R.id.username);
//            profile_img = itemView.findViewById(R.id.profile_image);
            //  img_on = itemView.findViewById(R.id.img_on);
        }
    }


}






