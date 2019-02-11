package com.dq.haoxuesheng.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dq.haoxuesheng.R;
import com.dq.haoxuesheng.base.BaseAdapter;
import com.dq.haoxuesheng.base.ViewHolder;
import com.dq.haoxuesheng.entity.IndexDetailData;

import java.util.List;

/**
 * 图片显示适配器
 * Created by jingang on 2019/1/24.
 */

public class ImgAdapter extends BaseAdapter<String> {
    public ImgAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_img;
    }

    @Override
    public void onBindItemHolder(ViewHolder holder, int position) {
        ImageView imageView = holder.getView(R.id.item_iv);

        Glide
                .with(mContext)
                .load(mDataList.get(position).toString())
                .into(imageView);

    }
}
