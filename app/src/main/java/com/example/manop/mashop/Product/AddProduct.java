package com.example.manop.mashop.Product;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manop.mashop.R;
import com.example.manop.mashop.Shop.Shop;
import com.example.manop.mashop.Startup.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddProduct extends AppCompatActivity {

    private Button mSubmitBtn;
    private ImageButton mSelectImage;
    private EditText mPostTitle;
    private static final int GALLERY_REQUEST = 1;
    private EditText mPostDesc;
    private EditText mProductPrice;
    private Uri mImageUri = null;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseProduct;
    private ProgressDialog mprogressbar;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUSer;
    private DatabaseReference mDatabaseShop;
    private TextView product_quantity;
    private Button add_btn;
    private Button reduce_btn;
    private int quantity = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        bindViews();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUSer = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        clickEvents();

    }

    private void bindViews() {
        mprogressbar = new ProgressDialog(this);
        mDatabaseProduct = FirebaseDatabase.getInstance().getReference().child("Product");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mPostTitle = (EditText) findViewById(R.id.editText1);
        mPostDesc = (EditText) findViewById(R.id.editText2);
        mProductPrice = (EditText) findViewById(R.id.editText3);
        mSubmitBtn = (Button) findViewById(R.id.btn);
        mSelectImage = (ImageButton) findViewById(R.id.imageButton2);
        mDatabaseShop = FirebaseDatabase.getInstance().getReference().child("Shop");
        product_quantity = (TextView) findViewById(R.id.product_quantity);
        reduce_btn = (Button) findViewById(R.id.reduce_btn);
        add_btn = (Button) findViewById(R.id.add_btn);
    }

    private void clickEvents() {
        reduce_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity <= 0 ){
                    Toast.makeText(AddProduct.this,"Cannot reduce quantity anymore!",Toast.LENGTH_SHORT).show();
                }
                else if(quantity >= 0 ){
                    quantity -= 1;
                    product_quantity.setText(Integer.toString(quantity));
                }
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity += 1;
                product_quantity.setText(Integer.toString(quantity));

            }
        });
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");

                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });

    }

    private void startPosting() {

        mprogressbar.setMessage("Posting...");

        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getText().toString().trim();
        final String price_val = mProductPrice.getText().toString().trim();
        final String prod_quan = Integer.toString(quantity);

        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri != null) {
            //can post
            // mImageUri= Uri.fromFile(new File(mImageUri.getLastPathSegment()));
            mprogressbar.show();
            StorageReference filepath = mStorageRef.child("Product_Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = mDatabaseProduct.push();//cret uniquid
                    mDatabaseUSer.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newPost.child("name").setValue(title_val);
                            newPost.child("description").setValue(desc_val);
                            newPost.child("IMAGE").setValue(downloadUrl.toString());
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("price").setValue(price_val);
                            newPost.child("quantity").setValue(prod_quan).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent  = new Intent(AddProduct.this, Shop.class);
                                    intent.putExtra("FRAGMENT_ID", 0);
                                    startActivity(intent);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mprogressbar.dismiss();
                    Toast.makeText(AddProduct.this, "Posted Successfully!!!!!",
                            Toast.LENGTH_SHORT).show();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProduct.this, "Unable to post Please TRY AGAIN!!!!",
                            Toast.LENGTH_LONG).show();
                }
            });
        }

        mDatabaseProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
//                   Log.d("productUID",  ds.child("uid").getValue().toString());
                    try {
                        if ((ds.child("uid").getValue().toString()).equals(mCurrentUser.getUid())) {
                            mDatabaseShop.child(mCurrentUser.getUid()).child("product").child((ds.getKey())).setValue(ds.getValue());
                        }
                    }catch(Exception e){e.printStackTrace();}
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        mDatabaseProduct.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds: dataSnapshot.getChildren())
//                    Log.d("Products",ds.getKey());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                mSelectImage.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d("setupError", error + "");
            }
        }
    }

    }
