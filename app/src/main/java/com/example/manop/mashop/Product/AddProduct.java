package com.example.manop.mashop.Product;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.manop.mashop.R;
import com.example.manop.mashop.Shop.Shop;
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

public class AddProduct extends AppCompatActivity {

    private Button mSubmitBtn;
    private static final int GALLERY_REQUEST = 999;
    private ImageButton mSelectImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private EditText mProductPrice;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseShop;
    private ProgressDialog mprogressbar;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUSer;


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
        mDatabaseShop = FirebaseDatabase.getInstance().getReference().child("Product");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mPostTitle = (EditText) findViewById(R.id.editText1);
        mPostDesc = (EditText) findViewById(R.id.editText2);
        mProductPrice = (EditText) findViewById(R.id.editText3);
        mSubmitBtn = (Button) findViewById(R.id.btn);
        mSelectImage = (ImageButton) findViewById(R.id.imageButton2);
    }

    private void clickEvents() {
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

        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri != null) {
            //can post
            // mImageUri= Uri.fromFile(new File(mImageUri.getLastPathSegment()));
            mprogressbar.show();
            StorageReference filepath = mStorageRef.child("Product_Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = mDatabaseShop.push();//cret uniquid
                    mDatabaseUSer.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newPost.child("name").setValue(title_val);
                            newPost.child("description").setValue(desc_val);
                            newPost.child("IMAGE").setValue(downloadUrl.toString());
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("price").setValue(price_val).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);
        }

    }
}