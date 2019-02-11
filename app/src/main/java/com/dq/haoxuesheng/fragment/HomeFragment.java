package com.dq.haoxuesheng.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dq.haoxuesheng.Interface.OnItemClickListeners;
import com.dq.haoxuesheng.R;
import com.dq.haoxuesheng.activity.HomeListActivity;
import com.dq.haoxuesheng.adapter.IndexChildAdapter;
import com.dq.haoxuesheng.base.BaseAdapter;
import com.dq.haoxuesheng.base.BaseFragment;
import com.dq.haoxuesheng.base.ViewHolder;
import com.dq.haoxuesheng.entity.EntityBase;
import com.dq.haoxuesheng.entity.IndexData;
import com.dq.haoxuesheng.openssl.Base64Utils;
import com.dq.haoxuesheng.openssl.RSAUtils;
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
 * 首页
 * Created by jingang on 2018/12/31.
 */

public class HomeFragment extends BaseFragment {
    @Bind(R.id.lRrcyclerView)
    LRecyclerView lRrcyclerView;

    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private IndexAdapter mAdapter;
    private IndexChildAdapter mChildAdapter;

    private int pagesize = 0;

    private String id, zsid, key;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_home, null);
        ButterKnife.bind(this, view);

        setAdapter();
        getIndex("indexlist");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setAdapter() {
        mAdapter = new IndexAdapter(getActivity());
        lRrcyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        lRrcyclerView.setAdapter(lRecyclerViewAdapter);

        lRrcyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getIndex("indexlist");
            }
        });
    }

    /**
     * @param action
     */
    public void getIndex(String action) {
        String PATH_RSA = "action=" + action;
        Log.e("ssssss", "首页参数 = " + PATH_RSA);
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
            byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
            xUtilsIndex(Base64Utils.encode(encryptByte).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void xUtilsIndex(String sign) {
        Log.e("ssssss", "首页接口 = " + HttpPath.URL + "sign=" + sign);
        HashMap<String, String> map = new HashMap<>();
        map.put("sign", sign);
        HttpxUtils.Get(getActivity(),
                HttpPath.URL,
                map, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        setClearId();
                        Log.e("ssssss", result);
                        EntityBase entityBase = GsonUtil.gsonIntance().gsonToBean(result, EntityBase.class);
                        if (entityBase.getStatus() == 1) {
                            mAdapter.clear();
                            IndexData data = GsonUtil.gsonIntance().gsonToBean(result, IndexData.class);
                            pagesize = data.getData().size();
                            lRrcyclerView.refreshComplete(pagesize);
                            mAdapter.addAll(data.getData());
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

    private class IndexAdapter extends BaseAdapter<IndexData.DataBean> {
        public IndexAdapter(Context context) {
            super(context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_home;
        }

        @Override
        public void onBindItemHolder(ViewHolder holder, final int position) {
            TextView textView = holder.getView(R.id.info_text);
            RecyclerView recyclerView = holder.getView(R.id.rv_home);

            textView.setText(mDataList.get(position).getTitle());
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

            mChildAdapter = new IndexChildAdapter(mContext);
            mChildAdapter.addAll(mDataList.get(position).getList());
            recyclerView.setAdapter(mChildAdapter);

            mChildAdapter.setOnItemClickListener(new OnItemClickListeners() {
                @Override
                public void onItemClick(View view, int positions) {
                    if (mDataList.get(position).getId().equals("zs")) {
                        zsid = "&zsid=" + mDataList.get(position).getList().get(positions).getId();
                        id = "&tid=&lid=";
                    } else {
                        zsid = "&zsid=";
                        id = "&tid=&lid=" + mDataList.get(position).getList().get(positions).getId();
                    }
                    setIntent(id, zsid, "");

                }
            });

        }

    }

    /**
     * 跳转作文列表
     *
     * @param id   一级分类id
     * @param zsid 字数id
     */
    public void setIntent(String id, String zsid, String key) {
        Intent intent = new Intent(getActivity(), HomeListActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("zsid", zsid);
        intent.putExtra("key", key);
        startActivity(intent);

        setClearId();
    }

    /**
     * 清除选择id
     */
    public void setClearId() {
        id = "";
        zsid = "";
        key = "";
    }
}