/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2021 Tuya Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NO
 */

package com.tuya.appsdk.sample.device.config.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.tuya.appsdk.sample.device.config.R;
import com.tuya.appsdk.sample.device.config.ap.DeviceConfigAPActivity;
import com.tuya.appsdk.sample.device.config.ble.DeviceConfigBleAndDualActivity;
import com.tuya.appsdk.sample.device.config.ez.DeviceConfigEZActivity;
import com.tuya.appsdk.sample.device.config.mesh.DeviceConfigMeshActivity;
import com.tuya.appsdk.sample.device.config.qrcode.DeviceConfigQrCodeDeviceActivity;
import com.tuya.appsdk.sample.device.config.zigbee.gateway.DeviceConfigZbGatewayActivity;
import com.tuya.appsdk.sample.device.config.zigbee.sub.DeviceConfigZbSubDeviceActivity;
import com.tuya.appsdk.sample.resource.HomeModel;

import kotlin.jvm.internal.Intrinsics;

/**
 * Device configuration func Widget
 *
 * @author chuanfeng <a href="mailto:developer@tuya.com"/>
 * @since 2021/2/18 1:49 PM
 */
public class DeviceConfigFuncWidget {
    public final View render(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.device_config_view_func, null, false);
        Intrinsics.checkExpressionValueIsNotNull(rootView, "rootView");
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        // EZ Mode
        rootView.findViewById(R.id.tvEzMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeModel.getCurrentHome(v.getContext()) == 0) {
                    Toast.makeText(
                            rootView.getContext(),
                            rootView.getContext().getString(R.string.home_current_home_tips),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                } else {
                    v.getContext().startActivity(new Intent(v.getContext(), DeviceConfigEZActivity.class));
                }
            }
        });

        // AP Mode
        rootView.findViewById(R.id.tvApMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeModel.getCurrentHome(v.getContext()) == 0) {
                    Toast.makeText(
                            rootView.getContext(),
                            rootView.getContext().getString(R.string.home_current_home_tips),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                } else {
                    v.getContext().startActivity(new Intent(v.getContext(), DeviceConfigAPActivity.class));
                }
            }
        });

        // Ble Low Energy
        rootView.findViewById(R.id.tv_ble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeModel.getCurrentHome(v.getContext()) == 0) {
                    Toast.makeText(
                            rootView.getContext(),
                            rootView.getContext().getString(R.string.home_current_home_tips),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                } else {
                    v.getContext().startActivity(new Intent(v.getContext(), DeviceConfigBleAndDualActivity.class));
                }

            }
        });

    }
}
