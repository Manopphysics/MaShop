package com.example.manop.mashop.Adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.manop.mashop.R;

import java.util.ArrayList;

/**
 * Created by Hosam Azzam on 06/11/2017.
 */

public class Slider_Pager_Adapter extends PagerAdapter {
    Context context;
    ArrayList<Integer> image_arraylist;
    private LayoutInflater layoutInflater;

    public Slider_Pager_Adapter(Context context, ArrayList<Integer> image_arraylist) {
        this.context = context;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slider_home_layout, container, false);

        ImageView im_slider = view.findViewById(R.id.im_slider);
        im_slider.setImageResource(image_arraylist.get(position));
        im_slider.setScaleType(ImageView.ScaleType.FIT_XY);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}