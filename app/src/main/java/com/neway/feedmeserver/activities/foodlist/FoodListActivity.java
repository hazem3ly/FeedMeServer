package com.neway.feedmeserver.activities.foodlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.neway.feedmeserver.R;
import com.neway.feedmeserver.activities.home.HomeActivity;
import com.neway.feedmeserver.bases.BaseActivity;
import com.neway.feedmeserver.model.Category;
import com.neway.feedmeserver.model.Food;
import com.neway.feedmeserver.model.Navegator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hazem Ali
 * On 5/7/2018.
 */
public class FoodListActivity extends BaseActivity implements FoodListContract.View {

    FoodListPresenter mPresenter;

    RecyclerView foods_recycler;
    LinearLayoutManager layoutManager;
    FloatingActionButton fab;
    String menuKey = null;
    private FirebaseRecyclerAdapter adapter;


    @Override
    protected int getContentResource() {
        return R.layout.activity_foods;
    }

    @Override
    protected void init(@Nullable Bundle state) {

        if (!getKey()) {
            finish();
        }

        mPresenter = new FoodListPresenter();
        mPresenter.attach(this);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        foods_recycler = findViewById(R.id.foods_list);
        foods_recycler.setHasFixedSize(true);
        foods_recycler.setLayoutManager(layoutManager);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showAddDialog(FoodListActivity.this, menuKey);

            }
        });

        mPresenter.loadFoodsList(menuKey);
    }


    private boolean getKey() {
        if (getIntent().hasExtra(Navegator.BUNDLE_DATA)) {
            Bundle data = getIntent().getBundleExtra(Navegator.BUNDLE_DATA);
            if (data != null && data.containsKey("KEY")) {
                menuKey = data.getString("KEY");
                return menuKey != null;
            }
        }
        return false;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public void onDataLoaded(FirebaseRecyclerAdapter adapter) {
        this.adapter = adapter;
        foods_recycler.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(Food model, String foodID) {

//        FoodDetailsActivity.SELECTED_FOOD = model;
//        FoodDetailsActivity.FOOD_ID = foodID;
//        Navegator.navigateToActivity(this, FoodDetailsActivity.class);
        Toast.makeText(this, model.getName(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        String title = item.getTitle().toString();
        switch (title) {
            case "Update":
                mPresenter.showUpdateDialog(FoodListActivity.this,adapter.getRef(item.getOrder()).
                        getKey(), (Food) adapter.getItem(item.getOrder()));
                break;
            case "Delete":
                mPresenter.showDeleteDialog(FoodListActivity.this,adapter.getRef(item.getOrder()).getKey());
                break;
        }


        return super.onContextItemSelected(item);
    }

}
