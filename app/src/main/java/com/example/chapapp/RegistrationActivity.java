package com.example.chapapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chapapp.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class RegistrationActivity extends AppCompatActivity {
    TextView txt_signin;
    CircleImageView profile_image;
    EditText reg_name, reg_pass, reg_email, reg_cpass;
    TextView signup_btn;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri imageUri;
    String imageURI;
    ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        txt_signin = findViewById(R.id.txt_signin);
        reg_name = findViewById(R.id.reg_name);
        reg_pass = findViewById(R.id.reg_pass);
        reg_email = findViewById(R.id.reg_email);
        reg_cpass = findViewById(R.id.reg_cpass);
        profile_image = findViewById(R.id.profile_image);
        signup_btn = findViewById(R.id.signup_btn);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = reg_name.getText().toString();
                String email = reg_email.getText().toString();
                String password = reg_pass.getText().toString();
                String cpassword = reg_cpass.getText().toString();
                String status="Hey There I,m using This Application";
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cpassword)) {

                    Toast.makeText(RegistrationActivity.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {

                    reg_email.setError("Invalid Email");
                    Toast.makeText(RegistrationActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(cpassword)) {

                    Toast.makeText(RegistrationActivity.this, "Password Does not match", Toast.LENGTH_SHORT).show();

                } else if (password.length() < 6) {

                    Toast.makeText(RegistrationActivity.this, "please enter valid password", Toast.LENGTH_SHORT).show();

                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference reference = database.getReference().child("users").child(Objects.requireNonNull(auth.getUid()));
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                             if(imageUri!=null){
                                 storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                     @Override
                                     public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                         if (task.isSuccessful()) {
                                             storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                 @Override
                                                 public void onSuccess(Uri uri) {
                                                      imageURI = uri.toString();
                                                      Users users = new Users(auth.getUid(),name,email,imageURI,status);
                                                      reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<Void> task) {
                                                              if(task.isSuccessful()){

                                                         startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                                                              }
                                                              else{
                                                                  Toast.makeText(RegistrationActivity.this, "Error in creating a new user", Toast.LENGTH_SHORT).show();
                                                              }
                                                          }
                                                      });
                                                 }
                                             });
                                         }
                                     }
                                 });
                             }
                             else
                             {
                             String status="Hey There I,m using This Application";
                                 imageURI = "https://firebasestorage.googleapis.com/v0/b/chapapp-ac57d.appspot.com/o/profile.png?alt=media&token=2b25f848-1717-4f44-91e6-6b1950dc1465";
                                 Users users = new Users(auth.getUid(),name,email,imageURI,status);
                                 reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         if(task.isSuccessful()){
                                             startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                                         }
                                         else{
                                             Toast.makeText(RegistrationActivity.this, "Error in creating a new user", Toast.LENGTH_SHORT).show();
                                         }
                                     }
                                 });
                             }
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 11);
            }
        });


        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            if (data != null) {
                imageUri = data.getData();
                profile_image.setImageURI(imageUri);
            }
        }
    }
}