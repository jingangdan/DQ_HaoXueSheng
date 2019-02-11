package com.dq.haoxuesheng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dq.haoxuesheng.Interface.OnItemClickListeners;
import com.dq.haoxuesheng.R;
import com.dq.haoxuesheng.activity.HomeDetailActivity;
import com.dq.haoxuesheng.adapter.HomeListAdapter;
import com.dq.haoxuesheng.base.BaseApplication;
import com.dq.haoxuesheng.base.BaseFragment;
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
 * 收藏
 * Created by jingang on 2019/1/21.
 */

public class CollectionFragment extends BaseFragment implements OnItemClickListeners {
    @Bind(R.id.lRrcyclerView)
    LRecyclerView lRrcyclerView;

    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private HomeListAdapter mAdapter;

    private int page = 1, pagesize = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_collection, null);
        ButterKnife.bind(this, view);

        setAdapter();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getCollectionList("shoucanglist", BaseApplication.getInstance().getUserUid(), page, pagesize);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCollectionList("shoucanglist", BaseApplication.getInstance().getUserUid(), page, pagesize);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setAdapter() {
        mAdapter = new HomeListAdapter(getActivity(), 1, this);
        lRrcyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        lRrcyclerView.setAdapter(lRecyclerViewAdapter);
        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getActivity(), HomeDetailActivity.class)
                        .putExtra("id", mAdapter.getDataList().get(position).getId())
                        .putExtra("title", mAdapter.getDataList().get(position).getTitle()));
            }
        });

        lRrcyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getCollectionList("shoucanglist", BaseApplication.getInstance().getUserUid(), page, pagesize);
            }
        });

        lRrcyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                getCollectionList("shoucanglist", BaseApplication.getInstance().getUserUid(), page, pagesize);
            }
        });

    }

    /**
     * 收藏列表前加密
     *
     * @param uid
     * @param page
     * @param pagesize
     */
    public void getCollectionList(String action, String uid, int page, int pagesize) {
        String PATH_RSA = "action=" + action + "&uid=" + uid + "&page=" + page + "&pagesize=" + pagesize;
        Log.e("ssssss", "收藏列表参数 = " + PATH_RSA);
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
            byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
            xUtilsCollectionList(Base64Utils.encode(encryptByte).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 收藏列表
     *
     * @param sign
     */
    public void xUtilsCollectionList(String sign) {
        Log.e("ssssss", "收藏列表接口 = " + HttpPath.URL + "sign=" + sign);
        HashMap<String, String> map = new HashMap<>();
        map.put("sign", sign);
        HttpxUtils.Get(getActivity(),
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

    /**
     * 取消收藏前加密
     *
     * @param action
     * @param uid
     * @param id
     */
    public void postUnCollection(String action, String uid, String id, int position) {
        String PATH_RSA = "action=" + action + "&uid=" + uid + "&id=" + id;
        Log.e("ssssss", "取消收藏参数 = " + PATH_RSA);
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
            byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
            xUtilsUnCollection(Base64Utils.encode(encryptByte).toString(), position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消收藏
     *
     * @param sign
     */
    public void xUtilsUnCollection(String sign, final int position) {
        Log.e("ssssss", "取消收藏接口 = " + HttpPath.URL + "sign=" + sign);
        HashMap<String, String> map = new HashMap<>();
        map.put("sign", sign);
        HttpxUtils.Get(getActivity(),
                HttpPath.URL,
                map, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("ssssss", result);
                        EntityBase entityBase = GsonUtil.gsonIntance().gsonToBean(result, EntityBase.class);
                        if (entityBase.getStatus() == 1) {
                            showMessage(entityBase.getMessage());
                            mAdapter.getDataList().remove(position);
                            mAdapter.notifyDataSetChanged();
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

    @Override
    public void onItemClick(View view, int position) {
        postUnCollection("delshoucang", BaseApplication.getInstance().getUserUid(), mAdapter.getDataList().get(position).getId(), position);
    }
}