package com.senarios.coneqtlive.Views.Notification;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.senarios.coneqtlive.Adapter.NotificationAdapter;
import com.senarios.coneqtlive.Adapter.NotificationAdapterToday;
import com.senarios.coneqtlive.ApiServices;
import com.senarios.coneqtlive.Model.CompleteModel;
import com.senarios.coneqtlive.Model.Earlier;
import com.senarios.coneqtlive.Model.NotificationData;
import com.senarios.coneqtlive.Model.NotificationList;
import com.senarios.coneqtlive.Model.Today;
import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.ConeqtCreator.BroadCastingEventCreatorActivity;
import com.senarios.coneqtlive.Views.ConeqtCreator.CreateEventActivity;
import com.senarios.coneqtlive.Views.ConeqtCreator.CreateEventHistoryActivity;
import com.senarios.coneqtlive.Views.TouchBlackHoleView;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NotificationActivity extends AppCompatActivity {

    private TextView firstname , firstLetter;
    ImageView headerImage;

    private TextView todaytext , earliertext;

    RecyclerView todayRecyclerView , earlierRecyclerView ;
    NotificationAdapterToday adapterToday;
    NotificationAdapter notificationAdapter;

    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    TouchBlackHoleView blackHoleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initLayout();
        getNotificationList();
        getNotificationEarlier();
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signInValue = preferences.getString("signedInKey", "abc");
        String image = preferences.getString("imageName", "");
        String idUser = preferences.getString("first_name", "abc");
        String LastUser = preferences.getString("last_name", "abc");
        if (signInValue.equals("Yes")) {
            firstname.setText(idUser);
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
        } else {
            Toast.makeText(getApplicationContext(), "nothing here ", Toast.LENGTH_SHORT).show();
        }
    }

    private void initLayout() {
        firstname = findViewById(R.id.nameTextView);
        headerImage = findViewById(R.id.headerImage);
        firstLetter = findViewById(R.id.idFirstLetter);
        todaytext = findViewById(R.id.idtoday);
        earliertext = findViewById(R.id.idearlier);
        todayRecyclerView = findViewById(R.id.idTodayRecyclerView);
        earlierRecyclerView = findViewById(R.id.idEarlierRecyclerView);
        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);

    }

    private void getNotificationList() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");

        Call<NotificationList> call = apiPost.getNotification("Bearer " + signinValue );
        call.enqueue(new retrofit2.Callback<NotificationList>() {
            @Override
            public void onResponse(Call<NotificationList> call, Response<NotificationList> response) {
                if (response.isSuccessful()) {
                    NotificationList resObj = response.body();

                    List<Today> list = response.body().getData().getToday();

                    if(list !=null) {
                        if (resObj.getSuccess() == true) {
                            idProgressBarRelative.setVisibility(View.GONE);
                            lottieAnimationView.setVisibility(View.GONE);
                            blackHoleView.disableTouch(false);

                            adapterToday = new NotificationAdapterToday(list,getApplicationContext());
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                            todayRecyclerView.setLayoutManager(layoutManager);
                            todayRecyclerView.setAdapter(adapterToday);
                            adapterToday.notifyDataSetChanged();

                            Log.e("NotificationList", new Gson().toJson(response.body()));
//                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            idProgressBarRelative.setVisibility(View.GONE);
                            lottieAnimationView.setVisibility(View.GONE);
                            blackHoleView.disableTouch(false);
                            Toast.makeText(NotificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        todaytext.setVisibility(View.VISIBLE);
                        todayRecyclerView.setVisibility(View.GONE);
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<NotificationList> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(NotificationActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(NotificationActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void getNotificationEarlier() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");

        Call<NotificationList> call = apiPost.getNotification("Bearer " + signinValue );
        call.enqueue(new retrofit2.Callback<NotificationList>() {
            @Override
            public void onResponse(Call<NotificationList> call, Response<NotificationList> response) {
                if (response.isSuccessful()) {
                    NotificationList resObj = response.body();

                    List<Earlier> list = response.body().getData().getEarlier();

                    if(list !=null) {
                        if (resObj.getSuccess() == true) {
                            idProgressBarRelative.setVisibility(View.GONE);
                            lottieAnimationView.setVisibility(View.GONE);
                            blackHoleView.disableTouch(false);

                            notificationAdapter = new NotificationAdapter(list,getApplicationContext());
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                            earlierRecyclerView.setLayoutManager(layoutManager);
                            earlierRecyclerView.setAdapter(notificationAdapter);
                            notificationAdapter.notifyDataSetChanged();

                            Log.e("NotificationList", new Gson().toJson(response.body()));
//                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NotificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            idProgressBarRelative.setVisibility(View.GONE);
                            lottieAnimationView.setVisibility(View.GONE);
                            blackHoleView.disableTouch(false);
                        }
                    } else {
                        earliertext.setVisibility(View.VISIBLE);
                        earlierRecyclerView.setVisibility(View.GONE);
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<NotificationList> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(NotificationActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(NotificationActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }
}