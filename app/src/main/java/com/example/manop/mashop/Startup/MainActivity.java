package com.example.manop.mashop.Startup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.manop.mashop.Fragments.HomeFragment;
import com.example.manop.mashop.Function.SearchActivity;
import com.example.manop.mashop.Shop.CreateShop;
import com.example.manop.mashop.Fragments.FragmentTest;
import com.example.manop.mashop.R;
import com.example.manop.mashop.Shop.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser currentUser;
    private String currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav1);
        }
        firebaseInit();
        if(currentUserID != null) {
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // NullPointerException when shop is deleted!!!
                    if (dataSnapshot.child(currentUserID).child("seller").getValue(String.class).equals("false")) {
                        //showItemReg();
                        //hideItemShop();
                        showItem(R.id.nav_reg_shop);
                        hideItem(R.id.nav_my_shop);
                    } else if (dataSnapshot.child(currentUserID).child("seller").getValue(String.class).equals("true")){
                        showItem(R.id.nav_my_shop);
                        hideItem(R.id.nav_reg_shop);
                        //hideItemReg();
                        //showItemShop();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav3:
                Toast.makeText(this,"NAV 3",Toast.LENGTH_SHORT).show();break;
            case R.id.nav4:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_reg_shop:
                Intent createshop = new Intent(MainActivity.this,CreateShop.class);
                startActivity(createshop);
                //Toast.makeText(this,"Will add this activity soon!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_my_shop:
                Intent myshop = new Intent(MainActivity.this,Shop.class);
                startActivity(myshop);
                break;
            case R.id.nav_logout:
                logout();
                break;
        }
        // set item as selected to persist highlight
        item.setChecked(true);
        // close drawer when item is tapped
        drawer.closeDrawers();
        return true;
    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    private void firebaseInit() {

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                }
            }
        };
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            currentUserID = currentUser.getUid();
        }

        //Toast.makeText(this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }
@Override
public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_search){
            Toast.makeText(this,"WILL SEARCH",Toast.LENGTH_SHORT).show();
            Intent search = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(search);
        }
        return super.onOptionsItemSelected(item);
}
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void logout() {
        mAuth.signOut();

    }
    private void showItem(final int id)
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(id).setVisible(true);
    }
    private void hideItem(final int id)
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(id).setVisible(false);
    }
//    private void showItemShop()
//    {
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        Menu nav_Menu = navigationView.getMenu();
//        nav_Menu.findItem(R.id.nav_my_shop).setVisible(true);
//    }
//    private void hideItemShop()
//    {
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        Menu nav_Menu = navigationView.getMenu();
//        nav_Menu.findItem(R.id.nav_my_shop).setVisible(false);
//    }
}
