package com.example.manop.mashop.Users.add;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

/**
 * Author:
 * Created on: 8/28/2016 , 11:06 AM
 * Project: FirebaseChat
 */

public interface AddUserContract {
    interface View {
        void onAddUserSuccess(String message);

        void onAddUserFailure(String message);
    }

    interface Presenter {
        void addUser(Context context, FirebaseUser firebaseUser);
    }

    interface Interactor {
        void addUserToDatabase(Context context, FirebaseUser firebaseUser);
    }

    interface OnUserDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
