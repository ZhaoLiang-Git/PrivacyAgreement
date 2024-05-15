package com.gyyx.androidsdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.gzuliyujiang.oaid.DeviceIdentifier;
import com.gyyx.androidsdk.Tool.HttpTool;
import com.gyyx.androidsdk.Tool.MySharedPreferences;
import com.gyyx.androidsdk.Tool.UnityTool;
import com.gyyx.androidsdk.androidsdk.R;
import com.gyyx.androidsdk.notchlib.NotchScreenManager;

import java.util.Random;

import androidx.annotation.Nullable;

public class PrivacyAgreementActivityNew extends Activity {
    static String GamesID;
    public static Activity mActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        GamesID = UnityTool.getAMValue("SDK_GameID",this);
       // setRequestedOrientation(MainActivity.getUnityPlayerScreenOrientation());
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置窗口没有标题
        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setDisplayCutoutCanUse(this);
        setContentView(R.layout.privacy_agreement_layout_new);
        //把布局画到刘海外边
        NotchScreenManager.getInstance().setDisplayInNotch(this);
        init();
    }
    private void init() {
        TextView textView = findViewById(R.id.tv_content);
        TextView tvCancel= findViewById(R.id.tv_cancelnew);
        TextView tvAgree= findViewById(R.id.tv_agreenew);
        tvCancel.setOnClickListener(view ->startFinish());
        tvAgree.setOnClickListener(view -> enterApp());
        String infoString = getIntent().getStringExtra("TIPS");
        String str = this.getString(R.string.authorize_dialog_content);//获取string xml中的内容
        textView.setText(str);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        String fullString = str + "\n" + infoString;
        ssb.append(fullString);
        final int start = str.indexOf("《");//第一个出现的位置
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //打开隐私政策界面，查看隐私政策  https://game-yxy.gyyx.cn/pet/HCYProtected.html
                if (GamesID.equals("0")){
                    startActivity(new Intent(PrivacyAgreementActivityNew.this, PrivacyAgreementWebViewActivity.class).putExtra("URL","file:///android_asset/Protected.html").putExtra("TYPE","Protected"));
                }
                else {
                    startActivity(new Intent(PrivacyAgreementActivityNew.this, PrivacyAgreementWebViewActivity.class).putExtra("URL","https://lingge-pt.gyyx.cn/publice/agreement/content?id="+GamesID).putExtra("TYPE","Protected"));
                }
            }

            @Override

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(0xffff0000);//设置文件颜色红色
                // 去掉下划线
                ds.setUnderlineText(false);
            }

        }, start, start + 6, 0);

        final int end = str.lastIndexOf("《");//最后一个出现的位置

        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //查看用户协议
                if (GamesID.equals("0")){
                    startActivity(new Intent(PrivacyAgreementActivityNew.this, PrivacyAgreementWebViewActivity.class).putExtra("URL","file:///android_asset/Account.html").putExtra("TYPE","Account"));
                }
                else {
                    startActivity(new Intent(PrivacyAgreementActivityNew.this, PrivacyAgreementWebViewActivity.class).putExtra("URL", "https://lingge-pt.gyyx.cn/publice/agreement/content?id=" + GamesID + "&type=service_content").putExtra("TYPE","Account"));
                }
            }

            @Override

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(0xffff0000);//设置文件颜色红色
                // 去掉下划线
                ds.setUnderlineText(false);
            }

        }, end, end + 6, 0);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    //
    private void enterApp() {
        //同意并继续，关闭界面并保存状态
        new Thread() {
            public void run() {
                try {
                    MainActivity.EnterUnityActivity();
                    DeviceIdentifier.register(MainActivity.mActivity.getApplication());
                    //HttpTool.postHttpsXXG(UnityTool.GetMac(MainActivity.mContext),DeviceIdentifier.getOAID(MainActivity.mContext),"",DeviceIdentifier.getAndroidID(MainActivity.mContext),String.valueOf(GamesID),UAMain.getIPv4(),DeviceIdentifier.getPseudoID(),null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        MainActivity.sharedPreferencesUtil.saveData(MySharedPreferences.Contants.IS_FIRST_START, false);
        finish();
    }

    private void startFinish() {
        //更改状态，finish APP
        //发送打点请求
//        new Thread() {
//            public void run() {
//                try {
//                    HttpTool.postHttps("event_4", MainActivity.mActivity);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
        //点击不同意退出应用
        if (mActivity!=null)mActivity.finish();
        if (MainActivity.mActivity !=null)MainActivity.mActivity.finish();
        // 关闭杀死进程
        // android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 获取是不是第一次进入app
     * */
    private boolean getisFirst(){
        boolean isFirst = (boolean) MainActivity.sharedPreferencesUtil.getData(MySharedPreferences.Contants.IS_FIRST_START, true);
        return isFirst;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
        }
        return true;
    }

    //返回5位随机数用来避免链接加载缓存
    private String randomNum(){
        Random random = new Random();
        int num = -1 ;

       for (int i = 0;i<1;i++){
            num = (int)(random.nextDouble()*(100000 - 10000) + 10000);
            //if(!(
           // num+"").contains("4")) break ;

        }
        Log.i("Gyyx", "输出随机数"+num+"");
       return num+"";
    }
}
