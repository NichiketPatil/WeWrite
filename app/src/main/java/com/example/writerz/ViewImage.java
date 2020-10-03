package com.example.writerz;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;


public class ViewImage extends AppCompatActivity {

    String handwritingNo;
    String hURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

          handwritingNo = getIntent().getStringExtra("handwritingNo");
          hURL = getIntent().getStringExtra("hURL");
          PhotoView photoView = findViewById(R.id.photo_view);
          Glide.with(ViewImage.this)
                  .load(hURL)
                  .diskCacheStrategy(DiskCacheStrategy.ALL)
                  .placeholder(R.drawable.spinner)
                  .error(R.drawable.handwriting_sample)
                  .into(photoView);

          getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
