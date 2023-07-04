package com.gyyx.androidsdk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gyyx.androidsdk.Tool.HttpTool;
import com.gyyx.androidsdk.Tool.MySharedPreferences;
import com.gyyx.androidsdk.Tool.UnityTool;
import com.gyyx.androidsdk.androidsdk.R;

import java.util.Random;

import androidx.appcompat.app.AlertDialog;

public class PrivacyAgreementActivity {

    AlertDialog dialog;

    /**
     * 隐私同意弹窗
     * */
    public void startDialog(Context context) {
        if (context == null){
            return;
        }
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        //对话框弹出后点击或按返回键不消失;
        dialog.setCancelable(false);

        final Window window = dialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.privacy_agreement_layout);
            window.setGravity(Gravity.CENTER);
            //window.setWindowAnimations(com.jm.core.R.style.anim_panel_up_from_bottom);//设置动画
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            //params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);
            TextView textView = window.findViewById(R.id.tv_1);
            TextView tvCancel= window.findViewById(R.id.tv_cancel);
            TextView tvAgree= window.findViewById(R.id.tv_agree);
            tvCancel.setOnClickListener(view ->startFinish());
            tvAgree.setOnClickListener(view -> enterApp());

            String str = context.getString(R.string.authorize_dialog_content);//获取string xml中的内容
            textView.setText(str);

            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append(str);
            final int start = str.indexOf("《");//第一个出现的位置
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //打开隐私政策界面，查看隐私政策
                    MainActivity.mActivity.startActivity(new Intent(MainActivity.mActivity, PrivacyAgreementWebViewActivity.class).putExtra("URL","https://game-yxy.gyyx.cn/pet/Protected.html?"+randomNum()));
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
                    MainActivity.mActivity.startActivity(new Intent(MainActivity.mActivity, PrivacyAgreementWebViewActivity.class).putExtra("URL","https://game-yxy.gyyx.cn/pet/Account.html?"+randomNum()));
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
    }

    //
    private void enterApp() {
        //同意并继续，关闭界面并保存状态
        UnityTool.callUnity("Agree", "ok!");
        new Thread() {
            public void run() {
                try {
                    //HttpTool.postHttps("event_3",null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        MainActivity.sharedPreferencesUtil.saveData(MySharedPreferences.Contants.IS_FIRST_START, false);
        dialog.cancel();
    }

    private void startFinish() {
        //更改状态，finish APP
       // SPUtils.getInstance().put(SP_IS_FIRST_ENTER_APP, true);
        UnityTool.callUnity("Disagree", "No!");
        new Thread() {
            public void run() {
                try {
                    //HttpTool.postHttps("event_4", MainActivity.mActivity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        //dialog.cancel();
        //MainActivity.unityActivity.finish();//点击不同意退出应用
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

    //返回5位随机数用来避免链接加载缓存
    private String randomNum(){
        Random random = new Random();
        int num = -1 ;

        for (int i = 0;i<1;i++){
            num = (int)(random.nextDouble()*(100000 - 10000) + 10000);
        }
        Log.i("Gyyx", "输出随机数"+num+"");
        return num+"";
    }
}
