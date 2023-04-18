package com.gyyx.androidsdk.Tool;

import android.app.Activity;
import android.util.Log;


import com.gyyx.androidsdk.PrivacyAgreementActivity;
import com.gyyx.androidsdk.PrivacyAgreementActivityNew;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

//http请求工具类
public class HttpTool {
    public static String getHtml(String pathUrl) throws Exception {
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(pathUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //Get请求不需要DoOutPut
            conn.setDoOutput(false);
            conn.setDoInput(true);
            // 设置连接超时为5秒
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //连接服务器
            conn.connect();
            // 判断请求Url是否成功
            Log.i("GYYX","输出"+conn.getResponseCode());
            if (200 == conn.getResponseCode()) {
                //请求成功
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                    System.out.println(line);
                }
                Log.i("GYYX","请求成功");
            } else {
                //请求失败
                Log.i("GYYX","请求失败显示默认的隐私协议");
                return "300";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Log.i("GYYX","请求出现异常显示默认的隐私协议");
            return "300";
        }
        finally {
            try{
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return result.toString();
    }

    public static void postHttps(String event ,Activity activity) throws IOException {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try{
            URL url = new URL("http://yg-game.gyyx.cn:11227/send-message");
//            JSONObject FacebookLoginInfoReturn = new JSONObject();
//            FacebookLoginInfoReturn.put("logEventType",event);
//            FacebookLoginInfoReturn.put("logOccurTime", new Date().getTime());
//            FacebookLoginInfoReturn.toString();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //获取输出流
            out = new OutputStreamWriter(conn.getOutputStream());
            JSONObject param = new JSONObject();
            param.put("topic", "sgjxs_test_cn");
            String messageJsonValue ="{\"logEventType\":\""+event+"\",\"logOccurTime\":"+new Date().getTime()+"}";
            Log.i("GYYX",messageJsonValue);
            //String messageJsonValue = "{"+"logEventType"+":"+event+","+"logOccurTime"+":"+ new Date().getTime() +"}";
            param.put("message", messageJsonValue);
            String jsonStr = param.toString();
            out.write(jsonStr);
            out.flush();
            out.close();
            //取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null){
                    result.append(line);
                    System.out.println(line);
                }
                //请求成功
                Log.i("GYYX","请求成功");
                if (PrivacyAgreementActivityNew.mActivity !=null)PrivacyAgreementActivityNew.mActivity.finish();
                if (activity!=null)activity.finish();
            }else{
                System.out.println("ResponseCode is an error code:" + conn.getResponseCode());
                //请求失败
                Log.i("GYYX","请求失败");
                if (PrivacyAgreementActivityNew.mActivity !=null)PrivacyAgreementActivityNew.mActivity.finish();
                if (activity!=null)activity.finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }

    public static void postHttpsXXG(String mac,String oaid,String idfa,String androidid,int gameid ,Activity activity) throws IOException {
        Log.i("GYYX mac 信息",mac);
        Log.i("GYYX oaid 信息",oaid);
        Log.i("GYYX idfa 信息",idfa);
        Log.i("GYYX androidid 信息",androidid);
        Log.i("GYYX gameid 信息",gameid+"");
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try{
            URL url = new URL("https://lingge-pt.gyyx.cn/api/dot/activate");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //获取输出流
            out = new OutputStreamWriter(conn.getOutputStream());
            JSONObject param = new JSONObject();
            param.put("mac", mac);
            param.put("oaid",  oaid);
            param.put("idfa", idfa);
            param.put("androidid", androidid);
            param.put("game_id", gameid);
            String jsonStr = param.toString();
            out.write(jsonStr);
            out.flush();
            out.close();
            //取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null){
                    result.append(line);
                    System.out.println(line);
                }
                //请求成功
                Log.i("GYYX","请求成功");
                if (PrivacyAgreementActivityNew.mActivity !=null)PrivacyAgreementActivityNew.mActivity.finish();
                if (activity!=null)activity.finish();
            }else{
                System.out.println("ResponseCode is an error code:" + conn.getResponseCode());
                //请求失败
                Log.i("GYYX","请求失败");
                if (PrivacyAgreementActivityNew.mActivity !=null)PrivacyAgreementActivityNew.mActivity.finish();
                if (activity!=null)activity.finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }
}
