package com.example.testapp.entity;

import android.content.Context;

import com.example.testapp.utils.Web;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// 商品实体
public class DummyContent {

    public List<DummyItem> items;
    private List<HashMap<String, Object>> itemList;

    private Context context;

    public DummyContent(Context context) {
        this.context = context;
    }

    public boolean init() {
        Novate novate = new Novate.Builder(context).baseUrl(Web.PREFIX_LOCAL.val()).build();
        items = new ArrayList<>();
        itemList = new ArrayList<>();
        novate.rxGet("/itemList", new HashMap<>(), new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                itemList = new Gson().fromJson(response, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                }.getType());
                HashMap<String, Object> map;
                for (int i = 0; i < itemList.size(); i++) {
                    map = itemList.get(i);
                    double temp = (double) map.get("id");
                    double price = (double) map.get("price");
                    String name = (String) map.get("name");
                    String ownerName = (String) map.get("ownerName");
                    String content_image = (String) map.get("content_image");
                    String content_image_owner = (String) map.get("content_image_owner");
                    String id = String.valueOf((int) temp);
                    addItem(createDummyItem(name, content_image, ownerName, content_image_owner, price, id));
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
        return true;
    }

    public boolean init(String param) {
        Novate novate = new Novate.Builder(context).baseUrl(Web.PREFIX_LOCAL.val()).build();
        items = new ArrayList<>();
        itemList = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("param", param);
        novate.rxPost("/itemList", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                itemList = new Gson().fromJson(response, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                }.getType());
                HashMap<String, Object> map;
                for (int i = 0; i < itemList.size(); i++) {
                    map = itemList.get(i);
                    double temp = (double) map.get("id");
                    double price = (double) map.get("price");
                    String name = (String) map.get("name");
                    String ownerName = (String) map.get("ownerName");
                    String content_image = (String) map.get("content_image");
                    String content_image_owner = (String) map.get("content_image_owner");
                    String id = String.valueOf((int) temp);
                    addItem(createDummyItem(name, content_image, ownerName, content_image_owner, price, id));
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
        return true;
    }

    private void addItem(DummyItem item) {
        items.add(item);
    }


    private DummyItem createDummyItem(String name, String content_image,
                                      String ownerName, String content_image_owner, double price, String id) {
        return new DummyItem(id, name, ownerName, content_image, content_image_owner, price);
    }

    public static class DummyItem {
        public String id;
        public String name;
        public String ownerName;
        public String content_image;
        public String content_image_owner;
        public double price;

        public DummyItem(String id, String name, String ownerName, String content_image, String content_image_owner, double price) {
            this.id = id;
            this.name = name;
            this.ownerName = ownerName;
            this.content_image = content_image;
            this.content_image_owner = content_image_owner;
            this.price = price;
        }
    }
}