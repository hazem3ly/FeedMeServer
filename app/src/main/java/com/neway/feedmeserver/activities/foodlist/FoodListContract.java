package com.neway.feedmeserver.activities.foodlist;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.neway.feedmeserver.bases.BaseMvpPresenter;
import com.neway.feedmeserver.bases.BaseView;
import com.neway.feedmeserver.model.Category;
import com.neway.feedmeserver.model.Food;

import java.util.List;

/**
 * Created by Hazem Ali
 * On 5/7/2018.
 */
public class FoodListContract {


    // User actions. Presenter will implement
    interface Presenter extends BaseMvpPresenter<View> {
        void loadFoodsList(String menuKey);
        void showAddDialog(Context context, String  categoryId);
        void showUpdateDialog(Context context,String key,Food food);
        void showDeleteDialog(Context context,String key);

    }

    // Action callbacks. Activity/Fragment will implement
    interface View extends BaseView {
        void onDataLoaded(FirebaseRecyclerAdapter adapter);
        void onItemClicked(Food model, String foodId);
    }

}
