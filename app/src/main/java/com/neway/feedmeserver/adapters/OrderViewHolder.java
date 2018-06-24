package com.neway.feedmeserver.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.neway.feedmeserver.R;
import com.neway.feedmeserver.interfaces.OnItemClickListener;


public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, order_address;
    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        order_address = itemView.findViewById(R.id.order_address);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) itemClickListener.onClick(v, getAdapterPosition(), false);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select The Action");

        menu.add(0, 0, getAdapterPosition(), "Update");
        menu.add(0, 1, getAdapterPosition(), "Delete");
    }
}
