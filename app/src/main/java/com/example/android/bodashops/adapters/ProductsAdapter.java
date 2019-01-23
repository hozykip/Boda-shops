package com.example.android.bodashops.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.model.Product;
import com.example.android.bodashops.R;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Product> productList;
    private RequestOptions options;

    public ProductsAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;

        //request options for Glide
        options = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.image_error);

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_row_layout,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tv_price.setText("Ksh. "+product.getPrice());
        holder.tv_prodName.setText(product.getProductName());
        holder.tv_stock.setText("In stock: "+product.getQuantity());

        //load image from internet and set it to imageview using Glide
        String img_url = Config.IMG_BASE_URL + product.getImage();
        Glide.with(mCtx).load(img_url).apply(options).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView tv_prodName, tv_stock, tv_price;
        AppCompatImageView thumbnail;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_prodName = itemView.findViewById(R.id.product_name);
            tv_price = itemView.findViewById(R.id.price);
            tv_stock = itemView.findViewById(R.id.stock);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }
}
