package com.example.root.dronoid;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static android.content.ContentValues.TAG;
import static java.nio.charset.StandardCharsets.*;


public class Menu extends Activity {
    private ImageButton scanBtn;
    private TextView menu_text;
    private ImageView spinner;
    private AnimationDrawable animation;
    public static BluetoothAdapter adapter = null;
    public static BluetoothDevice mBtDevice = null;
    private BluetoothGattCharacteristic characteristic;
    private boolean mScanning;
    private BluetoothGatt mBluetoothGatt;
    private Handler mHandler = new Handler();
    private static final long SCAN_PERIOD = 5000;
    private String moduleName = "JDY-09-V4.3";
    private String moduleAdress = "BC:6A:29:C0:1E:92";
    private Vibrator v;
    public static boolean connection_lost = false;

    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;


    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    @SuppressLint("ResourceType")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        scanBtn = findViewById(R.id.launch);
        menu_text = findViewById(R.id.menu_text);
        spinner = findViewById(R.id.spinner);

        spinner.setBackgroundResource(R.drawable.spinner);
        animation = (AnimationDrawable) spinner.getBackground();

        adapter = BluetoothAdapter.getDefaultAdapter();

        if (connection_lost) {
            menu_text.setText("Connection lost!");
            connection_lost = false;
        }

        if (adapter == null)
        {
            message("Bluetooth not available on your device, sorry, have to quit!");
            finish();
        }

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                animation.start();
                menu_text.setText("Scanning...");
                if (!adapter.isEnabled())
                {
                    adapter.enable();
                    SystemClock.sleep(2000);
                }
                scanLeDevice(true);
            }
        });
    }

    BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run() {
                    if (device != null)
                    {
                        if (device.getAddress().equals(moduleAdress))
                        {
                            menu_text.setText("Device Found baby!");
                            mBtDevice = device;
                            spinner.setVisibility(View.GONE);
                            animation.stop();
                            goToController();
                        }
                    }
                }
            });
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void scanLeDevice(final boolean enable) {
        if (enable)
        {
            mHandler.postDelayed(new Runnable() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run() {
                    mScanning = false;
                    adapter.stopLeScan(scanCallback);
                    if (mBtDevice == null) {
                        spinner.setVisibility(View.GONE);
                        animation.stop();
                        menu_text.setText("Device not found, sorry!");
                    }

                }
            }, SCAN_PERIOD);
            mScanning = true;
                adapter.startLeScan(scanCallback);
        } else {
            mScanning = false;
                adapter.stopLeScan(scanCallback);
        }

    }

    public void message(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void goToController()
    {
        Intent ctrl = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(ctrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    @Override
    protected void onDestroy() {
        if (adapter.isEnabled())
            adapter.disable();
        super.onDestroy();
    }
}