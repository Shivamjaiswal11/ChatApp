package com.example.chapapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chapapp.Adapter.MessageAdapter;
import com.example.chapapp.Model.Messages;
import com.example.chapapp.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {

    TextView recivename;
    CircleImageView profile_pic;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
ImageView sendbtn;
EditText messageedt;
public static String sImage;
public static String rImage;
RecyclerView messageRV;
ArrayList<Messages> messagesArrayList;
MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String Senderuid=firebaseAuth.getUid();
        String Recivername = getIntent().getStringExtra("name");
        String Reciver_Image = getIntent().getStringExtra("ReciverImage");
        String Reciveruid = getIntent().getStringExtra("uid");

        String senderroom=Senderuid+Reciveruid;
        String reciverroom=Reciveruid+Senderuid;


        recivename = findViewById(R.id.recivename);
        messageedt = findViewById(R.id.messageedt);
        sendbtn = findViewById(R.id.sendbtn);
        messageRV=findViewById(R.id.messageRV);
        profile_pic = findViewById(R.id.profile_image1);

        messagesArrayList= new ArrayList<>();

        recivename.setText(Recivername);
        Picasso.get().
                load(Reciver_Image)
                .into(profile_pic);

        DatabaseReference reference=database.getReference().child("users").child(firebaseAuth.getUid());
        DatabaseReference chatreference=database.getReference().child("chats").child(senderroom).child("messages");

        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Messages messages=dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);

                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
          reference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  sImage=snapshot.child("imageUri").getValue().toString();
                  rImage=Reciver_Image;
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=messageedt.getText().toString();
                if(message.isEmpty()){
                    Toast.makeText(ChatActivity.this, "Please enter valid message", Toast.LENGTH_SHORT).show();
                    return;
                }
                messageedt.setText("");
                Date date= new Date();
                Messages messages=new Messages(message,Senderuid,date.getTime());

                database.getReference().child("chats")
                        .child(reciverroom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        database.getReference().child("chats")
                                .child(senderroom)
                                .child("messages")
                                .push()
                                .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });
   LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
   linearLayoutManager.setStackFromEnd(true);
   messageRV.setLayoutManager(linearLayoutManager);
   messageAdapter=new MessageAdapter(ChatActivity.this,messagesArrayList);
   messageRV.setAdapter(messageAdapter);


    }
}