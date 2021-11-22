package com.example.chapapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapapp.ChatActivity;
import com.example.chapapp.HomeActivity;
import com.example.chapapp.Model.Users;
import com.example.chapapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    Context homeActivity;
    ArrayList<Users> usersArrayList;

    public UserAdapter(Context homeActivity, ArrayList<Users> usersArrayList) {
        this.homeActivity = homeActivity;
        this.usersArrayList = usersArrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Users users=usersArrayList.get(position);
        holder.user_name.setText(users.getName());
        holder.user_status.setText(users.getStatus());
        Picasso.get().load(users.getImageUri()).into(holder.user_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(homeActivity, ChatActivity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("ReciverImage",users.getImageUri());
                intent.putExtra("uid",users.getUid());
                homeActivity.startActivity(intent);

            }
        });

  }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView user_image;
        TextView user_name,user_status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            user_image=itemView.findViewById(R.id.user_image);
            user_name=itemView.findViewById(R.id.user_name);
            user_status=itemView.findViewById(R.id.user_status);
        }
    }
}
