package com.example.manop.mashop.Chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manop.mashop.R;
import com.example.manop.mashop.Startup.LoginActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivityOld extends AppCompatActivity {

    EditText input;
    RecyclerView chatRecView;
    DatabaseReference dbChatRef;
    private String shopuid;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_old);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        FloatingActionButton fab = findViewById(R.id.fab);
        shopuid = getIntent().getExtras().getString("shopuid","");
        chatRecView = findViewById(R.id.list_of_messages);
        dbChatRef = FirebaseDatabase.getInstance().getReference("/chat");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(false);
        chatRecView.setHasFixedSize(true);
        chatRecView.setLayoutManager(layoutManager);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        if (mCurrentUser == null) {
            startActivity(new Intent(ChatActivityOld.this, LoginActivity.class));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabClick();
            }
        });
    }
    private String setOneToOneChat(String uid1, String uid2)
    {
//Check if user1’s id is less than user2's
        if(uid1.compareTo(uid2) < 0) return uid1+"+"+uid2;
        else return uid2+"+"+uid1;
    }
    private void fabClick() {
        if (mCurrentUser == null) {
            Toast.makeText(getApplicationContext(), "Not logged in!", Toast.LENGTH_SHORT).show();
            onStart();
        } else {
            input = findViewById(R.id.input);
            // Read the input field and push a new instance
            // of ChatMessage to the Firebase database
            String message = input.getText().toString();
            if (message.isEmpty()) {
                input.setError("You can't post an empty Message. !!");
            } else {
                if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() == null) {
                    String ui = mCurrentUser.getUid();
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("chat").child(setOneToOneChat(shopuid,ui))
                            .push()
                            .setValue(new ChatMessage(
                                    input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getEmail(),
                                    "null")
                            );
                    Log.d("abcdabcd", String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()));
                    // Clear the input
                } else {
                    String ui = mCurrentUser.getUid();
                    Log.d("shopuid",shopuid);
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("chat").child(setOneToOneChat(shopuid,ui))
                            .push()
                            .setValue(new ChatMessage(
                                    input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getEmail(),
                                    FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString())
                            );
                }
            }
            input.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        final FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder>(
                        ChatMessage.class,
                        R.layout.message_row,
                        ChatViewHolder.class,
                        dbChatRef.child(setOneToOneChat(shopuid,
                                mCurrentUser.getUid()))) {
                    @Override
                    protected void populateViewHolder(ChatViewHolder viewHolder, ChatMessage model, int position) {
                        final String chatKey = getRef(position).getKey();

                        viewHolder.setMessageText(model.getMessageText());
                        viewHolder.setMessageTime(model.getMessageTime());
                        viewHolder.setUserName(model.getMessageUser());
                        viewHolder.setUserProfileImage(model.getProfileUrl(), getApplicationContext());
                    }
                };
        chatRecView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView messageTime;
        TextView messageText;
        CircleImageView userProfileImage;

        public ChatViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.message_user);
            messageTime = itemView.findViewById(R.id.message_time);
            messageText = itemView.findViewById(R.id.message_text);
            userProfileImage = itemView.findViewById(R.id.profile_image);
        }

        public void setUserName(String usr) {
            userName.setText(usr);
        }

        public void setMessageTime(long time) {
            messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                    time));
        }

        public void setMessageText(String message) {
            messageText.setText(message);
        }

        public void setUserProfileImage(String profile_url, Context mctx) {
            if (profile_url == "null"){
                userProfileImage.setImageResource(R.drawable.one);
            }else{
                Picasso.with(mctx)
                        .load(profile_url)
                        .into(userProfileImage);
            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

}
