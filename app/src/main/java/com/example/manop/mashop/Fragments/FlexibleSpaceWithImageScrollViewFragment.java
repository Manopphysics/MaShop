/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.manop.mashop.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manop.mashop.Function.AccountSettings;
import com.example.manop.mashop.R;
import com.example.manop.mashop.Startup.SetupActivity;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;

public class FlexibleSpaceWithImageScrollViewFragment extends FlexibleSpaceWithImageBaseFragment<ObservableScrollView> {
    private Button changeName, changePass, logout;
    private TextView seller;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flexiblespacewithimagescrollview, container, false);
        changeName = (Button) view.findViewById(R.id.changeName);
        changePass = (Button) view.findViewById(R.id.changePass);
        logout = (Button) view.findViewById(R.id.flogout);
        seller = (TextView) view.findViewById(R.id.seller);
        mAuth = FirebaseAuth.getInstance();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setup = new Intent(getActivity(), SetupActivity.class);
                startActivity(setup);
            }
        });
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = mAuth.getCurrentUser().getEmail();

                auth.sendPasswordResetEmail( emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(),"Email sent to reset password",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                seller.setText(dataSnapshot.child("seller").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
        // TouchInterceptionViewGroup should be a parent view other than ViewPager.
        // This is a workaround for the issue #117:
        // https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
        scrollView.setTouchInterceptionViewGroup((ViewGroup) view.findViewById(R.id.fragment_root));

        // Scroll to the specified offset after layout
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_SCROLL_Y)) {
            final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
            ScrollUtils.addOnGlobalLayoutListener(scrollView, new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, scrollY);
                }
            });
            updateFlexibleSpace(scrollY, view);
        } else {
            updateFlexibleSpace(0, view);
        }

        scrollView.setScrollViewCallbacks(this);

        return view;
    }

    @Override
    public void updateFlexibleSpace(int scrollY) {
        // Sometimes scrollable.getCurrentScrollY() and the real scrollY has different values.
        // As a workaround, we should call scrollVerticallyTo() to make sure that they match.
        Scrollable s = getScrollable();
        s.scrollVerticallyTo(scrollY);

        // If scrollable.getCurrentScrollY() and the real scrollY has the same values,
        // calling scrollVerticallyTo() won't invoke scroll (or onScrollChanged()), so we call it here.
        // Calling this twice is not a problem as long as updateFlexibleSpace(int, View) has idempotence.
        updateFlexibleSpace(scrollY, getView());
    }

    @Override
    protected void updateFlexibleSpace(int scrollY, View view) {
        ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);

        // Also pass this event to parent Activity
        AccountSettings parentActivity =
                (AccountSettings) getActivity();
        if (parentActivity != null) {
            parentActivity.onScrollChanged(scrollY, scrollView);
        }
    }
}
