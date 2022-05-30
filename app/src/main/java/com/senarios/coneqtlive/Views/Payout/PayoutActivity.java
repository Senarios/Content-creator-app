package com.senarios.coneqtlive.Views.Payout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.senarios.coneqtlive.ApiServices;
import com.senarios.coneqtlive.Model.CreatorFilter;
import com.senarios.coneqtlive.Model.NotificationCountModel;
import com.senarios.coneqtlive.Model.OverViewModel;
import com.senarios.coneqtlive.Model.Payout;
import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.ConeqtCreator.CreateEventActivity;
import com.senarios.coneqtlive.Views.ConeqtCreator.CreateEventHistoryActivity;
import com.senarios.coneqtlive.Views.Notification.NotificationActivity;
import com.senarios.coneqtlive.Views.OverView.OverViewActivity;
import com.senarios.coneqtlive.Views.Settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.senarios.coneqtlive.Views.TouchBlackHoleView;
import com.senarios.coneqtlive.stripe.Model.TosAcceptance;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PayoutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button WithdrawBtn;
    private ImageView notification;
    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    TextView available, pending;
    private TextView nameTextView, month , firstLetter;
    private TextView eventText , revenueTxt , soldTxt , refundTxt;
    private ImageView headerImage;
    int stat;
    TouchBlackHoleView blackHoleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout);

        initLayout();
//        getPayout();
        getPayoutList();
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
        WithdrawBtn = findViewById(R.id.idWithdrawBtn);
        notification = findViewById(R.id.notification_image);
        available = findViewById(R.id.label);
        pending = findViewById(R.id.pendingText);
        month = findViewById(R.id.month);
        firstLetter = findViewById(R.id.idFirstLetter);

        nameTextView = findViewById(R.id.nameTextView);

        headerImage = findViewById(R.id.headerImage);

        eventText = findViewById(R.id.idEventTxt);
        revenueTxt = findViewById(R.id.idRevenueTxt);
        soldTxt = findViewById(R.id.idSoldTxt);
        refundTxt = findViewById(R.id.idRefundTxt);

        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);

        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, R.layout.selected_month);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(4);
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
                        startActivity(new Intent(PayoutActivity.this, OverViewActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idCreatteEvent:
                        startActivity(new Intent(PayoutActivity.this, CreateEventHistoryActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idPayouts:
//                        startActivity(new Intent(PayoutActivity.this, PayoutActivity.class));
//                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idSettings:
                        startActivity(new Intent(PayoutActivity.this, SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        WithdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PayoutActivity.this, PayoutWithDrawActivity.class));
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayoutActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
            }
        });
    }

    private void getPayout() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
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
                    if (resObj.getSuccess() == true) {
                        available.setText("£" + response.body().getAvailable());
                        pending.setText("£" + response.body().getPending());
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        Toast.makeText(PayoutActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    Toast.makeText(PayoutActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Payout> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(PayoutActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(PayoutActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });
    }

    private void getPayoutList() {
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
                    if(res.getSuccess()==true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        assert response.body() != null;
                        eventText.setText(response.body().getData().getTotalEvents().toString().trim());
                        revenueTxt.setText("£" + response.body().getData().getRevenue());
                        soldTxt.setText(response.body().getData().getTickets().toString().trim());
                        refundTxt.setText("£" + response.body().getData().getRefunds());
                        available.setText("£" + response.body().getData().getAvailableBalance());
                        pending.setText("£" + response.body().getData().getPendingBalance());
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(PayoutActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<OverViewModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(PayoutActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(PayoutActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PayoutActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PayoutActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(PayoutActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PayoutActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(PayoutActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(PayoutActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PayoutActivity.this, OverViewActivity.class));
        overridePendingTransition(0, 0);
    }
}