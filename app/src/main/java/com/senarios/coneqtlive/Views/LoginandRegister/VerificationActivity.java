package com.senarios.coneqtlive.Views.LoginandRegister;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.senarios.coneqtlive.ApiServices;
import com.senarios.coneqtlive.Model.CheckingStripeAccount;
import com.senarios.coneqtlive.Model.ForgetPassword;
import com.senarios.coneqtlive.Model.VerificationCode;
import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.ConeqtCreator.CreateEventActivity;
import com.senarios.coneqtlive.Views.ConeqtCreator.WelcomeEvent;
import com.senarios.coneqtlive.Views.OverView.OverViewActivity;
import com.senarios.coneqtlive.Views.TouchBlackHoleView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerificationActivity extends AppCompatActivity {
    private ImageView backImage;
    private RelativeLayout errorLayout , resendLayout;
    private Button resendBtn;
    private EditText editText1 , editText2 ,editText3 ,editText4;
    private TextView counterTxtView;
    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    TouchBlackHoleView blackHoleView;
    private boolean isFullOtp = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initLayout();
        editTextInput();
    }


    private void initLayout() {
        backImage = findViewById(R.id.backImage);
        errorLayout = findViewById(R.id.errorLayout);
        resendBtn = findViewById(R.id.idResendBtn);
        resendLayout = findViewById(R.id.idResendLayout);
        counterTxtView = findViewById(R.id.counterTextView);
        editText1 = findViewById(R.id.editCode);
        editText2 = findViewById(R.id.editCode1);
        editText3 = findViewById(R.id.editCode2);
        editText4 = findViewById(R.id.editCode3);

        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);

        //////
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
                resendBtn.getBackground().setColorFilter(ContextCompat.getColor(VerificationActivity.this, R.color.black), PorterDuff.Mode.MULTIPLY);
                resendBtn.setTextColor(ContextCompat.getColor(VerificationActivity.this, R.color.white));
            }
        }.start();

        clickListener();
    }

    private void clickListener() {
        backImage.setOnClickListener(v -> {
            onBackPressed();
        });
        resendBtn.setOnClickListener(v -> {
            resendBtn.setBackgroundResource(R.drawable.btn_verification_background);
            resendBtn.setTextColor(ContextCompat.getColor(VerificationActivity.this, R.color.textClr));
           getResetForgetPassword();
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
                    resendBtn.getBackground().setColorFilter(ContextCompat.getColor(VerificationActivity.this, R.color.black), PorterDuff.Mode.MULTIPLY);
                    resendBtn.setTextColor(ContextCompat.getColor(VerificationActivity.this, R.color.white));

                }
            }.start();
        });
    }

    private void editTextInput() {
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
                    errorLayout.setVisibility(View.GONE);
                    editText1.setBackgroundResource(R.drawable.background_box);
                    editText2.setBackgroundResource(R.drawable.background_box);
                    editText3.setBackgroundResource(R.drawable.background_box);
                    editText4.setBackgroundResource(R.drawable.background_box);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1) {
                    getVerifyCode(editText1.getText().toString() +editText2.getText().toString()+ editText3.getText().toString()+ editText4.getText().toString());
                }
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
                    Toast.makeText(VerificationActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(VerificationActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/verify/")
                .addConverterFactory(GsonConverterFactory.create(
                        gson)).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        int verifyId = preferences.getInt("verifyId",0);
        Call<VerificationCode> call = apiPost.getVerifyCode(verifyId, Integer.parseInt(code));
        call.enqueue(new retrofit2.Callback<VerificationCode>() {
            @Override
            public void onResponse(Call<VerificationCode> call, Response<VerificationCode> response) {
                if (response.isSuccessful()) {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    Log.e("CreateEvent", new Gson().toJson(response.body()));
                    VerificationCode verificationCode = response.body();
                    if (verificationCode.getSuccess()==true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(VerificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(VerificationActivity.this, LoginActivity.class));
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        errorLayout.setVisibility(View.VISIBLE);
                        editText1.setBackgroundResource(R.drawable.error_background_edittext);
                        editText2.setBackgroundResource(R.drawable.error_background_edittext);
                        editText3.setBackgroundResource(R.drawable.error_background_edittext);
                        editText4.setBackgroundResource(R.drawable.error_background_edittext);
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
            public void onFailure(Call<VerificationCode> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(VerificationActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(VerificationActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }
}