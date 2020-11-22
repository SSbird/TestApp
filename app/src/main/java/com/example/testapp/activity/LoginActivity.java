package com.example.testapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.MyApp;
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

// 登录页面
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int VERIFY_WRONG_PASSWORD = 0;
    private static final int VERIFY_WRONG_CODE = 1;
    private static final int SEND_CODE_OK = 2;
    private static final int SEND_CODE_FAILED = 3;

    private Novate novate;
    private MyApp app;
    private Handler_Login handler_login;

    private Button btn_sendCode;
    private EditText phoneNumber;
    private TextView other;
    private EditText inputValue;
    private CountDownButtonHelper countDown;
    private String[] choices = {"使用密码登录", "使用验证码登录"};
    private String[] hints = {"请输入验证码", "请输入密码"};
    private boolean[] clks = {true, false};
    static int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        novate = new Novate.Builder(LoginActivity.this).baseUrl(Web.PREFIX_LOCAL.val()).build();
        initComponents();
        handler_login = new Handler_Login(LoginActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:    // 用户登录
                HashMap<String, Object> paramMap = new HashMap<>();
                String param = String.valueOf(inputValue.getText());
                app.getApp_map().put("phone", String.valueOf(phoneNumber.getText()));
                paramMap.put("phoneNumber", String.valueOf(phoneNumber.getText()));
                if (String.valueOf(other.getText()).equals(choices[1])) {        //使用密码登录
                    paramMap.put("password", param);
                    sendHttp(paramMap, "/login_password");
                } else {            //使用短信验证码登录
                    paramMap.put("code", param);
                    sendHttp(paramMap, "/login_code");
                }
                break;
            case R.id.btn_get_verify_code:      // 请求验证码
                paramMap = new HashMap<>();
                paramMap.put("phoneNumber", String.valueOf(phoneNumber.getText()));
                sendHttp(paramMap, "/verify");
                countDown.start();
                break;
            case R.id.tv_other_login:
                i = (i + 1) % 2;
                btn_sendCode.setClickable(clks[i]);     // 切换验证码发送按钮状态
                if (i == 1) {     // 输入密码
                    inputValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {     // 输入验证码
                    inputValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                inputValue.setHint(hints[i]);
                other.setText(choices[i]);
                break;
            case R.id.btn_register:     // 注册页面
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
    }

    void initComponents() {     // 面板组件初始化
        app = (MyApp) getApplication();
        btn_sendCode = findViewById(R.id.btn_get_verify_code);
        phoneNumber = findViewById(R.id.phone_number);
        other = findViewById(R.id.tv_other_login);
        inputValue = findViewById(R.id.et_verify_code);
        countDown = new CountDownButtonHelper(btn_sendCode, 60);
        Button btn_login = findViewById(R.id.btn_login);
        Button btn_reg = findViewById(R.id.btn_register);
        btn_login.setOnClickListener(this);
        btn_reg.setOnClickListener(this);
        btn_sendCode.setOnClickListener(this);
        other.setOnClickListener(this);
    }

    void sendHttp(HashMap<String, Object> map, String url) {
        novate.rxPost(url, map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                Message msg = new Message();
                switch (response) {
                    case "send_code_ok":
                        msg.what = SEND_CODE_OK;
                        handler_login.sendMessage(msg);
                        break;
                    case "send_code_failed":
                        msg.what = SEND_CODE_FAILED;
                        handler_login.sendMessage(msg);
                        break;
                    case "login_failed":
                        msg.what = VERIFY_WRONG_PASSWORD;
                        handler_login.sendMessage(msg);
                        break;
                    case "verify_failed":
                        msg.what = VERIFY_WRONG_CODE;
                        handler_login.sendMessage(msg);
                        break;
                    default:
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("login_state", "yes");
                        startActivity(intent);
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Log.d("LoginActivity.sendHttp", "请求出错!");
                XToastUtils.error("请求出错！");
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }

    static class Handler_Login extends Handler {

        WeakReference<LoginActivity> weakReference;

        public Handler_Login(LoginActivity loginActivity) {
            this.weakReference = new WeakReference<>(loginActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case VERIFY_WRONG_PASSWORD:
                    SnackbarUtils.Short(weakReference.get().btn_sendCode, "手机号或密码错误")
                            .gravityFrameLayout(Gravity.TOP).confirm().warning().danger().messageCenter().show();
                    break;
                case VERIFY_WRONG_CODE:
                    SnackbarUtils.Short(weakReference.get().btn_sendCode, "验证码错误")
                            .gravityFrameLayout(Gravity.TOP).confirm().danger().messageCenter().show();
                    break;
                case SEND_CODE_OK:
                    SnackbarUtils.Short(weakReference.get().btn_sendCode, "发送成功")
                            .gravityFrameLayout(Gravity.TOP).confirm().messageCenter().show();
                    break;
                case SEND_CODE_FAILED:
                    SnackbarUtils.Short(weakReference.get().btn_sendCode, "发送失败")
                            .gravityFrameLayout(Gravity.TOP).confirm().danger().messageCenter().show();
                    break;
            }
        }
    }
}