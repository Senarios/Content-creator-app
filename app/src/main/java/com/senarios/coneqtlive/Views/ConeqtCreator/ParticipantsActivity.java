package com.senarios.coneqtlive.Views.ConeqtCreator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.senarios.coneqtlive.Adapter.ParticipantsAdapter;
import com.senarios.coneqtlive.ApiServices;
import com.senarios.coneqtlive.Model.BroadcastBlock;
import com.senarios.coneqtlive.Model.KickoutUser;
import com.senarios.coneqtlive.Model.ParticipentsModel;
import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.TouchBlackHoleView;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.alterac.blurkit.BlurLayout;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParticipantsActivity extends AppCompatActivity implements ParticipantsAdapter.BlockAndKick {
    BlurLayout blurLayout;
    RelativeLayout parentLayout;
    RecyclerView recyclerView;
    ImageView crossBtn;
    ParticipantsAdapter adapter;
    private String channelName;
    private String token;
    DatabaseReference databaseReference;
    ArrayList<ParticipentsModel> participantsModel = new ArrayList<>();
    int count = 0;
    private TextView TextViewLive;

    String userid;

    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    TouchBlackHoleView blackHoleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);

        channelName = getIntent().getStringExtra("Id");
        token = getIntent().getStringExtra("Agora_Token");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("streams");

        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        Integer IdUser = preferences.getInt("IdUser", 0);

        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);
        TextViewLive = findViewById(R.id.TextViewLive);

        parentLayout = findViewById(R.id.parentLayout);
        parentLayout.setAlpha((float) 0.7);

        crossBtn = findViewById(R.id.crossBtn);
        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.show_participants_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userid = Integer.toString(IdUser);

        FirebaseDatabase.getInstance().getReference().child("streams")
                .child(channelName).child("participents").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int count = (int) snapshot.getChildrenCount();
                    TextViewLive.setText(String.valueOf(count));

                } else {
                    TextViewLive.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



//        databaseReference = FirebaseDatabase.getInstance().getReference().child("streams").child("BlockedUsers").child(userid);
//        Map<String, String> map = new HashMap<>();
//        map.put("User Id", userid);
//
//        databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//            }
//        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName).child("participents");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                participantsModel = new ArrayList<>();
                if (snapshot.exists()) {
                    count = 0;
                    for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                        participantsModel.add(new ParticipentsModel(datasnapshot.child("name").getValue().toString(), datasnapshot.child("image").getValue().toString(), Integer.parseInt(datasnapshot.child("userId").getValue().toString())));
                        count++;

                    }
                    adapter = new ParticipantsAdapter(participantsModel, getApplicationContext(), ParticipantsActivity.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ParticipantsActivity.this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else {
                    adapter = new ParticipantsAdapter(new ArrayList<>(), getApplicationContext(), ParticipantsActivity.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ParticipantsActivity.this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    public void BlockUsers(Integer Id) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName).child("BlockedUsers").child(String.valueOf(Id));
        Map<String, String> map = new HashMap<>();
        map.put("User Id", String.valueOf(Id));

        databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()) {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName).child("participents").child(String.valueOf(Id));
                    databaseReference.removeValue();
                }

            }
        });

    }

    private void blockUserList(String userName , int Id) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");

        Call<BroadcastBlock> call = apiPost.getBroadcastBlock("Bearer " + signinValue, Id, userName);
        call.enqueue(new retrofit2.Callback<BroadcastBlock>() {
            @Override
            public void onResponse(Call<BroadcastBlock> call, Response<BroadcastBlock> response) {
                if (response.isSuccessful()) {
                    Log.e("BlockUser", new Gson().toJson(response.body()));
                    BroadcastBlock resObj = response.body();

                    if (resObj.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        BlockUsers(Id);
                        Toast.makeText(getApplicationContext(), "User is blocked", Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(ParticipantsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<BroadcastBlock> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(ParticipantsActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(ParticipantsActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    private void kickOutUser(int Id) {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/viewer/kick/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Integer idEvent = preferences.getInt("idEvent", 0);

        Call<KickoutUser> call = apiPost.getKickOut("Bearer " + signinValue, idEvent, Id);
        call.enqueue(new retrofit2.Callback<KickoutUser>() {
            @Override
            public void onResponse(Call<KickoutUser> call, Response<KickoutUser> response) {
                if (response.isSuccessful()) {
                    KickoutUser resObj = response.body();

                    if (resObj.getSuccess() == true) {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        BlockUsers(Id);
                        Log.e("EndStream", new Gson().toJson(response.body()));
                        Toast.makeText(getApplicationContext(), "User is kicked out", Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(ParticipantsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<KickoutUser> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(ParticipantsActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(ParticipantsActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });


    }

    @Override
    public void blockKick(String name , Integer Id) {
        blockUserList(name , Id);
    }

    @Override
    public void kick(Integer Id) {
        kickOutUser(Id);
    }

}