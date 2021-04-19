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

package com.tuya.appsdk.sample.device.mgt.list.adapter;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.appsdk.sample.device.mgt.R;
import com.tuya.appsdk.sample.device.mgt.control.activity.DeviceMgtControlActivity;
import com.tuya.appsdk.sample.device.mgt.control.activity.FingerBotMgtControlPanelActivity;
import com.tuya.appsdk.sample.device.mgt.list.activity.DeviceMgtListActivity;
import com.tuya.appsdk.sample.device.mgt.list.activity.DeviceSubZigbeeActivity;
import com.tuya.smart.api.MicroContext;
import com.tuya.smart.panelcaller.api.AbsPanelCallerService;
import com.tuya.smart.sdk.bean.DeviceBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;

/**
 * Device list adapter
 *
 * @author chuanfeng <a href="mailto:developer@tuya.com"/>
 * @since 2021/2/21 10:06 AM
 */
public final class DeviceMgtAdapter extends RecyclerView.Adapter<DeviceMgtAdapter.ViewHolder> {

    public ArrayList<DeviceBean> data = new ArrayList<>();
    int type;
    private OnGoPanelPageListener mPanelPageListener;

    @NotNull
    public final ArrayList<DeviceBean> getData() {
        return this.data;
    }

    public final void setData(ArrayList<DeviceBean> list, int type) {
        this.data = list;
        this.type = type;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.device_mgt_item, parent, false));
        holder.itemView.setOnClickListener(v -> {
            switch (type) {
                case 1:
                    Log.e("pid", "finger robot pid:" + ((DeviceBean) DeviceMgtAdapter.this.getData()
                            .get(holder.getAdapterPosition())).getProductId());
                    if (TextUtils.equals(((DeviceBean) DeviceMgtAdapter.this.getData()
                            .get(holder.getAdapterPosition())).getProductId(), "y6kttvd6")) {
                        Intent intent = new Intent(v.getContext(), FingerBotMgtControlPanelActivity.class);
                        intent.putExtra("deviceId", ((DeviceBean) DeviceMgtAdapter.this.getData().get(holder.getAdapterPosition())).getDevId());
                        v.getContext().startActivity(intent);

                    } else {
                        mPanelPageListener.onPanelPage(((DeviceBean)
                                DeviceMgtAdapter.this.getData().get(holder.getAdapterPosition())).getDevId());
                    }


                    break;
                case 2:
                    // Navigate to zigBee sub device management
                    Intent intent2 = new Intent(v.getContext(), DeviceSubZigbeeActivity.class);
                    intent2.putExtra("deviceId", ((DeviceBean) DeviceMgtAdapter.this.getData().get(holder.getAdapterPosition())).getDevId());
                    v.getContext().startActivity(intent2);
                    break;
                case 3:
                    // Navigate to device management
                    Intent intent3 = new Intent(v.getContext(), DeviceMgtControlActivity.class);
                    intent3.putExtra("deviceId", ((DeviceBean) DeviceMgtAdapter.this.getData().get(holder.getAdapterPosition())).getDevId());
                    v.getContext().startActivity(intent3);
                    break;

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceMgtAdapter.ViewHolder holder, int position) {
        DeviceBean bean = (DeviceBean) data.get(position);
        holder.tvDeviceName.setText(bean.name);
        holder.tvStatus.setText(holder.itemView.getContext().getString(bean.getIsOnline() ? R.string.device_mgt_online : R.string.device_mgt_offline));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDeviceName;
        private final TextView tvStatus;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
            tvStatus = itemView.findViewById(R.id.tvDeviceStatus);
        }
    }

    public interface OnGoPanelPageListener {
        void onPanelPage(String deviceId);
    }

    public void setPanelListener(OnGoPanelPageListener listener) {
        this.mPanelPageListener = listener;
    }

}

