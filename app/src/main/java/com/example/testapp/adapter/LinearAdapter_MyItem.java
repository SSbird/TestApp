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
import com.example.testapp.activity.MainActivity;
import com.example.testapp.utils.Web;
import com.example.testapp.utils.XToastUtils;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

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
        double temp = (double) dataList.get(position).get("id");
        Glide.with(mContext).load(Web.PREFIX_LOCAL.val() + dataList.get(position).get("image")).into(holder.imageView);
        holder.textView.setText("￥".concat(String.valueOf(dataList.get(position).get("price"))));
        holder.hide_id.setText(String.valueOf((int) temp));
        holder.imageView.setOnClickListener(this);
        holder.btn_delete.setOnClickListener(this);
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
        if (view.getId() == R.id.btn_delete) {
            new MaterialDialog.Builder(mContext)
                    .content("要下架该商品吗？")
                    .positiveText("确认")
                    .negativeText("取消")
                    .onPositive((dialog, which) -> {
                        Novate novate = new Novate.Builder(mContext).baseUrl(Web.PREFIX_LOCAL.val()).build();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("gid", id);
                        novate.rxPost("/deleteItem", map, new RxStringCallback() {
                            @Override
                            public void onNext(Object tag, String response) {
                                XToastUtils.info("商品下架成功");
                                Intent intent = new Intent(mContext, MainActivity.class);
                                mContext.startActivity(intent);
                            }

                            @Override
                            public void onError(Object tag, Throwable e) {
                                XToastUtils.error("请求出错!");
                            }

                            @Override
                            public void onCancel(Object tag, Throwable e) {

                            }
                        });
                    })
                    .show();
        } else {
            Intent intent = new Intent(mContext, CommodityInfoActivity.class);
            intent.putExtra("id", id);
            mContext.startActivity(intent);
        }
    }

    static class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView hide_id;
        private ImageView imageView;
        private ButtonView btn_delete;

        public LinearViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_self_image);
            textView = itemView.findViewById(R.id.price_self);
            hide_id = itemView.findViewById(R.id.hide_id_myItem);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
