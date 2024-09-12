package com.gyyx.androidsdk.Tool;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

public class IPUntils {

   public static String IP = "";
   public static String getInterIP1() throws Exception {
      return InetAddress.getLocalHost().getHostAddress();
   }

   public static String getInterIP2() throws SocketException {
      String localip = null;// 本地IP，如果没有配置外网IP则返回它
      String netip = null;// 外网IP
      Enumeration<NetworkInterface> netInterfaces;
      netInterfaces = NetworkInterface.getNetworkInterfaces();
      InetAddress ip = null;
      boolean finded = false;// 是否找到外网IP
      while (netInterfaces.hasMoreElements() && !finded) {
         NetworkInterface ni = netInterfaces.nextElement();
         Enumeration<InetAddress> address = ni.getInetAddresses();
         while (address.hasMoreElements()) {
            ip = address.nextElement();
            if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
               netip = ip.getHostAddress();
               finded = true;
               break;
            } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
               localip = ip.getHostAddress();
            }
         }
      }
      if (netip != null && !"".equals(netip)) {
         return netip;
      } else {
         return localip;
      }
   }

   public static String getIPV4(){
      BufferedReader in = null;
      StringBuilder result = new StringBuilder();
      HttpURLConnection conn = null;
      try {
         URL url = new URL("http://ip-api.com/json/?lang=zh-CN");
         conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("GET");
         //Get请求不需要DoOutPut
         conn.setDoOutput(false);
         conn.setDoInput(true);
         // 设置连接超时为5秒
         conn.setConnectTimeout(2000);
         conn.setReadTimeout(2000);
         conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         conn.setUseCaches(false);
         //连接服务器
         conn.connect();

         if (200 == conn.getResponseCode()) {
            //请求成功
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
               result.append(line);
               System.out.println(line);
            }
            Log.i("GYYX","获取ip请求成功");
         } else {
            //请求失败
            Log.i("GYYX","获取ip请求失败");
         }
      }
      catch (Exception e){
         e.printStackTrace();
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
      try {
         //正常处理流程
         JSONObject jsonObj = new JSONObject(result.toString());
         String query = jsonObj.getString("query");
         IP = query;
         return query;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return "";
   }
}
