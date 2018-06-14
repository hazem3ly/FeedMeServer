package com.neway.feedmeserver.activities.orderStatus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.neway.feedmeserver.R;
import com.neway.feedmeserver.bases.BaseActivity;

/**
 * Created by Hazem Ali
 * On 5/7/2018.
 */
public class OrderStatusActivity extends BaseActivity implements OrderStatusContract.View {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    OrderStatusPresenter mPresenter;

    @Override
    protected int getContentResource() {
        return R.layout.activity_order_status;
    }

    @Override
    protected void init(@Nullable Bundle state) {

        mPresenter = new OrderStatusPresenter();
        mPresenter.attach(this);

        recyclerView = findViewById(R.id.ordersList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mPresenter.getOrders();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public void onDataLoaded(FirebaseRecyclerAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
