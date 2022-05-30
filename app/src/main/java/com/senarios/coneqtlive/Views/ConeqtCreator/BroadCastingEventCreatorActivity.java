package com.senarios.coneqtlive.Views.ConeqtCreator;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.senarios.coneqtlive.Adapter.MessageAdapter;
import com.senarios.coneqtlive.Adapter.ParticipantsAdapter;
import com.senarios.coneqtlive.ApiServices;
import com.senarios.coneqtlive.Model.CompleteModel;
import com.senarios.coneqtlive.Model.MessageModel;
import com.senarios.coneqtlive.Model.ParticipantsModel;
import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.TouchBlackHoleView;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.models.ClientRoleOptions;
import io.agora.rtc.video.VideoCanvas;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BroadCastingEventCreatorActivity extends AppCompatActivity {

    // Fill the App ID of your project generated on Agora Console.
    private String appId = "f5b328dc4dfe41c7bfbe5e216b7c515e";
    // Fill the channel name.
    private String channelName;
    // Fill the temp token generated on Agora Console.
    private String token;

    ArrayList<String> tempy = new ArrayList<>();
    private RtcEngine mRtcEngine;
    private TextView nameTxtView, firstLetter , firstLetter1;

    // Viewers id,s
    private ImageView imageViewEye, close_stream, voice;
    private TextView viewer_tv;
    private TextView eventNameTxt, eventDesTxt;

    // Reactions id,s
    private EditText comments;
    private ImageView likeImg, heartImg, headerImage , headerImage1 ;

    private FrameLayout fl_local, fl_remote;

    private RelativeLayout liveLayout;
    private LinearLayout showMessageLayout, view_participants;
    private ImageView idHeaderCartBtn, personImage, idBelowArrow;


    private RelativeLayout cloneContainer, cloneContainer2 , nameContainer;
    int thumby = 0, hearty = 0;

    private ArrayList<MessageModel> arrayList;
    private ArrayList<ParticipantsModel> participantsModelArrayList = new ArrayList<>();
    private ParticipantsAdapter participantsAdapter;
    private MessageAdapter adapter;
    private RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbref;
    String userid;
    MessageModel messageModel;

    RelativeLayout idProgressBarRelative;
    LottieAnimationView lottieAnimationView;
    TouchBlackHoleView blackHoleView;

    String image;
    String idUser;
    String LastUser;


    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the remote host joining the channel to get the uid of the host.
        public void onUserJoined(int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Call setupRemoteVideo to set the remote video view after getting uid from the onUserJoined callback.
                    setupRemoteVideo(uid);
                }
            });
        }
    };

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    int j = 0;
    boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_casting_event_creator);

        channelName = getIntent().getStringExtra("Id");
        token = getIntent().getStringExtra("Agora_Token");
        dbref = FirebaseDatabase.getInstance().getReference();


        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            channelName = getIntent().getStringExtra("Id");
            token = getIntent().getStringExtra("Agora_Token");
            initializeAndJoinChannel(token, channelName);
        }

        imageViewEye = findViewById(R.id.imageEye);
        close_stream = findViewById(R.id.close_stream);
        viewer_tv = findViewById(R.id.TextViewLive);

        /// Relative Layout OF LottieAnimation
        idProgressBarRelative = findViewById(R.id.idProgressBarRelative);
        lottieAnimationView = findViewById(R.id.lottie_main);
        blackHoleView = findViewById(R.id.blackHole);

        eventNameTxt = findViewById(R.id.idEventNameBroadcast);
        eventDesTxt = findViewById(R.id.idEventDescriptionBroadcast);
        nameTxtView = findViewById(R.id.nameTextView);
        firstLetter = findViewById(R.id.idFirstLetter);
        firstLetter1 = findViewById(R.id.idFirstLetter1);
        headerImage = findViewById(R.id.headerImage);
        headerImage1 = findViewById(R.id.headerImage1);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String Name = preferences.getString("idName", "abc");
        String Des = preferences.getString("idDescription", "abc");
        eventNameTxt.setText(Name);
        eventDesTxt.setText(Des);

        String signinValue = preferences.getString("signedInKey", "abc");
        image = preferences.getString("imageName", "");
        idUser = preferences.getString("first_name", "abc");
        LastUser = preferences.getString("last_name", "abc");
        ///////// default userId and change to String.
        Integer IdUser = preferences.getInt("IdUser", 0);
        userid = Integer.toString(IdUser);

        if (signinValue.equals("Yes")) {
            nameTxtView.setText(idUser + " " + LastUser);

            if (image != null && !image.isEmpty()) {
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
                String s = test.substring(0, 1).toUpperCase();
                String s1 = test1.substring(0, 1).toUpperCase();
                firstLetter.setText(s + s1);
                firstLetter.setVisibility(View.VISIBLE);
            }
        }

        liveLayout = findViewById(R.id.abc);
        showMessageLayout = findViewById(R.id.showMessageLayout);
        view_participants = findViewById(R.id.view_participants);
        idHeaderCartBtn = findViewById(R.id.idHeaderCartBtn);

        view_participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BroadCastingEventCreatorActivity.this, ParticipantsActivity.class).putExtra("Id", channelName)
                        .putExtra("Agora_Token", token));
            }
        });

        close_stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        idHeaderCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show) {
                    idHeaderCartBtn.setImageDrawable(getDrawable(R.drawable.ic_up_downarrow));
                    showMessageLayout.setVisibility(View.VISIBLE);
                    show = false;
                } else {
                    idHeaderCartBtn.setImageDrawable(getDrawable(R.drawable.ic_down_arrow_video));
                    showMessageLayout.setVisibility(View.GONE);
                    show = true;
                }

            }
        });
        //Reactions Initializations
        likeImg = findViewById(R.id.likeImage);
        heartImg = findViewById(R.id.heartImage);

        // Relative Layout Initializations

        cloneContainer = findViewById(R.id.heart);

        cloneContainer2 = findViewById(R.id.like);

        nameContainer = findViewById(R.id.userReactionRL);


        messageModel = new MessageModel();
        recyclerView = findViewById(R.id.live_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final int[] size = {0};

        dbref = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName);
        dbref.child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MessageModel model = snapshot1.getValue(MessageModel.class);
                    arrayList.add(model);

                }

                if (j == 0) {
                    j = 1;
                    size[0] = arrayList.size();
                }
                if (size[0] != arrayList.size()) {
                    adapter = new MessageAdapter(arrayList, BroadCastingEventCreatorActivity.this);
                    recyclerView.setAdapter(adapter);

                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    adapter.notifyDataSetChanged();
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        comments = findViewById(R.id.idEdtEmployeeAddress);

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        dbref = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName).child("reactions");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot datasnapshot : snapshot.getChildren()) {

                        if (!tempy.contains(datasnapshot.getKey())) {
                            heartOnClick(datasnapshot.child("type").getValue().toString(), datasnapshot.child("url").getValue().toString() , datasnapshot.child("firstname").getValue().toString());
                                            tempy.add(datasnapshot.getKey());


                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference().child("streams")
                .child(channelName).child("participents").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int count = (int) snapshot.getChildrenCount();
                    viewer_tv.setText(String.valueOf(count));

                } else {
                    viewer_tv.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        dbref = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName);
        dbref.child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild("video")) {
                    dbref.child("status").child("video").setValue("enable");
                }
                if (!snapshot.hasChild("audio")) {
                    dbref.child("status").child("audio").setValue("unmute");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Cancel", error.getMessage());
            }
        });

    }

    private void closeDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView = layoutInflater.inflate(R.layout.close_popup, null, false);
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

        TextView alertTextView = inflatedView.findViewById(R.id.idGiftCardAlertTextView);
        TextView cancelBtn = inflatedView.findViewById(R.id.idCancelTxt);
        TextView endBtn = inflatedView.findViewById(R.id.idEndTxt);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
            }
        });
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.dismiss();
                mRtcEngine.leaveChannel();
                EndStream();

            }
        });
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 22) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                channelName = getIntent().getStringExtra("Id");
                token = getIntent().getStringExtra("Agora_Token");
                initializeAndJoinChannel(token, channelName);

            }
        }

    }

    private void initializeAndJoinChannel(String token, String channelName) {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), appId, mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("Check the error.");
        }

        // For a live streaming scenario, set the channel profile as BROADCASTING.
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        // Set the client role as AUDIENCE and the latency level as low latency.
        ClientRoleOptions clientRoleOptions = new ClientRoleOptions();
        clientRoleOptions.audienceLatencyLevel = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER, clientRoleOptions);

        // By default, video is disabled, and you need to call enableVideo to start a video stream.
        mRtcEngine.enableVideo();

        FrameLayout container = findViewById(R.id.local_video_view_container);

        // Call CreateRendererView to create a SurfaceView object and add it as a child to the FrameLayout.
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        // Pass the SurfaceView object to Agora so that it renders the local video.
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FILL, 0));
        mRtcEngine.startPreview();

        // Join the channel with a token.
        mRtcEngine.joinChannel(token, channelName, "", 0);
    }

    public void onLocalAudioMuteClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            iv.setSelected(false);
            iv.clearColorFilter();
            dbref = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName).child("status").child("audio");
            dbref.setValue("Unmute");
            iv.setImageResource(R.drawable.ic_audio_mute);

        } else {
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
            dbref = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName).child("status").child("audio");
            dbref.setValue("Mute");
            iv.setImageResource(R.drawable.ic_unmute_pic);
        }


        mRtcEngine.muteLocalAudioStream(iv.isSelected());
    }

    public void onLocalVideoMuteClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            iv.setSelected(false);
            iv.clearColorFilter();
            dbref = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName).child("status").child("video");
            dbref.setValue("enable");
            iv.setImageResource(R.drawable.ic_pause_video);

        } else {
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.DARKEN.MULTIPLY);
            dbref = FirebaseDatabase.getInstance().getReference().child("streams").child(channelName).child("status").child("video");
            dbref.setValue("disable");
            iv.setImageResource(R.drawable.ic_unmutevideo_pic);

        }

