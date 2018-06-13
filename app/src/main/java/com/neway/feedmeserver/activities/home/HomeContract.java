package com.neway.feedmeserver.activities.home;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.neway.feedmeserver.bases.BaseMvpPresenter;
import com.neway.feedmeserver.bases.BaseView;
import com.neway.feedmeserver.model.Category;

/**
 * Created by Hazem Ali
 * On 5/7/2018.
 */
public class HomeContract {


    // User actions. Presenter will implement
    interface Presenter extends BaseMvpPresenter<View> {
        void loadCategories();
    }

    // Action callbacks. Activity/Fragment will implement
    interface View extends BaseView {
        void onDataLoaded(FirebaseRecyclerAdapter adapter);

        void onItemClicked(String menuKey, Category model);
    }

}
