package com.example.manop.mashop.Shop;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manop.mashop.Product.AddProduct;
import com.example.manop.mashop.Product.MyProducts;
import com.example.manop.mashop.R;
import com.example.manop.mashop.Startup.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.example.manop.mashop.Views.CircleImageView;


public class Shop extends AppCompatActivity{
    private TextView shopname;
    private TextView shopdesc;
    private Button addProd;
    private Button delShop;
    private Button myProd;
    private Button view_statistics;
    private ImageView shopImage;
    private DatabaseReference shopDB;
    private DatabaseReference UserDB;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        bindView();
        firebaseInit();
        Log.d("CURR",currentUser.getUid());
        shopDB.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    shopname.setText(dataSnapshot.child("name").getValue(String.class));
                    shopdesc.setText(dataSnapshot.child("description").getValue(String.class));
                    Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(shopImage);
                }catch(Exception e){e.printStackTrace();}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        addProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPro = new Intent(Shop.this, AddProduct.class);
                startActivity(addPro);
                //Toast.makeText(getActivity(),"WILL ADD THE Intent POST PRODUCT",Toast.LENGTH_SHORT).show();
            }
        });
        delShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopDB.child(currentUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        UserDB.child(currentUser.getUid()).child("seller").setValue("false");
                        Intent mainact = new Intent(Shop.this, MainActivity.class);
                        startActivity(mainact);
                    }
                });

            }
        });
        myProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myprod = new Intent(Shop.this, MyProducts.class);
                startActivity(myprod);
            }
        });

        view_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stats = new Intent(Shop.this,ShopStatistics.class);
                startActivity(stats);
            }
        });
    }
    public void firebaseInit(){
        shopDB = FirebaseDatabase.getInstance().getReference().child("Shop");
        UserDB = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //shopDB.keepSynced(true);
        //UserDB.keepSynced(true);
    }
    public void bindView(){
        shopImage = (ImageView) findViewById(R.id.shopImage);
        shopname = (TextView) findViewById(R.id.shoptitle);;
        shopdesc = (TextView) findViewById(R.id.shopdesc);
        addProd = (Button) findViewById(R.id.add_prod_btn);
        myProd = (Button) findViewById(R.id.btn_my_product);
        delShop = (Button) findViewById(R.id.del_shop_btn);
        view_statistics = (Button ) findViewById(R.id.view_statistics);
    }
}


//import android.net.Uri;
//import android.os.Bundle;
//import com.google.android.material.appbar.AppBarLayout;
//import com.google.android.material.appbar.CollapsingToolbarLayout;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import android.view.Menu;
//import android.view.View;
//import android.view.animation.AlphaAnimation;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import com.example.manop.mashop.R;
//
//import com.squareup.picasso.Picasso;
////import com.facebook.drawee.view.SimpleDraweeView;
////import com.facebook.drawee.backends.pipeline.Fresco;
//import com.example.manop.mashop.Views.CircleImageView;
//
//public class Shop extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
////
////    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
////    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
////    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
////    final Uri imageUri = Uri.parse("http://i.imgur.com/VIlcLfg.jpg");
////
////    private boolean mIsTheTitleVisible          = false;
////    private boolean mIsTheTitleContainerVisible = true;
////
////    private AppBarLayout appbar;
////    private CollapsingToolbarLayout collapsing;
////    private ImageView coverImage;
////    private FrameLayout framelayoutTitle;
////    private LinearLayout linearlayoutTitle;
////    private Toolbar toolbar;
////    private TextView textviewTitle;
////    private CircleImageView avatar;
////
////    /**
////     * Find the Views in the layout
////     * Auto-created on 2016-03-03 11:32:38 by Android Layout Finder
////     * (http://www.buzzingandroid.com/tools/android-layout-finder)
////     */
////    private void findViews() {
////        appbar = (AppBarLayout)findViewById( R.id.appbar );
////        collapsing = (CollapsingToolbarLayout)findViewById( R.id.collapsing );
////        coverImage = (ImageView)findViewById( R.id.imageview_placeholder );
////        framelayoutTitle = (FrameLayout)findViewById( R.id.framelayout_title );
////        linearlayoutTitle = (LinearLayout)findViewById( R.id.linearlayout_title );
////        toolbar = (Toolbar)findViewById( R.id.toolbar );
////        textviewTitle = (TextView)findViewById( R.id.textview_title );
////        avatar = (CircleImageView)findViewById(R.id.avatar);
////    }
////
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
//////        Fresco.initialize(this);
////        setContentView(R.layout.activity_shop);
////        findViews();
////
////        toolbar.setTitle("");
////        appbar.addOnOffsetChangedListener(this);
////
////        setSupportActionBar(toolbar);
////        startAlphaAnimation(textviewTitle, 0, View.INVISIBLE);
////
////        //set avatar and cover
////        avatar.setImageURI(imageUri);
////        coverImage.setImageResource(R.drawable.round_style);
////    }
////
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
////        return true;
////    }
////
////    @Override
////    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
////        int maxScroll = appBarLayout.getTotalScrollRange();
////        float percentage = (float) Math.abs(offset) / (float) maxScroll;
////
////        handleAlphaOnTitle(percentage);
////        handleToolbarTitleVisibility(percentage);
////    }
////
////    private void handleToolbarTitleVisibility(float percentage) {
////        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
////
////            if(!mIsTheTitleVisible) {
////                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
////                mIsTheTitleVisible = true;
////            }
////
////        } else {
////
////            if (mIsTheTitleVisible) {
////                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
////                mIsTheTitleVisible = false;
////            }
////        }
////    }
////
////    private void handleAlphaOnTitle(float percentage) {
////        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
////            if(mIsTheTitleContainerVisible) {
////                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
////                mIsTheTitleContainerVisible = false;
////            }
////
////        } else {
////
////            if (!mIsTheTitleContainerVisible) {
////                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
////                mIsTheTitleContainerVisible = true;
////            }
////        }
////    }
////
////    public static void startAlphaAnimation (View v, long duration, int visibility) {
////        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
////                ? new AlphaAnimation(0f, 1f)
////                : new AlphaAnimation(1f, 0f);
////
////        alphaAnimation.setDuration(duration);
////        alphaAnimation.setFillAfter(true);
////        v.startAnimation(alphaAnimation);
////    }
////}

