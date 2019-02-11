package com.dq.haoxuesheng.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dq.haoxuesheng.R;
import com.dq.haoxuesheng.adapter.ImgAdapter;
import com.dq.haoxuesheng.base.BaseActivity;
import com.dq.haoxuesheng.entity.EntityBase;
import com.dq.haoxuesheng.entity.IndexDetailData;
import com.dq.haoxuesheng.openssl.Base64Utils;
import com.dq.haoxuesheng.openssl.RSAUtils;
import com.dq.haoxuesheng.utils.DateUtils;
import com.dq.haoxuesheng.utils.GsonUtil;
import com.dq.haoxuesheng.utils.HttpPath;
import com.dq.haoxuesheng.utils.HttpxUtils;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import org.xutils.common.Callback;

import java.security.PrivateKey;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 首页--作文列表--作文详情
 * Created by jingang on 2019/1/24.
 */

public class HomeDetailActivity extends BaseActivity {
    @Bind(R.id.lRrcyclerView)
    LRecyclerView lRrcyclerView;
    private TextView tvTitle, tvType, tvContent;
    private String title, id;

    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private ImgAdapter mAdapter;
    private View headView;

    private boolean isLike = false;//收藏状态

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.layout_lrecyclerview);
        ButterKnife.bind(this);
        setIvBack();
        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");
        setTvTitle("作文详情");
        setAdapter();
        getIndexShow("show", getUserUid(), id);
        /*收藏*/
        getIvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLike) {
                    postCollection("shoucang", getUserUid(), id);
                } else {
                    postUnCollection("delshoucang", getUserUid(), id);
                }

            }
        });
    }

    public void setAdapter() {
        mAdapter = new ImgAdapter(this);
        lRrcyclerView.setLayoutManager(new LinearLayoutManager(this));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        lRrcyclerView.setAdapter(lRecyclerViewAdapter);

        lRrcyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getIndexShow("show", getUserUid(), id);
            }
        });

        setHeadView();
        setFootView();
    }

    public void setHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.include_homedetail_head, null);
        headView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        lRecyclerViewAdapter.addHeaderView(headView);
        tvTitle = headView.findViewById(R.id.tv_homedetail_title);
        tvType = headView.findViewById(R.id.tv_homedetail_type);
    }

    public void setFootView() {
        View footView = LayoutInflater.from(this).inflate(R.layout.include_homedetail_foot, null);
        lRecyclerViewAdapter.addFooterView(footView);
        tvContent = footView.findViewById(R.id.tv_homedetail_content);
    }

    /**
     * 作文详情前加密
     *
     * @param action
     * @param uid
     * @param id
     */
    public void getIndexShow(String action, String uid, String id) {
        String PATH_RSA = "action=" + action + "&uid=" + uid + "&id=" + id;
        Log.e("ssssss", "作文详情参数 = " + PATH_RSA);
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
            byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
            xUtilsIndexShow(Base64Utils.encode(encryptByte).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 作文详情
     *
     * @param sign
     */
    public void xUtilsIndexShow(String sign) {
        Log.e("ssssss", "作文详情接口 = " + HttpPath.URL + "sign=" + sign);
        HashMap<String, String> map = new HashMap<>();
        map.put("sign", sign);
        HttpxUtils.Get(this,
                HttpPath.URL,
                map, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("ssssss", result);
                        EntityBase entityBase = GsonUtil.gsonIntance().gsonToBean(result, EntityBase.class);
                        if (entityBase.getStatus() == 1) {
                            setUI(result);
                        } else {
                            showMessage(entityBase.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("ssssss", "失败，请检查网络" + ex);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        lRrcyclerView.refreshComplete(pagesize);
                    }

                    @Override
                    public void onFinished() {
                        lRrcyclerView.refreshComplete(pagesize);
                    }
                });
    }

    /**
     * 收藏前加密
     *
     * @param action
     * @param uid
     * @param id
     */
    public void postCollection(String action, String uid, String id) {
        String PATH_RSA = "action=" + action + "&uid=" + uid + "&id=" + id;
        Log.e("ssssss", "收藏参数 = " + PATH_RSA);
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
            byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
            xUtilsCollection(Base64Utils.encode(encryptByte).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 收藏
     *
     * @param sign
     */
    public void xUtilsCollection(String sign) {
        Log.e("ssssss", "收藏接口 = " + HttpPath.URL + "sign=" + sign);
        HashMap<String, String> map = new HashMap<>();
        map.put("sign", sign);
        HttpxUtils.Get(this,
                HttpPath.URL,
                map, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("ssssss", result);
                        EntityBase entityBase = GsonUtil.gsonIntance().gsonToBean(result, EntityBase.class);
                        if (entityBase.getStatus() == 1) {
                            showMessage(entityBase.getMessage());
                            isLike = true;
                            getIvRight().setImageResource(R.mipmap.ic_like001);
                        } else {
                            showMessage(entityBase.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("ssssss", "失败，请检查网络" + ex);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }
                });
    }

    /**
     * 取消收藏前加密
     *
     * @param action
     * @param uid
     * @param id
     */
    public void postUnCollection(String action, String uid, String id) {
        String PATH_RSA = "action=" + action + "&uid=" + uid + "&id=" + id;
        Log.e("ssssss", "取消收藏参数 = " + PATH_RSA);
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
            byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
            xUtilsUnCollection(Base64Utils.encode(encryptByte).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消收藏
     *
     * @param sign
     */
    public void xUtilsUnCollection(String sign) {
        Log.e("ssssss", "取消收藏接口 = " + HttpPath.URL + "sign=" + sign);
        HashMap<String, String> map = new HashMap<>();
        map.put("sign", sign);
        HttpxUtils.Get(this,
                HttpPath.URL,
                map, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("ssssss", result);
                        EntityBase entityBase = GsonUtil.gsonIntance().gsonToBean(result, EntityBase.class);
                        if (entityBase.getStatus() == 1) {
                            showMessage(entityBase.getMessage());
                            isLike = false;
                            getIvRight().setImageResource(R.mipmap.ic_like002);
                        } else {
                            showMessage(entityBase.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("ssssss", "失败，请检查网络" + ex);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }
                });
    }


    /**
     * 更新UI
     *
     * @param result
     */
    @SuppressLint("SetTextI18n")
    public void setUI(String result) {
        IndexDetailData data = GsonUtil.gsonIntance().gsonToBean(result, IndexDetailData.class);
        tvTitle.setText(data.getData().getTitle());

        tvType.setText(data.getData().getType() + " " + data.getData().getZishu() + " " + DateUtils.timesOne(data.getData().getAtime()));
//        tvType.setText(data.getData().getZishu()
//                + "." + data.getData().getType()
//                + "." + data.getData().getTizai()
//                + ":" + data.getData().getTitle());

        isLike = data.getData().isIscang();
        if (isLike) {
            getIvRight().setImageResource(R.mipmap.ic_like001);
        } else {
            getIvRight().setImageResource(R.mipmap.ic_like002);
        }

        //1.先创建SpannableString对象
        SpannableString spannableString = new SpannableString(data.getData().getContent());
        //2.设置文本缩进的样式，参数arg0，首行缩进的像素，arg1，剩余行缩进的像素,这里我将像素px转换成了手机独立像素dp
        LeadingMarginSpan.Standard what = new LeadingMarginSpan.Standard(dp2px(this, 28), 0);
        //3.进行样式的设置了,其中参数what是具体样式的实现对象,start则是该样式开始的位置，end对应的是样式结束的位置，参数flags，定义在Spannable中的常量
        spannableString.setSpan(what, 0, spannableString.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        tvContent.setText(spannableString);

        mAdapter.clear();
        mAdapter.addAll(data.getData().getImglist());


    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
