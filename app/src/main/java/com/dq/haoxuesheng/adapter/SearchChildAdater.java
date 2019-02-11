package com.dq.haoxuesheng.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.dq.haoxuesheng.Interface.OnItemClickListeners;
import com.dq.haoxuesheng.R;
import com.dq.haoxuesheng.base.BaseAdapter;
import com.dq.haoxuesheng.base.ViewHolder;
import com.dq.haoxuesheng.entity.IndexData;

/**
 * 搜索二级分类适配器
 * Created by jingang on 2019/1/25.
 */

public class SearchChildAdater extends BaseAdapter<IndexData.DataBean.ListBean> {
    private OnItemClickListeners onItemClickListener;
    private int index = -1;//标记当前选择的选项

    public void setOnItemClickListener(OnItemClickListeners onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SearchChildAdater(Context context) {
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
                    index = position;
                    int position = holder.getLayoutPosition(); // 1
                    onItemClickListener.onItemClick(holder.itemView, position); // 2
                    notifyDataSetChanged();
                }
            });
        }

        if (index == position) {
            textView.setTextColor(Color.rgb(241, 83, 83));
        } else {
            textView.setTextColor(Color.rgb(51, 51, 51));
        }

        textView.setText(mDataList.get(position).getTitle());

    }
}
