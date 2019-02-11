package com.dq.haoxuesheng.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dq.haoxuesheng.R;
import com.dq.haoxuesheng.base.BaseApplication;
import com.dq.haoxuesheng.view.MyProgressDialog;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.xutils.BuildConfig;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @Title 封装xUtils网络请求
 * @Authour jingang
 * @Time 2016年7月27日 下午3:25:21
 */
public class HttpxUtils {
    /**
     * 发送get请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable Get(String url, Map<String, String> map, final Callback.CommonCallback<String> callback) {
        Log.d("Http==Get=", "url=" + url);
        Log.d("Http==Get=", "map=" + (map == null ? "" : map.toString()));
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                if (isUserError(null,result)){
//                    onFinished();
//                    return;
//                }
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callback.onCancelled(cex);
            }

            @Override
            public void onFinished() {
                callback.onFinished();
            }
        });
        return cancelable;
    }

    public static <T> Callback.Cancelable Get(final Activity activity, String url, Map<String, String> map, final Callback.CommonCallback<String> callback) {
        Log.d("Http==Get=", "url=" + url);
        Log.d("Http==Get=", "map=" + (map == null ? "" : map.toString()));
        final MyProgressDialog myProgressDialog = new MyProgressDialog(activity);
        try {
            myProgressDialog.show();
        } catch (Exception e) {
        }
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                if (isUserError(activity,result))
//                    return;
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callback.onCancelled(cex);
            }

            @Override
            public void onFinished() {
                if (myProgressDialog != null && myProgressDialog.isShowing()) {
                    myProgressDialog.dismiss();
                }
                callback.onFinished();
            }
        });
        return cancelable;
    }

    /**
     * 发送post请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable Post(final Activity activity, String url, Map<String, Object> map, final Callback.CommonCallback<String> callback) {
        Log.d("Http==Post=", "url=" + url);
        Log.d("Http==Post=", "map=" + (map == null ? "" : map.toString()));
        final MyProgressDialog myProgressDialog = new MyProgressDialog(activity);
        try {
            myProgressDialog.show();
        } catch (Exception e) {
        }
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                if (isUserError(activity,result)){
//                    return;
//                }
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callback.onCancelled(cex);
            }

            @Override
            public void onFinished() {
                if (myProgressDialog != null && myProgressDialog.isShowing()) {
                    myProgressDialog.dismiss();
                }
                callback.onFinished();
            }
        });
        return cancelable;
    }

    /**
     * 上传文件
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable UpLoadFile(Activity activity, String url, Map<String, Object> map, final Callback.CommonCallback<String> callback) {
        Log.d("Http==UpLoadFile=", "url=" + url);
        final MyProgressDialog myProgressDialog = new MyProgressDialog(activity);
        try {
            myProgressDialog.show();
        } catch (Exception e) {
        }
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callback.onCancelled(cex);
            }

            @Override
            public void onFinished() {
                if (myProgressDialog != null && myProgressDialog.isShowing()) {
                    myProgressDialog.dismiss();
                }
                callback.onFinished();
            }
        });
        return cancelable;
    }

    /**
     * 下载文件
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable DownLoadFile(Activity activity, String url, String filepath, final Callback.CommonCallback<String> callback) {
        final MyProgressDialog myProgressDialog = new MyProgressDialog(activity);
        try {
            myProgressDialog.show();
        } catch (Exception e) {
        }
        RequestParams params = new RequestParams(url);
        // 设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        Callback.Cancelable cancelable = x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callback.onCancelled(cex);
            }

            @Override
            public void onFinished() {
                if (myProgressDialog != null && myProgressDialog.isShowing()) {
                    myProgressDialog.dismiss();
                }
                callback.onFinished();
            }
        });
        return cancelable;
    }

    public class JsonResponseParser implements ResponseParser {
        // 检查服务器返回的响应头信息
        @Override
        public void checkResponse(UriRequest request) throws Throwable {
        }


        /**
         * 转换result为resultType类型的对象
         *
         * @param resultType  返回值类型(可能带有泛型信息)
         * @param resultClass 返回值类型
         * @param result      字符串数据
         * @return
         * @throws Throwable
         */
        @Override
        public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
            return new Gson().fromJson(result, resultClass);
        }
    }

}