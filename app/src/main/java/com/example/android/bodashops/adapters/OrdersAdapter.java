package com.example.android.bodashops.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;
import com.example.android.bodashops.activities.OrderDetailsActivity;
import com.example.android.bodashops.model.OrdersModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>
{
    private Context mCtx;
    private List<OrdersModel> ordersList;
    //private RequestOptions options;
    //ProgressBar bar;

    public OrdersAdapter(Context mCtx, List<OrdersModel> ordersList) {
        this.mCtx = mCtx;
        this.ordersList = ordersList;

    }

    @Override
    public OrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.pending_orders_view,parent,false);

        final OrdersViewHolder viewHolder = new OrdersViewHolder(view);

        viewHolder.view_container.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mCtx, OrderDetailsActivity.class);
                        intent.putExtra(Config.ORDERID, ordersList.get(viewHolder.getAdapterPosition()).getOrderId());
                        intent.putExtra(Config.BUYERNAME, ordersList.get(viewHolder.getAdapterPosition()).getBuyerName());
                        intent.putExtra(Config.ORDERTOTALPRICE, ordersList.get(viewHolder.getAdapterPosition()).getTotalOrderPrice());
                        intent.putExtra(Config.ORDERLOCATION, ordersList.get(viewHolder.getAdapterPosition()).getOrderLocation());
                        intent.putExtra(Config.BUYERPHONE, ordersList.get(viewHolder.getAdapterPosition()).getBuyerPhone());
                        intent.putExtra(Config.ORDERTIME, ordersList.get(viewHolder.getAdapterPosition()).getOrderTime());
                        intent.putExtra(Config.ITEMSCOUNT, ordersList.get(viewHolder.getAdapterPosition()).getOrdersCount());

                        String orderCompletion = ordersList.get(viewHolder.getAdapterPosition()).getOrderCompletion();
                        intent.putExtra(Config.ORDERCOMPLETED, orderCompletion);

                        intent.putExtra(Config.ORDERCOMPLETED, ordersList.get(viewHolder.getAdapterPosition()).getOrderCompletion());
                        mCtx.startActivity(intent);
                    }
                }
        );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrdersAdapter.OrdersViewHolder holder, int position) {
        OrdersModel order = ordersList.get(position);

        holder.tv_orderId.setText(order.getOrderId());
        holder.tv_buyerName.setText(order.getBuyerName());
        holder.tv_orderStatus.setText(order.getOrderCompletion());
        holder.tv_buyerPhone.setText(order.getBuyerPhone());
        holder.tv_dateOfBooking.setText(order.getOrderTime());
        holder.tv_orderLocation.setText(order.getOrderLocation());
        holder.tv_price.setText(order.getTotalOrderPrice());
        holder.tv_itemCount.setText(order.getOrdersCount());

    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class OrdersViewHolder extends RecyclerView.ViewHolder{

        TextView tv_orderId;
        TextView tv_dateOfBooking;
        TextView tv_buyerName;
        TextView tv_buyerPhone;
        TextView tv_orderLocation;
        TextView tv_itemCount;
        TextView tv_price;
        TextView tv_orderStatus;

        LinearLayout view_container;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_orderId = itemView.findViewById(R.id.orderId);
            tv_dateOfBooking = itemView.findViewById(R.id.dateOfBooking);
            tv_buyerName = itemView.findViewById(R.id.orderPerson);
            tv_buyerPhone = itemView.findViewById(R.id.phoneNoField);
            tv_orderLocation = itemView.findViewById(R.id.location);
            tv_itemCount = itemView.findViewById(R.id.itemsCount);
            tv_price = itemView.findViewById(R.id.orderTotalPrice);
            tv_orderStatus = itemView.findViewById(R.id.orderStatus);
            view_container = itemView.findViewById(R.id.ordersContainer);
        }
    }
}
