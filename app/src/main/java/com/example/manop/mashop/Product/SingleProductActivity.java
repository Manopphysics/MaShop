package com.example.manop.mashop.Product;

import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;


import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manop.mashop.Chat.ChatActivity;
import com.example.manop.mashop.R;
import com.example.manop.mashop.Startup.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.an.customfontview.CustomTextView;

import org.w3c.dom.Text;

public class SingleProductActivity extends AppCompatActivity {

    private ImageView singelImage;
    private CustomTextView singleTitle, singleDesc, productPrice;
    private TextView pDiscountPrice;
    String post_key = null;
    String uid;
    private DatabaseReference mDatabase;
    private Button deleteBtn;
    private Button chat_button;
    private ShineButton wish_button;
    private FirebaseAuth mAuth;
    private DatabaseReference mShop;
    private DatabaseReference mDatabaseLike;
    private TextView like_count;
    private boolean mProcessLike = false;
    private Button minus_btn;
    private Button plus_btn;
    private TextView quantity;
    private int pquantity = 0;
    private String chat_email;
    private String chat_uid;
    private String chat_tok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);



        plus_btn = (Button) findViewById(R.id.plus_btn);
        minus_btn = (Button) findViewById(R.id.minus_btn);
        quantity = (TextView) findViewById(R.id.quantity_tv);
        singelImage = (ImageView)findViewById(R.id.singleImageview);
        singleTitle = (CustomTextView)findViewById(R.id.singleTitle);
        singleDesc = (CustomTextView) findViewById(R.id.singleShortDesc);
        productPrice = (CustomTextView) findViewById(R.id.product_price);
        pDiscountPrice = (TextView) findViewById(R.id.product_discount);
        chat_button = (Button) findViewById(R.id.chat_button);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Product");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
        wish_button = (ShineButton) findViewById(R.id.wish_button);
        wish_button.init(this);
        mShop = FirebaseDatabase.getInstance().getReference().child("Shop");
        post_key = getIntent().getExtras().getString("PostID");
        like_count = (TextView) findViewById(R.id.like_count);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        mAuth = FirebaseAuth.getInstance();
        Log.d("MAINLIKE",mDatabaseLike.toString());

        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pquantity += 1;
                quantity.setText(Integer.toString(pquantity));
                FirebaseDatabase.getInstance().getReference().child("Product").child(post_key).child("quantity")
                        .setValue(Integer.toString(pquantity));
                FirebaseDatabase.getInstance().getReference().child("Shop").child(mAuth.getCurrentUser().getUid())
                        .child("product").child(post_key).child("quantity").setValue(Integer.toString(pquantity));
            }
        });
        minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pquantity <= 0 ){
                    Toast.makeText(SingleProductActivity.this,"Cannot reduce quantity anymore!",Toast.LENGTH_SHORT).show();
                }
                else if(pquantity >= 0 ){
                    pquantity -= 1;
                    quantity.setText(Integer.toString(pquantity));
                    FirebaseDatabase.getInstance().getReference().child("Product").child(post_key).child("quantity")
                            .setValue(Integer.toString(pquantity));
                    FirebaseDatabase.getInstance().getReference().child("Shop").child(mAuth.getCurrentUser().getUid())
                            .child("product").child(post_key).child("quantity").setValue(Integer.toString(pquantity));
                }
            }
        });
        mDatabaseLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = 0;
                String likcount;
                if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                    count = dataSnapshot.child(post_key).getChildrenCount();
                    if(count == 1){likcount =Long.toString(count)+" Like";  like_count.setText(likcount);}
                    else if (count > 1){likcount =Long.toString(count)+" Likes";  like_count.setText(likcount);}
//                    wish_button.setImageResource(R.drawable.ic_yes_heart_colored);
                } else {
                    count = dataSnapshot.child(post_key).getChildrenCount();
                    if(count == 1){likcount =Long.toString(count)+" Like";  like_count.setText(likcount);}
                    else if (count > 1 || count == 0){likcount =Long.toString(count)+" Likes";  like_count.setText(likcount);}
//                    wish_button.setImageResource(R.drawable.ic_no_heart_gray);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        wish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProcessLike = true;
                mDatabaseLike.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (mProcessLike) {
                            if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                                mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                mProcessLike = false;
                            } else {
                                mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
                                mProcessLike = false;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });


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

                                        chat_email = dataSnapshot.child("email").getValue(String.class);
                                        chat_uid = dataSnapshot.child("uid").getValue(String.class);
                                        chat_tok = dataSnapshot.child("firebaseToken").getValue(String.class);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                }
                );

                ChatActivity.startActivity(SingleProductActivity.this,
                        chat_email,
                        chat_uid,
                        chat_tok);}
        });
        plus_btn.setVisibility(View.INVISIBLE);
        minus_btn.setVisibility(View.INVISIBLE);
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
                    String pquan = (String) dataSnapshot.child("quantity").getValue();
                    product_price = product_price.replaceAll("[-+.^:,]", "");
                    String post_uid = (String) dataSnapshot.child("uid").getValue();

                    pquantity = Integer.parseInt(pquan);
                    quantity.setText(pquan);
                    singleTitle.setText(post_title);
                    singleDesc.setText(post_desc);
                    productPrice.setText("฿" + product_price);
                    Double dummyDiscount = Double.parseDouble(product_price);
                    pDiscountPrice.setText(Double.toString(dummyDiscount + (0.04) * dummyDiscount));
                    pDiscountPrice.setPaintFlags(pDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    Picasso.get().load(post_image).into(singelImage);
                    if (mAuth.getCurrentUser().getUid().equals(post_uid)) {

                        deleteBtn.setVisibility(View.VISIBLE);
                        plus_btn.setVisibility(View.VISIBLE);
                        minus_btn.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){e.printStackTrace();;}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}