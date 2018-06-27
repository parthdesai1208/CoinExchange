package com.p1208.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rootMainactivity)
    ScrollView rootMainactivity;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 404) {
            Snackbar.make(rootMainactivity, "Account is deleted successfully",
                    Snackbar.LENGTH_LONG).show();
            Log.e("detect","delete");
        }
    }

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
