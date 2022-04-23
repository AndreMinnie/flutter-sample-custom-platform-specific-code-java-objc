package com.example.batterylevel_objc_java;

import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugin.common.MethodChannel;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "samples.flutter.dev/battery";
    private ShellUtils shellUtils = null;

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            // Note: this method is invoked on the main thread.
                            if (call.method.equals("getBatteryLevel")) {
                                int batteryLevel = getBatteryLevel();

                                if (batteryLevel != -1) {
                                    result.success(batteryLevel);
                                } else {
                                    result.error("UNAVAILABLE", "Battery level not available.", null);
                                }
                            }

                            if (call.method.equals("gpioSetStateOn")) {
                                boolean gpioSetState = gpioSetStateOne(1);

                                if (gpioSetState != false) {
                                    result.success("1");
                                } else {
                                    result.error("UNAVAILABLE", "GPIO State not available.", null);
                                }
                            }
                            else if (call.method.equals("gpioSetStateOff")) {
                                boolean gpioSetState = gpioSetStateOne(0);

                                if (gpioSetState != false) {
                                    result.success("1");
                                } else {
                                    result.error("UNAVAILABLE", "GPIO State not available.", null);
                                }
                            }


                        }
                );
        }
    private int getBatteryLevel() {
        int batteryLevel = -1;
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else {
            Intent intent = new ContextWrapper(getApplicationContext()).
                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }

        return batteryLevel;
    }

    private String gpioReadStateOne() {
        String value = null;
        File file = new File("/sys/class/gpio/gpio22/value");
        try {
            InputStream instream = new FileInputStream(file);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                value = buffreader.readLine();
                instream.close();
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }

    public boolean gpioSetStateOne(int state){

        String cmd = "echo "+state+" > /sys/class/gpio/gpio22/value\n";
        ShellUtils.CommandResult commandResult = shellUtils.execCommand(cmd, false);
        if (commandResult.result == 0 ) {
//            displayToast(name, " set state succeed !");
            return true;
        }else {
            System.out.println("errerr--> "+commandResult.errorMsg);
//            displayToast(name, " set state failed !");
            return false;
        }
    }

}


//public class MainActivity extends FlutterActivity {
//    // You do not need to override onCreate() in order to invoke
//    // GeneratedPluginRegistrant. Flutter now does that on your behalf.
//
//    // ...retain whatever custom code you had from before (if any).
//    new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
//    .setMethodCallHandler(
//                (call, result) -> {
//                    if (call.method.equals("getBatteryLevel")) {
//                        int batteryLevel = getBatteryLevel();
//
//                        if (batteryLevel != -1) {
//                            result.success(batteryLevel);
//                        } else {
//                            result.error("UNAVAILABLE", "Battery level not available.", null);
//                        }
//                    } else {
//                        result.notImplemented();
//                    }
//            }
//    );
//}




