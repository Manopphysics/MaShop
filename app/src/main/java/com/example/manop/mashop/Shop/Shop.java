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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Shop extends AppCompatActivity {
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
                FirebaseDatabase.getInstance().getReference().child("Product").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            if(ds.child("uid").getValue().toString().equals(currentUser.getUid())){
                               ds.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

    @Override
    public void onBackPressed(){
        Intent main = new Intent(Shop.this, MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main);
    }
}