package com.hdty.app.coolweather.util;

/**
 * Created by Administrator on 2016/6/25.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
