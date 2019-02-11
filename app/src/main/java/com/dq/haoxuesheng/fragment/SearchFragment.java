package com.dq.haoxuesheng.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dq.haoxuesheng.Interface.OnItemClickListeners;
import com.dq.haoxuesheng.R;
import com.dq.haoxuesheng.activity.HomeListActivity;
import com.dq.haoxuesheng.adapter.SearchChildAdater;
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
 * 搜索
 * Created by jingang on 2019/1/21.
 */

public class SearchFragment extends BaseFragment {
    @Bind(R.id.lRrcyclerView)
    LRecyclerView lRrcyclerView;

    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private IndexAdapter mAdapter;
    private SearchChildAdater mChildAdapter;

    private View footView;
    private EditText etSearch;
    private Button butSearch;

    private int pagesize = 0;

    //  一级分类编号 二级分类编号 字数编号 关键词
    //private String tid = "", zsid = "", id = "", key = "";

    //    id = tid+lid(一二级分类编号) 字数编号 关键词
    private String id, zsid, key, tid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_search, null);
        ButterKnife.bind(this, view);

        setAdapter();
        getSearchType("searchtype");
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getSearchType("searchtype");
        }
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
                id = "";
                zsid = "";
                tid = "";
                getSearchType("searchtype");
            }
        });
        setFootView();
    }

    public void setFootView() {
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.include_search_foot, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
        footView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        lRecyclerViewAdapter.addFooterView(footView);

        etSearch = footView.findViewById(R.id.etSearch);
        butSearch = footView.findViewById(R.id.butSearch);
        butSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = etSearch.getText().toString().trim();
                if (TextUtils.isEmpty(tid) && TextUtils.isEmpty(zsid) && TextUtils.isEmpty(key)) {
                    showMessage("请选择条件");
                    return;
                }
                if (TextUtils.isEmpty(tid)) {
                    id = "&tid=&lid=";
                } else {
                    id = tid + "&lid=";
                }
                setIntent(id, zsid, key);
            }
        });
    }

    /**
     * 筛选条件前加密
     *
     * @param action
     */
    public void getSearchType(String action) {
        String PATH_RSA = "action=" + action;
        Log.e("ssssss", "首页参数 = " + PATH_RSA);
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
            byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
            xUtilsSearchType(Base64Utils.encode(encryptByte).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 筛选条件
     *
     * @param sign
     */
    public void xUtilsSearchType(String sign) {
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

            mChildAdapter = new SearchChildAdater(mContext);
            mChildAdapter.addAll(mDataList.get(position).getList());
            recyclerView.setAdapter(mChildAdapter);

            mChildAdapter.setOnItemClickListener(new OnItemClickListeners() {
                @Override
                public void onItemClick(View view, int positions) {
                    if (position == 0) {
                        tid = "&" + mDataList.get(position).getAction() + "=" + mDataList.get(position).getList().get(positions).getId();
                    } else if (position == 1) {
                        zsid = "&" + mDataList.get(position).getAction() + "=" + mDataList.get(position).getList().get(positions).getId();
                    }
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
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 清除条件
     */
    public void setClearId() {
        id = "";
        zsid = "";
        tid = "";
        key = "";
        etSearch.setText("");
    }
}