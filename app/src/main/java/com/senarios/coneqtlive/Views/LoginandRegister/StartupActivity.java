package com.senarios.coneqtlive.Views.LoginandRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.senarios.coneqtlive.R;
import com.senarios.coneqtlive.Views.OverView.OverViewActivity;


public class StartupActivity extends AppCompatActivity {
    private Button startedBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        startedBtn= findViewById(R.id.idLoginBtnLogin);

        initLayout();

    }
    private void initLayout() {
        startedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartupActivity.this, LoginActivity.class));
            }
        });

    }
}