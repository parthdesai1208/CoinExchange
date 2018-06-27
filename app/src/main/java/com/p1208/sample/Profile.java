package com.p1208.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

import static com.p1208.sample.Signup.PICK_IMAGE;

public class Profile extends AppCompatActivity {
    @BindView(R.id.profile_name)
    TextView profileName;
    @BindView(R.id.profile_age)
    TextView profileAge;
    @BindView(R.id.profile_img_profile_page)
    ImageView profileImgProfilePage;
    @BindView(R.id.coin_gif)
    ImageView coinGif;
    @BindView(R.id.coin)
    TextView coin;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    @BindView(R.id.add_image1)
    ImageView addImage1;
    @BindView(R.id.horizontal_scrollview_for_linear_layout)
    HorizontalScrollView horizontalScrollviewForLinearLayout;
    @BindView(R.id.root_profile)
    ScrollView rootProfile;
    ImageView fiveImage;
    DatabaseReference userObject;
    List<user> userList = new ArrayList<>();
    List<String> searchData = new ArrayList<>();
    int tempBalance;
    String tempname;

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
                //region display user details like name,image,balance,age
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getName().trim().equals(SessionManager.getname(getBaseContext()))) {
                        tempBalance = userList.get(i).getBalance();
                        tempname = userList.get(i).getName();
                        profileName.setText(Html.fromHtml(
                                String.format(
                                        getResources().getString(R.string.profile_name), userList.get(i).getName())));
                        coin.setText(Html.fromHtml(
                                String.format(getResources().getString(R.string.coin_text), userList.get(i).getBalance())));
                        Glide.with(getApplicationContext())
                                .load(Uri.parse(userList.get(i).getImage()))
                                .apply(new RequestOptions().error(R.drawable.profile_logo))
                                .apply(RequestOptions.fitCenterTransform())
                                .into(profileImgProfilePage);
                        profileAge.setText(Html.fromHtml(
                                String.format(
                                        getResources().getString(R.string.profile_age), userList.get(i).getAge())));
                        return;
                    }
                }
                //endregion
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(rootProfile, databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private int getDetailsByUserName(String mname) {
        int mtemp = 0;
        searchData.clear();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getName().trim().equals(mname)) {
                searchData.add(0, String.valueOf(userList.get(i).getId()));
                searchData.add(1, String.valueOf(userList.get(i).getName()));
                searchData.add(2, String.valueOf(userList.get(i).getPassword()));
                searchData.add(3, String.valueOf(userList.get(i).getBalance()));
                searchData.add(4, String.valueOf(userList.get(i).getImage()));
                searchData.add(5, String.valueOf(userList.get(i).getAge()));
                mtemp = userList.get(i).getBalance();
            }
        }
        return mtemp;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utill.isConnected(this)) {
            new FancyGifDialog.Builder(this)
                    .setTitle("No internet")
                    .setMessage("You will not run this app without internet")
                    .setPositiveBtnText("OK")
                    .setPositiveBtnBackground("#6bc5dd")
                    .setGifResource(R.drawable.nointernet)
                    .setNegativeBtnBackground("#ffffff")
                    .isCancellable(false)
                    .OnPositiveClicked(new FancyGifDialogListener() {
                        @Override
                        public void OnClick() {
                            Intent a = new Intent(Intent.ACTION_MAIN);
                            a.addCategory(Intent.CATEGORY_HOME);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(a);
                        }
                    })
                    .build();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        userObject = FirebaseDatabase.getInstance().getReference("user");

        Glide.with(getApplicationContext())
                .asGif()
                .load(R.drawable.coin)
                .apply(RequestOptions.fitCenterTransform())
                .into(coinGif);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {

            case R.id.log_out:
                new FancyGifDialog.Builder(this)
                        .setTitle("Are you sure?")
                        .setMessage("You will be logged out.")
                        .setNegativeBtnText("Cancel")
                        .setNegativeBtnBackground("#E04343")
                        .setPositiveBtnText("Logout")
                        .setPositiveBtnBackground("#6bc5dd")
                        .setGifResource(R.drawable.running_out)
                        .isCancellable(false)
                        .OnPositiveClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                //clear session
                                SessionManager.clearsession(getBaseContext());
                                startActivity(new Intent(Profile.this, MainActivity.class));
                            }
                        })
                        .OnNegativeClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                //put blank here so by default dialog will dismiss
                            }
                        })
                        .build();
                return true;
            case R.id.deleteUser:
                new FancyGifDialog.Builder(this)
                        .setTitle("Are you sure?")
                        .setMessage("Your account will delete permanently")
                        .setNegativeBtnText("Cancel")
                        .setNegativeBtnBackground("#E04343")
                        .setPositiveBtnText("Delete")
                        .setPositiveBtnBackground("#6bc5dd")
                        .setGifResource(R.drawable.running_out)
                        .isCancellable(false)
                        .OnPositiveClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                //clear session
                                SessionManager.clearsession(getBaseContext());
                                //region delete user permanently
                                getDetailsByUserName(tempname);
                                DatabaseReference dR = FirebaseDatabase
                                        .getInstance()
                                        .getReference("user").child(searchData.get(0));
                                dR.removeValue();
                                //endregion
                                startActivityForResult(new
                                                Intent(Profile.this, MainActivity.class),
                                        404);

                            }
                        })
                        .OnNegativeClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                //put blank here so by default dialog will dismiss
                            }
                        })
                        .build();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private ArrayList<user> createnamedataforsearch() {
        ArrayList<user> items = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            if (!userList.get(i).getName().trim().equals(SessionManager.getname(getBaseContext()))) {
                items.add(new user(userList.get(i).getName()));
            }
        }
        return items;
    }

    @OnClick({R.id.add_image1})
    public void onViewClicked(final View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.add_image1:
                if (SessionManager.getimagecount(view.getContext()) + 1 > 4) {
                    Snackbar.make(rootProfile, "You cannot set more than 4 image", Snackbar.LENGTH_LONG)
                            .show();
                } else {

                    LayoutInflater layoutInflater =
                            (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    assert layoutInflater != null;
                    final View addView = layoutInflater.inflate(R.layout.image_layout, null);
                    addView.setId(SessionManager.getimagecount(view.getContext()) + 1);
                    fiveImage = addView.findViewById(R.id.five_image);
                    final ImageView cancelImage = addView.findViewById(R.id.cancelimage);
                    fiveImage.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            cancelImage.setVisibility(View.VISIBLE);
                            return true;
                        }
                    });
                    cancelImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelImage.setVisibility(View.INVISIBLE);
                            ((LinearLayout) addView.getParent()).removeView(addView);
                            SessionManager.setimagecount(view.getContext(), SessionManager.getimagecount(view.getContext()) - 1);
                        }
                    });

                    linearlayout.addView(addView);

                    intent = new Intent();
                    intent.setType(getString(R.string.image_type));
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), 1);
                }
                break;
            default:
                break;
        }
    }

    private void update(String id, String name, String password, int point, String image, int age) {
        user mUser = new user(id, name, password, point, image, age);
        userObject.child(id).setValue(mUser);
    }

    @OnClick(R.id.send_coin)
    public void onsendcoinbuttonClicked() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.enterpoint, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();

        final EditText editText = dialogView.findViewById(R.id.enterpointedittext);
        final Button okenterpoint = dialogView.findViewById(R.id.okbtnenterpoint);
        okenterpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tempBalance < Integer.parseInt(editText.getText().toString().trim())) {
                    editText.setText("");
                    editText.setHint("You have not enough balance");
                } else {
                    new SimpleSearchDialogCompat(v.getContext(), "Search...",
                            "Type your friend name here...", null,
                            createnamedataforsearch(),
                            new SearchResultListener<user>() {
                                @Override
                                public void onSelected(final BaseSearchDialogCompat dialog,
                                                       final user item, int position) {
                                    alertDialog.dismiss();
                                    AlertDialog.Builder DBsearch = new AlertDialog.Builder(dialog.getContext());
                                    LayoutInflater inflater = dialog.getLayoutInflater();
                                    final View dialogView = inflater.inflate(R.layout.searchuser, null);
                                    DBsearch.setView(dialogView);
                                    final AlertDialog adsearch = DBsearch.create();
                                    adsearch.show();
                                    TextView username = dialogView.findViewById(R.id.usernameinsearch);
                                    username.setText(item.getName());
                                    TextView balance = dialogView.findViewById(R.id.balanceinsearch);
                                    balance.setText("Balance : " + getDetailsByUserName(item.getName()));
                                    ImageView userimageinsearch = dialogView.findViewById(R.id.userimageinsearch);
                                    Glide.with(getApplicationContext())
                                             .load(Uri.parse(searchData.get(4).toString().trim()))
                                            .apply(RequestOptions.fitCenterTransform())
                                            .apply(new RequestOptions().error(R.drawable.profile_logo))
                                            .into(userimageinsearch);
                                    ImageView pay = dialogView.findViewById(R.id.payimage);
                                    pay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            adsearch.dismiss();
                                            if (!Utill.isConnected(v.getContext())) {
                                                new FancyGifDialog.Builder(Profile.this)
                                                        .setTitle("No internet")
                                                        .setMessage("You will not run this app without internet")
                                                        .setPositiveBtnText("OK")
                                                        .setPositiveBtnBackground("#6bc5dd")
                                                        .setGifResource(R.drawable.nointernet)
                                                        .isCancellable(false)
                                                        .setNegativeBtnBackground("#ffffff")
                                                        .OnPositiveClicked(new FancyGifDialogListener() {
                                                            @Override
                                                            public void OnClick() {
                                                                Intent a = new Intent(Intent.ACTION_MAIN);
                                                                a.addCategory(Intent.CATEGORY_HOME);
                                                                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(a);
                                                            }
                                                        })
                                                        .build();
                                            } else {
                                                //update receiver user
                                                int receiverOldBalance = Integer.parseInt(searchData.get(3).toString());
                                                int newBalance = Integer.parseInt(editText.getText().toString().trim());
                                                int updatedBalance = receiverOldBalance + newBalance;
                                                update(searchData.get(0).toString()
                                                        , searchData.get(1).toString(),
                                                        searchData.get(2).toString(),
                                                        updatedBalance,
                                                        searchData.get(4).toString(),
                                                        Integer.parseInt(searchData.get(5).trim()));
                                                //update sender user
                                                getDetailsByUserName(SessionManager.getname(dialog.getContext()));
                                                int senderOldBalance = Integer.parseInt(searchData.get(3).trim());
                                                updatedBalance = senderOldBalance - newBalance;
                                                update(searchData.get(0).toString(),
                                                        searchData.get(1).toString(),
                                                        searchData.get(2).toString(),
                                                        updatedBalance,
                                                        searchData.get(4).toString(),
                                                        Integer.parseInt(searchData.get(5).trim()));

                                                Snackbar.make(rootProfile, "Send Successfully", Snackbar.LENGTH_LONG)
                                                        .show();

                                            }
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });
        alertDialog.show();

    }

    @OnClick(R.id.editprofileimage)
    public void onEditProfileImageClicked() {
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
                    .into(profileImgProfilePage);
            getDetailsByUserName(tempname);
            update(searchData.get(0).trim(),
                    searchData.get(1).trim(),
                    searchData.get(2).trim(),
                    Integer.parseInt(searchData.get(3).trim()),
                    data.getData().toString(),
                    Integer.parseInt(searchData.get(5)));
        }
    }
}
