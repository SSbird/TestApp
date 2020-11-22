package com.example.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.utils.Web;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

// 已购买商品列表适配器
public class LinearAdapter_MyBuy extends RecyclerView.Adapter<LinearAdapter_MyBuy.LinearViewHolder> {

    private Context mContext;
    private List<HashMap<String, Object>> dataList;

    public LinearAdapter_MyBuy(Context context, List<HashMap<String, Object>> List) {
        this.mContext = context;
        dataList = List;
    }

    @NotNull
    @Override
    public LinearAdapter_MyBuy.LinearViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.my_buy_item, parent, false));
    }


    @Override
    public void onBindViewHolder(LinearAdapter_MyBuy.LinearViewHolder holder, int position) {
        double temp = (double) dataList.get(position).get("price");
        holder.price.setText(String.valueOf(temp));
        holder.shopper_name.setText(String.valueOf(dataList.get(position).get("shopper_name")));
        Glide.with(mContext).load(Web.PREFIX_LOCAL.val() + dataList.get(position).get("shop_item")).into(holder.imageView);
        Glide.with(mContext).load(Web.PREFIX_LOCAL.val() + dataList.get(position).get("shopper_head")).into(holder.head_icon);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView price;
        private TextView shopper_name;
        private ImageView imageView;
        private ImageView head_icon;

        public LinearViewHolder(View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price_pay);
            shopper_name = itemView.findViewById(R.id.shopper_name);
            imageView = itemView.findViewById(R.id.shop_item_image);
            head_icon = itemView.findViewById(R.id.shopper_head_icon);
        }
    }
}
