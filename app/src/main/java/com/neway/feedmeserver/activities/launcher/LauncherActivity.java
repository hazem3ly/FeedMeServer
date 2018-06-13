package com.neway.feedmeserver.activities.launcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.neway.feedmeserver.R;
import com.neway.feedmeserver.activities.home.HomeActivity;
import com.neway.feedmeserver.bases.BaseActivity;
import com.neway.feedmeserver.model.App;
import com.neway.feedmeserver.model.Navegator;
import com.neway.feedmeserver.model.User;
import com.neway.feedmeserver.widget.FButton;
import com.rengwuxian.materialedittext.MaterialEditText;


/**
 * Created by Hazem Ali
 * On 5/5/2018.
 */
public class LauncherActivity extends BaseActivity implements LauncherContract.View {

    FButton signInBtn, doSignBtn;
    LauncherPresenter mPresenter;

    View loginView, launcherView;

    ProgressBar progress_bar;

    MaterialEditText loginPasswordEt, loginPhoneEt;

    @Override
    protected int getContentResource() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void init(@Nullable Bundle state) {

        mPresenter = new LauncherPresenter();
        mPresenter.attach(this);


        progress_bar = findViewById(R.id.progress_bar);
        loginView = findViewById(R.id.login_screen);
        launcherView = findViewById(R.id.launcher_screen);

        loginPasswordEt = findViewById(R.id.sign_in_password_et);
        loginPhoneEt = findViewById(R.id.sign_in_phone_et);

        doSignBtn = findViewById(R.id.do_login);
        doSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginPhoneEt.getText().toString().isEmpty() && !loginPasswordEt.getText().toString().isEmpty())
                    mPresenter.doLogin(loginPhoneEt.getText().toString(), loginPasswordEt.getText().toString());
                else
                    Toast.makeText(LauncherActivity.this, "Empty Fields", Toast.LENGTH_SHORT).show();
            }
        });

        signInBtn = findViewById(R.id.login_btn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.performSignIn();
            }
        });

    }

    @Override
    public void onSignInCallback(boolean successful, User user) {
        if (successful) {
            App.setCurrentUser(user);
            Navegator.navigateToActivity(this, HomeActivity.class);
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Login Error ", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void switchToLogin() {
        loginView.setVisibility(View.VISIBLE);
        launcherView.setVisibility(View.GONE);
    }


    @Override
    public void switchToLauncher() {
        loginView.setVisibility(View.GONE);
        launcherView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (launcherView.getVisibility() == View.GONE)
            mPresenter.backToLauncher();
        else
            super.onBackPressed();
    }

    @Override
    public void showLoading() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progress_bar.setVisibility(View.GONE);
    }
}
