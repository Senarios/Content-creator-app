package com.senarios.coneqtlive.Views.Settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.login.LoginManager;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.senarios.coneqtlive.Adapter.BlockedAdapter;
import com.senarios.coneqtlive.ApiServices;
import com.senarios.coneqtlive.Controller.RealPathUtil;
import com.senarios.coneqtlive.Model.BlockUserSetting;
import com.senarios.coneqtlive.Model.ChangeSetting;
import com.senarios.coneqtlive.Model.CheckingStripeAccount;
import com.senarios.coneqtlive.Model.DatumBlock;
import com.senarios.coneqtlive.Model.DesktopNotification;
import com.senarios.coneqtlive.Model.Logout;
import com.senarios.coneqtlive.Model.NotificationCountModel;
import com.senarios.coneqtlive.Model.UnBlockUser;
import com.senarios.coneqtlive.Model.UpdateProfile;
import com.senarios.coneqtlive.Model.UpdateStripeAccount;
import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.AuthSession;
import com.senarios.coneqtlive.Views.ConeqtCreator.CreateEventHistoryActivity;
import com.senarios.coneqtlive.Views.LoginandRegister.LoginActivity;
import com.senarios.coneqtlive.Views.Notification.NotificationActivity;
import com.senarios.coneqtlive.Views.OverView.OverViewActivity;
import com.senarios.coneqtlive.Views.Payout.PayoutActivity;
import com.senarios.coneqtlive.Views.TouchBlackHoleView;
import com.senarios.coneqtlive.stripe.CreateStripeAccountActivity;
import com.senarios.coneqtlive.stripe.Model.AccountLinksRes;
import com.senarios.coneqtlive.stripe.Model.AccountsInfo;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsActivity extends AppCompatActivity implements BlockedAdapter.UnBlockUser {
    private LinearLayout stripe_connectLayout , linearContinue , preferLayout;
    private ImageView notification;
    private LinearLayout connectTextView ;

    private EditText passEdit;
    private TextView browseText , stripeTxt,logoutBtn , blocKTxt;
    private Button  saveBtn;
    private RelativeLayout logoutLayout ;
    private TextView changeTxt, nameTextView,firstLetter, firstLetter1 , noUser , prefer;
    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    ImageView userImage, headerImage;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private EditText firstname, lastname, email, phoneNo;
    Uri uri;
    String path;
    String oldPassword;
    String newPassword;
    String confirmPassword;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])" +
            "(?=.*[a-z])" +
            "(?=.*[A-Z])" +
            "(?=.*[@#$%^&+=])" +
            "(?=\\S+$)." +
            "{8,15}" +
            "$");

    private RecyclerView recyclerView;
    private BlockedAdapter adapter;
    CheckBox check;
    TouchBlackHoleView blackHoleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initLayout();
        getStripeCheck();

        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signInValue = preferences.getString("signedInKey", "abc");
        String image = preferences.getString("imageName", "");
        String idUser = preferences.getString("first_name", "abc");
        String emailUser = preferences.getString("user_email_sheet", "");
        String socialEmail = preferences.getString("googleEmail", "");
        String phoneUser = preferences.getString("phone_no", "");
        String LastUser = preferences.getString("last_name", "");
        Integer device = preferences.getInt("device_notification", 0);
        if (signInValue.equals("Yes")) {
            if (device == 0) {
                check.setChecked(false);
            } else {
                check.setChecked(true);
            }
            nameTextView.setText(idUser);
            firstname.setText(idUser);
            lastname.setText(LastUser);
            if (emailUser != null && !emailUser.isEmpty()) {
                email.setText(emailUser);
                email.setFocusable(false);
            } else if (socialEmail != null && !socialEmail.isEmpty()) {
                email.setText(socialEmail);
                email.setFocusable(false);
            }
            phoneNo.setText(phoneUser);
            phoneNo.setFocusable(false);

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
                                firstLetter1.setText(s + s1);
                                firstLetter1.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(userImage);
                firstLetter1.setVisibility(View.GONE);
            } else {
                String test = idUser;
                String test1 = LastUser;
                String s = test.substring(0, 1).toUpperCase();
                String s1 = test1.substring(0, 1).toUpperCase();
                firstLetter1.setText(s + s1);
                firstLetter1.setVisibility(View.VISIBLE);
            }
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
        } else {

            Toast.makeText(getApplicationContext(), "nothing here ", Toast.LENGTH_SHORT).show();
        }
        getNotificationCount();
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }
    private boolean isShown = false;

    private void initLayout() {
        check = findViewById(R.id.check);
        notification = findViewById(R.id.notification_image);
        connectTextView = findViewById(R.id.connectTextView);
        passEdit = findViewById(R.id.idEmailLoginFishIt);
        stripe_connectLayout = findViewById(R.id.connect_stripe);
        linearContinue = findViewById(R.id.linearContinue);
        stripeTxt = findViewById(R.id.stripeTxt);
        logoutBtn = findViewById(R.id.idLogout);
        logoutLayout = findViewById(R.id.idLogoutLayout);
        saveBtn = findViewById(R.id.idSaveBtn);
        firstLetter = findViewById(R.id.idFirstLetter);
        firstLetter1 = findViewById(R.id.idFirstLetter1);
        changeTxt = findViewById(R.id.idForgetPassTvLogin);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.editEmail);
        phoneNo = findViewById(R.id.phoneNo);
        userImage = findViewById(R.id.userImage);
        headerImage = findViewById(R.id.headerImage);
        nameTextView = findViewById(R.id.nameTextView);
        browseText = findViewById(R.id.captureTxtView);
        noUser = findViewById(R.id.noUser);
        prefer = findViewById(R.id.prefer);
        preferLayout = findViewById(R.id.preferLayout);
        blocKTxt = findViewById(R.id.blocKTxt);

        recyclerView = findViewById(R.id.idBlockRecyclerview);

        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);


        allClickListener();
    }

    private void allClickListener() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.setting_bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.idSettings);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.idOverView:
                    startActivity(new Intent(SettingsActivity.this, OverViewActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.idCreatteEvent:
                    startActivity(new Intent(SettingsActivity.this, CreateEventHistoryActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.idPayouts:
                    startActivity(new Intent(SettingsActivity.this, PayoutActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.idSettings:
//                        startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
//                        overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });
        changeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailChangePopup();
            }
        });

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check.isChecked()) {
                    getDesktopNotification(1);
                } else {
                    getDesktopNotification(0);
                }
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
            }
        });

        connectTextView.setOnClickListener(v -> {
            accountCreate();
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogout();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        browseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(SettingsActivity.this)
                        .crop(16f, 9f)
                        .maxResultSize(1080, 1080)
                        .start(101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            Context context = SettingsActivity.this;
            path = RealPathUtil.getRealPath(context, uri);
            userImage.setImageURI(uri);
            SharedPreferences.Editor editor = getSharedPreferences("apiToken", MODE_PRIVATE).edit();
            editor.putString("img", path);
            editor.apply();
            firstLetter1.setVisibility(View.GONE);
            userImage.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(SettingsActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void emailChangePopup() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView = layoutInflater.inflate(R.layout.email_change_popup, null, false);
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

        EditText oldEdit = inflatedView.findViewById(R.id.idOldPassword);
        EditText newEdit = inflatedView.findViewById(R.id.idPasswordLogin);
        EditText newChangeEdit = inflatedView.findViewById(R.id.confirmNewPassword);

        ImageView showPasswordIcon = inflatedView.findViewById(R.id.showPasswordIcon);
        ImageView showPasswordIcon2 = inflatedView.findViewById(R.id.showPassword);
        ImageView showPasswordIcon3 = inflatedView.findViewById(R.id.confirmNewPasswordImage);

        showPasswordIcon.setOnClickListener(v -> {
            if (isShown) {
                showPasswordIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyeicon_pass));
                oldEdit.setTransformationMethod(new PasswordTransformationMethod());
                oldEdit.setSelection(oldEdit.getText().length());
                isShown = false;
            } else {
                showPasswordIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_resetpasswordeye));
                oldEdit.setTransformationMethod(null);
                oldEdit.setSelection(oldEdit.getText().length());
                isShown = true;
            }
        });

        showPasswordIcon2.setOnClickListener(v -> {
            if (isShown) {
                showPasswordIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyeicon_pass));
                newEdit.setTransformationMethod(new PasswordTransformationMethod());
                newEdit.setSelection(newEdit.getText().length());
                isShown = false;
            } else {
                showPasswordIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_resetpasswordeye));
                newEdit.setTransformationMethod(null);
                newEdit.setSelection(newEdit.getText().length());
                isShown = true;
            }
        });

        showPasswordIcon3.setOnClickListener(v -> {
            if (isShown) {
                showPasswordIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyeicon_pass));
                newChangeEdit.setTransformationMethod(new PasswordTransformationMethod());
                newChangeEdit.setSelection(newChangeEdit.getText().length());
                isShown = false;
            } else {
                showPasswordIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_resetpasswordeye));
                newChangeEdit.setTransformationMethod(null);
                newChangeEdit.setSelection(newChangeEdit.getText().length());
                isShown = true;
            }
        });

        Button button = inflatedView.findViewById(R.id.idSaveBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword = oldEdit.getText().toString().trim();
                if (oldPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter old password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!PASSWORD_PATTERN.matcher(oldPassword).matches()) {
                    Toast.makeText(SettingsActivity.this, "Old Password is not correct", Toast.LENGTH_LONG).show();
                    return;
                }
                newPassword = newEdit.getText().toString().trim();
                if (newPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter new password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!PASSWORD_PATTERN.matcher(newPassword).matches()) {
                    Toast.makeText(SettingsActivity.this, "Password must have minimum 8 characters at least 1 Uppercase Alphabet, 1 Lowercase Alphabet, 1 Number and 1 Special Character", Toast.LENGTH_LONG).show();
                    return;
                }
                confirmPassword = newChangeEdit.getText().toString().trim();
                if (confirmPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!PASSWORD_PATTERN.matcher(confirmPassword).matches()) {
                    Toast.makeText(SettingsActivity.this, "Password must have minimum 8 characters at least 1 Uppercase Alphabet, 1 Lowercase Alphabet, 1 Number and 1 Special Character", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "New password and Confirm password should be same", Toast.LENGTH_SHORT).show();
                } else {
                    changePasswordSetting(newPassword, oldPassword);
                    filterPopup.dismiss();
                }
            }
        });

        ImageView okBtn = inflatedView.findViewById(R.id.close);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
            }
        });
    }

    private void validateData() {

        String FirstName = firstname.getText().toString().trim();
        Log.wtf("Settings", "" + FirstName);

        if (FirstName.isEmpty()) {
            Toast.makeText(SettingsActivity.this, "Enter First name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (FirstName.length() > 25) {
            Toast.makeText(SettingsActivity.this, "Name too long ", Toast.LENGTH_SHORT).show();
            return;
        }

        String LastName = lastname.getText().toString().trim();
        Log.wtf("Settings", "" + LastName);

        if (LastName.isEmpty()) {
            Toast.makeText(SettingsActivity.this, "Enter lastname ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (LastName.length() > 25) {
            Toast.makeText(SettingsActivity.this, "lastname too long ", Toast.LENGTH_SHORT).show();
            return;
        } else {
            SettingUpUser(FirstName, LastName);
        }
    }

    private void SettingUpUser(String firstname, String lastname) {
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
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String img = preferences.getString("img", "");

        MultipartBody.Part body = null;

        if(img !=null && !img.isEmpty()) {
            File file = new File(img);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }
        RequestBody FirstName =
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(firstname));
        RequestBody Lastname =
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(lastname));
        ApiServices apiPost = retrofit.create(ApiServices.class);
        String signinValue = preferences.getString("apiToken", "abc");
        Call<UpdateProfile> call = apiPost.getUpdate("Bearer "+ signinValue, FirstName, Lastname , body);
        call.enqueue(new retrofit2.Callback<UpdateProfile>() {
            @Override
            public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {

                if (response.isSuccessful()) {
                    UpdateProfile registerObj = response.body();
                    if (registerObj.getSuccess() == true) {
                        Log.e("SettingActivity", new Gson().toJson(response.body()));
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        SharedPreferences.Editor editor = getSharedPreferences("apiToken", MODE_PRIVATE).edit();
                        editor.putString("imageName", response.body().getData().getImageUrl());
                        editor.putString("first_name", firstname);
                        editor.putString("last_name", lastname);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(SettingsActivity.this,"Already Updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfile> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SettingsActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SettingsActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });

    }


    private void changePasswordSetting(String newPassword, String oldPassword) {
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
        Call<ChangeSetting> call = apiPost.changeSettings("Bearer " + signinValue, newPassword, oldPassword);
        call.enqueue(new retrofit2.Callback<ChangeSetting>() {
            @Override
            public void onResponse(Call<ChangeSetting> call, Response<ChangeSetting> response) {

                if (response.isSuccessful()) {
                    ChangeSetting registerObj = response.body();
                    if (registerObj.getSuccess() == true) {
                        Log.e("SettingsChangePassword", new Gson().toJson(response.body()));
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangeSetting> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SettingsActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SettingsActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });
    }

    private void accountCreate() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.stripe.com/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences editor = getSharedPreferences("apiToken", MODE_PRIVATE);
        String user_email = editor.getString("user_email_sheet", "");
        if(user_email==null || user_email.isEmpty() ) {
            user_email =  editor.getString("googleEmail", "");
        }
        Call<AccountsInfo> call = apiPost.createUser("Bearer sk_test_51Jo7TGHC0KdocDH8NFpknppOflgFxc89Ws2JNh6zrwju47t8fw0Z2cH2VlLGF9fmYBEleUUAsFjZLKsWVdTEHjUp004NvPQKKG", "express", "Gb", user_email, true, true);
        call.enqueue(new retrofit2.Callback<AccountsInfo>() {
            @Override
            public void onResponse(Call<AccountsInfo> call, Response<AccountsInfo> response) {
                Log.e("Success", new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    updateAccount(response.body().getId());
                    Toast.makeText(getApplicationContext(), "successfully added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "process failed", Toast.LENGTH_SHORT).show();
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                }
            }

            @Override
            public void onFailure(Call<AccountsInfo> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SettingsActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SettingsActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });
    }

    private void updateAccount(String account_Id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/stripe/update/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Call<UpdateStripeAccount> call = apiPost.UpdateLink("Bearer " + signinValue, account_Id);
        call.enqueue(new retrofit2.Callback<UpdateStripeAccount>() {
            @Override
            public void onResponse(Call<UpdateStripeAccount> call, Response<UpdateStripeAccount> response) {
                Log.e("Success", new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    accountLink(account_Id, "account_onboarding", "https://play.google.com/apps/internaltest", "https://conneqt.senarios.co");

                }
            }

            @Override
            public void onFailure(Call<UpdateStripeAccount> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SettingsActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SettingsActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void accountLink(String account, String type, String returnUrl, String refreshUrl) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.stripe.com/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        Call<AccountLinksRes> call = apiPost.accountLink("Bearer sk_test_51Jo7TGHC0KdocDH8NFpknppOflgFxc89Ws2JNh6zrwju47t8fw0Z2cH2VlLGF9fmYBEleUUAsFjZLKsWVdTEHjUp004NvPQKKG", account, type, returnUrl, refreshUrl);
        call.enqueue(new retrofit2.Callback<AccountLinksRes>() {
            @Override
            public void onResponse(retrofit2.Call<AccountLinksRes> call, Response<AccountLinksRes> response) {
                Log.e("AccountSuccess", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    startActivity(new Intent(SettingsActivity.this, CreateStripeAccountActivity.class).putExtra("url", response.body().getUrl()).putExtra("returnUrl",returnUrl));
                    Toast.makeText(getApplicationContext(), "Welcome to stripe account", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<AccountLinksRes> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SettingsActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SettingsActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getBlockList() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Call<BlockUserSetting> call = apiPost.getBlockList("Bearer " + signinValue);
        call.enqueue(new retrofit2.Callback<BlockUserSetting>() {
            @Override
            public void onResponse(Call<BlockUserSetting> call, Response<BlockUserSetting> response) {
                if (response.isSuccessful()) {
                    BlockUserSetting resObj = response.body();

                    List<DatumBlock> list = response.body().getData();

                    if(list !=null && !list.isEmpty()) {
                        if (resObj.getSuccess() == true) {
                            recyclerView.setVisibility(View.VISIBLE);
                            noUser.setVisibility(View.GONE);
                            Log.e("BlockedList", new Gson().toJson(response.body()));
                            adapter = new BlockedAdapter(list,getApplicationContext(),SettingsActivity.this);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            SharedPreferences.Editor editor = getSharedPreferences("apiToken", MODE_PRIVATE).edit();
                            editor.putString("viewer_Id", response.body().getData().get(0).getContentViewerId().toString());
                            editor.apply();
                        } else {
                            Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noUser.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Process Failed ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BlockUserSetting> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SettingsActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SettingsActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getUnBlockList(String Id ) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Call<UnBlockUser> call = apiPost.getUnBlockList("Bearer " + signinValue, Integer.valueOf(Id));
        call.enqueue(new retrofit2.Callback<UnBlockUser>() {
            @Override
            public void onResponse(Call<UnBlockUser> call, Response<UnBlockUser> response) {
                if (response.isSuccessful()) {
                    UnBlockUser resObj = response.body();
                    if (resObj.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Log.e("UnBlockedList", new Gson().toJson(response.body()));
                        getBlockList();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UnBlockUser> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SettingsActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SettingsActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });
    }

    @Override
    public void userUnBlock(String Id) {
        getUnBlockList(Id);
    }

    private void getDesktopNotification(Integer notify) {
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
        Call<DesktopNotification> call = apiPost.getDesktopNotification("Bearer " + signinValue , notify);
        call.enqueue(new retrofit2.Callback<DesktopNotification>() {
            @Override
            public void onResponse(Call<DesktopNotification> call, Response<DesktopNotification> response) {
                if (response.isSuccessful()) {
                    DesktopNotification resObj = response.body();
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    if (resObj.getSuccess() == true) {
                        Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(SettingsActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(SettingsActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<DesktopNotification> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SettingsActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SettingsActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

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
                    Log.e("SettingsStrip", new Gson().toJson(response.body()));

                    CheckingStripeAccount checkingStripeAccount = response.body();

                    if (checkingStripeAccount.getSuccess() ==true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        stripe_connectLayout.setVisibility(View.GONE);
                        linearContinue.setVisibility(View.GONE);
                        stripeTxt.setVisibility(View.GONE);
                        preferLayout.setVisibility(View.VISIBLE);
                        prefer.setVisibility(View.VISIBLE);
                        blocKTxt.setVisibility(View.VISIBLE);
                        getBlockList();

                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        stripe_connectLayout.setVisibility(View.VISIBLE);
                        linearContinue.setVisibility(View.VISIBLE);
                        stripeTxt.setVisibility(View.VISIBLE);
                        preferLayout.setVisibility(View.GONE);
                        prefer.setVisibility(View.GONE);
                        blocKTxt.setVisibility(View.GONE);
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(SettingsActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckingStripeAccount> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SettingsActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SettingsActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SettingsActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SettingsActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SettingsActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getLogout() {
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
        Call<Logout> call = apiPost.getLogout("Bearer " + signinValue , "mobile");
        call.enqueue(new retrofit2.Callback<Logout>() {
            @Override
            public void onResponse(Call<Logout> call, Response<Logout> response) {
                if (response.isSuccessful()) {
                    Log.e("SettingsStrip", new Gson().toJson(response.body()));

                    Logout logout = response.body();

                    if (logout.getSuccess() ==true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        SharedPreferences.Editor e = getSharedPreferences("apiToken", MODE_PRIVATE).edit();
                        e.putString("signedInKey", "No");
                        e.clear();
                        e.apply();
                        AuthSession authSession = new AuthSession(SettingsActivity.this);
                        authSession.removeSession();
                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        gsc.signOut();
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();



                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);

                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(SettingsActivity.this, "Process Failed ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Logout> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SettingsActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SettingsActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SettingsActivity.this, OverViewActivity.class));
        overridePendingTransition(0, 0);
    }
}