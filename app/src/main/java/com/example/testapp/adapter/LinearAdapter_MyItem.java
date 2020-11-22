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
import com.example.testapp.activity.CommodityInfoActivity;
import com.example.testapp.R;
import com.example.testapp.utils.Web;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class LinearAdapter_MyItem extends RecyclerView.Adapter<LinearAdapter_MyItem.LinearViewHolder> implements View.OnClickListener {

    private Context mContext;

    private HashMap<String, Object> dataMap;
    private List<HashMap<String, Object>> dataList;

    public LinearAdapter_MyItem(Context context, HashMap<String, Object> hashMap, List<HashMap<String, Object>> List) {
        this.mContext = context;
        dataMap = hashMap;
        dataList = List;
    }

    @NotNull
    @Override
    public LinearAdapter_MyItem.LinearViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_self, parent, false));
    }


    @Override
    public void onBindViewHolder(LinearAdapter_MyItem.LinearViewHolder holder, int position) {
        holder.textView.setText("￥".concat(String.valueOf(dataList.get(position).get("price"))));
        double temp = (double) dataList.get(position).get("id");
        holder.hide_id.setText(String.valueOf((int) temp));
        Glide.with(mContext).load(Web.PREFIX_LOCAL.val() + dataList.get(position).get("image")).into(holder.imageView);
        holder.imageView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return (int) dataMap.get("user_release_count");
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

    static class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView hide_id;
        private ImageView imageView;

        public LinearViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_self_image);
            textView = itemView.findViewById(R.id.price_self);
            hide_id = itemView.findViewById(R.id.hide_id_myItem);
        }
    }
}
