package com.senarios.coneqtlive.Views.OverView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonParseException;
import com.opencsv.CSVWriter;
import com.senarios.coneqtlive.ApiServices;
import com.senarios.coneqtlive.Model.CancelEvent;
import com.senarios.coneqtlive.Model.CheckingStripeAccount;
import com.senarios.coneqtlive.Model.CreatorFilter;
import com.senarios.coneqtlive.Model.DownloadCsv;
import com.senarios.coneqtlive.Model.DownloadDatum;
import com.senarios.coneqtlive.Model.Link;
import com.senarios.coneqtlive.Model.NotificationCountModel;
import com.senarios.coneqtlive.Model.OverViewData;
import com.senarios.coneqtlive.Model.OverViewModel;
import com.senarios.coneqtlive.Model.StartEventModel;
import com.senarios.coneqtlive.Model.TopThree;
import com.senarios.coneqtlive.Model.UnBlockUser;
import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.ConeqtCreator.BroadCastingEventCreatorActivity;
import com.senarios.coneqtlive.Views.ConeqtCreator.CreateEventActivity;
import com.senarios.coneqtlive.Views.ConeqtCreator.CreateEventHistoryActivity;
import com.senarios.coneqtlive.Views.Notification.NotificationActivity;
import com.senarios.coneqtlive.Views.Payout.PayoutActivity;
import com.senarios.coneqtlive.Views.Settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.senarios.coneqtlive.Views.TouchBlackHoleView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.internal.authenticator.JavaNetAuthenticator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OverViewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView createEvent;
    private ImageView notification;
    private TextView nameTextView ,month , firstLetter;
    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    /////////////Second ItemView
    private TextView eventText, revenueTxt, soldTxt, refundTxt, nameTxt, timeTxt, descriptionTxt, costTxt;
    private ImageView headerImage, itemImage;
    private LinearLayout linearLayout , idLinearTop;
    String formattedDate, dateTime, count, title, body;
    int stat;
    int filter;
    int num;

    String EventTime;
    String conv;

    private TextView ticketSoldTxt , pastHourTicketTxt , durationTimer ;
    private TextView idTravel , idEventHistoryTimeDuration;
    ////////////

    /////Top Three Earning
    private TextView firstNameTxt, secondNameTxt, thirdNameTxt, firstTicketTxt, secondTicketTxt, thirdTicketTxt,
            firstRevenueTxt, secondRevenueTxt, thirdRevenueTxt;
    private ImageView firstImage, secondImage, thirdImage;
    private LinearLayout firstLayout, secondLayout, thirdLayout;
    private CardView topCardView;
    TouchBlackHoleView blackHoleView;
    private TextView totalRevenueTxt;
    private TextView startBtn , cancelBtn;
    private ImageView imgShare , idDownload;

    public static final String CHANNEL_ID = "exampleChannel";

    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view);

        notificationManager = NotificationManagerCompat.from(this);

        initLayout();
        getOverViewList();
        createNotificationChannel();

        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signInValue = preferences.getString("signedInKey", "abc");
        String image = preferences.getString("imageName", "");
        String idUser = preferences.getString("first_name", "abc");
        String LastUser = preferences.getString("last_name", "abc");
        if (signInValue.equals("Yes")) {
            nameTextView.setText(idUser);

            if(!image.isEmpty() && image !=null) {
                Glide.with(this)
                        .load(image) // image url
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                String test = idUser;
                                String test1 = LastUser;
                                String s=test.substring(0,1).toUpperCase();
                                String s1=test1.substring(0,1).toUpperCase();
                                firstLetter.setText(s+ s1);
                                firstLetter.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(headerImage);
                firstLetter.setVisibility(View.GONE);
            } else {
                String test = idUser;
                String test1 = LastUser;
                String s=test.substring(0,1).toUpperCase();
                String s1=test1.substring(0,1).toUpperCase();
                firstLetter.setText(s+ s1);
                firstLetter.setVisibility(View.VISIBLE);
            }
        }
        getNotificationCount();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void initLayout() {
        createEvent = findViewById(R.id.createevent);
        notification = findViewById(R.id.notification_image);
        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, R.layout.selected_month);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(4);

        nameTextView = findViewById(R.id.nameTextView);
        month = findViewById(R.id.month);
        firstLetter = findViewById(R.id.idFirstLetter);

        headerImage = findViewById(R.id.headerImage);

        itemImage = findViewById(R.id.idimage);

        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);

        eventText = findViewById(R.id.idEventTxt);
        revenueTxt = findViewById(R.id.idRevenueTxt);
        soldTxt = findViewById(R.id.idSoldTxt);
        refundTxt = findViewById(R.id.idRefundTxt);

        totalRevenueTxt = findViewById(R.id.idRevenueHistory);

        nameTxt = findViewById(R.id.idEventName);
        timeTxt = findViewById(R.id.idEventTime);
        descriptionTxt = findViewById(R.id.idEventDescription);
        costTxt = findViewById(R.id.idEventCost);
        linearLayout = findViewById(R.id.linearLayout);
        idLinearTop = findViewById(R.id.idLinearTop);

        firstLayout = findViewById(R.id.idFirstLayout);
        secondLayout = findViewById(R.id.idSecondLayout);
        thirdLayout = findViewById(R.id.idThirdLayout);

        ////////Images
        firstImage = findViewById(R.id.idFirstImage);
        secondImage = findViewById(R.id.idSecondImage);
        thirdImage = findViewById(R.id.idThirdImage);

        ///////Names
        firstNameTxt = findViewById(R.id.idFirstName);
        secondNameTxt = findViewById(R.id.idSecondName);
        thirdNameTxt = findViewById(R.id.idThirdName);

        /////// TicketPrice
        firstTicketTxt = findViewById(R.id.idFirstTicket);
        secondTicketTxt = findViewById(R.id.idSecondTicket);
        thirdTicketTxt = findViewById(R.id.idThirdTicket);

        ////// Revenue
        firstRevenueTxt = findViewById(R.id.idFirstRevenue);
        secondRevenueTxt = findViewById(R.id.idSecondRevenue);
        thirdRevenueTxt = findViewById(R.id.idThirdRevenue);

        ticketSoldTxt = findViewById(R.id.idSoldTicketTxt);
        pastHourTicketTxt = findViewById(R.id.pastSoldTextView);
        durationTimer = findViewById(R.id.durationTimer);

        topCardView = findViewById(R.id.eventCardView);

        idTravel = findViewById(R.id.idTravel);
        idEventHistoryTimeDuration = findViewById(R.id.idEventHistoryTimeDuration);

        startBtn = findViewById(R.id.btn);
        cancelBtn = findViewById(R.id.idCancelBtnFirstCardView);
        imgShare = findViewById(R.id.imgShare);
        idDownload = findViewById(R.id.idDownload);
        allClickListener();

    }

    private void allClickListener() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.overview_bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.idOverView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.idOverView:
