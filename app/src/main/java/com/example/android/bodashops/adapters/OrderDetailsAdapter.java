package com.example.android.bodashops.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;
import com.example.android.bodashops.activities.OrderDetailsActivity;
import com.example.android.bodashops.model.OrderDetailsModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrdersViewHolder>
{
    private Context mCtx;
    private List<OrderDetailsModel> orderdetsList;
    private RequestOptions options;

    public OrderDetailsAdapter(Context mCtx, List<OrderDetailsModel> ordersList)
    {
        this.mCtx = mCtx;
        this.orderdetsList = ordersList;

        options = new RequestOptions().centerCrop().placeholder(R.drawable.ic_image_black_24dp).error(R.drawable.ic_image_black_24dp);
    }

    public OrderDetailsAdapter.OrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.orderproductrow,parent,false);

        final OrderDetailsAdapter.OrdersViewHolder viewHolder = new OrderDetailsAdapter.OrdersViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position)
    {
        OrderDetailsModel order = orderdetsList.get(position);

        holder.prodNameView.setText(order.getProdName());
        holder.totalPriceView.setText(order.getPrice());
        holder.qtyView.setText(order.getQty());

        String imgUrl = Config.IMG_BASE_URL + order.getImage();
        Glide.with(mCtx).load(imgUrl).apply(options).into(holder.imageView);

    }

    public int getItemCount() {
        return orderdetsList.size();
    }

    class OrdersViewHolder extends RecyclerView.ViewHolder
    {
        TextView prodNameView;
        TextView qtyView;
        TextView totalPriceView;
        ImageView imageView;

        LinearLayout view_container;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            prodNameView = itemView.findViewById(R.id.prodNameOrderdets);
            qtyView = itemView.findViewById(R.id.qtyOrderdets);
            totalPriceView = itemView.findViewById(R.id.totalProdPriceOrderdets);
            imageView = itemView.findViewById(R.id.imageOrderProd);
        }
    }
}
