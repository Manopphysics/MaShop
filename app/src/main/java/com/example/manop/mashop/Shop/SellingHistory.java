package com.example.manop.mashop.Shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.manop.mashop.R;


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

public class SellingHistory extends AppCompatActivity {
    private EditText mSearchField;
    private ImageButton mSearchBtn;

    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_history);
        
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Product");
        

        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new GridLayoutManager(this, 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mResultList.addItemDecoration(itemDecoration);
        sellingHistoryList();

    }

    private void sellingHistoryList() {
        
        FirebaseRecyclerAdapter<Product, HistoryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, HistoryViewHolder>(

                Product.class,
                R.layout.sell_history_item,
                HistoryViewHolder.class,
                FirebaseDatabase.getInstance().getReference().child("Shop").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("sell_history")

        ) {
            @Override
            protected void populateViewHolder(HistoryViewHolder viewHolder, Product model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setName(model.getName());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(), model.getIMAGE());
                viewHolder.setOrderNum(Integer.toString(position+1));
                viewHolder.setQuantity(model.getQuantity());
                viewHolder.setTotalPrice(Double.toString(Double.parseDouble(model.getPrice())*Double.parseDouble(model.getQuantity())));


            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);

    }


    // View Holder Class

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView order_num,history_product_name,history_product_price,history_product_quantity,history_total_price;
        ImageView history_product_image;
        FirebaseAuth mAuth;
        Context context;
        DatabaseReference mDatabaseLike;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            context = mView.getContext();
            mAuth = FirebaseAuth.getInstance();

            order_num = (TextView) mView.findViewById(R.id.order_num);
            history_product_name = (TextView) mView.findViewById(R.id.history_product_name);
            history_product_price = (TextView) mView.findViewById(R.id.history_product_price);
            history_product_quantity = (TextView) mView.findViewById(R.id.history_product_quantity);
            history_total_price = (TextView) mView.findViewById(R.id.history_total_price);
            history_product_image = (ImageView) mView.findViewById(R.id.history_product_image);
        }

        public void setPrice(String price) {
            history_product_price.setText(price);
        }

        public void setName(String title) {
            history_product_name.setText(title);
        }

        public void setOrderNum(String orderNum){
            order_num.setText(orderNum);
        }

        public void setQuantity(String quan){
            history_product_quantity.setText(quan);
        }

        public void setTotalPrice(String tp){
            history_total_price.setText(tp);
        }

        public void setImage(final Context ctx, final String IMAGE) {
            Glide.with(ctx)
                    .load(IMAGE)
                    .into(history_product_image);

        }

    }
}
