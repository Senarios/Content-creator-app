package com.senarios.coneqtlive.Views.Payout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.google.gson.GsonBuilder;
import com.senarios.coneqtlive.ApiServices;
import com.senarios.coneqtlive.Model.NotificationCountModel;
import com.senarios.coneqtlive.Model.Payout;
import com.senarios.coneqtlive.Model.PayoutWithdrawAmount;
import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.ConeqtCreator.CreateEventActivity;
import com.senarios.coneqtlive.Views.ConeqtCreator.CreateEventHistoryActivity;
import com.senarios.coneqtlive.Views.Notification.NotificationActivity;
import com.senarios.coneqtlive.Views.OverView.OverViewActivity;
import com.senarios.coneqtlive.Views.Settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.senarios.coneqtlive.Views.TouchBlackHoleView;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PayoutWithDrawActivity extends AppCompatActivity {

    private ImageView notification;
    private RadioButton priceRadioBtn, amountRadioBtn;
    private Button payBtn;
    private TextView nameTextView , firstLetter;
    private ImageView headerImage;
    private TextView available, pending, availableRadioTxt , otherRadioTxt , enterAmountTxt , totalAmountTxt ;
    private Button PayBtn;
    String amount;
    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    TouchBlackHoleView blackHoleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_with_draw);

        initLayout();
        getPayoutBalance();
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

    private void initLayout() {
        notification = findViewById(R.id.notification_image);
        priceRadioBtn = findViewById(R.id.priceRadioBtn);
        amountRadioBtn = findViewById(R.id.amountRadioBtn);
        payBtn = findViewById(R.id.idPayBtn);
        firstLetter = findViewById(R.id.idFirstLetter);
        nameTextView = findViewById(R.id.nameTextView);
        headerImage = findViewById(R.id.headerImage);
        available = findViewById(R.id.label);
        pending = findViewById(R.id.pendingText);
        availableRadioTxt = findViewById(R.id.availableBalance);
        otherRadioTxt = findViewById(R.id.otherAmount);
        enterAmountTxt = findViewById(R.id.enterAmountTxt);
        amount = enterAmountTxt.getText().toString().trim();
        totalAmountTxt = findViewById(R.id.totalAmountTxt);
        PayBtn = findViewById(R.id.idPayBtn);
        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);
        allClickListener();

    }

    private void allClickListener() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.payout_bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.idPayouts);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.idOverView:
                        startActivity(new Intent(PayoutWithDrawActivity.this, OverViewActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idCreatteEvent:
                        startActivity(new Intent(PayoutWithDrawActivity.this, CreateEventHistoryActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idPayouts:
//                        startActivity(new Intent(PayoutActivity.this, PayoutActivity.class));
//                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idSettings:
                        startActivity(new Intent(PayoutWithDrawActivity.this, SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayoutWithDrawActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        priceRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                amountRadioBtn.setChecked(false);
                enterAmountTxt.setText("");
                getPayoutBalance();
            }
        });

        amountRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                priceRadioBtn.setChecked(false);
                getPayoutBalance();
            }
        });
        enterAmountTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              totalAmountTxt.setText("£" + enterAmountTxt.getText().toString().trim());
              PayBtn.setText("WITHDRAW " + " £"+ enterAmountTxt.getText().toString().trim());
              amount=enterAmountTxt.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.contains("-")) {
                    Toast.makeText(getApplicationContext(), "amount should be in number" ,Toast.LENGTH_SHORT).show();
                    enterAmountTxt.setText("");
                    return;
                }else {
                    getPayoutWithDraw();
                }

            }
        });
    }

    private void getPayoutBalance() {
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
        Call<Payout> call = apiPost.getPayoutBalance("Bearer " + signinValue);
        call.enqueue(new retrofit2.Callback<Payout>() {
            @Override
            public void onResponse(Call<Payout> call, Response<Payout> response) {
                if (response.isSuccessful()) {
                    Payout resObj = response.body();
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    if (resObj.getSuccess() == true) {
                        available.setText("£" + response.body().getAvailable());
                        pending.setText("£" + response.body().getPending());
                        if(priceRadioBtn.isChecked()) {

                            amount= String.valueOf(response.body().getAvailable());
                            availableRadioTxt.setText("£" + response.body().getAvailable());
                            totalAmountTxt.setText("£" + response.body().getAvailable());
                            PayBtn.setText("WITHDRAW"+ " £" + response.body().getAvailable());
                            enterAmountTxt.setEnabled(false);
                        } else if(amountRadioBtn.isChecked()) {
                            enterAmountTxt.setEnabled(true);
                            totalAmountTxt.setText("£0");
                            PayBtn.setText("WITHDRAW "+ " £0");
                        }
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(PayoutWithDrawActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(PayoutWithDrawActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Payout> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(PayoutWithDrawActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(PayoutWithDrawActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });
    }

    private void getPayoutWithDraw() {
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
        Call<PayoutWithdrawAmount> call = apiPost.getWithDrawAmount("Bearer " + signinValue ,Double.parseDouble(amount));
        call.enqueue(new retrofit2.Callback<PayoutWithdrawAmount>() {
            @Override
            public void onResponse(Call<PayoutWithdrawAmount> call, Response<PayoutWithdrawAmount> response) {
                if (response.isSuccessful()) {
                    PayoutWithdrawAmount resObj = response.body();
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    if (resObj.getSuccess() == true) {
                        startActivity(new Intent(PayoutWithDrawActivity.this, PayoutActivity.class));
                        Toast.makeText(PayoutWithDrawActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        Toast.makeText(PayoutWithDrawActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PayoutWithDrawActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                }

            }

            @Override
            public void onFailure(Call<PayoutWithdrawAmount> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(PayoutWithDrawActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(PayoutWithDrawActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });
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
                    Toast.makeText(PayoutWithDrawActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(PayoutWithDrawActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(PayoutWithDrawActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}