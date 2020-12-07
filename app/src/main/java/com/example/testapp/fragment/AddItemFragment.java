package com.example.testapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.testapp.utils.GlideEngine;
import com.example.testapp.utils.Web;
import com.example.testapp.utils.XToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.shadow.ShadowTextView;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

// 商品上架页面
public class AddItemFragment extends Fragment {

    private ImageView imageView;
    private ShadowTextView button;
    private EditText input;
    private EditText item_info;
    private EditText item_name;
    private ButtonView button_sure;
    private Novate novate;

    private String path = null;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        novate = new Novate.Builder(getActivity()).baseUrl(Web.PREFIX_LOCAL.val()).build();
        return inflater.inflate(R.layout.fragment_addcoms, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initComponents();
        input.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        button.setOnClickListener(view1 -> {    // 选择图片
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(GlideEngine.createGlideEngine())
                    .isWeChatStyle(true)
                    .selectionMode(PictureConfig.SINGLE)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        });
        button_sure.setOnClickListener(view12 -> {
            String value_desc = String.valueOf(item_info.getText());
            String value_name = String.valueOf(item_name.getText());
            String value_price = String.valueOf(input.getText());
            MyApp app = (MyApp) getActivity().getApplication();
            String phone = (String) app.getApp_map().get("phone");
            if (value_desc.equals("") || value_name.equals("") || value_price.equals("") || path == null) {
                XToastUtils.warning("请填写完整信息");
            } else {
                File file = new File(path);
                MediaType mediaType = MediaType.parse("image/*");
                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(), RequestBody.create(mediaType, file))
                        .addFormDataPart("desc", value_desc)
                        .addFormDataPart("name", value_name)
                        .addFormDataPart("price", value_price)
                        .addFormDataPart("owner", phone)
                        .build();
                novate.RxUploadWithBody("/addItemInfo", body, new RxStringCallback() {
                    @Override
                    public void onError(Object tag, Throwable e) {

                    }

                    @Override
                    public void onCancel(Object tag, Throwable e) {

                    }

                    @Override
                    public void onNext(Object tag, String response) {
                        XToastUtils.info("商品上架成功");
                        resetValues();
                    }
                });
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
            for (LocalMedia local : list) {
                path = local.getPath();
                Glide.with(this).load(new File(path)).into(imageView);
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
        path = null;
    }

}
