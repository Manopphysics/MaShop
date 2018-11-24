package com.example.manop.mashop.Product;

import android.graphics.Paint;
import android.os.Bundle;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private DatabaseReference mDatabase;
    private Button deleteBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        singelImage = (ImageView)findViewById(R.id.singleImageview);
        singleTitle = (TextView)findViewById(R.id.singleTitle);
        singleDesc = (TextView)findViewById(R.id.singleShortDesc);
        productPrice = (TextView) findViewById(R.id.product_price);
        pDiscountPrice = (TextView) findViewById(R.id.product_discount);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Product");
        post_key = getIntent().getExtras().getString("PostID");
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        mAuth = FirebaseAuth.getInstance();
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
                String post_title = (String) dataSnapshot.child("name").getValue();
                String post_desc = (String) dataSnapshot.child("description").getValue();
                String post_image = (String) dataSnapshot.child("IMAGE").getValue();
                String product_price = (String) dataSnapshot.child("price").getValue();
                product_price = product_price.replaceAll("[-+.^:,]","");
                String post_uid = (String) dataSnapshot.child("uid").getValue();

                singleTitle.setText(post_title);
                singleDesc.setText(post_desc);
                productPrice.setText("฿"+product_price);
                Double dummyDiscount = Double.parseDouble(product_price);
                pDiscountPrice.setText(Double.toString(dummyDiscount+(0.04)*dummyDiscount));
                pDiscountPrice.setPaintFlags(pDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                Picasso.get().load(post_image).into(singelImage);
                if (mAuth.getCurrentUser().getUid().equals(post_uid)){

                    deleteBtn.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}