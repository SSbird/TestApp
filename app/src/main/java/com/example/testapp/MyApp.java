package com.example.testapp;

import android.app.Application;

import com.xuexiang.xhttp2.XHttpSDK;
import com.xuexiang.xui.XUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyApp extends Application {

    private static HashMap<String,Object> app_map;
    private static List<HashMap<String,Object>> user_item_list;
    private static List<HashMap<String,Object>> user_coll_list;

    @Override
    public void onCreate() {
        super.onCreate();
        app_map = new HashMap<>();
        user_item_list = new ArrayList<>();
        XUI.init(this);
        XUI.debug(true);
        XHttpSDK.init(this);
        XHttpSDK.debug("XHttp");
        XHttpSDK.setBaseUrl("http://10.0.2.2");
    }

    public HashMap<String, Object> getApp_map() {
        return app_map;
    }

    public static List<HashMap<String, Object>> getUser_item_list() {
        return user_item_list;
    }

    public static void setUser_item_list(List<HashMap<String, Object>> user_item_list) {
        MyApp.user_item_list = user_item_list;
    }

    public static void setUser_coll_list(List<HashMap<String, Object>> user_coll_list) {
        MyApp.user_coll_list = user_coll_list;
    }

    public static List<HashMap<String, Object>> getUser_coll_list() {
        return user_coll_list;
    }
}
