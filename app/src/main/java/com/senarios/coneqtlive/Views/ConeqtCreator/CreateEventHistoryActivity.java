package com.senarios.coneqtlive.Views.ConeqtCreator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.senarios.coneqtlive.Adapter.CreateEventHistoryAdapter;
import com.senarios.coneqtlive.Adapter.CreateEventHistoryAdapterPast;
import com.senarios.coneqtlive.ApiServices;
import com.senarios.coneqtlive.Model.CancelEvent;
import com.senarios.coneqtlive.Model.CreateEventHistory;
import com.senarios.coneqtlive.Model.Link;
import com.senarios.coneqtlive.Model.NotificationCountModel;
import com.senarios.coneqtlive.Model.Past;
import com.senarios.coneqtlive.Model.StartEventModel;
import com.senarios.coneqtlive.Model.Upcoming;
import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.Notification.NotificationActivity;
import com.senarios.coneqtlive.Views.OverView.OverViewActivity;
import com.senarios.coneqtlive.Views.Payout.PayoutActivity;
import com.senarios.coneqtlive.Views.Settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.senarios.coneqtlive.Views.TouchBlackHoleView;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateEventHistoryActivity extends AppCompatActivity implements CreateEventHistoryAdapter.StartEvent {

    private Button upcoming_event, past_event;
    private ImageView notification, imgShare, imgSecondShare;
    private LinearLayout errorMessageLayout, pastLayout, upComingLayout;
    private TextView startBtn, cancelBtnFirst;
    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    private CreateEventHistoryAdapter createEventHistoryAdapter;
    private CreateEventHistoryAdapterPast createEventHistoryAdapterPast;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private TextView nameTextView, firstLetter, eventcancelTxt;
    private ImageView headerImage;
    boolean upcoming = true;
    TouchBlackHoleView blackHoleView;
    private TextView displayTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_history);

        initLayout();
        getCreateEventHistory();
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signInValue = preferences.getString("signedInKey", "abc");
        String image = preferences.getString("imageName", "");
        String idUser = preferences.getString("first_name", "abc");
        String LastUser = preferences.getString("last_name", "abc");

        if (signInValue.equals("Yes")) {
            nameTextView.setText(idUser);

            if (!image.isEmpty() && image != null) {
                Glide.with(this)
                        .load(image) // image url
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                String test = idUser;
                                String test1 = LastUser;
                                String s = test.substring(0, 1).toUpperCase();
                                String s1 = test1.substring(0, 1).toUpperCase();
                                firstLetter.setText(s + s1);
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
                String s = test.substring(0, 1).toUpperCase();
                String s1 = test1.substring(0, 1).toUpperCase();
                firstLetter.setText(s + s1);
                firstLetter.setVisibility(View.VISIBLE);
            }
        }
        getNotificationCount();
    }

    private void initLayout() {
        startBtn = findViewById(R.id.btn);
        notification = findViewById(R.id.notification_image);
        errorMessageLayout = findViewById(R.id.eventCancelLayout);
        eventcancelTxt = findViewById(R.id.eventcancelTxt);
        cancelBtnFirst = findViewById(R.id.idCancelBtnFirstCardView);
        upcoming_event = findViewById(R.id.tabbar_upcoming);
        recyclerView = findViewById(R.id.event_history_RV);
        past_event = findViewById(R.id.tabbar_past);
        firstLetter = findViewById(R.id.idFirstLetter);
        nameTextView = findViewById(R.id.nameTextView);
        displayTxt = findViewById(R.id.idDisplay);

        headerImage = findViewById(R.id.headerImage);

        ///share Image
        imgShare = findViewById(R.id.imgShare);

        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);

        swipeRefreshLayout = findViewById(R.id.events_swipe);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (upcoming) {
                    getCreateEventHistory();
                } else {
                    getCreateEventHistoryPast();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        allClickListener();
    }

    private void allClickListener() {
        upcoming_event.setOnClickListener(v -> {
            upcoming = true;
            upcoming_event.setTextColor(getResources().getColor(R.color.white));
            upcoming_event.setBackground(getResources().getDrawable(R.drawable.upcoming_selected));
            past_event.setTextColor(getResources().getColor(R.color.black));
            past_event.setBackground(getResources().getDrawable(R.drawable.past_unselected));
            getCreateEventHistory();
        });
        past_event.setOnClickListener(v -> {
            upcoming = false;
            past_event.setTextColor(getResources().getColor(R.color.white));
            past_event.setBackground(getResources().getDrawable(R.drawable.past_selected));
            upcoming_event.setTextColor(getResources().getColor(R.color.black));
            upcoming_event.setBackground(getResources().getDrawable(R.drawable.upcoming_unselected));
            getCreateEventHistoryPast();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.create_bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.idCreatteEvent);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.idOverView:
                        startActivity(new Intent(CreateEventHistoryActivity.this, OverViewActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idCreatteEvent:
//                        startActivity(new Intent(CreateEventHistoryActivity.this, CreateEventHistoryActivity.class));
//                        overridePendingTransition(0,0);
                        return true;
                    case R.id.idPayouts:
                        startActivity(new Intent(CreateEventHistoryActivity.this, PayoutActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idSettings:
                        startActivity(new Intent(CreateEventHistoryActivity.this, SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEventHistoryActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    private void shareDialog(Link link) {

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
                SharingToSocialMedia("com.twitter.android", link.getToOpen());
            }
        });
        ImageView linkedin = inflatedView.findViewById(R.id.idLinkedin);
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharingToSocialMedia("com.linkedin.android", link.getToOpen());
            }
        });
        ImageView faceBook = inflatedView.findViewById(R.id.idFacebook);
        faceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharingToSocialMedia("com.facebook.katana", link.getToOpen());
            }
        });


        TextView toShow = inflatedView.findViewById(R.id.toShowLink);
        toShow.setText(link.getToShow());


        ImageView cancelBtn = inflatedView.findViewById(R.id.close);

        Button copyBtn = inflatedView.findViewById(R.id.copy_link);

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("EditText", link.getToOpen().trim());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(CreateEventHistoryActivity.this, "Copied", Toast.LENGTH_SHORT).show();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
            }
        });

    }

    private void getCreateEventHistory() {
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
        Call<CreateEventHistory> call = apiPost.CreateEventHistoryRes("Bearer " + signinValue, "name", "type", "description", "2021-12-24 11:13:59", "2hour", 60, "1");
        call.enqueue(new retrofit2.Callback<CreateEventHistory>() {
            @Override
            public void onResponse(Call<CreateEventHistory> call, Response<CreateEventHistory> response) {
                if (response.isSuccessful()) {
                    List<Upcoming> list = response.body().getData().getUpcoming();

                    if (list != null && !list.isEmpty()) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        recyclerView.setVisibility(View.VISIBLE);
                        displayTxt.setVisibility(View.GONE);
                        createEventHistoryAdapter = new CreateEventHistoryAdapter((ArrayList<Upcoming>) list, getApplicationContext(), CreateEventHistoryActivity.this);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(createEventHistoryAdapter);
                        createEventHistoryAdapter.notifyDataSetChanged();
                    } else {
                        displayTxt.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(), "No Items in Recyclerview", Toast.LENGTH_SHORT).show();
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "No Items in Recyclerview", Toast.LENGTH_SHORT).show();
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                }
            }

            @Override
            public void onFailure(Call<CreateEventHistory> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void getCreateEventHistoryPast() {
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
        Call<CreateEventHistory> call = apiPost.CreateEventHistoryRes("Bearer " + signinValue, "name", "type", "description", "2021-12-24 11:13:59", "2hour", 60, "1");
        call.enqueue(new retrofit2.Callback<CreateEventHistory>() {
            @Override
            public void onResponse(Call<CreateEventHistory> call, Response<CreateEventHistory> response) {
                if (response.isSuccessful()) {
                    List<Past> list = response.body().getData().getPast();

                    if (list != null && !list.isEmpty()) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        recyclerView.setVisibility(View.VISIBLE);
                        displayTxt.setVisibility(View.GONE);
                        createEventHistoryAdapterPast = new CreateEventHistoryAdapterPast((ArrayList<Past>) list, getApplicationContext());
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(createEventHistoryAdapterPast);
                        createEventHistoryAdapterPast.notifyDataSetChanged();

                    } else {
                        displayTxt.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                }
            }

            @Override
            public void onFailure(Call<CreateEventHistory> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void CancelStream(String Id, String name) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/stripe/payout/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Call<CancelEvent> call = apiPost.getCancelEvent("Bearer " + signinValue, Integer.valueOf(Id));
        call.enqueue(new retrofit2.Callback<CancelEvent>() {
            @Override
            public void onResponse(Call<CancelEvent> call, Response<CancelEvent> response) {
                if (response.isSuccessful()) {
                    Log.e("CancelStream", new Gson().toJson(response.body()));
                    CancelEvent resObj = response.body();

                    if (resObj.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        errorMessageLayout.setVisibility(View.VISIBLE);
                        eventcancelTxt.setText("Event"+ " " + name + " " + "was canceled");
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(CreateEventHistoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<CancelEvent> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    @Override
    public void doStart(String Id, String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = Calendar.getInstance().getTime();
        Date date2 = null;
        try {
            date2 = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = 0;
        if (date2 != null) {
            diff = date1.getTime() - date2.getTime();
        }
        if (diff > 0)
            getStartEvent(Id);
        else
            Toast.makeText(getApplicationContext(), "You can't start event before Event time!", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void doCancel(String Id, String name) {
        CancelStream(Id, name);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                errorMessageLayout.setVisibility(View.GONE);
                eventcancelTxt.setText(name);
                eventcancelTxt.setVisibility(View.GONE);
            }
        }, 5000);
    }

    @Override
    public void doShare(Link Link) {
        shareDialog(Link);
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
                        Intent intent = new Intent(CreateEventHistoryActivity.this, BroadCastingEventCreatorActivity.class);
                        intent.putExtra("Id", resObj.getData().getId().toString());
                        intent.putExtra("Agora_Token", resObj.getData().getAgoraToken().toString());
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Successfully StartEvent", Toast.LENGTH_SHORT).show();

                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(CreateEventHistoryActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(CreateEventHistoryActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<StartEventModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });

    }

    public void SharingToSocialMedia(String application, String linkOpen) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, linkOpen);
        boolean installed = checkAppInstall(application);
        if (installed) {
            intent.setPackage(application);
            startActivity(intent);
        } else {
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

                    if (resObj.getSuccess() == true) {
                        if (resObj.getCount() > 0) {
                            notification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notificationnew));
                        } else {
                            notification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification));
                        }
                    } else {
                        notification.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification));
                    }
                } else {
                    Toast.makeText(CreateEventHistoryActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventHistoryActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CreateEventHistoryActivity.this, OverViewActivity.class));
        overridePendingTransition(0, 0);
    }
}