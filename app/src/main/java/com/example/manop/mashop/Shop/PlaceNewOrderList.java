package com.example.manop.mashop.Shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.manop.mashop.Decorator.ItemOffsetDecoration;
import com.example.manop.mashop.Product.Product;
import com.example.manop.mashop.R;
import com.example.manop.mashop.Startup.LoginActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PlaceNewOrderList extends AppCompatActivity {
    private RecyclerView mProductList;
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
    private String RUID,REMAIL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //====================================================
        setContentView(R.layout.activity_place_new_order_list);
        firebaseInit();
        productList();
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        RUID = getIntent().getExtras().getString("RUID");
        REMAIL = getIntent().getExtras().getString("senderEmail");
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
                mDatabaseShop) {
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
                        Intent singleActivity = new Intent(PlaceNewOrderList.this,PlaceNewOrder.class);
                        singleActivity.putExtra("PostID", post_key);
                        singleActivity.putExtra("RUID",RUID);
                        singleActivity.putExtra("senderEmail",RUID);
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
                    Intent loginIntent = new Intent(PlaceNewOrderList.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                }//if there is no curreent user then only move to liginActivity
            }
        };
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseProduct= FirebaseDatabase.getInstance().getReference().child("Product");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDBRefSetup = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseShop = FirebaseDatabase.getInstance().getReference().child("Shop").child(mCurrentUser.getUid()).child("product");
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

        }


        public void setPrice(String price) {
            try {
                pricetv.setText("฿" + price);
            }catch(Exception e){e.printStackTrace();}
        }

        public void setTitle(String title) {
            try {
                post_title.setText(title);
            }catch(Exception e){e.printStackTrace();}
        }

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