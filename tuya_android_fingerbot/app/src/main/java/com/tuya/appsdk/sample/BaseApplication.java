/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2021 Tuya Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tuya.appsdk.sample;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tuya.appsdk.sample.device.config.util.sp.SpUtils;
import com.tuya.appsdk.sample.device.mgt.control.activity.FingerBotMgtControlPanelActivity;
import com.tuya.appsdk.sample.device.mgt.main.BizBundleFamilyServiceImpl;
import com.tuya.smart.api.MicroContext;
import com.tuya.smart.api.router.UrlBuilder;
import com.tuya.smart.api.service.RedirectService;
import com.tuya.smart.api.service.RouteEventListener;
import com.tuya.smart.api.service.ServiceEventListener;
import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.optimus.sdk.TuyaOptimusSdk;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.wrapper.api.TuyaWrapper;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Base Application
 *
 * @author chuanfeng <a href="mailto:developer@tuya.com"/>
 * @since 2021/2/9 10:41 AM
 */
public final class BaseApplication extends Application {
    private String mDeviceId, mPid;
    private DeviceBean mDeviceBean;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        TuyaHomeSdk.init(this);
        TuyaHomeSdk.setDebugMode(true);

        TuyaWrapper.init(this, new RouteEventListener() {
            @Override
            public void onFaild(int errorCode, UrlBuilder urlBuilder) {
                Log.e("router not implement", "router not implement" + urlBuilder.target + urlBuilder.params.toString());
            }
        }, new ServiceEventListener() {
            @Override
            public void onFaild(String serviceName) {
                Log.e("service not implement", "service not implement：" + serviceName);
            }
        });

        TuyaOptimusSdk.init(this);
        //Register for home service, mall business package can not register for this service
        TuyaWrapper.registerService(AbsBizBundleFamilyService.class, new BizBundleFamilyServiceImpl());

        SpUtils.getInstance().initSp(this);
        ZXingLibrary.initDisplayOpinion(this);

        //intercept the existing route and jump to the custom implementation page with parameters
        RedirectService service = MicroContext.getServiceManager().findServiceByInterface(RedirectService.class.getName());
        service.registerUrlInterceptor(new RedirectService.UrlInterceptor() {
            @Override
            public void forUrlBuilder(UrlBuilder urlBuilder, RedirectService.InterceptorCallback interceptorCallback) {
                //Intercept the event of clicking the panel right menu and jump to the custom page with the parameters of urlBuilder
                if (!TextUtils.isEmpty(urlBuilder.params.toString())
                        && !TextUtils.isEmpty(urlBuilder.params.getString("devId"))) {
                    mDeviceId = urlBuilder.params.getString("devId");
                    mDeviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(mDeviceId);
                    mPid = mDeviceBean.getProductId();
                }

                //Intercept child device pages based on product IDs
                if (urlBuilder.target.equals("panel_rn")
                        && mPid.equals("y6kttvd6")) {
                    interceptorCallback.interceptor("interceptor");
                    Intent intent = new Intent(urlBuilder.context, FingerBotMgtControlPanelActivity.class);
                    intent.putExtra("deviceId",mDeviceId);
                    urlBuilder.context.startActivity(intent);
                } else {
                    interceptorCallback.onContinue(urlBuilder);
                }
            }
        });
    }
}
