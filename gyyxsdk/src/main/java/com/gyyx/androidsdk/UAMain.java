package com.gyyx.androidsdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.github.gzuliyujiang.oaid.DeviceID;
import com.github.gzuliyujiang.oaid.DeviceIdentifier;
import com.github.gzuliyujiang.oaid.IGetter;
import com.gyyx.androidsdk.Tool.HttpTool;
import com.gyyx.androidsdk.Tool.MySharedPreferences;
import com.gyyx.androidsdk.Tool.UnityTool;
import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

public class UAMain {

    public static Context unityContext;
    public static Activity unityActivity;
    private static int isDebug = 0;
    public static UnityPlayer unityPlayer;

    //init方法，用来传入上下文
    public static void initContext(Context paramContext, UnityPlayer player) {
        unityContext = paramContext.getApplicationContext();
        unityActivity = (Activity) paramContext;
        unityPlayer = player;
        UnityTool.unityLog("InitContext ok!");
        UnityTool.callUnity("InitContext", "InitContext ok!");
        try {
            if (PrivacyAgreementActivityNew.GamesID != 0){
                HttpTool.postHttpsXXG(getMac(),getOAID(),"",getAndroidID(),String.valueOf(PrivacyAgreementActivityNew.GamesID),getIPv4(),getPseudoID(),null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initSdk(String json) {
        try {
            UnityTool.unityLog("init sdk with params:" + json);
            JSONObject info = new JSONObject(json);
            JSONObject adInfoJson = info.getJSONObject("adInfo");
            String a = adInfoJson.getString("ISDEBUG");
            try
            {
                isDebug = Integer.parseInt(a);
            } catch (NumberFormatException e)
            {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            UnityTool.unityLog("android sdk init fail!");
            return;
        }
        UnityTool.callUnity("InitSDKCallback", "init ok!");
    }

    /**
     * 显示web界面
     */
    public static void ShowWeb(String Url){
        //打开隐私政策界面，查看隐私政策
        unityActivity.startActivity(new Intent(unityActivity, PrivacyAgreementWebViewActivity.class).putExtra("URL",Url));

    }

    public static String PrivacyAgreementState(){
        String First = "";
        boolean isFirst = (boolean) MainActivity.sharedPreferencesUtil.getData(MySharedPreferences.Contants.IS_FIRST_START, true);
        if (isFirst) {
            //未同意
            First  = "0";

        } else {
            //已同意
            First  = "1";
        }
        return First;
    }
    // 获取IMEI，只支持Android 10之前的系统，需要READ_PHONE_STATE权限，可能为空
    public static String getImei(){
       return DeviceIdentifier.getIMEI(unityContext);
    }
    // 获取安卓ID，可能为空
    public static String getAndroidID(){
        return DeviceIdentifier.getAndroidID(unityContext);
    }
    // 获取数字版权管理ID，可能为空
    public static String getWidevineID(){
        return DeviceIdentifier.getWidevineID();
    }
    // 获取伪造ID，根据硬件信息生成，不会为空，有大概率会重复
    public static String getPseudoID(){
        return DeviceIdentifier.getPseudoID();
    }
    // 获取GUID，随机生成，不会为空
    public static String getGUID(){
        return DeviceIdentifier.getGUID(unityContext);
    }
    // 是否支持OAID/AAID
    public static boolean supportedOAID(){
        return  DeviceID.supportedOAID(unityContext);
    }
    // 获取OAID/AAID，同步调用
    public static String getOAID(){ return  DeviceIdentifier.getOAID(unityContext); }
    //获取mac,可能为空
    public static String getMac(){return  UnityTool.GetMac(unityContext); }
    //获取IPv4
    public static String getIPv4() { return  UnityTool.getIPv4Address(true); }
    //获取IPv6
    public static String getIPv6() { return  UnityTool.getIPv4Address(false); }

    // 获取OAID/AAID，异步回调

    public static void getOAIDAsynchronization(){
        DeviceID.getOAID(unityContext, new IGetter() {
            @Override
            public void onOAIDGetComplete(String result) {
                // 不同厂商的OAID/AAID格式是不一样的，可进行MD5、SHA1之类的哈希运算统一
                UnityTool.callUnity("OAIDAsynchronization", result);
            }

            @Override
            public void onOAIDGetError(Exception error) {
                // 获取OAID/AAID失败
                UnityTool.callUnity("OAIDAsynchronization", "");
            }
        });
    }
}
