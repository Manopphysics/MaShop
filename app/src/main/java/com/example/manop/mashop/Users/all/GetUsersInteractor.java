package com.example.manop.mashop.Users.all;

import android.text.TextUtils;
import android.util.Log;


import com.example.manop.mashop.Chat.User;
import com.example.manop.mashop.Utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author:
 * Created on: 9/2/2016 , 10:08 PM
 * Project: FirebaseChat
 */

public class GetUsersInteractor implements GetUsersContract.Interactor {
    private static final String TAG = "GetUsersInteractor";

    private GetUsersContract.OnGetAllUsersListener mOnGetAllUsersListener;

    public GetUsersInteractor(GetUsersContract.OnGetAllUsersListener onGetAllUsersListener) {
        this.mOnGetAllUsersListener = onGetAllUsersListener;
    }


    @Override
    public void getAllUsersFromFirebase() {
        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<User> users = new ArrayList<>();
                Log.d("seller",dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("seller").toString());
                if((dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("seller").getValue().toString().equals("false"))) {
                    Log.d("seller", "false out loop");
                    while (dataSnapshots.hasNext()) {
                        DataSnapshot dataSnapshotChild = dataSnapshots.next();

                        Log.d("seller", "false in loop");
                        if (dataSnapshotChild.child("seller").getValue().toString().equals("true")) {
                            Log.d("seller", "false: true");
                            User user = dataSnapshotChild.getValue(User.class);
                            if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                Log.d("seller", "added 1");
                                users.add(user);
                            }
                        }
                    }
                }
                    if((dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("seller").getValue().toString().equals("true")))
                    {
                        while (dataSnapshots.hasNext()) {
                            DataSnapshot dataSnapshotChild = dataSnapshots.next();

                            Log.d("seller", "true");
                            if (dataSnapshotChild.child("seller").getValue().toString().equals("false")) {
                                Log.d("seller", "true: false");
                                User user = dataSnapshotChild.getValue(User.class);
                                if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    Log.d("seller", "added 2");
                                    users.add(user);
                                }
                            }
                        }
                    }
                    Log.d("seller","NOTHING HAPPENED");
                mOnGetAllUsersListener.onGetAllUsersSuccess(users);
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetAllUsersListener.onGetAllUsersFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getChatUsersFromFirebase() {
        /*FirebaseDatabase.getInstance().getReference().child(Constants.ARG_CHAT_ROOMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots=dataSnapshot.getChildren().iterator();
                List<User> users=new ArrayList<>();
                while (dataSnapshots.hasNext()){
                    DataSnapshot dataSnapshotChild=dataSnapshots.next();
                    dataSnapshotChild.getRef().
                    Chat chat=dataSnapshotChild.getValue(Chat.class);
                    if(chat.)4
                    if(!TextUtils.equals(user.uid,FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}
