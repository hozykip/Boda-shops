package com.example.android.bodashops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bodashops.R;
import com.example.android.bodashops.model.ProductAttributesModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAttributesAdapter extends RecyclerView.Adapter<ProductAttributesAdapter.ProductAttributesViewHolder>
{
    private Context mCtx;
    private List<ProductAttributesModel> productAttributesList;
    ProgressBar bar;

    public ProductAttributesAdapter(Context mCtx, List<ProductAttributesModel> productAttributesList, ProgressBar progressBar) {
        this.mCtx = mCtx;
        this.productAttributesList = productAttributesList;
        this.bar = progressBar;

    }

    @NonNull
    @Override
    public ProductAttributesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.product_attributes_rows,parent,false);

        return new ProductAttributesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAttributesViewHolder holder, int position) {
        ProductAttributesModel productAttributes = productAttributesList.get(position);

        holder.tv_attr.setText(productAttributes.getAttribute());
        holder.tv_val.setText(productAttributes.getValue());

        bar.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return productAttributesList.size();
    }

    class ProductAttributesViewHolder extends RecyclerView.ViewHolder{

        TextView tv_attr, tv_val;
        LinearLayout view_container;

        public ProductAttributesViewHolder(@NonNull View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.item_row_container);
            tv_attr = itemView.findViewById(R.id.tv_attribute_prod_attrs);
            tv_val = itemView.findViewById(R.id.tv_value_prod_attrs);
        }
    }
}
