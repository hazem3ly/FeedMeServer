package com.neway.feedmeserver.activities.orderStatus;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
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
                requests) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, final Request model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(App.covertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.order_address.setText(model.getAddress());

                viewHolder.setItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        getView().OnRequestClicked(model);
                    }
                });

            }
        };

        getView().hideLoading();
        getView().onDataLoaded(adapter);
    }

    @Override
    public void showUpdateDialog(Context context, final String key, final Request request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Request");
        builder.setMessage("Please Change Order Status");
        View view = LayoutInflater.from(context).inflate(R.layout.update_order_layout, null);
        final MaterialSpinner spinner = view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed", "On My Way", "Shipped");

        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request.setStatus(String.valueOf(spinner.getSelectedIndex()));
                requests.child(key).setValue(request);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    @Override
    public void showDeleteDialog(Context context, final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Request");
        builder.setMessage("Are You Sure Delete This Request");

        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                requests.child(key).removeValue();

            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


}
