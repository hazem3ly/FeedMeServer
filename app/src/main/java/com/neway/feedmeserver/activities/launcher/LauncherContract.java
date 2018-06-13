package com.neway.feedmeserver.activities.launcher;


import com.neway.feedmeserver.bases.BaseMvpPresenter;
import com.neway.feedmeserver.bases.BaseView;
import com.neway.feedmeserver.model.User;

/**
 * Created by Hazem Ali
 * On 5/5/2018.
 */
public class LauncherContract {

    // User actions. Presenter will implement
    interface Presenter extends BaseMvpPresenter<View> {
        void performSignIn();

        void backToLauncher();

        void doLogin(String phone, String password);
    }

    // Action callbacks. Activity/Fragment will implement
    interface View extends BaseView {
        void onSignInCallback(boolean successful, User user);

        void switchToLogin();

        void switchToLauncher();
    }

}
