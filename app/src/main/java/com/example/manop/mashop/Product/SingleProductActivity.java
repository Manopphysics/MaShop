package com.example.manop.mashop.Product;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manop.mashop.Chat.ChatActivity;
import com.example.manop.mashop.Chat.ChatActivityOld;
import com.example.manop.mashop.Chat.UserListingActivity;
import com.example.manop.mashop.R;
import com.example.manop.mashop.Startup.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleProductActivity extends AppCompatActivity {

    private ImageView singelImage;
    private TextView singleTitle, singleDesc, productPrice,pDiscountPrice;
    String post_key = null;
    String uid;
    private DatabaseReference mDatabase;
    private Button deleteBtn;
    private ImageButton chat_button;
    private FirebaseAuth mAuth;
    private DatabaseReference mShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);




        singelImage = (ImageView)findViewById(R.id.singleImageview);
        singleTitle = (TextView)findViewById(R.id.singleTitle);
        singleDesc = (TextView)findViewById(R.id.singleDesc);
        productPrice = (TextView) findViewById(R.id.product_price);
        pDiscountPrice = (TextView) findViewById(R.id.product_discount);
        chat_button = (ImageButton) findViewById(R.id.chat_button);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Product");
        mShop = FirebaseDatabase.getInstance().getReference().child("Shop");
        post_key = getIntent().getExtras().getString("PostID");
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        mAuth = FirebaseAuth.getInstance();
        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UserListingActivity.startActivity(SingleProductActivity.this,
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Intent chat = new Intent(SingleProductActivity.this,ChatActivity.class);
                        uid = dataSnapshot.child(post_key).child("uid").getValue(String.class);
//                        chat.putExtra("shopuid",uid);
//                        startActivity(chat);
                        mShop.child(uid).child("user").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ChatActivity.startActivity(SingleProductActivity.this,
                                        dataSnapshot.child("email").getValue(String.class),
                                        dataSnapshot.child("uid").getValue(String.class),
                                        dataSnapshot.child("firebaseToken").getValue(String.class));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        deleteBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(post_key).removeValue();

                Intent mainintent = new Intent(SingleProductActivity.this, MainActivity.class);
                startActivity(mainintent);
            }
        });


        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String post_title = (String) dataSnapshot.child("name").getValue();
                    String post_desc = (String) dataSnapshot.child("description").getValue();
                    String post_image = (String) dataSnapshot.child("IMAGE").getValue();
                    String product_price = (String) dataSnapshot.child("price").getValue();
                    product_price = product_price.replaceAll("[-+.^:,]", "");
                    String post_uid = (String) dataSnapshot.child("uid").getValue();

                    singleTitle.setText(post_title);
                    singleDesc.setText(post_desc);
                    productPrice.setText("฿" + product_price);
                    Double dummyDiscount = Double.parseDouble(product_price);
                    pDiscountPrice.setText(Double.toString(dummyDiscount + (0.04) * dummyDiscount));
                    pDiscountPrice.setPaintFlags(pDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    Picasso.with(SingleProductActivity.this).load(post_image).into(singelImage);
                    if (mAuth.getCurrentUser().getUid().equals(post_uid)) {

                        deleteBtn.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){e.printStackTrace();;}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}