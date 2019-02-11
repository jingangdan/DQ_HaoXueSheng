package com.dq.haoxuesheng.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dq.haoxuesheng.Interface.OnItemClickListeners;
import com.dq.haoxuesheng.R;
import com.dq.haoxuesheng.base.BaseAdapter;
import com.dq.haoxuesheng.base.ViewHolder;
import com.dq.haoxuesheng.entity.IndexListData;
import com.dq.haoxuesheng.utils.DateUtils;

/**
 * 作文列表适配器
 * Created by jingang on 2019/1/23.
 */

public class HomeListAdapter extends BaseAdapter<IndexListData.DataBean> {
    private TextView tvTitle, tvAuthor, tvType;
    private ImageView imageView;
    private int tag;
    private OnItemClickListeners onItemClickListener;

    public HomeListAdapter(Context context, int tag, OnItemClickListeners onItemClickListener) {
        super(context);
        this.tag = tag;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_home_list;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemHolder(ViewHolder holder, final int position) {
        tvTitle = holder.getView(R.id.item_tv_homelist_title);
        tvAuthor = holder.getView(R.id.item_tv_homelist_author);
        tvType = holder.getView(R.id.item_tv_homelist_type);
        imageView = holder.getView(R.id.item_iv_homelist_del);
        if (tag == 1) {
            imageView.setVisibility(View.VISIBLE);
            //tvType.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.GONE);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });

        tvTitle.setText(mDataList.get(position).getTitle());
        tvAuthor.setText(DateUtils.timesOne(mDataList.get(position).getAtime()));
        tvType.setText(mDataList.get(position).getType() + "  " + mDataList.get(position).getZishu());
    }

}
