package com.dq.haoxuesheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.dq.haoxuesheng.Interface.OnItemClickListeners;
import com.dq.haoxuesheng.R;
import com.dq.haoxuesheng.adapter.HomeListAdapter;
import com.dq.haoxuesheng.base.BaseActivity;
import com.dq.haoxuesheng.entity.EntityBase;
import com.dq.haoxuesheng.entity.IndexListData;
import com.dq.haoxuesheng.openssl.Base64Utils;
import com.dq.haoxuesheng.openssl.RSAUtils;
import com.dq.haoxuesheng.utils.GsonUtil;
import com.dq.haoxuesheng.utils.HttpPath;
import com.dq.haoxuesheng.utils.HttpxUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import org.xutils.common.Callback;

import java.security.PrivateKey;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 首页--作文列表
 * Created by jingang on 2019/1/23.
 */

public class HomeListActivity extends BaseActivity{
    @Bind(R.id.lRrcyclerView)
    LRecyclerView lRrcyclerView;

    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private HomeListAdapter mAdapter;
    // private String tid, lid, zsid, key,id;

    private String id, zsid, key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.layout_lrecyclerview);
        ButterKnife.bind(this);
        setIvBack();
        setTvTitle("作文列表");

        setAdapter();
        getIntentStr();
    }

    public void setAdapter() {
        mAdapter = new HomeListAdapter(this, 0, null);
        lRrcyclerView.setLayoutManager(new LinearLayoutManager(this));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        lRrcyclerView.setAdapter(lRecyclerViewAdapter);

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(HomeListActivity.this, HomeDetailActivity.class)
                        .putExtra("id", mAdapter.getDataList().get(position).getId())
                        .putExtra("title", mAdapter.getDataList().get(position).getTitle()));
            }
        });

        lRrcyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getIndexList("list", id, zsid, key, page, pagesize);
            }
        });

        lRrcyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                getIndexList("list", id, zsid, key, page, pagesize);
            }
        });


    }

    /**
     * 获取数据
     */
    public void getIntentStr() {
        id = getIntent().getStringExtra("id");
        zsid = getIntent().getStringExtra("zsid");
        key = getIntent().getStringExtra("key");

        Log.e("ssssss", id);
        Log.e("ssssss", "222 = " + zsid);

        getIndexList("list", id, zsid, key, 1, 10);

    }

    /**
     * 作文列表前加密
     *
     * @param action
     */
    public void getIndexList(String action, String id, String zsid, String key, int page, int pagesize) {
        String PATH_RSA = "action=" + action + id + zsid + "&key=" + key + "&page=" + page + "&pagesize=" + pagesize;
        Log.e("ssssss", "作文列表参数 = " + PATH_RSA);
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
            byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
            xUtilsIndexList(Base64Utils.encode(encryptByte).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 作文列表
     *
     * @param sign
     */
    public void xUtilsIndexList(String sign) {
        Log.e("ssssss", "作文列表接口 = " + HttpPath.URL + "sign=" + sign);
        HashMap<String, String> map = new HashMap<>();
        map.put("sign", sign);
        HttpxUtils.Get(this,
                HttpPath.URL,
                map, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("ssssss", result);
                        if (page == 1) {
                            mAdapter.clear();
                        }
                        EntityBase entityBase = GsonUtil.gsonIntance().gsonToBean(result, EntityBase.class);
                        if (entityBase.getStatus() == 1) {
                            IndexListData data = GsonUtil.gsonIntance().gsonToBean(result, IndexListData.class);
                            mAdapter.addAll(data.getData());
                            if (data.getData().size() < pagesize) {
                                lRrcyclerView.setNoMore(true);
                            }
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
}
