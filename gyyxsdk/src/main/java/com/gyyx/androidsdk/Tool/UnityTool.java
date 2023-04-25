package com.gyyx.androidsdk.Tool;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import com.gyyx.androidsdk.MainActivity;
import com.unity3d.player.UnityPlayer;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class UnityTool {
    private static final String UNITY_LOG = "GYYX";
    private static final String UNITY_LISTENER = "AndroidSDKListener";
    private static AlertDialog.Builder alert;

    /**
     * 在unity中打印log
     *
     * @param content 日志内容
     */
    public static void unityLog(String content) {
        callUnity(UNITY_LOG, content);
    }

    /**
     * 调用UnityC#脚本中的方法
     */
    public static void callUnity(String methodName, String params) {
        UnityPlayer.UnitySendMessage(UNITY_LISTENER, methodName, params);
    }

    // 传入msg，弹出一个Toast提示
    public static void showMessage(final String msg) {

        MainActivity.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.mContext, msg, Toast.LENGTH_LONG).show();
            }
        });

    }

    // 弹出一个提示窗口，窗口需要的文字信息从strings.xml里面获取，点击确认关闭
    public static void showAlertView() {

        alert = new AlertDialog.Builder(MainActivity.mActivity).setTitle("弹出窗口").setMessage(MainActivity.mContext.getResources().getIdentifier("msgAlert", "string", MainActivity.mContext.getPackageName())).setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                MainActivity.mActivity.finish();
            }
        });

        MainActivity.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                alert.show();
            }
        });
    }

    // 重启应用
    public static void restartApp() {
        Log.d("restartApp", "restartApp");
        // 延迟重启
        Intent intent = MainActivity.mContext.getPackageManager().getLaunchIntentForPackage(MainActivity.mContext.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(MainActivity.mActivity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) MainActivity.mContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, restartIntent); // 10ms后重启应用
        System.exit(0);
    }


    //获取AndroidManifest.xml中的meta-data
    public static int getAMValue(String name , Activity activity){
        if (name.equals("")||activity == null)
        {
            Log.i("GYYX","传入的参数错误请检查");
            return 0;
        }
        PackageManager pm = activity.getPackageManager();
        int val = 0;
        try {
            ApplicationInfo info =pm.getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
            val =  info.metaData.getInt(name);
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("GYYX","获取参数异常"+e.getMessage());
            e.printStackTrace();
        }
        return val;
    }
    public static String getStartPackageName(String name ,Activity activity){
        ActivityInfo actInfo = null;
        String packageName ="";
        try {
            //获取AndroidManifest.xml配置的元数据
            actInfo = activity.getPackageManager().getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
            packageName = actInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }
    //传入Activity对象获取屏幕朝向
    public static int getActivityOrientation(Activity activity){
        if (activity == null){
            return -2;
        }
        int orientation = activity.getRequestedOrientation();
        return orientation;
    }
    /**
     * 获得mac地址
     * @return
     */
    public static  String GetMac(Context context){
        String mac = "";
        String macx = getNewMac();
        if ("02:00:00:00:00:00".equals(macx)||"".equals(macx)){
            mac = tryGetWifiMac(context);
        }
        else {
            mac = getNewMac();
        }
        return mac;
    }

    /**
     * 通过网络接口取
     * @return
     */
    public static String getNewMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 通过WiFiManager获取mac地址
     * @param context
     * @return
     */
    public static String tryGetWifiMac(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();

        if (wi == null || wi.getMacAddress() == null) {
            return null;
        }
        if ("02:00:00:00:00:00".equals(wi.getMacAddress().trim())) {
            return null;
        } else {
            return wi.getMacAddress().trim();
        }
    }
    /**
     * 通过WiFiManager获取局域网IP地址
     * @param context
     * @return
     */
    public static String getIP(Context context){
        String ip ="";
        WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
            Log.d("GYY", "IP地址：" + ip);
        }
        return ip;
    }

    // 获取IPV4地址
    public static String getIPv4Address(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress address : addresses) {
                    if (!address.isLoopbackAddress()) {
                        String sAddr = address.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) {
                                return sAddr;
                            }
                        } else {
                            if (!isIPv4) {
                                // 删除ip6区域后缀
                                int delim = sAddr.indexOf('%');
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        }catch (Exception ignored) {
        }
        return "";
    }
}
