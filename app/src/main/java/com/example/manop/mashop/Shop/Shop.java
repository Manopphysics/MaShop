package com.example.manop.mashop.Shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.manop.mashop.Adapter.PagerAdapter;
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


public class Shop extends AppCompatActivity {
    private TextView shopname;
    private TextView shopdesc;
    private Button addProd;
    private Button delShop;
    private Button myProd;
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
                shopname.setText(dataSnapshot.child("name").getValue(String.class));
                shopdesc.setText(dataSnapshot.child("description").getValue(String.class));
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
    }
    public void firebaseInit(){
        shopDB = FirebaseDatabase.getInstance().getReference().child("Shop");
        UserDB = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //shopDB.keepSynced(true);
        //UserDB.keepSynced(true);
    }
    public void bindView(){
        shopname = (TextView) findViewById(R.id.shoptitle);;
        shopdesc = (TextView) findViewById(R.id.shopdesc);
        addProd = (Button) findViewById(R.id.add_prod_btn);
        myProd = (Button) findViewById(R.id.btn_my_product);
        delShop = (Button) findViewById(R.id.del_shop_btn);
    }
}