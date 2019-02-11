package com.dq.haoxuesheng.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dq.haoxuesheng.R;
import com.dq.haoxuesheng.base.BaseActivity;
import com.dq.haoxuesheng.base.BaseApplication;
import com.dq.haoxuesheng.entity.EntityBase;
import com.dq.haoxuesheng.fragment.CollectionFragment;
import com.dq.haoxuesheng.fragment.HomeFragment;
import com.dq.haoxuesheng.fragment.SearchFragment;
import com.dq.haoxuesheng.openssl.Base64Utils;
import com.dq.haoxuesheng.openssl.RSAUtils;
import com.dq.haoxuesheng.utils.GsonUtil;
import com.dq.haoxuesheng.utils.HttpPath;
import com.dq.haoxuesheng.utils.HttpxUtils;
import com.dq.haoxuesheng.utils.ScreenManagerUtils;

import org.xutils.common.Callback;

import java.security.PrivateKey;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.main_fl_content)
    FrameLayout mainFlContent;
    @Bind(R.id.main_iv_1)
    ImageView mainIv1;
    @Bind(R.id.main_tv_1)
    TextView mainTv1;
    @Bind(R.id.main_ll_1)
    LinearLayout mainLl1;
    @Bind(R.id.main_iv_2)
    ImageView mainIv2;
    @Bind(R.id.main_tv_2)
    TextView mainTv2;
    @Bind(R.id.main_ll_2)
    LinearLayout mainLl2;
    @Bind(R.id.main_iv_3)
    ImageView mainIv3;
    @Bind(R.id.main_tv_3)
    TextView mainTv3;
    @Bind(R.id.main_ll_3)
    LinearLayout mainLl3;
    @Bind(R.id.main_ll_bottom)
    LinearLayout mainLlBottom;

    private HomeFragment homeFragment;
    private CollectionFragment collectionFragment;
    private SearchFragment searchFragment;
    private Fragment[] fragments;
    private int index = 0;//点击的页卡索引
    private int currentTabIndex = 0;//当前的页卡索引
    private long exitTime = 0;//记录上次点击返回按钮的时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(TextUtils.isEmpty(getUserUid())){
            getCreateUser("createuser");
        }else{
            initData();
        }
    }

    /**
     * 初始化fragment
     */
    public void initData() {
        homeFragment = new HomeFragment();
        collectionFragment = new CollectionFragment();
        searchFragment = new SearchFragment();
        fragments = new Fragment[]{homeFragment, collectionFragment, searchFragment};
        setBottomColor();
        getSupportFragmentManager().beginTransaction().add(R.id.main_fl_content, fragments[index]).show(fragments[index]).commit();
    }

    /**
     * 控制fragment的变化
     */
    public void fragmentControl() {
        if (currentTabIndex != index) {
            removeBottomColor();
            setBottomColor();

            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.main_fl_content, fragments[index]);
            }
            trx.show(fragments[index]).commit();
            currentTabIndex = index;
        }
    }

    /**
     * 设置底部栏按钮变色
     */
    private void setBottomColor() {
        switch (index) {
            case 0:
                mainIv1.setImageResource(R.mipmap.ic_home001);
                mainTv1.setTextColor(ContextCompat.getColor(this, R.color.main_color));
                break;
            case 1:
                mainIv2.setImageResource(R.mipmap.ic_collection001);
                mainTv2.setTextColor(ContextCompat.getColor(this, R.color.main_color));
                break;
            case 2:
                mainIv3.setImageResource(R.mipmap.ic_search001);
                mainTv3.setTextColor(ContextCompat.getColor(this, R.color.main_color));
                break;
        }
    }

    /**
     * 清除底部栏颜色
     */
    private void removeBottomColor() {
        switch (currentTabIndex) {
            case 0:
                mainIv1.setImageResource(R.mipmap.ic_home002);
                mainTv1.setTextColor(ContextCompat.getColor(this, R.color.tv_color2));
                break;
            case 1:
                mainIv2.setImageResource(R.mipmap.ic_collection002);
                mainTv2.setTextColor(ContextCompat.getColor(this, R.color.tv_color2));
                break;
            case 2:
                mainIv3.setImageResource(R.mipmap.ic_search002);
                mainTv3.setTextColor(ContextCompat.getColor(this, R.color.tv_color2));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showMessage("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                ScreenManagerUtils.getInstance().finishAllActivityAndClose();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.main_ll_1, R.id.main_ll_2, R.id.main_ll_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_ll_1:
                //首页
                index = 0;
                fragmentControl();
                break;
            case R.id.main_ll_2:
                //流程
                index = 1;
                fragmentControl();
                break;
            case R.id.main_ll_3:
                //个人
                index = 2;
                fragmentControl();
                break;
        }
    }

    /**
     * 创建新用户前加密
     *
     * @param action
     */
    public void getCreateUser(String action) {
        String PATH_RSA = "action=" + action;
        Log.e("ssssss", "创建新用户参数 = " + PATH_RSA);
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
            byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
            xUtilsCreateUser(Base64Utils.encode(encryptByte).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建新用户
     *
     * @param sign
     */
    public void xUtilsCreateUser(String sign) {
        Log.e("ssssss", "创建新用户接口 = " + HttpPath.URL + "sign=" + sign);
        HashMap<String, Object> map = new HashMap<>();
        map.put("sign", sign);
        HttpxUtils.Post(MainActivity.this,
                HttpPath.URL,
                map, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("ssssss", result);
                        EntityBase entityBase = GsonUtil.gsonIntance().gsonToBean(result, EntityBase.class);
                        if (entityBase.getStatus() == 1) {
                            BaseApplication.getInstance().saveUserInfo(result);
                            initData();
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
}
