package com.example.testapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.testapp.R;
import com.example.testapp.adapter.MyItemRecyclerViewAdapter;
import com.example.testapp.entity.DummyContent;
import com.example.testapp.utils.XToastUtils;

import java.lang.ref.WeakReference;

//  商品列表Fragment
public class ItemFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private DummyContent dummyContent;  // 商品实体类
    private RecyclerView recyclerView;
    private MyItemRecyclerViewAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    private Handler_Item handler_item;

    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dummyContent = new DummyContent(getContext());
        dummyContent.init();
        handler_item = new Handler_Item(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        recyclerView = view.findViewById(R.id.items_list);
        adapter = new MyItemRecyclerViewAdapter(getActivity(), dummyContent.items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return view;
    }

    @Override
    public void onRefresh() {       // 刷新商品列表
        if (dummyContent.init()) {
            handler_item.sendEmptyMessageDelayed(1, 1000);
        }
    }

    static class Handler_Item extends Handler {

        WeakReference<ItemFragment> weakReference;

        public Handler_Item(ItemFragment weakReference) {
            this.weakReference = new WeakReference<>(weakReference);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                weakReference.get().adapter = new MyItemRecyclerViewAdapter(weakReference.get().getActivity(), weakReference.get().dummyContent.items);
                weakReference.get().adapter.notifyDataSetChanged();
                weakReference.get().recyclerView.setAdapter(weakReference.get().adapter);
                weakReference.get().refreshLayout.setRefreshing(false);
                XToastUtils.success("刷新成功");
            }
        }
    }

}