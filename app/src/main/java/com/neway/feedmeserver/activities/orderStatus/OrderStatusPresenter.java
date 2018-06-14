package com.neway.feedmeserver.activities.orderStatus;

import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neway.feedmeserver.R;
import com.neway.feedmeserver.adapters.OrderViewHolder;
import com.neway.feedmeserver.bases.BasePresenter;
import com.neway.feedmeserver.interfaces.OnItemClickListener;
import com.neway.feedmeserver.model.App;
import com.neway.feedmeserver.model.Request;
import com.neway.feedmeserver.model.User;

/**
 * Created by Hazem Ali
 * On 5/7/2018.
 */
public class OrderStatusPresenter extends BasePresenter<OrderStatusContract.View> implements OrderStatusContract.Presenter {
    FirebaseDatabase database;
    DatabaseReference requests;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    public OrderStatusPresenter() {
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
    }

    @Override
    public void getOrders() {
        getView().showLoading();
        User user = App.getCurrentUser();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout_item,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(user.getPhone())) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(covertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.order_address.setText(model.getAddress());

                viewHolder.setItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });

            }
        };

        getView().hideLoading();
        getView().onDataLoaded(adapter);
    }

    private String covertCodeToStatus(String status) {
        switch (status) {
            case "0":
                return "Placed";
            case "1":
                return "On It's Way";
            default:
                return "Shipped";
        }
    }


}
