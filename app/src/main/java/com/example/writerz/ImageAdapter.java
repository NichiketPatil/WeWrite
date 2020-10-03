package com.example.writerz;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

public class ImageAdapter extends PagerAdapter {
    Context context;
    private int[] GalImages = new int[] {
            R.drawable.ic_enter,
            R.drawable.ic_dashboard_black_24dp,
            R.drawable.ic_arrow_back_black_24dp,
            R.drawable.ic_chat_colur,
            R.drawable.ic_close_black_24dp,
            R.drawable.ic_check,
            R.drawable.ic_cheque,
            R.drawable.ic_chat_white_24dp
    };

    LayoutInflater mLayoutInflater;

    ImageAdapter(Context context){
        this.context=context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return GalImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView =  itemView.findViewById(R.id.imageView);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;
        imageView.setLayoutParams(params);
        imageView.getLayoutParams().height = 100;
        imageView.getLayoutParams().width = 100;
        imageView.setImageResource(GalImages[position]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
