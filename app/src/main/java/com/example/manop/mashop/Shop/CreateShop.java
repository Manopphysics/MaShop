package com.example.manop.mashop.Shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.manop.mashop.R;
import com.example.manop.mashop.Startup.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateShop extends AppCompatActivity {
    private Spinner categorySpinner;
    private EditText phonenum;
    private EditText shopname;
    private EditText shopdesc;
    private Button submitForm;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);
        bindViews();
        firebaseInit();
        addItemSpinner();
        clickEvents();
    }
    public void addItemSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("House hold");
        list.add("toys");
        list.add("bakery");
        list.add("equipments");
        list.add("Pharmaceutical");
        list.add("IT/ computers");
        list.add("Clothes");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter);
    }
    public void firebaseInit(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shop");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser() ;
    }
    public void bindViews(){
        submitForm = (Button) findViewById(R.id.submit_form);
        phonenum = (EditText) findViewById(R.id.phonenum);
        shopname = (EditText) findViewById(R.id.shopname);
        shopdesc = (EditText) findViewById(R.id.shdeesc);
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
    }
    public void clickEvents(){
        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference shopdb = mDatabase.child(currentUser.getUid());
                shopdb.child("phone").setValue(phonenum.getText().toString());
                shopdb.child("category").setValue(String.valueOf(categorySpinner.getSelectedItem()));
                shopdb.child("name").setValue(shopname.getText().toString());
                shopdb.child("description").setValue(shopdesc.getText().toString());
                mDatabaseUsers.child(currentUser.getUid()).child("seller").setValue("true");
                Intent main = new Intent(CreateShop.this, Shop.class);
                startActivity(main);
            }
        });
    }
}
