package com.dq.haoxuesheng.base;

import android.support.v4.app.Fragment;

import com.dq.haoxuesheng.utils.ToastUtils;

/**
 * @describe：
 * @author：jingang
 * @createdate：2018/03/22
 */

public class BaseFragment extends Fragment {

    public void showMessage(String message) {
        ToastUtils.getInstance(getActivity()).showMessage(message);
    }
}
