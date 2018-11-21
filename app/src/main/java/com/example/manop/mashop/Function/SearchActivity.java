package com.example.manop.mashop.Function;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.manop.mashop.Decorator.ItemOffsetDecoration;
import com.example.manop.mashop.Product.MyProducts;
import com.example.manop.mashop.Product.Product;
import com.example.manop.mashop.Product.SingleProductActivity;
import com.example.manop.mashop.R;
import com.example.manop.mashop.Startup.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchActivity extends AppCompatActivity {
    private Spinner sort_spinner;
    private EditText mSearchField;
    private ImageButton mSearchBtn;

    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        sort_spinner = findViewById(R.id.sort_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_by, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        sort_spinner.setAdapter(adapter);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Product");


        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);

        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new GridLayoutManager(this, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mResultList.addItemDecoration(itemDecoration);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String searchText = mSearchField.getText().toString();
                sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(SearchActivity.this,Long.toString(parent.getItemIdAtPosition(position)),Toast.LENGTH_SHORT).show();
                        long itemnum = parent.getItemIdAtPosition(position);
                        if(itemnum == 0){ //IF sort by low to high price
                            firebaseUserSearch(searchText,0);
                        }
                        else if(itemnum == 1){//IF sort by high to low price
                            firebaseUserSearch(searchText,1);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                firebaseUserSearch(searchText,0);

            }
        });

    }

    private void firebaseUserSearch(String searchText,int mode) {

        Toast.makeText(SearchActivity.this, "Started Search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mUserDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Product, ProductViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(

                Product.class,
                R.layout.product_item,
                ProductViewHolder.class,
                firebaseSearchQuery

        ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle(model.getName());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(), model.getIMAGE());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleActivity = new Intent(SearchActivity.this, SingleProductActivity.class);
                        singleActivity.putExtra("PostID", post_key);
                        startActivity(singleActivity);
                    }
                });

            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);

    }


    // View Holder Class

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView post_title;
        TextView likeCount;
        TextView pricetv;
        FirebaseAuth mAuth;
        Context context;
        DatabaseReference mDatabaseLike;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            context = mView.getContext();
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mDatabaseLike.keepSynced(true);
            mAuth = FirebaseAuth.getInstance();
            post_title = mView.findViewById(R.id.post_title);
            likeCount = mView.findViewById(R.id.post_like_count);
            pricetv = mView.findViewById(R.id.product_price);
        }

        public void setPrice(String price) {
            pricetv.setText(price);
        }

        public void setTitle(String title) {
            post_title.setText(title);
        }

        public void setImage(final Context ctx, final String IMAGE) {
            final ImageView post_image = mView.findViewById(R.id.post_image);
            Glide.with(ctx)
                    .load(IMAGE)
                    .into(post_image);

        }

    }
    }

