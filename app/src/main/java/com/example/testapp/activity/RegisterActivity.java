package com.example.testapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.R;
import com.example.testapp.utils.Web;
import com.example.testapp.utils.XToastUtils;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.utils.SnackbarUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;

// 注册页面
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText password;
    private EditText username;
    private EditText phoneNumber;
    private EditText code;
    private CountDownButtonHelper countDown;
    private Button btn_sendCode;
    private Button btn_reg;
    private Novate novate;
    private Handler_Reg handler_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();
        handler_reg = new Handler_Reg(this);
        countDown = new CountDownButtonHelper(btn_sendCode, 60);
        novate = new Novate.Builder(RegisterActivity.this).baseUrl(Web.PREFIX_LOCAL.val()).build();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.reg_get_verify_code) {
            countDown.start();
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("phoneNumber", String.valueOf(phoneNumber.getText()));
            sendHttp(paramMap, "/verify");
        } else {
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("phoneNumber", String.valueOf(phoneNumber.getText()));
            paramMap.put("username", String.valueOf(username.getText()));
            paramMap.put("password", String.valueOf(password.getText()));
            paramMap.put("code", String.valueOf(code.getText()));
            sendHttp(paramMap, "/register");
        }
    }

    void initComponents() {
        username = findViewById(R.id.reg_username);
        password = findViewById(R.id.reg_password);
        phoneNumber = findViewById(R.id.reg_phoneNumber);
        code = findViewById(R.id.reg_verify_code);
        btn_sendCode = findViewById(R.id.reg_get_verify_code);
        btn_reg = findViewById(R.id.btn_register);
        btn_sendCode.setOnClickListener(this);
        btn_reg.setOnClickListener(this);
    }

    void sendHttp(HashMap<String, Object> map, String url) {
        novate.rxPost(url, map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                Message msg = new Message();
                if ("send_code_ok".equals(response)) {
                    msg.what = 1;
                    handler_reg.sendMessage(msg);
                } else if ("send_code_failed".equals(response)) {
                    msg.what = 2;
                    handler_reg.sendMessage(msg);
                } else if ("reg_failed".equals(response)) {
                    msg.what = 3;
                    handler_reg.sendMessage(msg);
                } else {
                    XToastUtils.info("注册成功");
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }

    static class Handler_Reg extends Handler {

        WeakReference<RegisterActivity> weakReference;

        public Handler_Reg(RegisterActivity reg) {
            this.weakReference = new WeakReference<>(reg);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                SnackbarUtils.Short(weakReference.get().btn_reg, "发送成功")
                        .gravityFrameLayout(Gravity.TOP).confirm().messageCenter().show();
            } else if (msg.what == 2) {
                SnackbarUtils.Short(weakReference.get().btn_reg, "发送失败")
                        .gravityFrameLayout(Gravity.TOP).danger().messageCenter().show();
            } else {
                SnackbarUtils.Short(weakReference.get().btn_reg, "注册失败,请填写正确信息和验证码")
                        .gravityFrameLayout(Gravity.TOP).danger().messageCenter().show();
            }
        }
    }
}