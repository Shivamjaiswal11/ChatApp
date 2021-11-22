package com.example.chapapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chapapp.Adapter.UserAdapter;
import com.example.chapapp.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView RV;
    UserAdapter userAdapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView logout;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RV = findViewById(R.id.RV);
        logout=findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersArrayList= new ArrayList<>();


        DatabaseReference reference=database.getReference().child("users");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Users users =dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);

                }
           userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        RV.setLayoutManager(new LinearLayoutManager(this));
        userAdapter=new UserAdapter(HomeActivity.this,usersArrayList);
        RV.setAdapter(userAdapter);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 dialog=new Dialog(HomeActivity.this,R.style.Dialog);
                dialog.setContentView(R.layout.dialog_background);
                TextView yesbtn,nobtn;
                yesbtn=dialog.findViewById(R.id.yesbtn);
                nobtn=dialog.findViewById(R.id.nobtn);

                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this,RegistrationActivity.class));
                    }
                });

                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });


        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));
        }
    }
}