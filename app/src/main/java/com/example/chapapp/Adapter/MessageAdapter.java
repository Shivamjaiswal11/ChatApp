package com.example.chapapp.Adapter;


import static com.example.chapapp.ChatActivity.rImage;
import static com.example.chapapp.ChatActivity.sImage;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapapp.Model.Messages;
import com.example.chapapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messages> messagesArrayList;
    int SEND_item = 1;
    int RECIVER_item = 2;

    public MessageAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SEND_item) {
            View view;
            view = LayoutInflater.from(context).inflate(R.layout.sender_row, parent, false);
            return new SenderViewHolder(view);

        } else {
            View view;
            view = LayoutInflater.from(context).inflate(R.layout.reciver_row, parent, false);
            return new ReciverViewHolder(view);

        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = messagesArrayList.get(position);
        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder ViewHolder = (SenderViewHolder) holder;
            ViewHolder.txtmessage.setText(messages.getMessage());
            Picasso.get().load(sImage).into(ViewHolder.profile_image2);
        } else {

            ReciverViewHolder ViewHolder = (ReciverViewHolder) holder;
            ViewHolder.txtmessage.setText(messages.getMessage());
            Picasso.get().load(rImage).into(ViewHolder.profile_image3);
        }
    }


    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderID())) {
            return SEND_item;
        } else {
            return RECIVER_item;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile_image2;
        TextView txtmessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image2 = itemView.findViewById(R.id.profile_image2);
            txtmessage = itemView.findViewById(R.id.txtmessage);
        }
    }

    class ReciverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile_image3;
        TextView txtmessage;

        public ReciverViewHolder(@NonNull View itemView) {

            super(itemView);
            profile_image3 = itemView.findViewById(R.id.profile_image3);
            txtmessage = itemView.findViewById(R.id.txtmessage);
        }
    }
}
