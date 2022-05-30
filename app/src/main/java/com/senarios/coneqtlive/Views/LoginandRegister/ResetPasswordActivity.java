package com.senarios.coneqtlive.Views.LoginandRegister;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.senarios.coneqtlive.ApiServices;
import com.senarios.coneqtlive.Model.ForgetPassword;
import com.senarios.coneqtlive.Model.ResetChangedPassword;
import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.ConeqtCreator.CreateEventActivity;
import com.senarios.coneqtlive.Views.TouchBlackHoleView;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResetPasswordActivity  extends AppCompatActivity {

    private EditText passEdit;
    private Button sendBtn;
    private Context context = this;
    private ImageView showPasswordIcon ,backImage;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])" +
            "(?=.*[a-z])" +
            "(?=.*[A-Z])" +
            "(?=.*[@#$%^&+=])" +
            "(?=\\S+$)." +
            "{8,15}" +
            "$");

    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    TouchBlackHoleView blackHoleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        passEdit = findViewById(R.id.idEmailLoginFishIt);
        sendBtn = findViewById(R.id.idLoginBtnLogin);
        backImage = findViewById(R.id.backImage);
        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);

        initLayout();
    }

    private boolean isShown = false;

    @SuppressLint("ClickableViewAccessibility")
    private void initLayout() {
        // editText initialization
        passEdit = findViewById(R.id.idPasswordLoginFishIt);
        // Button initialization
        sendBtn = findViewById(R.id.idLoginBtnLogin);

        // Password showing Eye Icon Initialization
        showPasswordIcon = findViewById(R.id.showPasswordIcon);
        allClickListener();

    }

    private void allClickListener() {
        sendBtn.setOnClickListener(v -> {
           validateData();
        });

        showPasswordIcon.setOnClickListener(v -> {
            if (isShown){
                showPasswordIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyeicon_pass));
                passEdit.setTransformationMethod(new PasswordTransformationMethod());
                passEdit.setSelection(passEdit.getText().length());
                isShown = false;
            }else{
                showPasswordIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_resetpasswordeye));
                passEdit.setTransformationMethod(null);
                passEdit.setSelection(passEdit.getText().length());
                isShown = true;
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void validateData() {
        String Password = passEdit.getText().toString().trim();
        if(Password.isEmpty()){
            Toast.makeText(ResetPasswordActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!PASSWORD_PATTERN.matcher(Password).matches()) {
            Toast.makeText(ResetPasswordActivity.this, "Password must have minimum 8 characters at least 1 Uppercase Alphabet, 1 Lowercase Alphabet, 1 Number and 1 Special Character", Toast.LENGTH_LONG).show();
            return;
        } else {
            getChangedPassword(Password);
        }
    }

    private void getChangedPassword (String password) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String email = preferences.getString("EmailDone","abc" );
        Call<ResetChangedPassword> call = apiPost.getChangedPassword( email, password );
        call.enqueue(new retrofit2.Callback<ResetChangedPassword>() {
            @Override
            public void onResponse(Call<ResetChangedPassword> call, Response<ResetChangedPassword> response) {
                if (response.isSuccessful()) {
                    ResetChangedPassword resObj = response.body();

                    if(resObj.getSuccess()==true){
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<ResetChangedPassword> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(ResetPasswordActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(ResetPasswordActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });
    }
}
