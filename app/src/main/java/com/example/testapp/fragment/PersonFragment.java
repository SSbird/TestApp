package com.example.testapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.testapp.MyApp;
import com.example.testapp.R;
import com.example.testapp.activity.MyBuyActivity;
import com.example.testapp.activity.MyCollectActivity;
import com.example.testapp.activity.MyItemActivity;
import com.example.testapp.activity.MyMoneyActivity;
import com.example.testapp.activity.RequestListActivity;
import com.example.testapp.utils.Web;
import com.example.testapp.utils.XToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonFragment extends Fragment implements View.OnClickListener {

    private Novate novate;
    //    private MyApp app;
    //        app = (MyApp) getActivity().getApplication();

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView headIcon = view.findViewById(R.id.riv_head_pic);
        String phone = (String) MyApp.getApp_map().get("phone");
        novate = new Novate.Builder(getActivity()).baseUrl(Web.PREFIX_LOCAL.val()).build();
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        novate.rxPost("/getUserDisplay", map, new RxStringCallback() {
            @Override
            public void onError(Object tag, Throwable e) {
                XToastUtils.error("请求出错!");
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
            }

            @Override
            public void onNext(Object tag, String response) {
                if ("null".equals(response)) {      // 没有设置头像
                    Glide.with(PersonFragment.this).load(R.drawable.ic_default_head).into(headIcon);
                } else {
                    HashMap<String, Object> map = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>() {
                    }.getType());
                    TextView username = view.findViewById(R.id.person_username);
                    username.setText(String.valueOf(map.get("username")));
                    Glide.with(PersonFragment.this).load(Web.PREFIX_LOCAL.val() + map.get("head_icon")).into(headIcon);
                }
            }
        });
        initComponents();
    }

    //  跳转页面
    @Override
    public void onClick(View view) {
        switch (view.getId()) {     //  求购请求
            case R.id.customer_Request:
                HashMap<String, Object> requestMap = new HashMap<>();
                requestMap.put("shopper_id", MyApp.getApp_map().get("phone"));
                sendHttp("/getRequireList", requestMap);
                break;
            case R.id.myItem:       //  我发布的
                HashMap<String, Object> release_map = new HashMap<>();
                release_map.put("phoneNumber", MyApp.getApp_map().get("phone"));
                sendHttp("/getSelfItemCount", release_map);
                break;
            case R.id.myBuy:        //  我购买的
                HashMap<String, Object> map = new HashMap<>();
                map.put("phoneNumber", MyApp.getApp_map().get("phone"));
                sendHttp("/getSelfBuyItems", map);
                break;
            case R.id.myCollect:     //  我收藏的
                HashMap<String, Object> coll_map = new HashMap<>();
                coll_map.put("phoneNumber", MyApp.getApp_map().get("phone"));
                sendHttp("/getSelfCollectItems", coll_map);
                break;
            case R.id.self_wallet:      // 钱包
                HashMap<String, Object> wallet_map = new HashMap<>();
                wallet_map.put("phone", MyApp.getApp_map().get("phone"));
                sendHttp("/checkMyWallet", wallet_map);
                break;
        }
    }

    void initComponents() {
        Activity act = getActivity();
        if (act != null) {
            SuperTextView textView_myItem = act.findViewById(R.id.myItem);
            SuperTextView textView_myBuy = act.findViewById(R.id.myBuy);
            SuperTextView textView_myCollect = act.findViewById(R.id.myCollect);
            SuperTextView textView_myWallet = act.findViewById(R.id.self_wallet);
            SuperTextView textView_request = act.findViewById(R.id.customer_Request);
            textView_myItem.setOnClickListener(this);
            textView_myBuy.setOnClickListener(this);
            textView_myCollect.setOnClickListener(this);
            textView_myWallet.setOnClickListener(this);
            textView_request.setOnClickListener(this);
        }
    }

    void sendHttp(String url, HashMap<String, Object> map) {
        novate.rxPost(url, map, new RxStringCallback() {
            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }

            @Override
            public void onNext(Object tag, String response) {
                Intent intent;
                if ("/getSelfCollectItems".equals(url)) {
                    ArrayList<HashMap<String, Object>> mapList = new Gson().fromJson(response, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                    }.getType());
                    intent = new Intent(getActivity(), MyCollectActivity.class);
                    MyApp.setUser_coll_list(mapList);
                    startActivity(intent);
                } else if ("/checkMyWallet".equals(url)) {
                    intent = new Intent(getActivity(), MyMoneyActivity.class);
                    double temp = Double.parseDouble(response);
                    intent.putExtra("money", temp);
                    startActivity(intent);
                } else if ("/getSelfBuyItems".equals(url)) {
                    intent = new Intent(getActivity(), MyBuyActivity.class);
                    intent.putExtra("mapList", response);
                    startActivity(intent);
                } else if ("/getSelfItemCount".equals(url)) {
                    List<HashMap<String, Object>> mapList = new Gson().fromJson(response, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                    }.getType());
                    MyApp.getApp_map().put("user_release_count", mapList.size());
                    MyApp.setUser_item_list(mapList);
                    intent = new Intent(getActivity(), MyItemActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), RequestListActivity.class);
                    intent.putExtra("mapList", response);
                    startActivity(intent);
                }
            }
        });
    }

}
