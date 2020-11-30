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
import com.example.testapp.entity.DummyContent.DummyItem;
import com.example.testapp.utils.Web;

import org.jetbrains.annotations.NotNull;

import java.util.List;

//    自定义适配器
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>
        implements View.OnClickListener {

    private List<DummyItem> mValues;
    private Context context;

    //    这里传入ITEMS
    public MyItemRecyclerViewAdapter(Context context, List<DummyItem> items) {
        this.context = context;
        mValues = items;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }
    
    //    填充商品列表内容
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Glide.with(context).load(Web.PREFIX_LOCAL.val() + holder.mItem.content_image).into(holder.mImageView);
        Glide.with(context).load(Web.PREFIX_LOCAL.val() + holder.mItem.content_image_owner).into(holder.mImageView_Owner);
        holder.mContentView.setText(holder.mItem.name);
        holder.mOwnerName.setText(holder.mItem.ownerName);
        holder.mPrice.setText("￥".concat(String.valueOf(holder.mItem.price)));
        holder.mID.setText(holder.mItem.id);
        holder.mImageView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    //    点击商品跳转到商品详情页面
    //  把隐藏的商品ID拿出来去Web后端做查询
    @Override
    public void onClick(View view) {
        View temp = (View) view.getParent();
        TextView text_temp = temp.findViewById(R.id.id_hide);
        Intent intent = new Intent(context, CommodityInfoActivity.class);
        intent.putExtra("id", String.valueOf(text_temp.getText()));
        context.startActivity(intent);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;      // 商品图片
        public final ImageView mImageView_Owner;      // 卖家头像
        public final TextView mContentView;     // 商品名
        public final TextView mOwnerName;       // 卖家名
        public final TextView mPrice;       // 卖家名
        public final TextView mID;       // 商品ID(设置为不可见)
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.content_image);
            mImageView_Owner = view.findViewById(R.id.content_image_owner);
            mContentView = view.findViewById(R.id.content);
            mOwnerName = view.findViewById(R.id.owner);
            mPrice = view.findViewById(R.id.price);
            mID = view.findViewById(R.id.id_hide);
        }
    }
}