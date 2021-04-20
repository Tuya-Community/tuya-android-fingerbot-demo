package com.tuya.appsdk.sample.device.mgt.control.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.L;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuya.appsdk.sample.device.mgt.R;
import com.tuya.appsdk.sample.device.mgt.control.dpItem.DpFaultItem;
import com.tuya.appsdk.sample.device.mgt.control.dpItem.mesh.MeshDpBooleanItem;
import com.tuya.appsdk.sample.device.mgt.control.dpItem.mesh.MeshDpCharTypeItem;
import com.tuya.appsdk.sample.device.mgt.control.dpItem.mesh.MeshDpEnumItem;
import com.tuya.appsdk.sample.device.mgt.control.dpItem.mesh.MeshDpIntegerItem;
import com.tuya.appsdk.sample.device.mgt.control.dpItem.normal.DpBooleanItem;
import com.tuya.appsdk.sample.device.mgt.control.dpItem.normal.DpCharTypeItem;
import com.tuya.appsdk.sample.device.mgt.control.dpItem.normal.DpEnumItem;
import com.tuya.appsdk.sample.device.mgt.control.dpItem.normal.DpIntegerItem;
import com.tuya.appsdk.sample.device.mgt.control.dpItem.normal.DpRawTypeItem;
import com.tuya.appsdk.sample.device.mgt.main.SinglePicker;
import com.tuya.smart.android.device.bean.BitmapSchemaBean;
import com.tuya.smart.android.device.bean.BoolSchemaBean;
import com.tuya.smart.android.device.bean.EnumSchemaBean;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.android.device.bean.StringSchemaBean;
import com.tuya.smart.android.device.bean.ValueSchemaBean;
import com.tuya.smart.android.device.enums.DataTypeEnum;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.rnplugin.tyrctwheelviewmanager.picker.WheelPicker;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuya.smart.sdk.api.IDeviceListener;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Finger Bot feature
 *
 * @author shawang <a href="mailto:developer@tuya.com"/>
 * @since 2021/4/8 5:48 PM
 */
public class FingerBotMgtControlPanelActivity extends AppCompatActivity {


