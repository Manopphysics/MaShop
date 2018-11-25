package com.example.manop.mashop.Shop;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manop.mashop.Function.Email;
import com.example.manop.mashop.Function.GMailSender;
import com.example.manop.mashop.Function.MyCallback;
import com.example.manop.mashop.Product.Product;
import com.example.manop.mashop.Product.SingleProductActivity;
import com.example.manop.mashop.R;
import com.example.manop.mashop.Startup.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.example.manop.mashop.Chat.ChatActivity;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


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
    String receiverUID = null;
    private String REMAIL;
    private String history_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_new_order);


        ActionBar actionBar = getSupportActionBar();
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
        receiverUID = getIntent().getExtras().getString("RUID");
        try{Log.d("rruid",REMAIL);}catch (Exception e){e.printStackTrace();Log.d("rruid","RUID is null");}
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
                history_key = sellHistory.getKey();

                //sellHistory.child("total_price").setValue(Integer.parseInt(product.getPrice()) * Integer.parseInt(product.getQuantity()));

                FirebaseDatabase.getInstance().getReference().child("Users")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                sellHistory.setValue(product);
                                String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH.mm").format(new Date());
                                sellHistory.child("date").setValue(timeStamp);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                Vibrator vibratePhone = (Vibrator) getSystemService(PlaceNewOrder.VIBRATOR_SERVICE);
                readDataSaleHistory(new MyCallback() {
                    @Override
                    public void onCallback(String value) {

                    }

                    @Override
                    public void onCallbackEmailName(String name, String email) {

                    }

                    @Override
                    public void onCallbackProduct(ArrayList<Product> al) {

                    }

                    @Override
                    public void onCallbackSaleHistory(String date, String description, String name, String price, String quantity, String buyerName, String buyerEmail) {
                        Log.d("emailDEBUG",date+" "+description+" "+name+" "+price+" "+quantity+" "+buyerName+" "+buyerEmail);
                        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Product Order confirmation :"+"Product: "+name);
                        intent.putExtra(Intent.EXTRA_TEXT, "This email is generated automatically to confirm that you "+"("+
                        buyerName+")"+" Have Ordered\nProduct name: "+name+"\nProduct Description : "+description+"\n"+"Price: ฿"+price+ "  \nQuantity: "+quantity+"\n"+
                        "Total price= ฿"+(Double.parseDouble(price)*Double.parseDouble(quantity))+"\n"+
                        "This order has been placed on: "+date);
                        intent.setData(Uri.parse("mailto:"+buyerEmail)); // or just "mailto:" for blank
                        // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                        try {
                            startActivityForResult(Intent.createChooser(intent, "Send mail..."),0);
                            Log.d("emailDEBUG","STARTACTIVITY SUCCESS");
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(PlaceNewOrder.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            Log.d("emailDEBUG","ERROR");
                        }
                    }
                });

                vibratePhone.vibrate(400);


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



    public void readDataSaleHistory(final MyCallback mc){
        readData(new MyCallback() {
            @Override
            public void onCallback(final String value) {

            }

            @Override
            public void onCallbackEmailName(final String name, final String email) {
                FirebaseDatabase.getInstance().getReference().child("Shop").child(mAuth.getCurrentUser().getUid()).child("sell_history").child(history_key)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mc.onCallbackSaleHistory(dataSnapshot.child("date").getValue().toString(),
                                        dataSnapshot.child("description").getValue().toString(),
                                        dataSnapshot.child("name").getValue().toString(),
                                        dataSnapshot.child("price").getValue().toString(),
                                        dataSnapshot.child("quantity").getValue().toString(),
                                        email, name);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onCallbackProduct(ArrayList<Product> al) {

            }


            @Override
            public void onCallbackSaleHistory(String date, String description, String name, String price, String quantity, String buyerName, String buyerEmail) {

            }
        });



    }

    public void readData(final MyCallback mc){
        FirebaseDatabase.getInstance().getReference().child("Users").child(receiverUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mc.onCallbackEmailName(dataSnapshot.child("email").getValue().toString(),
                                        dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}