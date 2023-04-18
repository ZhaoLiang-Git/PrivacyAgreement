package com.gyyx.androidsdk;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.widget.Toolbar;

import com.gyyx.androidsdk.androidsdk.R;
import com.gyyx.androidsdk.notchlib.NotchScreenManager;

import androidx.annotation.Nullable;

public class PrivacyAgreementWebViewActivity extends Activity {
    public WebView mWebView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setRequestedOrientation(MainActivity.getUnityPlayerScreenOrientation());
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置窗口没有标题
        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.privacy_activity_agree_content);
        //把布局画到刘海外边
        NotchScreenManager.getInstance().setDisplayInNotch(this);
        Toolbar toolbar = findViewById(R.id.usercenter_toolbar);
        mWebView = findViewById(R.id.w_WebView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            WebView.setWebContentsDebuggingEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView!=null){
                    if ( mWebView.canGoBack()){
                        mWebView.goBack();
                    }
                    else {
                        finish();
                    }
                }
            }
        });
        getINFO();
    }

    private void getINFO() {
        // 获取传递过来的信息。
        String url = getIntent().getStringExtra("URL");
        String urlType =getIntent().getStringExtra("TYPE");
        setWebViewInfo(url,urlType);
    }

    public void setWebViewInfo(String url,String urltype){
        Log.i("Gyyx", "输出url ："+ url + "输出Type ："+urltype);
        //mWebView.setVisibility(View.VISIBLE);//显示协议界面
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.loadUrl(url);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                //有错误直接打开默认的协议避免出现错误
                if (urltype.equals("Account"))
                {
                    mWebView.loadUrl("file:///android_asset/Account.html");
                }
                else {
                    mWebView.loadUrl("file:///android_asset/Protected.html");
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int type  = mWebView.getVisibility();
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView!=null && type == View.VISIBLE) {
            if ( mWebView.canGoBack()){
                mWebView.goBack();
            }
            else {
                finish();
            }
            return true;
        }else {

            finish();
            return true;
        }
    }
}
