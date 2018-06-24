package com.neway.feedmeserver.activities.orderStatus;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.neway.feedmeserver.bases.BaseMvpPresenter;
import com.neway.feedmeserver.bases.BaseView;
import com.neway.feedmeserver.model.Category;
import com.neway.feedmeserver.model.Request;

/**
 * Created by Hazem Ali
 * On 5/7/2018.
 */
public class OrderStatusContract {


    // User actions. Presenter will implement
    interface Presenter extends BaseMvpPresenter<View> {
        void getOrders();
        void showUpdateDialog(Context context, String key, Request request);
        void showDeleteDialog(Context context,String key);
    }

    // Action callbacks. Activity/Fragment will implement
    interface View extends BaseView {
        void onDataLoaded(FirebaseRecyclerAdapter adapter);
        void OnRequestClicked(Request request);
        void finishActivity();
    }

}
