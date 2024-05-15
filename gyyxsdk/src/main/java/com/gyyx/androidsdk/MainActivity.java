package com.gyyx.androidsdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.github.gzuliyujiang.oaid.DeviceIdentifier;
import com.gyyx.androidsdk.Tool.HttpTool;
import com.gyyx.androidsdk.Tool.MySharedPreferences;
import com.gyyx.androidsdk.Tool.UnityTool;
import com.gyyx.androidsdk.androidsdk.R;
import com.unity3d.player.UnityPlayer;

import org.json.JSONObject;

import androidx.annotation.NonNull;
public class MainActivity extends Activity{
    public static MySharedPreferences.SharedPreferencesUtil sharedPreferencesUtil;
    public static Context mContext;
    public static Activity mActivity;
    //PrivacyAgreementActivity privacyAgreementActivity;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        mActivity = this;
        String GamesID = UnityTool.getAMValue("SDK_GameID",this);
        new Thread() {
            public void run() {
                try {
                    String result = HttpTool.getHtml("https://lingge-pt.gyyx.cn/publice/agreement/content?id="+GamesID+"&type=base");
                    if (result.equals("300")){
                        //请求出现异常的流程
                        String isOpen ="0";
                        String tips = mActivity.getString(R.string.authorize_dialog_content_new);//获取string xml中的内容
                        ShowPrivacyAgreementPanel(tips,isOpen);
                    }
                    else {
                        //正常处理流程
                        JSONObject jsonObj = new JSONObject(result);
                        JSONObject adInfoJson = jsonObj.getJSONObject("data");
                        String isOpen = adInfoJson.getString("switch");
                        String tips = adInfoJson.getString("tips");
                        ShowPrivacyAgreementPanel(tips,isOpen);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public static int getUnityPlayerScreenOrientation() {
        int screenOrientation = 1;
        try {
            ActivityInfo ai =  UnityPlayer.currentActivity.getPackageManager().getActivityInfo(UnityPlayer.currentActivity.getComponentName(), PackageManager.GET_META_DATA);
            screenOrientation = ai.screenOrientation;
            Log.d("GYYX", "Unity设置屏幕方向"+screenOrientation);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return screenOrientation;
    }
    protected void ShowPrivacyAgreementPanel(String tips,String isOpen){
        if (isOpen.equals("0")){
            //创建储存数据对象
            sharedPreferencesUtil = MySharedPreferences.SharedPreferencesUtil.getInstance(this);
            //获取存储的判断是否是同意隐私协议，默认为true
            boolean isFirst = (boolean) sharedPreferencesUtil.getData(MySharedPreferences.Contants.IS_FIRST_START, true);
            //你可以在这里做你想要的效果
            if (isFirst) {
                Log.i("GYYX","未同意过隐私协议app打开隐私同意");
                //privacyAgreementActivity = new PrivacyAgreementActivity();
                //privacyAgreementActivity.startDialog(MainActivity.this);
                startActivity(new Intent(MainActivity.this, PrivacyAgreementActivityNew.class).putExtra("TIPS",tips));

            } else {
                Log.i("GYYX","已同意不处理隐私协议");
                EnterUnityActivity();
                DeviceIdentifier.register(this.getApplication());//初始化获取设备信息的工具
            }
        }
    }


    //启动Unity Activity
    public static void EnterUnityActivity() {
        Intent unityAct = new Intent();
        String startPackageName = UnityTool.getStartPackageName("START_PACKAGE_NAME",mActivity);
        if (startPackageName.equals("")){
            unityAct.setClassName(mActivity, "com.unity3d.player.UnityPlayerActivity");
            mActivity.startActivity(unityAct);
        }
        else {
            Log.i("GYYX","获取unity的ActivitynName :"+startPackageName);
            unityAct.setClassName(mActivity, startPackageName);
            mActivity.startActivity(unityAct);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("MainActivity", "onRequestPermissionsResult:" + permissions.toString() + "  grantResults:" + grantResults.toString());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onPause() { super.onPause(); }

    @Override
    public void onStop() { super.onStop(); }

    @Override
    public void onResume() { super.onResume(); }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