//                        startActivity(new Intent(OverViewActivity.this, OverViewActivity.class));
//                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idCreatteEvent:
                        startActivity(new Intent(OverViewActivity.this, CreateEventHistoryActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idPayouts:
                        startActivity(new Intent(OverViewActivity.this, PayoutActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idSettings:
                        startActivity(new Intent(OverViewActivity.this, SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        createEvent.setOnClickListener(v -> {
//            startActivity(new Intent(OverViewActivity.this, CreateEventActivity.class));
            getStripeCheck();
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverViewActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        idDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                getDownloadEvent();
            }
        });
    }

    private void shareDialog(String toShow,String toOpen) {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView = layoutInflater.inflate(R.layout.share_popup, null, false);
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        final PopupWindow filterPopup = new PopupWindow(inflatedView, width, height, true);
        filterPopup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        filterPopup.setOutsideTouchable(false);
        filterPopup.setOutsideTouchable(true);
        filterPopup.showAtLocation(inflatedView, Gravity.CENTER, 0, 0);
        filterPopup.getContentView().setFocusableInTouchMode(true);
        filterPopup.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    filterPopup.dismiss();
                    return true;
                }
                return false;
            }
        });

        ImageView twitter = inflatedView.findViewById(R.id.idTwitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharingToSocialMedia("com.twitter.android",toOpen);
            }
        });
        ImageView linkedin = inflatedView.findViewById(R.id.idLinkedin);
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharingToSocialMedia("com.linkedin.android",toOpen);
            }
        });
        ImageView faceBook = inflatedView.findViewById(R.id.idFacebook);
        faceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharingToSocialMedia("com.facebook.katana",toOpen);
            }
        });



        TextView t00Show = inflatedView.findViewById(R.id.toShowLink);
        t00Show.setText(toShow);


        ImageView cancelBtn = inflatedView.findViewById(R.id.close);

        Button copyBtn = inflatedView.findViewById(R.id.copy_link);

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("EditText", toOpen.trim());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(OverViewActivity.this, "Copied", Toast.LENGTH_SHORT).show();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
            }
        });

    }

    private void getStripeCheck() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/stripe/")
                .addConverterFactory(GsonConverterFactory.create(
                        gson)).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Call<CheckingStripeAccount> call = apiPost.getStripeCheck("Bearer " + signinValue);
        call.enqueue(new retrofit2.Callback<CheckingStripeAccount>() {
            @Override
            public void onResponse(Call<CheckingStripeAccount> call, Response<CheckingStripeAccount> response) {
                if (response.isSuccessful()) {
                    Log.e("CreateEvent", new Gson().toJson(response.body()));
                    CheckingStripeAccount checkingStripeAccount = response.body();

                    if (checkingStripeAccount.getSuccess()==true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        startActivity(new Intent(OverViewActivity.this, CreateEventActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Please first connect with stripe account to create event", Toast.LENGTH_SHORT).show();
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                }
            }

            @Override
            public void onFailure(Call<CheckingStripeAccount> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void getOverViewList() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/")
                .addConverterFactory(GsonConverterFactory.create(
                        gson)).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Call<OverViewModel> call = apiPost.getOverViewList("Bearer " + signinValue);
        call.enqueue(new retrofit2.Callback<OverViewModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<OverViewModel> call, Response<OverViewModel> response) {
                if (response.isSuccessful()) {
                    Log.e("OverViewList", new Gson().toJson(response.body()));
                    OverViewModel res = response.body();
                    if (res.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        assert response.body() != null;
                        eventText.setText(response.body().getData().getTotalEvents().toString().trim());
                        revenueTxt.setText("£" + response.body().getData().getRevenue());
                        soldTxt.setText(response.body().getData().getTickets().toString().trim());
                        refundTxt.setText("£" + response.body().getData().getRefunds());

                        ///////////////////
                        if (response.body().getData().getOverViewUpcoming().size() > 0) {
                            linearLayout.setVisibility(View.VISIBLE);
                            nameTxt.setText(response.body().getData().getOverViewUpcoming().get(0).getName());

                            dateTime = String.valueOf(response.body().getData().getOverViewUpcoming().get(0).getTime());
                            formattedDate = "";
                            SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date newDate= null;
                            try {
                                newDate = spf.parse(dateTime);
                                timeTxt.setText("Time: " + getFormattedDate(newDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            idTravel.setText(response.body().getData().getOverViewUpcoming().get(0).getType());
                            ///////////
                            try {
                                Long time = Long.parseLong(response.body().getData().getOverViewUpcoming().get(0).getTimeDuration()) / 60;
                                Long mints, hours;

                                hours = TimeUnit.MINUTES.toHours(Long.valueOf(time));
                                mints = time - TimeUnit.HOURS.toMinutes(hours);
                                if (hours<= 0 && mints>=1) {
                                    idEventHistoryTimeDuration.setText(" | " + mints + "min ");
                                } else if (mints <= 0 && hours>=1) {
                                    idEventHistoryTimeDuration.setText(" | " + hours + "h ");
                                } else if (mints <= 0 && hours>=1) {
                                    idEventHistoryTimeDuration.setText(" | " + hours + "h ");
                                } else {
                                    idEventHistoryTimeDuration.setText(" | " + hours + "h "+mints + "min ");
                                }

                            } catch (JsonParseException e) {
                                e.printStackTrace();
                            }
                            //////////

                            startBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getStartEvent(response.body().getData().getOverViewUpcoming().get(0).getId().toString());
                                }
                            });

                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CancelStream(response.body().getData().getOverViewUpcoming().get(0).getId().toString());
                                }
                            });

                            imgShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    shareDialog(response.body().getData().getOverViewUpcoming().get(0).getLink().getToShow(),response.body().getData().getOverViewUpcoming().get(0).getLink().getToOpen());

                                }
                            });

                            /////////////////
                            EventTime = String.valueOf(response.body().getData().getOverViewUpcoming().get(0).getTime());
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
                                        durationTimer.setText(String.format("%02d:%02d:%02d ", hours %24 , minutes%60, seconds%60));
                                    }
                                    public void onFinish() {
                                    }
                                }.start();
                                Log.wtf("message", String.valueOf(diff));
                            } else {
                                durationTimer.setText("00:00:00");
                            }

                            /////////////
                            descriptionTxt.setText(response.body().getData().getOverViewUpcoming().get(0).getDescription());
                            costTxt.setText("£" + response.body().getData().getOverViewUpcoming().get(0).getTicketPrice().toString());
                            totalRevenueTxt.setText("£" + response.body().getData().getOverViewUpcoming().get(0).getTotalEventRevenue());

                            ticketSoldTxt.setText( response.body().getData().getOverViewUpcoming().get(0).getTotalTicketPurchased().toString());
                            pastHourTicketTxt.setText("(" + response.body().getData().getOverViewUpcoming().get(0).getLastHourTicketPurchased().toString() + " in the past hr)");

                            Picasso.with(getApplicationContext())
                                    .load(response.body().getData().getOverViewUpcoming().get(0).getImage1S3())
                                    .centerCrop()
                                    .placeholder(R.drawable.img)
                                    .resize(1800, 1800)
                                    .into(itemImage);

                        } else {
                            linearLayout.setVisibility(View.GONE);
                        }
                        if (response.body().getData().getTopThree().size() > 0) {

                            idLinearTop.setVisibility(View.VISIBLE);

                            firstLayout.setVisibility(View.VISIBLE);
                            topCardView.setVisibility(View.VISIBLE);
                            firstNameTxt.setText(response.body().getData().getTopThree().get(0).getName().trim());
                            firstTicketTxt.setText(response.body().getData().getTopThree().get(0).getEventPaymentsAndVerificationCount().toString());
                            firstRevenueTxt.setText("£" + response.body().getData().getTopThree().get(0).getEventPaymentsAndVerificationSumTicketPrice());
                            Picasso.with(getApplicationContext())
                                    .load(response.body().getData().getTopThree().get(0).getImage1S3())
                                    .centerCrop()
                                    .placeholder(R.drawable.img)
                                    .resize(96, 58)
                                    .into(firstImage);
                        } else {
                            idLinearTop.setVisibility(View.GONE);
                            firstLayout.setVisibility(View.GONE);
                            topCardView.setVisibility(View.GONE);
                        }
                        if (response.body().getData().getTopThree().size() > 1) {

                            secondLayout.setVisibility(View.VISIBLE);
                            secondNameTxt.setText(response.body().getData().getTopThree().get(1).getName().trim());
                            secondTicketTxt.setText(response.body().getData().getTopThree().get(1).getEventPaymentsAndVerificationCount().toString());
                            secondRevenueTxt.setText("£" +response.body().getData().getTopThree().get(1).getEventPaymentsAndVerificationSumTicketPrice());
                            Picasso.with(getApplicationContext())
                                    .load(response.body().getData().getTopThree().get(1).getImage1S3())
                                    .centerCrop()
                                    .placeholder(R.drawable.img)
                                    .resize(96, 58)
                                    .into(secondImage);
                        } else {
                            secondLayout.setVisibility(View.GONE);
                        }
                        if (response.body().getData().getTopThree().size() > 2) {
                            thirdLayout.setVisibility(View.VISIBLE);
                            thirdNameTxt.setText(response.body().getData().getTopThree().get(2).getName().trim());
                            thirdTicketTxt.setText(response.body().getData().getTopThree().get(2).getEventPaymentsAndVerificationCount().toString());
                            thirdRevenueTxt.setText("£" + response.body().getData().getTopThree().get(2).getEventPaymentsAndVerificationSumTicketPrice());
                            Picasso.with(getApplicationContext())
                                    .load(response.body().getData().getTopThree().get(2).getImage1S3())
                                    .centerCrop()
                                    .placeholder(R.drawable.img)
                                    .resize(96, 58)
                                    .into(thirdImage);
                        } else {
                            thirdLayout.setVisibility(View.GONE);
                        }

                    } else {
                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<OverViewModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void getFilter( ) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/overview/event/stats/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        String stating = preferences.getString("filtering", "abc");
        if (stating.equals("Today")) {
            stat = 1;
        } else if (stating.equals("This week")) {
            stat = 2;
        } else if (stating.equals("This Month")) {
            stat = 3;
        } else if (stating.equals("This Year")) {
            stat = 4;
        } else if (stating.equals("All")) {
            stat = 5;
        }

        Call<CreatorFilter> call = apiPost.getFilterList("Bearer " + signinValue, stat);
        call.enqueue(new retrofit2.Callback<CreatorFilter>() {
            @Override
            public void onResponse(Call<CreatorFilter> call, Response<CreatorFilter> response) {
                if (response.isSuccessful()) {
                    CreatorFilter resObj = response.body();
                    if (resObj.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Log.e("FilterList", new Gson().toJson(response.body()));
                        eventText.setText(response.body().getTotalEvents().toString().trim());
                        revenueTxt.setText("£" + response.body().getRevenue());
                        soldTxt.setText(response.body().getTickets().toString().trim());
                        refundTxt.setText("£" + response.body().getRefunds());
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CreatorFilter> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });
    }

    private void getStartEvent(String id) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");

        Call<StartEventModel> call = apiPost.getStartEvent("Bearer " + signinValue, Integer.valueOf(id));
        call.enqueue(new retrofit2.Callback<StartEventModel>() {
            @Override
            public void onResponse(Call<StartEventModel> call, Response<StartEventModel> response) {
                if (response.isSuccessful()) {
                    StartEventModel resObj = response.body();

                    if (resObj.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Intent intent = new Intent(OverViewActivity.this, BroadCastingEventCreatorActivity.class);
                        intent.putExtra("Id", resObj.getData().getId().toString());
                        intent.putExtra("Agora_Token", resObj.getData().getAgoraToken().toString());
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Successfully StartEvent", Toast.LENGTH_SHORT).show();

                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(OverViewActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(OverViewActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<StartEventModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });

    }

    private void CancelStream(String Id) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/stripe/payout/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Call<CancelEvent> call = apiPost.getCancelEvent("Bearer " + signinValue , Integer.valueOf(Id));
        call.enqueue(new retrofit2.Callback<CancelEvent>() {
            @Override
            public void onResponse(Call<CancelEvent> call, Response<CancelEvent> response) {
                if (response.isSuccessful()) {
                    CancelEvent resObj = response.body();
                    Log.e("CancelStream", new Gson().toJson(response.body()));

                    if (resObj.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        startActivity(new Intent(OverViewActivity.this, CreateEventHistoryActivity.class));
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(OverViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    }
                }  else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CancelEvent> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void getDownloadEvent() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/export/overview/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        String stating = preferences.getString("filtering", "");

        if (stating !=null && !stating.isEmpty()){
            filter = 1;
        }else {
            filter=0;
            stat = 5;
        }

        if (stating.equals("Today")) {
            stat = 1;
        } else if (stating.equals("This week")) {
            stat = 2;
        } else if (stating.equals("This Month")) {
            stat = 3;
        } else if (stating.equals("This Year")) {
            stat = 4;
        } else if (stating.equals("All")) {
            stat = 5;
        }
        Call<DownloadCsv> call = apiPost.getDownloadCsv("Bearer " + signinValue , filter, stat);
        call.enqueue(new retrofit2.Callback<DownloadCsv>() {
            @Override
            public void onResponse(Call<DownloadCsv> call, Response<DownloadCsv> response) {
                if (response.isSuccessful()) {
                    DownloadCsv resObj = response.body();

                    if (resObj.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(getApplicationContext(), "Successfully Downloaded", Toast.LENGTH_SHORT).show();
                        List<DownloadDatum> list = resObj.getData();
                        for (DownloadDatum datum:list){
                            count = String.valueOf(datum.getCount());
                            title = datum.getTitle();
                            body = String.valueOf(datum.getBody());
                            exportData(count,title,body);
                        }

                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(OverViewActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(OverViewActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<DownloadCsv> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        month.setText(text);
        SharedPreferences.Editor editor = getSharedPreferences("apiToken", MODE_PRIVATE).edit();
        editor.putString("filtering", text);
        editor.apply();
        getFilter();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    public void SharingToSocialMedia(String application,String linkOpen) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, linkOpen);

        boolean installed = checkAppInstall(application);
        if (installed) {
            intent.setPackage(application);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(),
                    "Installed application first", Toast.LENGTH_LONG).show();
        }

    }

    private boolean checkAppInstall(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }

    private void exportData(String c,String t,String bo) {
        if (ActivityCompat.checkSelfPermission(OverViewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(OverViewActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }else {

            try {
                File file=new File("/sdcard/Download/coneqtLive.csv");
                file.mkdirs();
                if(file.exists()) {
                    String csv="/sdcard/Download/coneqtLive"  + num +".csv";
                    CSVWriter csvWriter=new CSVWriter(new FileWriter(csv,true));
                    String b[]=new String[]{c,t,bo};
                    csvWriter.writeNext(b);
                    csvWriter.close();
                    CustomNotification(csv);
                }
//                Toast.makeText(OverViewActivity.this, "file successfully created", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
//                Toast.makeText(OverViewActivity.this, "exception", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void CustomNotification (String csv) {
        // Get the layouts to use in the custom notification
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);

        notificationLayout.setTextViewText(R.id.info, csv);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon)
                .setCustomContentView(notificationLayout)
//                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();

        notificationManager.notify(1, notification);
    }


    private void getNotificationCount() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/notification/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiServices = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Call<NotificationCountModel> call = apiServices.getNotificationCount("Bearer " + signinValue);
        call.enqueue(new Callback<NotificationCountModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<NotificationCountModel> call, Response<NotificationCountModel> response) {
                if (response.isSuccessful()) {
                    NotificationCountModel resObj = response.body();

                    if(resObj.getSuccess()==true) {
                        if(resObj.getCount()>0) {
                            notification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notificationnew));
                        } else {
                            notification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification));
                        }
                    } else {
                        notification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification));
                    }
                } else {
                    Toast.makeText(OverViewActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(OverViewActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(OverViewActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}