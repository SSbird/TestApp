package com.example.testapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.activity.CommodityInfoActivity;
import com.example.testapp.activity.MainActivity;
import com.example.testapp.utils.Web;
import com.example.testapp.utils.XToastUtils;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class LinearAdapter_RequestList extends RecyclerView.Adapter<LinearAdapter_RequestList.LinearViewHolder> implements View.OnClickListener {

    private Context mContext;

    private String shopper_id;

    private List<HashMap<String, Object>> dataList;

    public LinearAdapter_RequestList(Context context, List<HashMap<String, Object>> dataList, String shopper_id) {
        this.mContext = context;
        this.dataList = dataList;
        this.shopper_id = shopper_id;
    }

    @NotNull
    @Override
    public LinearAdapter_RequestList.LinearViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_request, parent, false));
    }


    @Override
    public void onBindViewHolder(LinearAdapter_RequestList.LinearViewHolder holder, int position) {
        holder.textView.setText("￥".concat(String.valueOf(dataList.get(position).get("price"))));
        double temp = (double) dataList.get(position).get("id");
        holder.hide_id.setText(String.valueOf((int) temp));
        holder.hide_buyer_id.setText(String.valueOf(dataList.get(position).get("buyer_id")));
        holder.buyer_name.setText(String.valueOf(dataList.get(position).get("buyer_name")));
        Glide.with(mContext).load(Web.PREFIX_LOCAL.val() + dataList.get(position).get("image")).into(holder.imageView);
        Glide.with(mContext).load(Web.PREFIX_LOCAL.val() + dataList.get(position).get("head_icon")).into(holder.head_icon);
        holder.imageView.setOnClickListener(this);
        holder.button_accept.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        if(dataList == null){
            return 0;
        }
        return dataList.size();
    }

    //  跳转至对应商品页面
    @Override
    public void onClick(View view) {
        View temp = (View) view.getParent();
        TextView viewById = temp.findViewById(R.id.hide_id_request);
        switch (view.getId()) {
            case R.id.accept:
                new MaterialDialog.Builder(mContext)
                        .content("要接受求购请求吗？")
                        .positiveText("确认")
                        .negativeText("取消")
                        .onPositive((dialog, which) -> {     // 确认选项的回调
                            Novate novate = new Novate.Builder(mContext).baseUrl(Web.PREFIX_LOCAL.val()).build();
                            TextView buyer_id = temp.findViewById(R.id.hide_id_buyer);
                            TextView price = temp.findViewById(R.id.price_request);
                            String number = String.valueOf(price.getText()).replaceAll("[￥]", "");
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("customer_id", buyer_id.getText());
                            map.put("shopper_id", shopper_id);
                            map.put("good_id", String.valueOf(viewById.getText()));
                            map.put("price", Double.parseDouble(number));
                            novate.rxPost("/delRequire", map, new RxStringCallback() {
                                @Override
                                public void onNext(Object tag, String response) {
                                    XToastUtils.info("交易成功");
                                    mContext.startActivity(new Intent(mContext, MainActivity.class));
                                }

                                @Override
                                public void onError(Object tag, Throwable e) {
                                    XToastUtils.info("请求出错");
                                }

                                @Override
                                public void onCancel(Object tag, Throwable e) {

                                }
                            });
                        })
                        .show();
                break;
            case R.id.item_request_image:
                String id = String.valueOf(viewById.getText());
                Intent intent = new Intent(mContext, CommodityInfoActivity.class);
                intent.putExtra("id", id);
                mContext.startActivity(intent);
                break;
        }
    }

    static class LinearViewHolder extends RecyclerView.ViewHolder {
        private RadiusImageView head_icon;      // 买家头像
        private TextView textView;
        private TextView hide_id;
        private TextView hide_buyer_id;
        private TextView buyer_name;
        private ImageView imageView;
        private Button button_accept;

        public LinearViewHolder(View itemView) {
            super(itemView);
            head_icon = itemView.findViewById(R.id.buyer_head);
            imageView = itemView.findViewById(R.id.item_request_image);
            textView = itemView.findViewById(R.id.price_request);
            hide_id = itemView.findViewById(R.id.hide_id_request);
            buyer_name = itemView.findViewById(R.id.buyer_name);
            hide_buyer_id = itemView.findViewById(R.id.hide_id_buyer);
            button_accept = itemView.findViewById(R.id.accept);
        }
    }
}
