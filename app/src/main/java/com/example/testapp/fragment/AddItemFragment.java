package com.example.testapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.testapp.MyApp;
import com.example.testapp.R;
import com.example.testapp.utils.Web;
import com.example.testapp.utils.XToastUtils;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.shadow.ShadowTextView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// 商品上架页面
public class AddItemFragment extends Fragment {

    private ImageView imageView;
    private ShadowTextView button;
    private EditText input;
    private EditText item_info;
    private EditText item_name;
    private ButtonView button_sure;
    private Handler_AddItem handler_addItem;

    private String path = null;
    private final String FILE_PREFIX = "/sdcard/Pictures/";

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addcoms, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initComponents();
        input.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        button.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 2);
        });
        button_sure.setOnClickListener(view12 -> {
            String value_desc = String.valueOf(item_info.getText());
            String value_name = String.valueOf(item_name.getText());
            String value_price = String.valueOf(input.getText());
            MyApp app = (MyApp) getActivity().getApplication();
            String phone = (String) app.getApp_map().get("phone");
            if (value_desc.equals("") || value_name.equals("") || value_price.equals("")) {
                XToastUtils.warning("请填写完整信息");
            } else {
                Novate novate = new Novate.Builder(getActivity()).baseUrl(Web.PREFIX_LOCAL.val()).build();
                HashMap<String, Object> map = new HashMap<>();
                map.put("desc", value_desc);
                map.put("name", value_name);
                map.put("price", value_price);
                map.put("owner", phone);
                novate.rxPost("addItemInfo", map, new RxStringCallback() {
                    @Override
                    public void onNext(Object tag, String response) {
                        Log.d("RXpost_uploadInfo", "成功..." + response);
                        handler_addItem.sendEmptyMessage(1);
                        resetValues();
                    }

                    @Override
                    public void onError(Object tag, Throwable e) {
                        resetValues();
                    }

                    @Override
                    public void onCancel(Object tag, Throwable e) {
                        resetValues();
                    }
                });
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2) {
            if (data != null) {
                Uri uri = data.getData();
                Glide.with(getActivity()).load(uri).into(imageView);
                path = Uri.decode(data.getDataString());
                handler_addItem = new Handler_AddItem(this);
            }
        }
    }

    void initComponents() {
        Activity act = getActivity();
        if (act != null) {
            imageView = act.findViewById(R.id.preview);
            button = act.findViewById(R.id.pic_upload);
            button_sure = act.findViewById(R.id.sure);
            input = act.findViewById(R.id.input_price);
            item_info = act.findViewById(R.id.item_info);
            item_name = act.findViewById(R.id.item_name);
        }
    }

    void resetValues() {
        imageView.setImageResource(R.mipmap.ic_preview);
        item_name.setText("");
        item_info.setText("");
        input.setText("");
    }

    static class Handler_AddItem extends Handler {

        WeakReference<AddItemFragment> weakReference;

        public Handler_AddItem(AddItemFragment addItemFragment) {
            this.weakReference = new WeakReference<>(addItemFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                OkHttpClient client = new OkHttpClient.Builder().build();
                String filePath = weakReference.get().FILE_PREFIX;
                String path = weakReference.get().path;
                File file_2 = new File(filePath + path.substring(path.lastIndexOf("/")));
                MediaType mediaType = MediaType.parse("image/jpg");
                RequestBody fileBody = RequestBody.create(mediaType, file_2);
                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM) // 表单类型(必填)
                        .addFormDataPart("file", file_2.getName(), fileBody)
                        .build();
                Request request = new Request.Builder()
                        .url(Web.PREFIX_LOCAL.val() + "addItemIcon")
                        .post(body)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }

                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                });
                XToastUtils.info("商品上架成功");
            }
        }
    }
}
