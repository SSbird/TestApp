package com.example.testapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.testapp.MyApp;
import com.example.testapp.R;
import com.example.testapp.utils.Web;
import com.example.testapp.utils.XToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.popupwindow.good.GoodView;
import com.xuexiang.xui.widget.popupwindow.good.IGoodView;

import java.lang.ref.WeakReference;
import java.util.HashMap;

// 商品详情页
public class CommodityInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_want;
    private ImageView sc_box;
    private TextView ownerName;
    private TextView desc;
    private TextView price;
    private ImageView img;
    private Novate novate;
    private String id;
    private String owner;

    private IGoodView goodView;

    private static final String INFO_ITEM = "/getItemInfo";
    private static final String COLL_DEL = "/delUserCollect";
    private static final String COLL_ADD = "/addUserCollect";
    private static final String REQUIRE = "/addRequire";
    private static final int ADD_REQUEST_SUCCESS = 2;
    private static final int NOT_ENOUGH_MONEY = 3;
    private static final int REQUEST_DUPLICATE = 4;

    private Handler_Commodity handler_commodity;
    private int[] select = {R.drawable.ic_collection, R.drawable.ic_collection_checked};
    private int current = 0;

    private HashMap<String, Object> infoMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        handler_commodity = new Handler_Commodity(CommodityInfoActivity.this);
        goodView = new GoodView(this);
        novate = new Novate.Builder(CommodityInfoActivity.this).baseUrl(Web.PREFIX_LOCAL.val()).build();
        id = intent.getStringExtra("id");
        setContentView(R.layout.activity_commodity_info);
        TitleBar titleBar = findViewById(R.id.titleBar_commodity);
        titleBar.setLeftClickListener(v -> this.finish());
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("phone", MyApp.getApp_map().get("phone"));
        novate.rxPost(INFO_ITEM, map, new RxStringCallback() {   // 获取商品数据
            @Override
            public void onError(Object tag, Throwable e) {
                XToastUtils.error("获取商品信息出错!");
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }

            @Override
            public void onNext(Object tag, String response) {
                HashMap<String, Object> temp;
                temp = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>() {
                }.getType());
                infoMap = temp;
                handler_commodity.sendEmptyMessage(1);
            }
        });
    }

    @Override
    public void onClick(View view) {        // 点击事件
        switch (view.getId()) {
            case R.id.wxy:      // 求购按钮
                new MaterialDialog.Builder(this)
                        .content("确定求购该商品吗？")
                        .positiveText("确认")
                        .negativeText("取消")
                        .onPositive((dialog, which) -> {     // 同意选项的回调
                            HashMap<String, Object> sc_map;
                            sc_map = new HashMap<>();
                            sc_map.put("customer_id", MyApp.getApp_map().get("phone"));
                            sc_map.put("shopper_id", owner);
                            sc_map.put("good_id", id);
                            sendHttp(sc_map, REQUIRE);
                        }).show();
                break;
            case R.id.sc:       // 收藏按钮
                HashMap<String, Object> sc_map;
                sc_map = new HashMap<>();
                sc_map.put("phoneNumber", MyApp.getApp_map().get("phone"));
                sc_map.put("id", getIntent().getStringExtra("id"));
                current = (current + 1) % 2;
                sc_box.setImageResource(select[current]);
                if (current == 1) {
                    goodView.setTextInfo("收藏成功", Color.parseColor("#f66467"), 14)
                            .show(view);
                    sendHttp(sc_map, COLL_ADD);
                } else {
                    goodView.setTextInfo("取消收藏", Color.parseColor("#f66467"), 14)
                            .show(view);
                    sendHttp(sc_map, COLL_DEL);
                }
                break;
        }
    }

    void sendHttp(HashMap<String, Object> map, String url) {
        novate.rxPost(url, map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if ("success".equals(response)) {
                    handler_commodity.sendEmptyMessage(ADD_REQUEST_SUCCESS);
                } else if ("not_enough_money".equals(response)) {
                    handler_commodity.sendEmptyMessage(NOT_ENOUGH_MONEY);
                } else if ("duplicate".equals(response)) {
                    handler_commodity.sendEmptyMessage(REQUEST_DUPLICATE);
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                XToastUtils.error("请求出错!");
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }

    // 初始化面板组件
    void initComponents() {
        btn_want = findViewById(R.id.wxy);
        sc_box = findViewById(R.id.sc);
        ownerName = findViewById(R.id.owner_name);
        desc = findViewById(R.id.information);
        price = findViewById(R.id.com_price);
        img = findViewById(R.id.image_item);
    }

    static class Handler_Commodity extends Handler {
        WeakReference<CommodityInfoActivity> weakReference;

        public Handler_Commodity(CommodityInfoActivity commodityInfoActivity) {
            this.weakReference = new WeakReference<>(commodityInfoActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {        // 给界面填充数据
                CommodityInfoActivity com = weakReference.get();
                double temp = (double) com.infoMap.get("price");
                com.initComponents();
                com.sc_box.setOnClickListener(com);
                com.btn_want.setOnClickListener(com);
                com.ownerName.setText((String) com.infoMap.get("ownerName"));
                com.desc.setText((String) com.infoMap.get("desc"));
                com.price.setText("￥".concat(String.valueOf((int) temp)));
                com.owner = (String) com.infoMap.get("owner");
                if ("yes".equals(com.infoMap.get("collected"))) {   // 已收藏
                    com.current = 1;
                } else {
                    com.current = 0;
                }
                com.sc_box.setImageResource(com.select[com.current]);
                if (com.owner != null && com.owner.equals(MyApp.getApp_map().get("phone"))) {      // 商品物主不可求购自己物品
                    com.btn_want.setBackgroundColor(com.getResources().getColor(R.color.xui_btn_disable_color));
                    com.btn_want.setClickable(false);
                } else {
                    com.btn_want.setClickable(true);
                }
                Glide.with(com).load(Web.PREFIX_LOCAL.val() + com.infoMap.get("image")).into(com.img);
            } else if (msg.what == ADD_REQUEST_SUCCESS) {        // 求购成功
                XToastUtils.info("已发出求购请求");
            } else if (msg.what == NOT_ENOUGH_MONEY) {    // 重复求购
                XToastUtils.warning("求购失败: 您的余额不足");
            } else {    // 重复求购
                XToastUtils.warning("请勿重复求购");
            }
        }
    }
}