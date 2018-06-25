package com.p1208.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {
    @BindView(R.id.eye)
    ImageView eye;
    @BindView(R.id.hide_eye)
    ImageView hideEye;
    @BindView(R.id.password_login_page)
    EditText password;
    @BindView(R.id.name_login_page)
    EditText name;
    @BindView(R.id.con_layout_login)
    ConstraintLayout conLayoutLogin;
    DatabaseReference userObject;
    List<user> userList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        userObject = FirebaseDatabase.getInstance().getReference("user");

    }

    @Override
    protected void onStart() {
        super.onStart();
        userObject.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    user mUser = mDataSnapshot.getValue(user.class);
                    userList.add(mUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick({R.id.eye, R.id.hide_eye})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eye:
                eye.setVisibility(View.INVISIBLE);
                hideEye.setVisibility(View.VISIBLE);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                password.setSelection(password.getText().length());
                break;
            case R.id.hide_eye:
                hideEye.setVisibility(View.INVISIBLE);
                eye.setVisibility(View.VISIBLE);
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                password.setSelection(password.getText().length());
                break;
        }
    }

    @OnClick(R.id.login_btn_login_page)
    public void onLoginButtonLoginClicked(View mView) {
        //region validation
        if (name.getText().toString().trim().length() <= 0) {
            name.setHintTextColor(getResources().getColor(R.color.red_for_alert));
            name.setFocusable(true);
            return;
        }
        if (password.getText().toString().trim().length() <= 0) {
            password.setHintTextColor(getResources().getColor(R.color.red_for_alert));
            password.setFocusable(true);
            return;
        }
        //endregion
        /*SessionManager.getInstance().startSession();
        SessionManager.getInstance().savePref(name.getText().toString().trim(),
                0,
                password.getText().toString().trim(),
                "");*/
        SessionManager.startSession(mView.getContext());
        SessionManager.savelogin(mView.getContext(), name.getText().toString().trim()
                , password.getText().toString().trim());
        //region check user in firebase database
        if (!userList.isEmpty()) {
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getName().trim().equals(name.getText().toString().trim())
                        && userList.get(i).getPassword().equals(password.getText().toString().trim())) {
                    startActivity(new Intent(Login.this, Profile.class));
                    return;
                }
            }
        }
        //endregion
        Snackbar.make(conLayoutLogin, "Couldn't login", Snackbar.LENGTH_LONG)
                .setAction("Sign up", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Login.this, Signup.class));
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }
}
