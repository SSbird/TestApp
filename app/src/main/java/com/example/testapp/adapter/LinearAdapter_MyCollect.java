package com.example.testapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.activity.CommodityInfoActivity;
import com.example.testapp.utils.Web;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

// 收藏列表适配器
public class LinearAdapter_MyCollect extends RecyclerView.Adapter<LinearAdapter_MyCollect.LinearViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<HashMap<String, Object>> dataList;

    public LinearAdapter_MyCollect(Context context, List<HashMap<String, Object>> List) {
        this.mContext = context;
        dataList = List;
    }

    @NotNull
    @Override
    public LinearAdapter_MyCollect.LinearViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_collect, parent, false));
    }


    @Override
    public void onBindViewHolder(LinearAdapter_MyCollect.LinearViewHolder holder, int position) {
        double temp = (double) dataList.get(position).get("id");
        Glide.with(mContext).load(Web.PREFIX_LOCAL.val() + dataList.get(position).get("image")).into(holder.imageView);
        holder.price_self.setText("￥".concat(String.valueOf(dataList.get(position).get("price"))));
        holder.itemName.setText(String.valueOf(dataList.get(position).get("name")));
        holder.hide_id.setText(String.valueOf((int) temp));
        holder.imageView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView price_self;
        private TextView itemName;
        private TextView hide_id;
        private ImageView imageView;

        public LinearViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.collectItem_name);
            imageView = itemView.findViewById(R.id.item_self_image);
            price_self = itemView.findViewById(R.id.price_self);
            hide_id = itemView.findViewById(R.id.hide_id_myItem);
        }
    }

    @Override
    public void onClick(View view) {
        View temp = (View) view.getParent();
        TextView viewById = temp.findViewById(R.id.hide_id_myItem);
        String id = String.valueOf(viewById.getText());
        Intent intent = new Intent(mContext, CommodityInfoActivity.class);
        intent.putExtra("id", id);
        mContext.startActivity(intent);
    }
}
