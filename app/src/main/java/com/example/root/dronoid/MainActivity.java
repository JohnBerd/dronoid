package com.example.root.dronoid;

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
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.root.dronoid.Menu.mBtDevice;

public class MainActivity extends Activity {

    private GameController game_controller;
    private GameControllerBg game_controller_bg;
    private BluetoothDevice mBluetoothDevice = null;
    public static BluetoothGatt mBluetoothGatt;
    public static BluetoothGattCharacteristic mBluetoothCaracteristic;

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        game_controller = findViewById(R.id.game_controller);
        game_controller_bg = findViewById(R.id.game_controller_bg);
        mBluetoothDevice = mBtDevice;
        mBluetoothGatt = mBtDevice.connectGatt(getApplicationContext(), false, mGattCallback);

    }

    private final BluetoothGattCallback mGattCallback =
            new BluetoothGattCallback() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                    int newState) {
                    String intentAction;
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        intentAction = ACTION_GATT_CONNECTED;
                        Log.i(TAG, "Connected to GATT server.");
                        Log.i(TAG, "Attempting to start service discovery:" +
                                mBluetoothGatt.discoverServices());

                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        intentAction = ACTION_GATT_DISCONNECTED;
                        Log.i(TAG, "Disconnected from GATT server.");
                        Menu.connection_lost = true;
                        goToMenu();
                    }
                }

                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                // New services discovered
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        mBluetoothGatt = gatt;
                        List<BluetoothGattService> services = gatt.getServices();
                        Log.i("test","service discovered");
                        Log.i(TAG, "Services: " + services.toString());
                        mBluetoothCaracteristic = services.get(3).getCharacteristics().get(0);
                    } else {
                        Log.w(TAG, "onServicesDiscovered received: " + status);
                    }
                }

                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                // Result of a characteristic read operation
                public void onCharacteristicRead(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic,
                                                 int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        Log.i("test","char read");
                        byte[] charValue = characteristic.getValue();
                        try {
                            String value = new String(charValue, "UTF-8");
                            Log.i(TAG, "Characteristic: " + value);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

    public void goToMenu()
    {
        Intent ctrl = new Intent(getApplicationContext(), Menu.class);
        startActivity(ctrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        game_controller.onResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}