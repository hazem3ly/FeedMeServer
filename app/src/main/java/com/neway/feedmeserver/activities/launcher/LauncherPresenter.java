package com.neway.feedmeserver.activities.launcher;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neway.feedmeserver.bases.BasePresenter;
import com.neway.feedmeserver.model.User;

/**
 * Created by Hazem Ali
 * On 5/5/2018.
 */
public class LauncherPresenter extends BasePresenter<LauncherContract.View> implements LauncherContract.Presenter {

    private final FirebaseDatabase database;
    private final DatabaseReference tableUser;

    public LauncherPresenter() {
        database = FirebaseDatabase.getInstance();
        tableUser = database.getReference("user");
    }

    @Override
    public void performSignIn() {
        getView().switchToLogin();
    }


    @Override
    public void backToLauncher() {
        getView().switchToLauncher();
    }

    @Override
    public void doLogin(String phone, String password) {
        getView().showLoading();
        login(phone, password);
    }


    private void login(final String phone, final String password) {
        tableUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(phone).exists()) {
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    if (user != null && user.getPassword().equals(password)
                            && Boolean.parseBoolean(user.getIsStaff())) {
                        user.setPhone(phone);
                        getView().onSignInCallback(true, user);
                        getView().hideLoading();
                    } else {
                        getView().hideLoading();
                        getView().onSignInCallback(false, null);
                    }
                } else {
                    getView().hideLoading();
                    getView().onSignInCallback(false, null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                getView().hideLoading();
                getView().onSignInCallback(false, null);
            }
        });
    }


}
