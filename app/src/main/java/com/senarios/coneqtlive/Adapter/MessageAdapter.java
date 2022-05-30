package com.senarios.coneqtlive.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.senarios.coneqtlive.Model.MessageModel;
import com.senarios.coneqtlive.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private ArrayList<MessageModel> arrayList = new ArrayList<>();
    private Context context;

    public MessageAdapter(ArrayList<MessageModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_itemviewrecylerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.message.setText(arrayList.get(position).getMessage());
        holder.nameTxt.setText(arrayList.get(position).getName()+ " " + arrayList.get(position).getLastName());
        SharedPreferences preferences = context.getSharedPreferences("apiToken", Context.MODE_PRIVATE);


        if(arrayList.get(position).getImage() !=null && !arrayList.get(position).getImage().isEmpty()) {
            Glide.with(context)
                    .load(arrayList.get(position).getImage()) // image url
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            String test =  arrayList.get(position).getName();
                            String test1 = arrayList.get(position).getLastName();
                            String s = test.substring(0, 1).toUpperCase();
                            String s1 = test1.substring(0, 1).toUpperCase();
                            holder.firstLetter.setText(s + s1);
                            holder.firstLetter.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.personImageView);
            holder.firstLetter.setVisibility(View.GONE);
        } else {
            String test =  arrayList.get(position).getName();
            String test1 = arrayList.get(position).getLastName();
            String s = test.substring(0, 1).toUpperCase();
            String s1 = test1.substring(0, 1).toUpperCase();
            holder.firstLetter.setText(s + s1);
            holder.firstLetter.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTxt ,minuteTxt, heartTxt ,message , firstLetter;
        private ImageView personImageView , heartImageView , shareImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameTextView);
            firstLetter = itemView.findViewById(R.id.idFirstLetter);
            minuteTxt = itemView.findViewById(R.id.minuteTextView);
            heartTxt = itemView.findViewById(R.id.noHeartTextView);
            message = itemView.findViewById(R.id.messageTextView);
            personImageView = itemView.findViewById(R.id.headerImage);
            heartImageView = itemView.findViewById(R.id.heartImage);
            shareImageView = itemView.findViewById(R.id.shareImage);
        }
    }
}

