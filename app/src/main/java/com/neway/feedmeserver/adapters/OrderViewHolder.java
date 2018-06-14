package com.neway.feedmeserver.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.neway.feedmeserver.R;
import com.neway.feedmeserver.interfaces.OnItemClickListener;


public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
