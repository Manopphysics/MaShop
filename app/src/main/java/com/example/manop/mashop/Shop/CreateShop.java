package com.example.manop.mashop.Shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.manop.mashop.Chat.User;
import com.example.manop.mashop.R;
import com.example.manop.mashop.Startup.MainActivity;
import com.example.manop.mashop.Startup.SetupActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.List;

public class CreateShop extends AppCompatActivity {
//    private Spinner categorySpinner;
    private EditText phonenum;
    private EditText shopname;
    private EditText shopdesc;
    private StorageReference mStorageRef;
    private Button submitForm;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser currentUser;
    private static final int GALLARY_REQUEST = 1;
    private Uri mImageUri = null;
    private ImageButton profile_image;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);
        bindViews();
        firebaseInit();
        addItemSpinner();
        clickEvents();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLARY_REQUEST && resultCode == RESULT_OK) {
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
                profile_image.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d("setupError", error + "");
            }
        }
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
//        categorySpinner.setAdapter(dataAdapter);
    }
    public void firebaseInit(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shop");
        mDatabase.keepSynced(true);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser() ;
    }
    public void bindViews(){
        mStorageRef = FirebaseStorage.getInstance().getReference().child("ShopImages");
        profile_image = (ImageButton) findViewById(R.id.shopImagebtn);
        submitForm = (Button) findViewById(R.id.save_profile);
        phonenum = (EditText) findViewById(R.id.phonenum);
        shopname = (EditText) findViewById(R.id.shopname);
        shopdesc = (EditText) findViewById(R.id.shopdesc);
//        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        mProgress = new ProgressDialog(this);
    }
    public void clickEvents(){
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLARY_REQUEST);
            }
        });
        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference shopdb = mDatabase.child(currentUser.getUid());
                final String name =shopname.getText().toString().trim();
                final String desc = shopdesc.getText().toString().trim();
                final String user_id = currentUser.getUid();
                if (!TextUtils.isEmpty(name) && mImageUri != null) {
                    mProgress.setMessage("Creating Your Shop.....");
                    mProgress.show();
                    StorageReference filepath = mStorageRef.child(mImageUri.getLastPathSegment());

                    filepath.putFile(mImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    String downloadUri = taskSnapshot.getDownloadUrl().toString();
                                    shopdb.child("name").setValue(name);
                                    shopdb.child("image").setValue(downloadUri);
                                    shopdb.child("description").setValue(desc);
                                    shopdb.child("phone").setValue(phonenum.getText().toString().trim());
                                    mProgress.dismiss();
                                    Toast.makeText(CreateShop.this, "Shop Created Successfully :)", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(CreateShop.this, "FAILED!!", Toast.LENGTH_LONG).show();
                                    mProgress.dismiss();
                                }
                            });
                }

                mDatabaseUsers.child(currentUser.getUid()).child("seller").setValue("true");
                Intent main = new Intent(CreateShop.this, Shop.class);
                startActivity(main);
            }
        });
    }
}
