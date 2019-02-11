package com.dq.haoxuesheng.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dq.haoxuesheng.Interface.OnItemClickListeners;
import com.dq.haoxuesheng.R;
import com.dq.haoxuesheng.base.BaseAdapter;
import com.dq.haoxuesheng.base.ViewHolder;
import com.dq.haoxuesheng.entity.IndexData;

/**
 * 首页二级适配器
 * Created by jingang on 2019/1/23.
 */

public class IndexChildAdapter extends BaseAdapter<IndexData.DataBean.ListBean> {
    private OnItemClickListeners onItemClickListener;

    public void setOnItemClickListener(OnItemClickListeners onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public IndexChildAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_home_child;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindItemHolder(final ViewHolder holder, final int position) {
        TextView textView = holder.getView(R.id.item_home_child);
        if (onItemClickListener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    onItemClickListener.onItemClick(holder.itemView, position); // 2
                }
            });
        }

        textView.setText(mDataList.get(position).getTitle());
    }
}
