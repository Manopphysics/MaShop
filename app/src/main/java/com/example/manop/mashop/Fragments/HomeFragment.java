package com.example.manop.mashop.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.example.manop.mashop.Adapter.Slider_Pager_Adapter;
import com.example.manop.mashop.Chat.UserListingActivity;
import com.example.manop.mashop.Function.SearchActivity;
import com.example.manop.mashop.R;
import com.example.manop.mashop.Shop.ShopListActivity;
import com.example.manop.mashop.Startup.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hosam Azzam on 28/01/2018.
 */

public class HomeFragment extends Fragment{

    Slider_Pager_Adapter sliderPagerAdapter;
    ArrayList<Integer> slider_image_list = new ArrayList<>();
    int page_position = 0;
    Timer timer;
    private ViewPager images_slider;
    private LinearLayout pages_dots;
    private TextView[] dots;
    private final String TAG = "BlankFragment";
    private Button visit_shop;
    private Button search_product;
    private Button chat_seller;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        images_slider = rootview.findViewById(R.id.image_page_slider);
        pages_dots = rootview.findViewById(R.id.image_page_dots);
        visit_shop = (Button) rootview.findViewById(R.id.visit_shop);
        search_product = (Button) rootview.findViewById(R.id.search_products);
        chat_seller = (Button) rootview.findViewById(R.id.chat_seller);
//        Toolbar tb = rootview.findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(tb);
        visit_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent visit = new Intent(getActivity(), ShopListActivity.class);
                startActivity(visit);
            }
        });
        search_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(getActivity(), SearchActivity.class);
                startActivity(search);
            }
        });
        chat_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserListingActivity.startActivity(getActivity(),
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });
        timer = new Timer();
        initSlider();
        scheduleSlider();


        return rootview;
    }

    public void initSlider() {
        addBottomDots(0);

        slider_image_list = new ArrayList<>();

        //Add few items to slider_image_list ,this should contain url of images which should be displayed in slider
        // here i am adding few sample image links from drawable, we will replace it later

        slider_image_list.add(R.drawable.slider1);
        slider_image_list.add(R.drawable.slider2);
        slider_image_list.add(R.drawable.slider3);
        slider_image_list.add(R.drawable.slider4);
        //slider_image_list.add(R.drawable.one);

        sliderPagerAdapter = new Slider_Pager_Adapter(getActivity(), slider_image_list);
        images_slider.setAdapter(sliderPagerAdapter);
        images_slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void scheduleSlider() {

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_list.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                images_slider.setCurrentItem(page_position, true);
            }
        };

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 200, 3000);
    }

    public void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        pages_dots.removeAllViews();
        pages_dots.setPadding(0, 0, 0, 20);
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#9f9f9f")); // un selected
            pages_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#2f383a")); // selected
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onPause() {
        timer.cancel();
        super.onPause();
    }
}
