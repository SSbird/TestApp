package com.example.testapp.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.testapp.MyApp;
import com.example.testapp.R;
import com.example.testapp.fragment.AddItemFragment;
import com.example.testapp.fragment.ItemFragment;
import com.example.testapp.fragment.PersonFragment;
import com.example.testapp.utils.Web;
import com.example.testapp.utils.XToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// 系统主页
public class MainActivity extends AppCompatActivity {

    private MyApp app;
    private static List<HashMap<String, Object>> mapList = new ArrayList<>();

    List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragments.add(new ItemFragment());
        fragments.add(new AddItemFragment());
        fragments.add(new PersonFragment());

        String state = getIntent().getStringExtra("login_state");
        if ("yes".equals(state)) {
            getIntent().removeExtra("login_state");
            XToastUtils.success("登录成功");
        }

        ViewPager mViewPager = findViewById(R.id.vp);
        TabLayout mtabLayout = findViewById(R.id.tabs);

        mtabLayout.addTab(mtabLayout.newTab().setIcon(R.mipmap.ic_commodity).setText(R.string.menu_commodity));
        mtabLayout.addTab(mtabLayout.newTab().setIcon(R.mipmap.ic_add).setText(R.string.menu_add));
        mtabLayout.addTab(mtabLayout.newTab().setIcon(R.mipmap.ic_person).setText((R.string.menu_profile)));

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);
        mtabLayout.setupWithViewPager(mViewPager);
        mtabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String temp = (String) tab.getText();
                if ("我的".equals(temp)) {        //  获取用户发布的商品信息，包含图片，价格等等，每点击一次就会更新APP中的Map
                    app = (MyApp) getApplication();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("phoneNumber", app.getApp_map().get("phone"));
                    Novate novate = new Novate.Builder(MainActivity.this).baseUrl(Web.PREFIX_LOCAL.val()).build();
                    novate.rxPost("/getSelfItemCount", map, new RxStringCallback() {
                        @Override
                        public void onError(Object tag, Throwable e) {

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {

                        }

                        @Override
                        public void onNext(Object tag, String response) {
                            mapList = new Gson().fromJson(response, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                            }.getType());
                            app.getApp_map().put("user_release_count", mapList.size());
                            MyApp.setUser_item_list(mapList);
                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mtabLayout.getTabAt(0).setIcon(R.mipmap.ic_commodity).setText(R.string.menu_commodity);
        mtabLayout.getTabAt(1).setIcon(R.mipmap.ic_add).setText(R.string.menu_add);
        mtabLayout.getTabAt(2).setIcon(R.mipmap.ic_person).setText(R.string.menu_profile);

    }


}