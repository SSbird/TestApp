package com.example.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class LinearAdapter_Comments extends RecyclerView.Adapter<LinearAdapter_Comments.LinearViewHolder> {

    private Context mContext;

    private static int i = 0;

    private static HashMap<String, Object> dataMap;
    private static List<HashMap<String, Object>> dataList;

    public LinearAdapter_Comments(Context context) {
        this.mContext = context;
    }

    @NotNull
    @Override
    public LinearAdapter_Comments.LinearViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new LinearAdapter_Comments.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, int position) {
        i++;
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView comment;
        private TextView username;
//        private ImageView imageView;

        public LinearViewHolder(View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.item_self_image);
//            comment = itemView.findViewById(R.id.comment);
//            username = itemView.findViewById(R.id.username);
        }
    }
}
