package com.neway.feedmeserver.activities.orderStatus;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.neway.feedmeserver.bases.BaseMvpPresenter;
import com.neway.feedmeserver.bases.BaseView;

/**
 * Created by Hazem Ali
 * On 5/7/2018.
 */
public class OrderStatusContract {


    // User actions. Presenter will implement
    interface Presenter extends BaseMvpPresenter<View> {
        void getOrders();
    }

    // Action callbacks. Activity/Fragment will implement
    interface View extends BaseView {
        void onDataLoaded(FirebaseRecyclerAdapter adapter);
        void finishActivity();
    }

}
