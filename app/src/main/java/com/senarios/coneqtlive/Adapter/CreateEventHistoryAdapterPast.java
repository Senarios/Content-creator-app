package com.senarios.coneqtlive.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonParseException;
import com.senarios.coneqtlive.Model.Past;
import com.senarios.coneqtlive.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CreateEventHistoryAdapterPast extends RecyclerView.Adapter<CreateEventHistoryAdapterPast.ViewHolder> {
    private ArrayList<Past> arrayList = new ArrayList<>();
    private Context context;
    String dateTime;
    Calendar calendar;
    String formattedDate;
    SimpleDateFormat simpleDateFormat;

    public CreateEventHistoryAdapterPast(ArrayList<Past> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CreateEventHistoryAdapterPast.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.create_eventhistory_past_recylerview, parent, false);
        return new CreateEventHistoryAdapterPast.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.idEventHistoryName.setText(arrayList.get(position).getName());

        dateTime = String.valueOf(arrayList.get(position).getTime());
        formattedDate = "";
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate= null;
        try {
            newDate = spf.parse(dateTime);
            holder.idEventHistoryTime.setText("Time: " + getFormattedDate(newDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.idEventHistoryDescription.setText(arrayList.get(position).getDescription());
        holder.idEventHistoryCost.setText("£" + arrayList.get(position).getTicketPrice());

        try {
            Long time = Long.parseLong(arrayList.get(position).getTimeDuration()) / 60;
            Long mints, hours;

            hours = TimeUnit.MINUTES.toHours(Long.valueOf(time));
            mints = time - TimeUnit.HOURS.toMinutes(hours);
            if (hours<= 0 && mints>=1) {
                holder.idEventHistoryTimeDuration.setText(" | " + mints + "min");
            } else if (mints <= 0 && hours>=1) {
                holder.idEventHistoryTimeDuration.setText(" | " + hours + "h");
            } else if (mints <= 0 && hours>=1) {
                holder.idEventHistoryTimeDuration.setText(" | " + hours + "h");
            } else {
                holder.idEventHistoryTimeDuration.setText(" | " + hours + "h "+mints + "min");
            }

        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        holder.idTravel.setText(arrayList.get(position).getType());

        holder.soldTxt.setText(arrayList.get(position).getTotalTicketPurchased().toString());
        holder.revenueTxt.setText("£" + arrayList.get(position).getTotalEventRevenue());

        Picasso.with(holder.itemView.getContext())
                .load(arrayList.get(position).getImage1S3())
                .centerCrop()
                .placeholder(R.drawable.img)
                .resize(1800, 1800)
                .into(holder.itemImage);
        holder.itemImage.setVisibility(View.VISIBLE);

        holder.number.setText((" " + arrayList.get(position).getRatesCount()));
        holder.ratingTxt.setText(("(" +  arrayList.get(position).getAvgRating() + ")"));
        holder.ratingBar.setRating((float) arrayList.get(position).getAvgRating());
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
        private TextView idEventHistoryName, idEventHistoryTime, idEventHistoryDescription, idEventHistoryCost , pastTextView,
                soldTxt , pastSoldTxt , durationTimer ,revenueTxt , idTravel , idEventHistoryTimeDuration,
                ratingTxt,number, viewerTxt;
        private TextView startTxt, cancelTxt;
        private ImageView imgShare, itemImage;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idEventHistoryName = itemView.findViewById(R.id.idEventHistoryName);
            idEventHistoryTime = itemView.findViewById(R.id.idEventHistoryTime);
            idEventHistoryDescription = itemView.findViewById(R.id.idEventHistoryDescription);
            idEventHistoryCost = itemView.findViewById(R.id.idEventHistoryCost);
            startTxt = itemView.findViewById(R.id.btn);
            startTxt.setVisibility(View.GONE);
            cancelTxt = itemView.findViewById(R.id.idCancelBtnFirstCardView);
            cancelTxt.setVisibility(View.GONE);
            imgShare = itemView.findViewById(R.id.imgShare);
            imgShare.setVisibility(View.GONE);
            itemImage = itemView.findViewById(R.id.idimage);
            pastTextView = itemView.findViewById(R.id.pastTextView);
            pastTextView.setVisibility(View.GONE);
            soldTxt = itemView.findViewById(R.id.idSoldTicketTxt);
            revenueTxt = itemView.findViewById(R.id.idRevenueHistory);
            durationTimer = itemView.findViewById(R.id.durationTimer);
            durationTimer.setVisibility(View.VISIBLE);
            idTravel = itemView.findViewById(R.id.idTravel);
            idEventHistoryTimeDuration = itemView.findViewById(R.id.idEventHistoryTimeDuration);
            ratingBar = itemView.findViewById(R.id.rating);
            ratingTxt = itemView.findViewById(R.id.ratingTxt);
            number = itemView.findViewById(R.id.number);
            viewerTxt = itemView.findViewById(R.id.viewerTxt);

        }
    }
    private String getFormattedDate(Date date)  {
        Calendar cal = Calendar.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        cal.setTime(date);
        //2nd of march 2015
        int day = cal.get(Calendar.DATE);

        switch (day % 10) {
            case 1:
                return new SimpleDateFormat( "hh:mm aa d'st' MMMM yyyy").format(date);
            case 2:
                return new SimpleDateFormat( "hh:mm aa d'nd' MMMM yyyy").format(date);
            case 3:
                return new SimpleDateFormat( "hh:mm aa d'rd' MMMM yyyy").format(date);
            default:
                return new SimpleDateFormat("hh:mm aa d'th' MMMM yyyy").format(date);
        }

    }
}
