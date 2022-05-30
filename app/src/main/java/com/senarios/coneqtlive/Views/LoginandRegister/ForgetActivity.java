package com.senarios.coneqtlive.Views.LoginandRegister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.senarios.coneqtlive.ApiServices;
import com.senarios.coneqtlive.Model.ForgetPassword;
import com.senarios.coneqtlive.Model.VerificationCode;
import com.senarios.coneqtlive.Model.VerifyPasswordReset;
import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.ConeqtCreator.CreateEventActivity;
import com.senarios.coneqtlive.Views.TouchBlackHoleView;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgetActivity  extends AppCompatActivity {

    private EditText emailEdit;
    private Button sendBtn;
    private ImageView backImage;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    TouchBlackHoleView blackHoleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        emailEdit = findViewById(R.id.idEmailLoginFishIt);
        sendBtn = findViewById(R.id.idLoginBtnLogin);
        backImage = findViewById(R.id.backImage);
        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);
        initLayout();

    }

    private void initLayout() {
        sendBtn.setOnClickListener(v -> {
            validateData();


        });

        backImage.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void validateData() {
        String Email = emailEdit.getText().toString().trim();
        if(Email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Email.matches(emailPattern)){
            Toast.makeText(ForgetActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        } else {
            SharedPreferences.Editor editor = getSharedPreferences("apiToken", MODE_PRIVATE).edit();
            editor.putString("EmailDone", Email);
            editor.apply();
            getForgetPassword(Email);
        }
    }

    private void ForgetPopup() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View inflatedView = layoutInflater.inflate(R.layout.guestjoinpopup, null, false);
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
        TextView counterTxtView = inflatedView.findViewById(R.id.counterTextView);
        Button resendBtn = inflatedView.findViewById(R.id.idResendBtn);
        new CountDownTimer(1000*60,1000 ) { // adjust the milli seconds here

            public void onTick(long l) {
                String sDuration = String.format(Locale.ENGLISH, "%02d:%02d"
                        ,TimeUnit.MILLISECONDS.toMinutes(l)
                        ,TimeUnit.MILLISECONDS.toSeconds(l) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
                counterTxtView.setText(sDuration);
                resendBtn.setEnabled(false);
                resendBtn.setClickable(false);
            }
            @Override
            public void onFinish() {
                resendBtn.setEnabled(true);
                resendBtn.setClickable(true);
                resendBtn.getBackground().setColorFilter(ContextCompat.getColor(ForgetActivity.this, R.color.black), PorterDuff.Mode.MULTIPLY);
                resendBtn.setTextColor(ContextCompat.getColor(ForgetActivity.this, R.color.white));
            }
        }.start();


        EditText editText1 = inflatedView.findViewById(R.id.editCode);
        EditText editText2 = inflatedView.findViewById(R.id.editCode1);
        EditText editText3 = inflatedView.findViewById(R.id.editCode2);
        EditText editText4 = inflatedView.findViewById(R.id.editCode3);


        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 1) {
                    editText2.requestFocus(View.FOCUS_DOWN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText3.requestFocus();
                if(count == 0) {
                    editText1.requestFocus(View.FOCUS_DOWN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText4.requestFocus();
                if(count == 0) {
                    editText2.requestFocus(View.FOCUS_DOWN);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText4.requestFocus();
                if(count == 0) {
                    editText3.requestFocus(View.FOCUS_DOWN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1) {
                    getVerifyCode(editText1.getText().toString()+editText2.getText().toString()+editText3.getText().toString()+editText4.getText().toString());
                }

            }
        });

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getResetForgetPassword();
                resendBtn.setBackgroundResource(R.drawable.btn_verification_background);
                resendBtn.setTextColor(ContextCompat.getColor(ForgetActivity.this, R.color.textClr));
                new CountDownTimer(1000*60,1000 ) { // adjust the milli seconds here
                    public void onTick(long l) {
                        String sDuration = String.format(Locale.ENGLISH, "%02d:%02d"
                                ,TimeUnit.MILLISECONDS.toMinutes(l)
                                ,TimeUnit.MILLISECONDS.toSeconds(l) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
                        counterTxtView.setText(sDuration);
                        resendBtn.setEnabled(false);
                        resendBtn.setClickable(false);
                    }
                    @Override
                    public void onFinish() {
                        resendBtn.setEnabled(true);
                        resendBtn.setClickable(true);
                        resendBtn.getBackground().setColorFilter(ContextCompat.getColor(ForgetActivity.this, R.color.black), PorterDuff.Mode.MULTIPLY);
                        resendBtn.setTextColor(ContextCompat.getColor(ForgetActivity.this, R.color.white));

                    }
                }.start();
            }
        });



        ImageView cancelBtn = inflatedView.findViewById(R.id.close);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
            }
        });

    }

    private void getForgetPassword (String email) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        Call<ForgetPassword> call = apiPost.getForgetPassword( email );
        call.enqueue(new retrofit2.Callback<ForgetPassword>() {
            @Override
            public void onResponse(Call<ForgetPassword> call, Response<ForgetPassword> response) {
                if (response.isSuccessful()) {
                    ForgetPassword resObj = response.body();
                    Log.e("getForgetPassword", new Gson().toJson(response.body()));
                    if(resObj.getSuccess()==true){
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        ForgetPopup();
                        Toast.makeText(getApplicationContext(), "Code is Sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                }
            }

            @Override
            public void onFailure(Call<ForgetPassword> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(ForgetActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(ForgetActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });
    }

    private void getResetForgetPassword () {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String email = preferences.getString("EmailDone","abc" );
        Call<ForgetPassword> call = apiPost.getForgetPassword( email );
        call.enqueue(new retrofit2.Callback<ForgetPassword>() {
            @Override
            public void onResponse(Call<ForgetPassword> call, Response<ForgetPassword> response) {
                if (response.isSuccessful()) {
                    ForgetPassword resObj = response.body();
                    Log.e("getResetForgetPassword", new Gson().toJson(response.body()));

                    if(resObj.getSuccess()==true){
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(getApplicationContext(), "Code is Sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                }
            }

            @Override
            public void onFailure(Call<ForgetPassword> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(ForgetActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(ForgetActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });
    }

    private void getVerifyCode(String code) {
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
        String email = preferences.getString("EmailDone","abc" );
        Call<VerifyPasswordReset> call = apiPost.getVerifiedPassword(email,code);
        call.enqueue(new retrofit2.Callback<VerifyPasswordReset>() {
            @Override
            public void onResponse(Call<VerifyPasswordReset> call, Response<VerifyPasswordReset> response) {
                if (response.isSuccessful()) {
                    Log.e("getVerifyCode", new Gson().toJson(response.body()));
                    VerifyPasswordReset verificationCode = response.body();
                    if (verificationCode.getSuccess()== true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(ForgetActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgetActivity.this, ResetPasswordActivity.class));
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<VerifyPasswordReset> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(ForgetActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(ForgetActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });
    }
}
