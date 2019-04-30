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
import com.example.android.bodashops.model.AccountModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AccountInfoAdapter extends RecyclerView.Adapter<AccountInfoAdapter.AccountInfoViewHolder>{

    private Context mCtx;
    private List<AccountModel> accountModelList;

    public AccountInfoAdapter(Context mCtx, List<AccountModel> accountModelList) {
        this.mCtx = mCtx;
        this.accountModelList = accountModelList;
    }

    @NonNull
    @Override
    public AccountInfoAdapter.AccountInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.account_info_previews,parent,false);

        final AccountInfoAdapter.AccountInfoViewHolder viewHolder = new AccountInfoAdapter.AccountInfoViewHolder(view);

        viewHolder.view_container.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mCtx, OrderDetailsActivity.class);
                        intent.putExtra(Config.ORDERID, accountModelList.get(viewHolder.getAdapterPosition()).getOrderId());
                        intent.putExtra(Config.ORDERCOMPLETED, true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mCtx.startActivity(intent);
                    }
                }
        );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountInfoAdapter.AccountInfoViewHolder holder, int position) {
        AccountModel accountModel = accountModelList.get(position);

        String amt = "Ksh. "+accountModel.getAmount();
        String id = "# "+accountModel.getOrderId();

        holder.amountTv.setText(amt);
        holder.idTv.setText(id);
        holder.dateTv.setText(accountModel.getDate());
    }

    @Override
    public int getItemCount() {
        return accountModelList.size();
    }

    class AccountInfoViewHolder extends RecyclerView.ViewHolder{

        TextView idTv;
        TextView dateTv;
        TextView amountTv;

        LinearLayout view_container;

        public AccountInfoViewHolder(@NonNull View itemView) {
            super(itemView);

            idTv = itemView.findViewById(R.id.idTvAIF);
            dateTv = itemView.findViewById(R.id.dateTvAIF);
            amountTv = itemView.findViewById(R.id.amountTvAIF);
            view_container = itemView.findViewById(R.id.parentLinearLayoutAIF);
        }
    }
}
