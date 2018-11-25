
package com.example.manop.mashop.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.manop.mashop.Product.AddProduct;
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

public class ShopFragment extends Fragment {
    private TextView shopname;
    private TextView shopdesc;
    private Button addProd;
    private Button delShop;
    private DatabaseReference shopDB;
    private DatabaseReference UserDB;
    private FirebaseUser currentUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);
        bindView(v);
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
                Intent addPro = new Intent(getActivity(), AddProduct.class);
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
                        Intent mainact = new Intent(getActivity(), MainActivity.class);
                        startActivity(mainact);
                    }
                });

            }
        });
        return v;
    }
    public void firebaseInit(){
        shopDB = FirebaseDatabase.getInstance().getReference().child("Shop");
        UserDB = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //shopDB.keepSynced(true);
        //UserDB.keepSynced(true);
    }
    public void bindView(View view){
        shopname = (TextView) view.findViewById(R.id.shoptitle);;
        shopdesc = (TextView) view.findViewById(R.id.shopdesc);
        addProd = (Button) view.findViewById(R.id.add_prod_btn);
        delShop = (Button) view.findViewById(R.id.del_shop_btn);
    }

}