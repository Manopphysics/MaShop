package com.example.manop.mashop.Product;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.*;
import com.bumptech.glide.Glide;
import com.example.manop.mashop.Decorator.ItemOffsetDecoration;
import com.example.manop.mashop.R;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.manop.mashop.Startup.LoginActivity;
import com.example.manop.mashop.Startup.MainActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProducts extends AppCompatActivity {
    private RecyclerView mProductList;
    private DatabaseReference mDatabaseProduct;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
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
        setContentView(R.layout.activity_my_products);
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
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mAuth.addAuthStateListener(mAuthListener);//important thing!!!for sign out!!!
        FirebaseRecyclerAdapter<Product, ProductViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(
                Product.class,
                R.layout.product_item,
                ProductViewHolder.class,
                mDatabaseProduct) {
            @Override
            protected void populateViewHolder(final ProductViewHolder viewHolder, final Product model, final int position) {
                final String post_key = getRef(position).getKey();
                Log.d("POST_KEY:",post_key);

                viewHolder.setTitle(model.getName());
//                viewHolder.setDesc(model.getDescription());
                viewHolder.setPrice(model.getPrice());
                //viewHolder.setUsername(model.get());
                //viewHolder.setTimeStamp(model.getTimestamp());
                viewHolder.setImage(getApplicationContext(), model.getIMAGE());
                //viewHolder.setUserImage(getApplicationContext(),model.getUserImage());
//                DatabaseReference mDatabseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                mDatabseUsers.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.hasChild("image")) {
//                            String uimage = String.valueOf(dataSnapshot.child("image").getValue());
//                            Log.d("fgfgfgfg", " " + uimage);
//                            viewHolder.setUserImage(getApplicationContext(),uimage);
//                            //Glide.with(getApplicationContext()).load(uimage).into(circleImageViewProfile);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleActivity = new Intent(MyProducts.this, SingleProductActivity.class);
                        singleActivity.putExtra("PostID", post_key);
                        startActivity(singleActivity);
                    }
                });
                //viewHolder.setLikeBtn(post_key);
//                viewHolder.mLikebtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mProcessLike = true;
//                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                if (mProcessLike) {
//                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
//                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
//                                        mProcessLike = false;
//                                    } else {
//                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
//                                        mProcessLike = false;
//                                    }
//                                }
//                            }
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                            }
//                        });
//                    }
//                });
                DatabaseReference ProductRef = FirebaseDatabase.getInstance().getReference("Product");
                DatabaseReference keyRef = ProductRef.child(post_key);
                DatabaseReference uidRef = keyRef.child("uid");





            }
        };
        mProductList.setAdapter(firebaseRecyclerAdapter);
    }

    private void firebaseInit() {

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MyProducts.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                }//if there is no curreent user then only move to liginActivity
            }
        };
        mDatabaseProduct= FirebaseDatabase.getInstance().getReference().child("Product");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDBRefSetup = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        mDatabaseProduct.keepSynced(true);
    }

    private void productList() {
        mProductList = (RecyclerView) findViewById(R.id.product_list);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        mProductList.setHasFixedSize(true);
        //mProductList.setLayoutManager(layoutManager);
        mProductList.setLayoutManager(new GridLayoutManager(this, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mProductList.addItemDecoration(itemDecoration);
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView post_title;
        TextView likeCount;
        TextView pricetv;
        ImageButton mLikebtn;
        FirebaseAuth mAuth;
        Context context;
        DatabaseReference mDatabaseLike;
        String DescTrim;
        final String ReadMore = "Read More";

        public ProductViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
//            mLikebtn = (ImageButton) mView.findViewById(R.id.post_like);
//            delPost = (ImageButton) mView.findViewById(R.id.delete_post);
            context = mView.getContext();
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mDatabaseLike.keepSynced(true);
            mAuth = FirebaseAuth.getInstance();
            post_title = mView.findViewById(R.id.post_title);
            likeCount = mView.findViewById(R.id.post_like_count);
            pricetv = mView.findViewById(R.id.product_price);
//            post_title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("MAinactivity", "someText");
//                }
//            });
        }
        public void setLikeBtn(final String post_key) {
            Log.d("MAINLIKE",mDatabaseLike.toString());
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long count = 0;
                    String likcount;
                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                        count = dataSnapshot.child(post_key).getChildrenCount();
                        if(count == 1){likcount =Long.toString(count)+" Like";  likeCount.setText(likcount);}
                        else if (count > 1){likcount =Long.toString(count)+" Likes";  likeCount.setText(likcount);}
                        mLikebtn.setImageResource(R.drawable.ic_yes_heart_colored);
                    } else {
                        count = dataSnapshot.child(post_key).getChildrenCount();
                        if(count == 1){likcount =Long.toString(count)+" Like";  likeCount.setText(likcount);}
                        else if (count > 1 || count == 0){likcount =Long.toString(count)+" Likes";  likeCount.setText(likcount);}
                        mLikebtn.setImageResource(R.drawable.ic_no_heart_gray);
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public void setPrice(String price) {
            pricetv.setText("฿"+price);
        }

        public void setTitle(String title) {
            post_title.setText(title);
        }

//        public void setDesc(String DESCRIPTION) {
//            TextView post_description = mView.findViewById(R.id.post_text);
//            try {
//                if (DESCRIPTION.length() >= 200) {
//
//                    DescTrim = DESCRIPTION.substring(0, 199);
//                    DescTrim += "... \n\n" + ReadMore;
//                    Spannable spannable = new SpannableString(DescTrim);
//                    spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 204, 214, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    post_description.setText(spannable, TextView.BufferType.SPANNABLE);
//                }
//                //post_description.setText(DescTrim);
//                else {
//                    post_description.setText(DESCRIPTION);
//                }
//            }catch (Exception se){se.printStackTrace();}
//        }

//        public void setUsername(String username) {
//            TextView post_username = mView.findViewById(R.id.post_username);
//            post_username.setText(username);
//        }

        public void updateLikeCount(long count){
            likeCount.setText(Long.toString(count));
        }
//        public void setTimeStamp(String timeStamp){
//            TextView product_date = (TextView) mView.findViewById(R.id.product_date);
//            product_date.setText(timeStamp);
//        }
        public void setImage(final Context ctx, final String IMAGE) {
            final ImageView post_image = mView.findViewById(R.id.post_image);
            Glide.with(ctx)
                    .load(IMAGE)
                    .into(post_image);

                        }

        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




}