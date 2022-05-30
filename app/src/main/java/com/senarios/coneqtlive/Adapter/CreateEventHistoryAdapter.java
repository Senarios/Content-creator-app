package com.senarios.coneqtlive.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonParseException;
import com.senarios.coneqtlive.Model.Link;
import com.senarios.coneqtlive.Model.Upcoming;
import com.senarios.coneqtlive.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CreateEventHistoryAdapter extends RecyclerView.Adapter<CreateEventHistoryAdapter.ViewHolder> {
    private ArrayList<Upcoming> arrayList = new ArrayList<>();
    private Context context;
    String dateTime;
    String EventTime;
    String conv;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String formattedDate;
    private StartEvent startEvent;

    public CreateEventHistoryAdapter(ArrayList<Upcoming> arrayList, Context context, StartEvent startEvent) {
        this.arrayList = arrayList;
        this.context = context;
        this.startEvent = startEvent;
    }

    public interface StartEvent {
        void doStart(String id, String time);

        void doCancel(String id , String name);

        void doShare(Link Link);
    }


    @NonNull
    @Override
    public CreateEventHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.create_eventhistory_recylerview, parent, false);
        return new CreateEventHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.idEventHistoryName.setText(arrayList.get(position).getName());

        SharedPreferences.Editor editor = context.getSharedPreferences("apiToken", MODE_PRIVATE).edit();
        editor.putString("idName", arrayList.get(position).getName());
        editor.apply();

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
        editor.putString("idDescription", arrayList.get(position).getDescription());
        editor.apply();

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

        holder.idEventHistoryCost.setText("£" + arrayList.get(position).getTicketPrice());

        holder.idTravel.setText(arrayList.get(position).getType());


        holder.soldTxt.setText(arrayList.get(position).getTotalTicketPurchased().toString());
        holder.pastSoldTxt.setText("("+ arrayList.get(position).getLastHourTicketPurchased().toString() + " in the past hr)");
        holder.revenueTxt.setText("£" + arrayList.get(position).getTotalEventRevenue());


        Picasso.with(holder.itemView.getContext())
                .load(arrayList.get(position).getImage1S3())
                .centerCrop()
                .placeholder(R.drawable.img)
                .resize(1800, 1800)
                .into(holder.itemImage);
        holder.itemImage.setVisibility(View.VISIBLE);

        holder.btnStart.setOnClickListener(v -> {
            startEvent.doStart(String.valueOf(arrayList.get(position).getId()), arrayList.get(position).getTime());
            editor.putInt("idEvent", arrayList.get(position).getId());
            editor.apply();
        });
        holder.idCancelBtnFirstCardView.setOnClickListener(v -> {
            startEvent.doCancel(String.valueOf(arrayList.get(position).getId()),arrayList.get(position).getName());
        });
        holder.imgShare.setOnClickListener(v -> {
            startEvent.doShare(arrayList.get(position).getLink());
        });

        holder.ratingTxt.setText(("(" +  arrayList.get(position).getAvgRating() + ")"));
        holder.ratingBar.setRating((float) arrayList.get(position).getAvgRating());

        /////////////////
        EventTime = String.valueOf(arrayList.get(position).getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = new Date(System.currentTimeMillis());
        Date date2 = null;
        try {
            date2 = df.parse(EventTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = date2.getTime() - date1.getTime();
        if(diff>0) {
            new CountDownTimer(diff, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    holder.durationTimer.setText(String.format("%02d:%02d:%02d ", hours %24 , minutes%60, seconds%60));

                }
                public void onFinish() {
                }
            }.start();
            Log.wtf("message", String.valueOf(diff));
        } else {
            holder.durationTimer.setText("00:00:00");
        }
        /////////////


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
        private TextView idEventHistoryName, idEventHistoryTime, idEventHistoryDescription, idEventHistoryCost, btnStart,
                idCancelBtnFirstCardView , durationTimer, soldTxt , pastSoldTxt , revenueTxt , idTravel ,
                idEventHistoryTimeDuration, ratingTxt,number, viewerTxt ;
        private ImageView imgShare, itemImage;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idEventHistoryName = itemView.findViewById(R.id.idEventHistoryName);
            idEventHistoryTime = itemView.findViewById(R.id.idEventHistoryTime);
            idEventHistoryDescription = itemView.findViewById(R.id.idEventHistoryDescription);
            idEventHistoryCost = itemView.findViewById(R.id.idEventHistoryCost);
            btnStart = itemView.findViewById(R.id.btn);
            idCancelBtnFirstCardView = itemView.findViewById(R.id.idCancelBtnFirstCardView);
            imgShare = itemView.findViewById(R.id.imgShare);
            itemImage = itemView.findViewById(R.id.idimage);

            soldTxt = itemView.findViewById(R.id.idSoldTicketTxt);
            pastSoldTxt = itemView.findViewById(R.id.pastSoldTextView);
            revenueTxt = itemView.findViewById(R.id.idRevenueHistory);
            idTravel = itemView.findViewById(R.id.idTravel);
            idEventHistoryTimeDuration = itemView.findViewById(R.id.idEventHistoryTimeDuration);
            durationTimer = itemView.findViewById(R.id.durationTimer);

            ratingBar = itemView.findViewById(R.id.rating);
            ratingTxt = itemView.findViewById(R.id.ratingTxt);
            number = itemView.findViewById(R.id.number);
            number.setVisibility(View.INVISIBLE);
            viewerTxt = itemView.findViewById(R.id.viewerTxt);
            viewerTxt.setVisibility(View.INVISIBLE);
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

