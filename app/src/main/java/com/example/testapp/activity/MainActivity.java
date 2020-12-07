package com.example.testapp.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.testapp.R;
import com.example.testapp.fragment.AddItemFragment;
import com.example.testapp.fragment.ItemFragment;
import com.example.testapp.fragment.PersonFragment;
import com.example.testapp.utils.XToastUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

// 系统主页
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragments.add(new ItemFragment());
        fragments.add(new AddItemFragment());
        fragments.add(new PersonFragment());
        titles.add(getResources().getString(R.string.menu_commodity));
        titles.add(getResources().getString(R.string.menu_add));
        titles.add(getResources().getString(R.string.menu_profile));

        String state = getIntent().getStringExtra("login_state");
        if ("yes".equals(state)) {
            getIntent().removeExtra("login_state");
            XToastUtils.success("登录成功");
        }

        viewPager = findViewById(R.id.vp);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
        };
        viewPager.setOffscreenPageLimit(fragments.size() - 1);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        for (int i = 0; i < titles.size(); i++) {
            if (item.getTitle().equals(titles.get(i))) {
                viewPager.setCurrentItem(i, true);
                return true;
            }
        }
        return false;
    }
}