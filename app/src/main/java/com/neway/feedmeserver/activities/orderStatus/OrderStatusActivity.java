package com.neway.feedmeserver.activities.orderStatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.neway.feedmeserver.R;
import com.neway.feedmeserver.activities.home.HomeActivity;
import com.neway.feedmeserver.activities.trackingOrderMap.TrackingOrderMapActivity;
import com.neway.feedmeserver.bases.BaseActivity;
import com.neway.feedmeserver.model.Category;
import com.neway.feedmeserver.model.Request;

/**
 * Created by Hazem Ali
 * On 5/7/2018.
 */
public class OrderStatusActivity extends BaseActivity implements OrderStatusContract.View {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    OrderStatusPresenter mPresenter;
    private FirebaseRecyclerAdapter adapter;

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
    public boolean onContextItemSelected(MenuItem item) {

        String title = item.getTitle().toString();
        switch (title) {
            case "Update":
                mPresenter.showUpdateDialog(OrderStatusActivity.this,adapter.getRef(item.getOrder()).
                        getKey(), (Request) adapter.getItem(item.getOrder()));
                break;
            case "Delete":
                mPresenter.showDeleteDialog(OrderStatusActivity.this,adapter.getRef(item.getOrder()).getKey());
                break;
        }


        return super.onContextItemSelected(item);
    }

    @Override
    public void onDataLoaded(FirebaseRecyclerAdapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnRequestClicked(Request request) {
        TrackingOrderMapActivity.currentRequest = request;
        startActivity(new Intent(this, TrackingOrderMapActivity.class));
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
