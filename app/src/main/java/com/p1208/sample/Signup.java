package com.p1208.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class Signup extends AppCompatActivity {
    @BindView(R.id.eye)
    ImageView eye;
    @BindView(R.id.hide_eye)
    ImageView hide_eye;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.age)
    EditText age;

    public static final int PICK_IMAGE = 1;
    @BindView(R.id.profile)
    CircleImageView profile;
    @BindView(R.id.name)
    EditText name;

    DatabaseReference userObject;
    Uri filePath;
    @BindView(R.id.root_signup)
    ScrollView root;
    @BindView(R.id.con_layout_signup)
    ConstraintLayout conLayoutSignup;
    List<user> userList = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        userObject.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot mDataSnapshot : dataSnapshot.getChildren()){
                     user mUser  =  mDataSnapshot.getValue(user.class);
                     userList.add(mUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        age.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    OnSignUpClick(v);
                    Utill.hideSoftInput(Signup.this);
                    return true;
                }
                return false;
            }
        });
        //firebase instance
        userObject = FirebaseDatabase.getInstance().getReference("user");
    }

    @OnClick(R.id.eye)
    public void OnEyeClick() {
        eye.setVisibility(View.INVISIBLE);
        hide_eye.setVisibility(View.VISIBLE);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        password.setSelection(password.getText().length());
    }

    @OnClick(R.id.hide_eye)
    public void OnHideEyeClick(View view) {
        hide_eye.setVisibility(View.INVISIBLE);
        eye.setVisibility(View.VISIBLE);
        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        password.setSelection(password.getText().length());
    }

    @OnClick(R.id.signup_btn_sign_page)
    public void OnSignUpClick(View view) {
        //region validation
        if (filePath == null) {
            Snackbar.make(conLayoutSignup, "Please choose image", Snackbar.LENGTH_LONG)
                    .setAction("Set", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onProfileClicked();
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                    .show();
            return;
        }
        if (name.getText().toString().trim().length() <= 0) {
            name.setHint(getResources().getString(R.string.name));
            name.setHintTextColor(getResources().getColor(R.color.red_for_alert));
            name.setFocusable(true);
            return;
        }
        if (password.getText().toString().trim().length() <= 0) {
            name.setHint(getResources().getString(R.string.password));
            password.setHintTextColor(getResources().getColor(R.color.red_for_alert));
            password.setFocusable(true);
            return;
        }
        //endregion
        //region check duplicate user
        for(int i=0;i<userList.size();i++){
            if(userList.get(i).getName().trim().equals(name.getText().toString().trim())){
                name.setText("");
                name.setHint("User is already taken");
                name.setFocusable(true);
                return;
            }
        }
        //endregion
        //region pref operation -start,save

        //start user session
        SessionManager.startSession(view.getContext());
        //save pref
        SessionManager.savePref(view.getContext(), name.getText().toString().trim(),
                Integer.parseInt(age.getText().toString().trim()),
                password.getText().toString().trim(),
                filePath.toString());
        //endregion
        //region save to firebase
        String id = userObject.push().getKey();
        user mUser = new user(id,name.getText().toString(),password.getText().toString(),
                50,filePath.toString(),Integer.parseInt(age.getText().toString().trim()));
        userObject.child(id).setValue(mUser);
        //endregion
        startActivity(new Intent(Signup.this, Profile.class));
    }

    @OnClick(R.id.profile)
    public void onProfileClicked() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Glide.with(getBaseContext())
                    .load(data.getData())
                    .thumbnail(0.5f)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(profile);
            filePath = data.getData();
        }
    }

}