    WheelPicker wheelPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finger_bot_mgt_activity_control_panel);

        String deviceId = getIntent().getStringExtra("deviceId");
        ITuyaDevice mDevice = TuyaHomeSdk.newDeviceInstance(deviceId);

        Toolbar toolbar = findViewById(R.id.topAppBar_finger);
        TextView tvLeft_time = findViewById(R.id.time_left);

        //toolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // device reset factory
                mDevice.resetFactory(new IResultCallback() {
                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(FingerBotMgtControlPanelActivity.this,
                                "Activate error-->" + errorMsg,
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    @Override
                    public void onSuccess() {
                        finish();
                    }
                });
            }
        });

        findViewById(R.id.btnRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDevice.removeDevice(new IResultCallback() {
                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(FingerBotMgtControlPanelActivity.this,
                                "Activate error-->" + errorMsg,
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    @Override
                    public void onSuccess() {
                        finish();
                    }
                });
            }
        });

        SeekBar seekBar_down = findViewById(R.id.seek_bar_down);
        TextView tv_down = findViewById(R.id.tv_bar_down);
        SeekBar seekBar_up = findViewById(R.id.seek_bar_up);
        TextView tv_up = findViewById(R.id.tv_bar_up);
        ImageButton ib_switch_mode = findViewById(R.id.ib_switch_mode);
        TextView tv_switch_invert = findViewById(R.id.tv_switch_invert);
        ImageButton  btn_switch= findViewById(R.id.btn_switch);
        // ImageButton  btn_background= findViewById(R.id.btn_background);
        TextView tv_btn_switch = findViewById(R.id.tv_btn);
        LinearLayout linearLayout = findViewById(R.id.ll_switch_invert);

        //initData, info from cloud
        initData();

        //Data changes of down_percent
        seekBar_up.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress>50){
                    progress = 50;
                    seekBar_up.setProgress(50);
                }
                String string = String.valueOf(progress) + "%";
                tv_up.setText(string);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                HashMap map = new HashMap();
                map.put("15", seekBar_up.getProgress());
                mDevice.publishDps(JSONObject.toJSONString(map), new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {

                    }

                    @Override
                    public void onSuccess() {
                        Log.i("tonghui","15 succeed");
                    }
                });
            }
        });

        //Data changes of up_percent
        seekBar_down.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress<50){
                    progress = 51;
                    seekBar_down.setProgress(51);
                }
                String string = String.valueOf(progress) + "%";
                tv_down.setText(string);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                HashMap map = new HashMap();
                map.put("9", seekBar_down.getProgress());
                mDevice.publishDps(JSONObject.toJSONString(map), new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {

                    }

                    @Override
                    public void onSuccess() {
                        Log.i("tonghui","9 succeed");
                    }
                });
            }
        });

        //Data changes of click_sustain_time
        tvLeft_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog bottomDialog = new Dialog(FingerBotMgtControlPanelActivity.this, R.style.BottomDialog);
                View contentView = LayoutInflater.from(FingerBotMgtControlPanelActivity.this).inflate(R.layout.dialog_content_time_left, null);
                bottomDialog.setContentView(contentView);
                ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                layoutParams.width = getResources().getDisplayMetrics().widthPixels;
                contentView.setLayoutParams(layoutParams);
                bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
                bottomDialog.show();

                wheelPicker = contentView.findViewById(R.id.wp_left_time);
                List<Double> list = new ArrayList<>();
                for(Double i = 2.0 ; i < 101 ; i ++){
                    list.add( i / 10.0);
                }
                wheelPicker.setData(list);
                initWP();
                initWPShow();
                initWPText();
                TextView tv_cancel = contentView.findViewById(R.id.tvCancel);
                TextView tv_confirm = contentView.findViewById(R.id.tvConfirm);

                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomDialog.dismiss();
                    }
                });

                tv_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tv_time_left = findViewById(R.id.time_left);
                        bottomDialog.dismiss();
                        Double time_left = (Double) wheelPicker.getData().get(wheelPicker.getCurrentItemPosition());
                        tv_time_left.setText(time_left+" s");
                        HashMap map = new HashMap();
                        map.put("10", (int)(time_left * 10));
                        mDevice.publishDps(JSONObject.toJSONString(map), new IResultCallback() {
                            @Override
                            public void onError(String code, String error) {

                            }

                            @Override
                            public void onSuccess() {
                                Log.i("tonghui","10 succeed");
                            }
                        });
                    }
                });
            }
        });

        //Data changes of switch
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_btn_switch.getText().equals(getResources().getString(R.string.btn_switch_Off))){
                    tv_btn_switch.setText(R.string.btn_switch_On);
                    btn_switch.setSelected(true);
                    HashMap map = new HashMap();
                    map.put("2", true);
                    mDevice.publishDps(JSONObject.toJSONString(map), new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                            Log.i("tonghui","101 unsucceed");
                        }

                        @Override
                        public void onSuccess() {
                            Log.i("tonghui","101 succeed");

                        }
                    });
                } else {
                    tv_btn_switch.setText(R.string.btn_switch_Off);
                    btn_switch.setSelected(false);
                    HashMap map = new HashMap();
                    map.put("2", false);
                    mDevice.publishDps(JSONObject.toJSONString(map), new IResultCallback(){
                        @Override
                        public void onError(String code, String error) {
                            Log.i("tonghui","101 unsucceed");
                        }

                        @Override
                        public void onSuccess() {
                            Log.i("tonghui","101 succeed");
                        }
                    });
                }
            }
        });

        //Data changes of mode
        ib_switch_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = findViewById(R.id.ll_switch_invert);
                if(!ib_switch_mode.isSelected()){
                    ib_switch_mode.setSelected(true);
                    linearLayout.setVisibility(View.VISIBLE);
                    HashMap map = new HashMap();
                    map.put("8", "switch");
                    map.put("101",false);
                    mDevice.publishDps(JSONObject.toJSONString(map), new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {

                        }

                        @Override
                        public void onSuccess() {
                            Log.i("tonghui","8 succeed");
                        }
                    });
                } else {
                    ib_switch_mode.setSelected(false);
                    linearLayout.setVisibility(View.INVISIBLE);
                    HashMap map = new HashMap();
                    map.put("8", "click");
                    map.put("101",true);
                    mDevice.publishDps(JSONObject.toJSONString(map), new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {

                        }

                        @Override
                        public void onSuccess() {
                            Log.i("tonghui","8 succeed");
                        }
                    });
                }
            }
        });

        //Data changes of control_back
        tv_switch_invert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_switch_invert.getText().equals(getResources().getString(R.string.Switch_invert_off))){
                    tv_switch_invert.setText(R.string.Switch_invert_on);
                    HashMap map = new HashMap();
                    map.put("11", "up_on");
                    mDevice.publishDps(JSONObject.toJSONString(map), new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {
                        }
                        @Override
                        public void onSuccess() {

                        }
                    });
                } else {
                    tv_switch_invert.setText(R.string.Switch_invert_off);
                    HashMap map = new HashMap();
                    map.put("11", "up_off");
                    mDevice.publishDps(JSONObject.toJSONString(map), new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {

                        }

                        @Override
                        public void onSuccess() {
                            Log.i("tonghui","11 succeed");
                        }
                    });
                }
            }
        });

        //Register devlisten to keep watch of the dp changes of the device
        mDevice.registerDevListener(new IDevListener() {
            @Override
            public void onDpUpdate(String devId, String dpStr) {
                JSONObject jsonObject= (JSONObject) JSON.parse(dpStr);
                Map<String, Object> map = jsonObject;
                Log.d("dpstr",dpStr);
                for (String key : map.keySet()) {
                    switch (key) {
                        case "8":
                            ImageButton ib_switch_mode = findViewById(R.id.ib_switch_mode);
                            if(String.valueOf(map.get(key)).equals("click")){
                                ib_switch_mode.setSelected(false);
                                linearLayout.setVisibility(View.INVISIBLE);
                            } else {
                                ib_switch_mode.setSelected(true);
                                linearLayout.setVisibility(View.VISIBLE);
                            }
                            break;
                        case "11":
                            TextView tv_switch_invert = findViewById(R.id.tv_switch_invert);
                            if(String.valueOf(map.get(key)).equals("up_off")){
                                tv_switch_invert.setText(R.string.Switch_invert_off);
                            } else {
                                tv_switch_invert.setText(R.string.Switch_invert_on);
                            }
                            break;
                        case "12":
                            TextView tvBattery = findViewById(R.id.tv_battery);
                            tvBattery.setText(String.valueOf(map.get(key))+"%");
                            break;
                        case "9":
                            TextView tv_bar_down = findViewById(R.id.tv_bar_down);
                            SeekBar sb_down = findViewById(R.id.seek_bar_down);
                            tv_bar_down.setText(String.valueOf(map.get(key))+"%");
                            sb_down.setProgress((int)map.get(key));
                            break;
                        case "15":
                            TextView tv_bar_up = findViewById(R.id.tv_bar_up);
                            SeekBar sb_up = findViewById(R.id.seek_bar_up);
                            tv_bar_up.setText(String.valueOf(map.get(key))+"%");
                            sb_up.setProgress((int)map.get(key));
                            break;
                        case "2":
                            ImageButton  btn_switch= findViewById(R.id.btn_switch);
                            TextView tv_btn_switch = findViewById(R.id.tv_btn);
                            if(String.valueOf(map.get(key)).equals("true")){
                                tv_btn_switch.setText(R.string.btn_switch_On);
                                btn_switch.setSelected(true);
                            } else {
                                tv_btn_switch.setText(R.string.btn_switch_Off);
                                btn_switch.setSelected(false);
                            }
                            break;
                        case "10":
                            TextView tv_time_left = findViewById(R.id.time_left);
                            Double i;
                            i = (int)map.get(key) / 10.0;
                            tv_time_left.setText(String.valueOf(i)+" s");
                            break;
                    }
                }
            }

            @Override
            public void onRemoved(String devId) {

            }

            @Override
            public void onStatusChanged(String devId, boolean online) {

            }

            @Override
            public void onNetworkStatusChanged(String devId, boolean status) {

            }

            @Override
            public void onDevInfoUpdate(String devId) {

            }
        });
    }

    //Data for wheel picker
    private void initWPShow() {
        wheelPicker.setCyclic(false);
        wheelPicker.isCyclic();
        wheelPicker.setIndicator(true);
        wheelPicker.setIndicatorColor(0xFF123456);
        wheelPicker.setIndicatorSize(3);

        wheelPicker.setCurtain(false);
        wheelPicker.setCurtainColor(0xFF777777);

        wheelPicker.setAtmospheric(true);

        wheelPicker.setCurved(true);

        wheelPicker.setItemAlign(WheelPicker.ALIGN_CENTER);
    }
    private void initWPText() {
        wheelPicker.setSelectedItemTextColor(0xFF000000);

        wheelPicker.setItemTextColor(0xFF888888);
        wheelPicker.setItemTextSize(30);
    }
    private void initWP() {
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
//                wheelPicker.setSelectedItemPosition(position);
            }
        });

        wheelPicker.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int offset) {

            }

            @Override
            public void onWheelSelected(int position) {

            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });
    }

    private void initData(){

        String deviceId = getIntent().getStringExtra("deviceId");
        Map<String, SchemaBean> map = TuyaHomeSdk.getDataInstance().getSchema(deviceId);
        Collection<SchemaBean> schemaBeans = map.values();

        for (SchemaBean bean : schemaBeans) {

            DeviceBean deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(deviceId);

            Object value = deviceBean.getDps().get(bean.getId());


            switch (bean.getId()) {
                case "8":
                    LinearLayout linearLayout = findViewById(R.id.ll_switch_invert);
                    ImageButton ib_switch_mode = findViewById(R.id.ib_switch_mode);
                    if(String.valueOf(value).equals("click")){
                        ib_switch_mode.setSelected(false);
                        linearLayout.setVisibility(View.INVISIBLE);
                    } else {
                        ib_switch_mode.setSelected(true);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                case "11":
                    TextView tv_switch_invert = findViewById(R.id.tv_switch_invert);
                    if(String.valueOf(value).equals("up_off")){
                        tv_switch_invert.setText(R.string.Switch_invert_off);
                    } else {
                        tv_switch_invert.setText(R.string.Switch_invert_on);
                    }
                    break;
                case "12":
                    TextView tvBattery = findViewById(R.id.tv_battery);
                    tvBattery.setText(String.valueOf(value)+"%");
                    break;
                case "9":
                    TextView tv_bar_down = findViewById(R.id.tv_bar_down);
                    SeekBar sb_down = findViewById(R.id.seek_bar_down);
                    tv_bar_down.setText(String.valueOf(value)+"%");
                    sb_down.setProgress((int)value);
                    break;
                case "15":
                    TextView tv_bar_up = findViewById(R.id.tv_bar_up);
                    SeekBar sb_up = findViewById(R.id.seek_bar_up);
                    tv_bar_up.setText(String.valueOf(value)+"%");
                    sb_up.setProgress((int)value);
                    break;
                case "2":
                    ImageButton  btn_switch= findViewById(R.id.btn_switch);
                    TextView tv_btn_switch = findViewById(R.id.tv_btn);
                    if(String.valueOf(value).equals("true")){
                        tv_btn_switch.setText(R.string.btn_switch_On);
                        btn_switch.setSelected(true);
                    } else {
                        tv_btn_switch.setText(R.string.btn_switch_Off);
                        btn_switch.setSelected(false);
                    }
                    break;
                case "10":
                    TextView tv_time_left = findViewById(R.id.time_left);
                    Double i;
                    i = (int)value / 10.0;
                    tv_time_left.setText(String.valueOf(i)+" s");
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

}