//        mRtcEngine.muteLocalVideoStream(iv.isSelected());
        mRtcEngine.muteLocalVideoStream(iv.isSelected());

        fl_local = (FrameLayout) findViewById(R.id.local_video_view_container);
        Log.wtf("on Local", "onLocalUserVideo->" + fl_local);
        SurfaceView surfaceView = (SurfaceView) fl_local.getChildAt(0);
        surfaceView.setZOrderMediaOverlay(!iv.isSelected());
    }

    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.local_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
    }

    public void heartOnClick(String tag , String url,String name) {
        disableAllParentsClip(tag.equals("heart") ? heartImg : heartImg);
        ImageView imageClone = cloneImage(tag);

        if(url !=null && !url.isEmpty()) {
            CircleImageView myImage = myCloneImage(tag,url,name);
            animateFlying(imageClone, myImage, null);
            animateFading(imageClone, myImage, null);
        } else {
            TextView nameText = myCloneName(name);
            animateFlying(imageClone, null, nameText);
            animateFading(imageClone, null, nameText);
        }
    }

    private ImageView cloneImage(String tag) {
        ImageView clone = new ImageView(BroadCastingEventCreatorActivity.this);
        clone.setLayoutParams(tag.equals("heart") ? heartImg.getLayoutParams() : heartImg.getLayoutParams());
        clone.setImageDrawable(tag.equals("heart") ? getResources().getDrawable(R.drawable.ic_heartbreak) : getResources().getDrawable(R.drawable.ic_heartbreak));
        if (tag.equals("heart")) {
            cloneContainer.addView(clone);
        } else {
            cloneContainer2.addView(clone);
        }
        return clone;
    }

    private CircleImageView myCloneImage(String tag , String url ,String name) {
        CircleImageView clone = new CircleImageView(BroadCastingEventCreatorActivity.this);
        clone.setLayoutParams(tag.equals("heart") ? heartImg.getLayoutParams() : heartImg.getLayoutParams());


        if(url !=null && !url.isEmpty()) {
            Glide.with(this)
                    .load(url) // image url
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            String test = name;
                            String s=test.substring(0,1).toUpperCase();
                            firstLetter1.setText(s);
                            firstLetter1.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(clone);
//            headerImage1.setVisibility(View.VISIBLE);
            firstLetter1.setVisibility(View.GONE);
        } else {
            String test = name;
            String s=test.substring(0,1).toUpperCase();
            firstLetter1.setText(s);
            firstLetter1.setVisibility(View.VISIBLE);
            headerImage1.setVisibility(View.GONE);
        }
        if (tag.equals("heart")) {
            cloneContainer.addView(clone);
        } else {
            cloneContainer2.addView(clone);
        }
        return clone;
    }

    private TextView myCloneName( String name) {
        TextView cloneText = new TextView(BroadCastingEventCreatorActivity.this);
        cloneText.setLayoutParams(heartImg.getLayoutParams());
        cloneText.setBackground(firstLetter1.getBackground());
        cloneText.setGravity(firstLetter1.getGravity());
        cloneText.setTextColor(ContextCompat.getColor(this,R.color.white));
        String test = name;
        String s=test.substring(0,1).toUpperCase();
        cloneText.setText(s);
        cloneText.setVisibility(View.VISIBLE);
        cloneContainer.addView(cloneText);
        return cloneText;
    }

    private void animateFading(ImageView imageView, CircleImageView myImage , TextView cloneText) {
        imageView.animate()
                .alpha(0f)
                .setDuration(2500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        cloneContainer.removeView(imageView);
                    }
                });
        if (myImage != null) {
            myImage.animate()
                    .alpha(0f)
                    .setDuration(2500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            cloneContainer.removeView(myImage);
                        }
                    });
        } else {
            cloneText.animate()
                    .alpha(0f)
                    .setDuration(2500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            cloneContainer.removeView(cloneText);
                        }
                    });
        }
    }

    private void animateFlying(ImageView imageView, CircleImageView myImage , TextView cloneText) {
        float x = 0f;
        float y = 0f;
        int r = new Random().nextInt(5000 - 1000) + 5000;
        float angle = 25f;

        Path path = new Path();
//        if (r % 2 == 0) {
//            path.addArc(new RectF(x, y - r, x + 2 * r, y + r), 180f, angle);
//        } else {
//            path.addArc(new RectF(x - 2 * r, y - r, x, y + r), 0f, -angle);
//        }
        path.addArc(new RectF(x - 4 * r, y - r, x, y + r), 1f, -angle);


        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, View.X, View.Y, path);
        objectAnimator.setDuration(8000);
        objectAnimator.start();

        ObjectAnimator objectAnimator2;
        if (myImage != null) {
            objectAnimator2 = ObjectAnimator.ofFloat(myImage, View.X, View.Y, path);
        } else {
            objectAnimator2 = ObjectAnimator.ofFloat(cloneText, View.X, View.Y, path);
        }
        objectAnimator2.setDuration(5000);
        objectAnimator2.start();

    }

    private void disableAllParentsClip(View view) {
        if (view.getParent() != null) {
            while (view.getParent() instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                viewGroup.setClipChildren(false);
                viewGroup.setClipToPadding(false);
                view = viewGroup;

            }
        }

    }

    private void EndStream() {
        idProgressBarRelative.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        blackHoleView.setVisibility(View.VISIBLE);
        blackHoleView.disableTouch(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://conneqt.senarios.co/content_creator/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiServices apiPost = retrofit.create(ApiServices.class);
        SharedPreferences preferences = getSharedPreferences("apiToken", MODE_PRIVATE);
        String signinValue = preferences.getString("apiToken", "abc");
        Integer idEvent = preferences.getInt("idEvent", 0);

        Call<CompleteModel> call = apiPost.getCompletedEvent("Bearer " + signinValue, idEvent);
        call.enqueue(new retrofit2.Callback<CompleteModel>() {
            @Override
            public void onResponse(Call<CompleteModel> call, Response<CompleteModel> response) {
                 if (response.isSuccessful()) {
                    CompleteModel resObj = response.body();

                    if (resObj.getSuccess() == true) {

                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);

                        FirebaseDatabase.getInstance().getReference().child("streams")
                                .child(channelName).child("participents").removeValue();
                        Log.e("EndStream", new Gson().toJson(response.body()));

                        startActivity(new Intent(BroadCastingEventCreatorActivity.this, CreateEventHistoryActivity.class));
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        idProgressBarRelative.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        blackHoleView.setVisibility(View.GONE);
                        blackHoleView.disableTouch(false);
                        Toast.makeText(BroadCastingEventCreatorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    idProgressBarRelative.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.GONE);
                    blackHoleView.setVisibility(View.GONE);
                    blackHoleView.disableTouch(false);
                    Toast.makeText(getApplicationContext(), "Process Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompleteModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(BroadCastingEventCreatorActivity.this, "Time out, Please try again", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(BroadCastingEventCreatorActivity.this, "Check you internet connection", Toast.LENGTH_SHORT).show();
                }
                idProgressBarRelative.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.GONE);
                blackHoleView.setVisibility(View.GONE);
                blackHoleView.disableTouch(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
            mRtcEngine.destroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
