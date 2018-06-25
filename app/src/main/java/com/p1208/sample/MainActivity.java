package com.p1208.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check pref and display according
        if (SessionManager.isLoggedIn(this)) {
            startActivity(new Intent(MainActivity.this, Profile.class));
        } else {
            setContentView(R.layout.activity_landing_page);
            ButterKnife.bind(this);
        }
    }

    @OnClick(R.id.signup_btn_landing_page)
    public void onSignupButtonClick(View view) {
        startActivity(new Intent(view.getContext(), Signup.class));
    }

    @OnClick(R.id.login_btn)
    public void onLoginButtonMainActivityClick(View view) {
        startActivity(new Intent(view.getContext(), Login.class));
    }
}
