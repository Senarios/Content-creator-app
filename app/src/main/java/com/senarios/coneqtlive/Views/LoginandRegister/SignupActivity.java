package com.senarios.coneqtlive.Views.LoginandRegister;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.senarios.coneqtlive.ApiServices;
import com.senarios.coneqtlive.Model.DataRegister;
import com.senarios.coneqtlive.Model.Register;
import com.senarios.coneqtlive.Model.SocialLogin;
import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.OverView.OverViewActivity;
import com.senarios.coneqtlive.Views.TouchBlackHoleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {

    private EditText firstname, lastname, phoneNo, email, passEdit;

    private CheckBox termsAndCondition;

    private Button continueBtn;

    private ImageView  googleBtn, facebookBtn, showPasswordIcon, backImage;

    private Context context = this;
    private boolean passwordVisible;
    boolean isAllFieldsChecked = false;
    private ImageView imgApple, imgGoogle, imgFacebook;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])" +
            "(?=.*[a-z])" +
            "(?=.*[A-Z])" +
            "(?=.*[@#$%^&+=])" +
            "(?=\\S+$)." +
            "{8,15}" +
            "$");
    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;

    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private static final int RC_SIGN_IN = 1;
    TouchBlackHoleView blackHoleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        facebookLoginAccess();
        getFcmToken();

        //// Edit Text
        firstname = findViewById(R.id.idFirstNameEdit);
        lastname = findViewById(R.id.idLastNameEdit);
        phoneNo = findViewById(R.id.idPhoneNoEdit);
        phoneNo.setTransformationMethod(null);
        email = findViewById(R.id.idEmailLoginFishIt);
        passEdit = findViewById(R.id.idPasswordLoginFishIt);
        /// Terms and condition CheckBox
        termsAndCondition = findViewById(R.id.check_remeber);

        //// Button Click
        continueBtn = findViewById(R.id.idLoginBtnLogin);
        // Password showing Eye Icon Initialization
        showPasswordIcon = findViewById(R.id.showPasswordIcon);

        //BackImage
        backImage = findViewById(R.id.backImage);
        googleLoginAccess();
        //// Social Login
        imgGoogle = findViewById(R.id.idGoogleLogin);
        imgFacebook = findViewById(R.id.idFacebookLogin);

        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);

        allClickListeners();

    }

    private boolean isShown = false;

    private void allClickListeners() {

        continueBtn.setOnClickListener(v -> {
            validateData();
        });

        showPasswordIcon.setOnClickListener(v -> {
            if (isShown) {
                showPasswordIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_eyeicon_pass));
                passEdit.setTransformationMethod(new PasswordTransformationMethod());
                passEdit.setSelection(passEdit.getText().length());
                isShown = false;
            } else {
                showPasswordIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_resetpasswordeye));
                passEdit.setTransformationMethod(null);
                passEdit.setSelection(passEdit.getText().length());
                isShown = true;
            }
        });

        backImage.setOnClickListener(v -> onBackPressed());


        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(SignupActivity.this, Arrays.asList("public_profile", "email"));
            }
        });
    }

    private void googleLoginAccess() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(SignupActivity.this);
        if(account !=null) {
            startActivity(new Intent(SignupActivity.this, OverViewActivity.class));
        }

    }

    private void googleSignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.wtf("FirebaseWithGoogleId", account.getId() +account.getDisplayName()+account.getGivenName().toString().trim());
                SharedPreferences preferences = getSharedPreferences("fcmToken", MODE_PRIVATE);
                String token = preferences.getString("fcmToken", "abc");
                getSocialLogin(account.getGivenName(),account.getFamilyName(), account.getId(),"google", String.valueOf(account.getPhotoUrl()),token);
                Toast.makeText(this, "google sign in succeed", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = getSharedPreferences("apiToken", MODE_PRIVATE).edit();
                editor.putString("googleEmail", account.getEmail());
                editor.apply();

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void facebookLoginAccess() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        AccessToken.getCurrentAccessToken().getPermissions();
                        GraphRequest request = GraphRequest.newMeRequest(
                                accessToken,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        {
                                            try {
                                                String fullName = object.getString("name");
                                                Log.wtf("fullName", fullName);
                                                String emailId = object.getString("email");
                                                Log.wtf("Email", emailId);
                                                String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                                Log.wtf("Url", url);
                                                SharedPreferences preferences = getSharedPreferences("fcmToken", MODE_PRIVATE);
                                                String token = preferences.getString("fcmToken", "abc");

                                                getSocialLogin(fullName,fullName, emailId , "facebook", url, token);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            // Application code
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, email, name,link,picture.type(large)");
                        request.setParameters(parameters);
                        request.executeAsync();
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        finish();

                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "on Cancel");

                    }
                    @Override
                    public void onError(@NonNull FacebookException e) {

                    }
                });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(SignupActivity.this, "sign up successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void validateData() {

        String FirstName = firstname.getText().toString().trim();
        Log.wtf("SignUpActivity", "" + FirstName);

        if(FirstName.isEmpty()){
            Toast.makeText(SignupActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(FirstName.length()>25){
            Toast.makeText(SignupActivity.this, "Name too long ", Toast.LENGTH_SHORT).show();
            return;
        }

        String LastName = lastname.getText().toString().trim();
        Log.wtf("SignUpActivity", "" + LastName);

        if(LastName.isEmpty()){
            Toast.makeText(SignupActivity.this, "Enter lastname ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(LastName.length()>25){
            Toast.makeText(SignupActivity.this, "lastname too long ", Toast.LENGTH_SHORT).show();
            return;
        }

        String PhoneNo = phoneNo.getText().toString().trim();
        Log.wtf("SignUpActivity", "" + PhoneNo);

        if(PhoneNo.isEmpty()){
            Toast.makeText(SignupActivity.this, "Enter phoneNo", Toast.LENGTH_SHORT).show();
            return;
        }

        if(PhoneNo.length()<11){
            Toast.makeText(SignupActivity.this, "phoneNo too short", Toast.LENGTH_SHORT).show();
            return;
        }

        if(PhoneNo.length()>15){
            Toast.makeText(SignupActivity.this, "phoneNo too long", Toast.LENGTH_SHORT).show();
            return;
        }
        String Email = email.getText().toString().trim();
        Log.wtf("SignUpActivity", "" + Email);

        if(Email.isEmpty()){
            Toast.makeText(SignupActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Email.matches(emailPattern)){
            Toast.makeText(SignupActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        String Password = passEdit.getText().toString().trim();
        Log.wtf("SignUpActivity", "" + Password);

        if(Password.isEmpty()){
            Toast.makeText(SignupActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!PASSWORD_PATTERN.matcher(Password).matches()) {
            Toast.makeText(SignupActivity.this, "Password must have minimum 8 characters at least 1 Uppercase Alphabet, 1 Lowercase Alphabet, 1 Number and 1 Special Character", Toast.LENGTH_LONG).show();
            return;
        }
        if(Password.length()<8){
            Toast.makeText(SignupActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Password.length()>15) {
            Toast.makeText(SignupActivity.this, "Password too long", Toast.LENGTH_SHORT).show();
            return;
        }
        if(termsAndCondition.isChecked()){
            SharedPreferences preferences = getSharedPreferences("fcmToken", MODE_PRIVATE);
            String token = preferences.getString("fcmToken", "abc");
            signUpUser(Email ,Password , PhoneNo , FirstName , LastName , token);
        } else {
            Toast.makeText(SignupActivity.this, "Please check CheckBox", Toast.LENGTH_SHORT).show();
        }
    }

    private void signUpUser(String email , String password , String number , String firstname , String lastname, String token) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        Call<Register> call = apiPost.registerUser( email, password, number,firstname, lastname , token);
        call.enqueue(new retrofit2.Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if(response.isSuccessful()){
                    Register registerObj = response.body();

                    List<DataRegister> list = Collections.singletonList(registerObj.getData());

                    if(registerObj.getSuccess()==true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Log.e("SignUpUser", new Gson().toJson(response.body()));
                        SharedPreferences.Editor editor = getSharedPreferences("apiToken", MODE_PRIVATE).edit();
                        editor.putString("first_name",firstname);
                        editor.putString("EmailDone",email);
                        editor.putInt("verifyId",response.body().getData().getId());
                        editor.putInt("verificationCode",response.body().getData().getVerificationCode());
                        editor.apply();
                        startActivity(new Intent(SignupActivity.this, VerificationActivity.class));
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(SignupActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<Register> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SignupActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SignupActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });

    }

    private void getSocialLogin(String firstname , String lastname , String email, String auth_type , String image, String token) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        Call<SocialLogin> call = apiPost.getSocialLogin(firstname, lastname, email, auth_type , image , token);
        call.enqueue(new retrofit2.Callback<SocialLogin>() {
            @Override
            public void onResponse(Call<SocialLogin> call, Response<SocialLogin> response) {
                if (response.isSuccessful()) {
                    SocialLogin resObj = response.body();

                    if (resObj.getSuccess() == true) {
                        Log.e("SocialUserResponse", new Gson().toJson(response.body()));
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        SharedPreferences.Editor editor = getSharedPreferences("apiToken", MODE_PRIVATE).edit();
                        editor.putString("apiToken", response.body().getData().getApiToken());
                        editor.putString("signedInKey", "Yes");
                        editor.putString("user_email_shee", email);
                        editor.putString("first_name",firstname);
                        editor.putString("last_name", lastname);
                        editor.putString("phone_no", response.body().getData().getPhone());
                        editor.putString("imageName", response.body().getData().getImageUrl());
                        editor.apply();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<SocialLogin> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(SignupActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(SignupActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);

            }
        });

    }

    private void getFcmToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        SharedPreferences.Editor editor = getSharedPreferences("fcmToken", MODE_PRIVATE).edit();
                        editor.putString("fcmToken", token);
                        editor.apply();

                    }
                });
    }
}
