package com.senarios.coneqtlive.Views.ConeqtCreator;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.senarios.coneqtlive.ApiServices;
import com.senarios.coneqtlive.Controller.RealPathUtil;
import com.senarios.coneqtlive.Model.CreateEvent;
import com.senarios.coneqtlive.Model.DatumEventListType;
import com.senarios.coneqtlive.Model.EventTypeList;
import com.senarios.coneqtlive.Model.Link;
import com.senarios.coneqtlive.Model.NotificationCountModel;
import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.Notification.NotificationActivity;
import com.senarios.coneqtlive.Views.OverView.OverViewActivity;
import com.senarios.coneqtlive.Views.Payout.PayoutActivity;
import com.senarios.coneqtlive.Views.Settings.SettingsActivity;
import com.senarios.coneqtlive.Views.TouchBlackHoleView;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateEventActivity extends AppCompatActivity {

    private AppCompatButton button;
    private ImageView notification, my_avatar;
    private TextView browseTxt;
    private String selectedImagePath;
    private ImageView captureImage;
    private Context context;
    private EditText eventName, eventTime, eventTimeDuration, eventDescription, eventCost;
    private CheckBox interactionCheckbox;
    LinearLayout idEventTimeLayout;
    RelativeLayout idProgressBarRelative , captureLayout;
    LottieAnimationView lottieAnimationView;
    Uri uri;
    String path;
    String timeZone = "am";
    String adjustedTime= "";
    private TextView nameTextView, firstLetter;
    private ImageView headerImage;
    List<DatumEventListType> eventlist;
    Map<String, String> map;
    int seconds;
    TouchBlackHoleView blackHoleView;
    Pattern PATTERN = Pattern.compile("[0-9]+");
    private Date selectedDate;
    String streamCheck = "1";

    AutoCompleteTextView eventType;
    private static final String[] COUNTRIES = new String[]{
            "Fun", "Travel", "Workout", "Yoga", "Concert", "Education"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        initLayout();
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
        getEventType();
        getNotificationCount();

        eventType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    eventType.setFocusableInTouchMode(false);
                    eventType.setFocusable(false);
                    eventType.clearFocus();

                    List<String> listt = new ArrayList<>();
                    map = new HashMap<>();
                    for (DatumEventListType datum : eventlist) {
                        listt.add(datum.getName());
                        map.put(datum.getName(), String.valueOf(datum.getId()));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateEventActivity.this,
                            android.R.layout.simple_list_item_1, listt);
                    eventType.setAdapter(adapter);
                    eventType.showDropDown();
                } else {
                    eventType.setFocusableInTouchMode(true);
                    eventType.setFocusable(true);
                    eventType.clearFocus();
                }
            }
        });
    }

    private void initLayout() {
        notification = findViewById(R.id.notification_image);
        my_avatar = findViewById(R.id.my_avatar);
        button = findViewById(R.id.idCreateEvent);
        browseTxt = findViewById(R.id.idEventBrowse);
        captureLayout = findViewById(R.id.captureLayout);
        captureImage = findViewById(R.id.my_avatar);
        nameTextView = findViewById(R.id.nameTextView);

        headerImage = findViewById(R.id.headerImage);
        firstLetter = findViewById(R.id.idFirstLetter);
        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);

        eventName = findViewById(R.id.idEventName);
        eventTime = findViewById(R.id.idEventTime);
        eventTimeDuration = findViewById(R.id.idEventTimeDuration);
        eventTimeDuration.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    String matches = eventTimeDuration.getText().toString();
                    if (PATTERN.matcher(matches).matches()) {
                        try {
                            int minutes = Integer.parseInt(eventTimeDuration.getText().toString());
                            seconds = minutes * 60;
                            long hours = TimeUnit.MINUTES.toHours(Long.valueOf(minutes));
                            long remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours);
                            if (minutes < 60) {
                                eventTimeDuration.setText(remainMinutes + "min");
                            } else if (minutes == 60) {
                                eventTimeDuration.setText(hours + "h ");
                            } else if (minutes == 120) {
                                eventTimeDuration.setText(hours + "h ");
                            } else {
                                eventTimeDuration.setText(hours + "h " + remainMinutes + "min ");
                            }

                        } catch (Exception e) {
                            if (matches.isEmpty()) {

                            } else if (matches.contains("hour") || matches.contains("hours") || matches.contains("minutes")) {
                                eventTimeDuration.setText(matches);
                            } else {
                                eventTimeDuration.setText("");
                                Toast.makeText(CreateEventActivity.this, "you cannot add time more than 24 hours or u put only number here ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        eventType = findViewById(R.id.idEventType);
        eventDescription = findViewById(R.id.idEventDescription);
        eventCost = findViewById(R.id.idEventCost);
        eventCost.setTransformationMethod(null);
        interactionCheckbox = findViewById(R.id.idCheckInteraction);

        idEventTimeLayout = findViewById(R.id.idEventTimeLayout);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, COUNTRIES);
        eventType.setAdapter(adapter);

        allClickListener();
    }

    private void allClickListener() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.create_bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.idCreatteEvent);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.idOverView:
                        startActivity(new Intent(CreateEventActivity.this, OverViewActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idCreatteEvent:
//                        startActivity(new Intent(CreateEventActivity.this, CreateEventActivity.class));
//                        overridePendingTransition(0,0);
                        return true;
                    case R.id.idPayouts:
                        startActivity(new Intent(CreateEventActivity.this, PayoutActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.idSettings:
                        startActivity(new Intent(CreateEventActivity.this, SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateData();
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEventActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             ImagePicker.Companion.with(CreateEventActivity.this)
                        .crop(1f,1f)
                        .maxResultSize(1080, 1080)
                        .start(101);
            }
        });

        captureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(CreateEventActivity.this)
                        .crop(1f,1f)
                        .maxResultSize(1080, 1080)
                        .start(101);
            }
        });

        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomCalenderPopup();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            Context context = CreateEventActivity.this;
            path = RealPathUtil.getRealPath(context, uri);
            captureImage.setImageURI(uri);
        } else {
            Toast.makeText(CreateEventActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
        }
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
                Toast.makeText(CreateEventActivity.this, "Copied", Toast.LENGTH_SHORT).show();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
                onBackPressed();
            }
        });

    }

    private void validateData() {

        String EventName = eventName.getText().toString().trim();
        Log.wtf("CreateEventActivity", "" + EventName);

        if (EventName.isEmpty()) {
            Toast.makeText(CreateEventActivity.this, "Please Enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        String EventTime = adjustedTime;

        Log.wtf("CreateEventActivity", "" + EventTime);

        if (EventTime.isEmpty()) {
            Toast.makeText(CreateEventActivity.this, "Please Enter Time Here ", Toast.LENGTH_SHORT).show();
            return;
        }

        long duration = 0;
        if ( !eventTimeDuration.getText().toString().isEmpty() ) {
            duration = seconds;
        }
        String EventTimeDuration = String.valueOf(seconds);
        Log.wtf("CreateEventActivity", "" + EventTimeDuration);

        if (duration <= 0) {
            Toast.makeText(CreateEventActivity.this, "Please Enter TimeDuration", Toast.LENGTH_SHORT).show();
            return;
        }

        String EventType = eventType.getText().toString().trim();
        Log.wtf("CreateEventActivity", "" + EventType);

        if (EventType.isEmpty()) {
            Toast.makeText(CreateEventActivity.this, "Please Enter Type", Toast.LENGTH_SHORT).show();
            return;
        }

        String EventDescription = eventDescription.getText().toString().trim();
        Log.wtf("CreateEventActivity", "" + EventDescription);

        if (EventDescription.isEmpty()) {
            Toast.makeText(CreateEventActivity.this, "Please Enter EventDescription", Toast.LENGTH_SHORT).show();
            return;
        }
        String EventCost = eventCost.getText().toString().trim();
        Log.wtf("CreateEventActivity", "" + EventCost);


        if (EventCost.isEmpty()) {
            Toast.makeText(CreateEventActivity.this, "Please EventCost", Toast.LENGTH_SHORT).show();
            return;
        }
        if (EventCost.length() > 5) {
            Toast.makeText(CreateEventActivity.this, "EventCost Amount should not be more than 5 Number", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if(interactionCheckbox.isChecked()) {
                streamCheck= "1";
            } else {
                streamCheck = "0";
            }
            getCreateEvent(EventName, EventType, EventDescription, EventTime, EventTimeDuration, EventCost, streamCheck);
        }
    }

    private void CustomCalenderPopup() {
        long millis_today = System.currentTimeMillis();
        selectedDate = new Date();
        selectedDate.setTime(millis_today);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView = layoutInflater.inflate(R.layout.calender_popup, null, false);
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

        ImageView cancelBtn = inflatedView.findViewById(R.id.close);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM-yyyy", Locale.getDefault());
        TextView textView = inflatedView.findViewById(R.id.calenderTextView);
        TextView amTextView, pmTextView;
        amTextView = inflatedView.findViewById(R.id.amTxt);
        pmTextView = inflatedView.findViewById(R.id.pmTxt);
        EditText hours = inflatedView.findViewById(R.id.idhour);
        hours.setTransformationMethod(null);
        EditText minutes = inflatedView.findViewById(R.id.idminutes);
        minutes.setTransformationMethod(null);
        Button saveBtn = inflatedView.findViewById(R.id.saveBtn);
        CompactCalendarView compactCalendarView = inflatedView.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        Calendar c = Calendar.getInstance();
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};
        String month = monthName[c.get(Calendar.MONTH)];
        int year = c.get(Calendar.YEAR);
        textView.setText(month + "-" + year);


        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                String formateDate = new SimpleDateFormat("yyyy-MM-dd").format(dateClicked);
                SharedPreferences.Editor editor = getSharedPreferences("dateClicked", MODE_PRIVATE).edit();
                editor.putString("dateClicked", formateDate);
                editor.apply();
                selectedDate = dateClicked;
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                textView.setText(simpleDateFormat.format(firstDayOfNewMonth));

            }
        });
        amTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeZone = "am";
                amTextView.setTextColor(getResources().getColor(R.color.white));
                amTextView.setBackground(getResources().getDrawable(R.drawable.am_selected));
                pmTextView.setTextColor(getResources().getColor(R.color.black));
                pmTextView.setBackground(getResources().getDrawable(R.drawable.pm_unslected));
            }
        });

        pmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeZone = "pm";
                pmTextView.setTextColor(getResources().getColor(R.color.white));
                pmTextView.setBackground(getResources().getDrawable(R.drawable.pm_selected));
                amTextView.setTextColor(getResources().getColor(R.color.black));
                amTextView.setBackground(getResources().getDrawable(R.drawable.am_unselected));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("dateClicked", MODE_PRIVATE);
                String signinValue = preferences.getString("dateClicked", "abc");
                if (signinValue.equals("abc")) {
                    signinValue = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                }
                eventTime.setText(signinValue + " " + hours.getText().toString().trim() + ":" + minutes.getText().toString().trim());
                if (timeZone.equals("pm")) {
                    String adjustedHour = "";
                    if(Integer.parseInt(hours.getText().toString().trim())<12) {
                        adjustedHour = String.valueOf((Integer.parseInt(hours.getText().toString().trim()) + 12));
                    } else {
                        adjustedHour = hours.getText().toString().trim();
                    }

                    adjustedTime = signinValue + " " + adjustedHour + ":" + minutes.getText().toString().trim()+ ":00";
                } else {

                    String adjustedHour = "";
                    if (!hours.getText().toString().isEmpty()) {
                        if(Integer.parseInt(hours.getText().toString().trim())<12) {
                            adjustedHour = String.valueOf((Integer.parseInt(hours.getText().toString().trim())));
                        } else {
                            adjustedHour = "00";
                        }

                        adjustedTime = signinValue + " " + adjustedHour + ":" + minutes.getText().toString().trim() + ":00";
                    }
                }

                if (hours.getText().toString().trim().isEmpty() && minutes.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter hours and minutes ", Toast.LENGTH_SHORT).show();
                } else if (!validateEventTime()) {
                    Toast.makeText(CreateEventActivity.this, "You can only select present and upcoming date and time only", Toast.LENGTH_SHORT).show();
                } else {
                    filterPopup.dismiss();
                    SharedPreferences.Editor editor = getSharedPreferences("dateClicked", MODE_PRIVATE).edit();
                    editor.clear();
                    editor.apply();
                    timeZone = "am";
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
            }
        });

        hours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override   
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    try {
                        int i = parseInt(String.valueOf(s));
                        if (i > 12) {
                            hours.setText("");
                            Toast.makeText(CreateEventActivity.this, "Hours should not be more than 12", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        hours.setText("");
                        Toast.makeText(CreateEventActivity.this, "put only numbers here", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        minutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    try {
                        int i = parseInt(String.valueOf(s));
                        if (i > 59) {
                            minutes.setText("");
                            Toast.makeText(CreateEventActivity.this, "Minutes should not be more than 59", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        minutes.setText("");
                        Toast.makeText(CreateEventActivity.this, "put only numbers here", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

    }

    private boolean validateEventTime() {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date eventTime = sdf.parse(adjustedTime);
            if ( System.currentTimeMillis() < eventTime.getTime() )
                result = true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void getEventType() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/api/event/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        Call<EventTypeList> call = apiPost.getEventType();
        call.enqueue(new retrofit2.Callback<EventTypeList>() {
            @Override
            public void onResponse(Call<EventTypeList> call, Response<EventTypeList> response) {
                if (response.isSuccessful()) {
                    EventTypeList resObj = response.body();
                    eventlist = resObj.getData();
                    if (resObj.getSuccess() == true) {

                        Log.e("EventType", new Gson().toJson(response.body()));
//                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateEventActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventTypeList> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getCreateEvent(String name, String type, String description, String time, String timeDuration, String ticketPrice, String streamInteraction) {
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
        File file;

        if (path == null) {
            Toast.makeText(getApplicationContext(), "Please Update Image First", Toast.LENGTH_SHORT).show();
            idProgressBarRelative.setVisibility(View.GONE);
            lottieAnimationView.setVisibility(View.GONE);
            blackHoleView.setVisibility(View.GONE);
            blackHoleView.disableTouch(false);
            return;
        } else {
            file = new File(path);
        }

//        Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image1", file.getName(), requestFile);

        RequestBody fullName =
                RequestBody.create(MediaType.parse("multipart/form-data"), name);

        RequestBody Type =
                RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(map.get(type)));

        RequestBody Description =
                RequestBody.create(MediaType.parse("multipart/form-data"), description);
        RequestBody Time =
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(time));
        RequestBody TimeDuration =
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(timeDuration));

        RequestBody TicketPrice =
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(ticketPrice));

        RequestBody StreamInteraction =
                RequestBody.create(MediaType.parse("multipart/form-data"), streamInteraction);

        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Call<CreateEvent> call = apiPost.getCreateEvent("Bearer " + signinValue, fullName, Type, Description, Time, TimeDuration, TicketPrice, StreamInteraction, body);
        call.enqueue(new retrofit2.Callback<CreateEvent>() {
            @Override
            public void onResponse(Call<CreateEvent> call, Response<CreateEvent> response) {
                if (response.isSuccessful()) {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    CreateEvent create = response.body();
                    if (create.getSuccess() == true) {
                        Log.e("CreateEvent", new Gson().toJson(response.body()));
                        Log.wtf("Image1_S3", response.body().getData().getImage1S3());
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        shareDialog(response.body().getLink());

                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(CreateEventActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<CreateEvent> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CreateEventActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(CreateEventActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(CreateEventActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
