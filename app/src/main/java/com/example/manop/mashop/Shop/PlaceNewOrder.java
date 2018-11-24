package com.example.manop.mashop.Shop;


import android.os.Bundle;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manop.mashop.Product.Product;
import com.example.manop.mashop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.example.manop.mashop.Chat.ChatActivity;


public class PlaceNewOrder extends AppCompatActivity {

    private ImageView singelImage;
    private TextView singleTitle, singleDesc, productPrice, quan_left_tv;
    String post_key = null;
    String uid;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mShop;
    private Button minus_btn;
    private Button plus_btn;
    private TextView quantity;
    private int pquantity = 0;
    private int quantity_left;
    private Button place_order;
    private Product product;
    private String chat_email;
    private String chat_uid;
    private String chat_tok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_new_order);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        place_order = (Button) findViewById(R.id.place_order);
        quan_left_tv = (TextView) findViewById(R.id.quan_left_tv);
        plus_btn = (Button) findViewById(R.id.plus_btn_pno);
        minus_btn = (Button) findViewById(R.id.minus_btn_pno);
        quantity = (TextView) findViewById(R.id.quantity_tv_pno);
        singelImage = (ImageView) findViewById(R.id.singleImageview_pno);
        singleTitle = (TextView) findViewById(R.id.singleTitle_pno);
        singleDesc = (TextView) findViewById(R.id.singleDesc_pno);
        productPrice = (TextView) findViewById(R.id.product_price_pno);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Product");
        mShop = FirebaseDatabase.getInstance().getReference().child("Shop");
        post_key = getIntent().getExtras().getString("PostID");
        mAuth = FirebaseAuth.getInstance();

        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("Product").child(post_key).child("quantity").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String dbquan = dataSnapshot.getValue().toString();
                        int intdbquan = Integer.parseInt(dbquan);
                        quantity_left = intdbquan;
                        quan_left_tv.setText(Integer.toString(quantity_left));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if (pquantity >= quantity_left) {
                    Toast.makeText(PlaceNewOrder.this, "ERROR cannot sell quantity more than what is left in stock", Toast.LENGTH_SHORT).show();
                } else {
                    pquantity += 1;
                    quantity.setText(Integer.toString(pquantity));
                }
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Product").child(post_key).child("quantity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String dbquan = dataSnapshot.getValue().toString();
                int intdbquan = Integer.parseInt(dbquan);
                quantity_left = intdbquan;
                quan_left_tv.setText(Integer.toString(quantity_left));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pquantity <= 0) {
                    Toast.makeText(PlaceNewOrder.this, "You must place an order of quantity GREATER than 0!", Toast.LENGTH_SHORT).show();
                } else if (pquantity >= 0) {
                    pquantity -= 1;
                    quantity.setText(Integer.toString(pquantity));
                }
                FirebaseDatabase.getInstance().getReference().child("Product").child(post_key).child("quantity").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String dbquan = dataSnapshot.getValue().toString();
                        int intdbquan = Integer.parseInt(dbquan);
                        quantity_left = intdbquan;
                        quan_left_tv.setText(Integer.toString(quantity_left));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                FirebaseDatabase.getInstance().getReference().child("Product").child(post_key).child("quantity").
                        setValue(Integer.toString(quantity_left));
                FirebaseDatabase.getInstance().getReference().child("Shop").child(mAuth.getCurrentUser().getUid())
                        .child("product").child(post_key).child("quantity").setValue(Integer.toString(quantity_left));
            }
        });
        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Product").child(post_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        product = dataSnapshot.getValue(Product.class);
                        product.setQuantity((quantity.getText().toString()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                FirebaseDatabase.getInstance().getReference().child("Product").child(post_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String dbquan = dataSnapshot.child("quantity").getValue().toString();
                        int intdbquan = Integer.parseInt(dbquan);
                        quantity_left = intdbquan - Integer.parseInt(quantity.getText().toString());
                        try{Log.d("quanleft",Integer.toString(quantity_left));}catch(Exception e){Log.d("quanleft","NULL WTF");}
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseDatabase.getInstance().getReference().child("Product").child(post_key).child("quantity").
                        setValue(Integer.toString(quantity_left-Integer.parseInt(quantity.getText().toString())));
                FirebaseDatabase.getInstance().getReference().child("Shop").child(mAuth.getCurrentUser().getUid())
                        .child("product").child(post_key).child("quantity").setValue(Integer.toString(quantity_left-Integer.parseInt(quantity.getText().toString())));
                //-================ new order history:
                final DatabaseReference sellHistory =
                FirebaseDatabase.getInstance().getReference().child("Shop").child(mAuth.getCurrentUser().getUid())
                        .child("sell_history").push();

                //sellHistory.child("total_price").setValue(Integer.parseInt(product.getPrice()) * Integer.parseInt(product.getQuantity()));

                FirebaseDatabase.getInstance().getReference().child("Users")
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sellHistory.setValue(product);
                        }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Vibrator vibratePhone = (Vibrator) getSystemService(PlaceNewOrder.VIBRATOR_SERVICE);

                vibratePhone.vibrate(400);

                FirebaseDatabase.getInstance().getReference().child("Product").addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             uid = dataSnapshot.child(post_key).child("uid").getValue(String.class);
                             mShop.child(uid).child("user").addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(DataSnapshot dataSnapshot) {
                                     chat_email = dataSnapshot.child("email").getValue(String.class);
                                     chat_uid = dataSnapshot.child("uid").getValue(String.class);
                                     chat_tok = dataSnapshot.child("firebaseToken").getValue(String.class);
                                 }
                                 @Override
                                 public void onCancelled(DatabaseError databaseError) {}
                             });}
                         @Override
                         public void onCancelled(DatabaseError databaseError) {}});

                ChatActivity.startActivity(PlaceNewOrder.this,
                        chat_email,
                        chat_uid,
                        chat_tok);
            }});


        plus_btn.setVisibility(View.INVISIBLE);
        minus_btn.setVisibility(View.INVISIBLE);


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
                    Picasso.get().load(post_image).into(singelImage);
                    if (mAuth.getCurrentUser().getUid().equals(post_uid)) {

                        plus_btn.setVisibility(View.VISIBLE);
                        minus_btn.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}