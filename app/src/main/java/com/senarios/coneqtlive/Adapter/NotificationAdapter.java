package com.senarios.coneqtlive.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.senarios.coneqtlive.Model.Earlier;
import com.senarios.coneqtlive.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Earlier> arrayList = new ArrayList<>();
    private Context context;
    String dateTime;
    String formattedDate;


    public NotificationAdapter(List<Earlier> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notication_today_itemlist, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        dateTime = (arrayList.get(position).getCreatedAt());
        formattedDate = "";
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM d ");
            Date date = sdf1.parse(dateTime);
            formattedDate = sdf2.format(date);
            holder.dateTxt.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.titleTxt1.setText(arrayList.get(position).getTitle());
        holder.title1Des.setText(arrayList.get(position).getBody());

        if (arrayList.get(position).getType().equals("event")) {
            holder.dateTxt.setVisibility(View.VISIBLE);
            holder.titleTxt1.setVisibility(View.VISIBLE);
            holder.title1Des.setVisibility(View.VISIBLE);
          holder.itemImageView.setImageResource(R.drawable.ic_notification_burning);
        }
        if (arrayList.get(position).getType().equals("balance_info")) {
            holder.dateTxt.setVisibility(View.VISIBLE);
            holder.titleTxt1.setVisibility(View.VISIBLE);
          holder.itemImageView.setImageResource(R.drawable.ic_notification_dollar);

        }
        if (arrayList.get(position).getType().equals("join_event")) {
            holder.dateTxt.setVisibility(View.VISIBLE);
            holder.titleTxt1.setVisibility(View.VISIBLE);
          holder.itemImageView.setImageResource(R.drawable.ic_notification_swing);
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
        private LinearLayout linearLayout, linearLayout1, linearLayout2;
        private TextView dateTxt, date1Txt, date2Txt, title1Des, titleTxt1, titleTxt2, titleTxt3;
        private ImageView itemImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ///////// Linear layout
            linearLayout = itemView.findViewById(R.id.idLinearLayout);
            ///////// date TextView
            dateTxt = itemView.findViewById(R.id.idDateLinear);
            ///////// Title TextView
            titleTxt1 = itemView.findViewById(R.id.idDateLinearTxt);
            ////////Title des
            title1Des = itemView.findViewById(R.id.idDateLinearDes);
            /////// ImageView Setup
            itemImageView = itemView.findViewById(R.id.itemImageView);


        }
    }
}
