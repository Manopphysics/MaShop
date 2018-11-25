package com.example.manop.mashop.Shop;

import android.content.Intent;

import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.manop.mashop.Decorator.ItemOffsetDecoration;
import com.example.manop.mashop.R;
import com.example.manop.mashop.Startup.LoginActivity;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShopListActivity extends AppCompatActivity {
    private RecyclerView mShopList;
    private DatabaseReference mDatabaseProduct;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseShop;
    private DatabaseReference mDBRefSetup;
    private DatabaseReference mDatabaseLike;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FloatingActionButton floatingActionButton;
    private boolean mProcessLike = false;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //====================================================
        setContentView(R.layout.activity_shop_list);
        firebaseInit();
        productList();
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseShop = FirebaseDatabase.getInstance().getReference().child("Shop");
        mAuth.addAuthStateListener(mAuthListener);//important thing!!!for sign out!!!
        FirebaseRecyclerAdapter<ShopModel, ShopViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ShopModel, ShopViewHolder>(
                ShopModel.class,
                R.layout.card_brand_home,
                ShopViewHolder.class,
                mDatabaseShop) {
            @Override
            protected void populateViewHolder(final ShopViewHolder viewHolder, final ShopModel model, final int position) {
                final String post_key = getRef(position).getKey();
                Log.d("POST_KEY:",post_key);

                viewHolder.setName(model.getName());
                viewHolder.setImage(getApplicationContext(), model.getImage());
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent singleActivity = new Intent(com.example.manop.mashop.Product.MyProducts.this, SingleProductActivity.class);
//                        singleActivity.putExtra("PostID", post_key);
//                        startActivity(singleActivity);
//                    }
//                });
                

            }
        };
        mShopList.setAdapter(firebaseRecyclerAdapter);
    }

    private void firebaseInit() {

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Intent loginIntent = new Intent(ShopListActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                }//if there is no curreent user then only move to liginActivity
            }
        };
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseProduct= FirebaseDatabase.getInstance().getReference().child("Product");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDBRefSetup = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseShop = FirebaseDatabase.getInstance().getReference().child("Shop");
        mDatabaseUsers.keepSynced(true);
        mDatabaseProduct.keepSynced(true);

    }

    private void productList() {
        mShopList = (RecyclerView) findViewById(R.id.shop_list);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        mShopList.setHasFixedSize(true);
        //mShopList.setLayoutManager(layoutManager);
        mShopList.setLayoutManager(new GridLayoutManager(this, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mShopList.addItemDecoration(itemDecoration);
    }




    // View Holder Class

    public static class ShopViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView shopname;
        ImageView shopimage;
        FirebaseAuth mAuth;
        Context context;
        DatabaseReference mDatabaseLike;

        public ShopViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            context = mView.getContext();
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mDatabaseLike.keepSynced(true);
            mAuth = FirebaseAuth.getInstance();
            shopimage = mView.findViewById(R.id.card_shop_image);
            shopname = mView.findViewById(R.id.card_shop_name);
        }

        public void  setName(String name){
            shopname.setText(name);
        }
        public void setImage(final Context ctx, final String IMAGE) {
            Glide.with(ctx)
                    .load(IMAGE)
                    .into(shopimage);

        }

    }
}

