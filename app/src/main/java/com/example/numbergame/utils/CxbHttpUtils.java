package com.example.numbergame.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.numbergame.Result;
import com.example.numbergame.config.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>function:
 * <p>User: LeeJohn
 * <p>Date: 2018/9/12
 * <p>Version: 1.0
 */

public class CxbHttpUtils {
    /**
     * 登录成功后的ticket
     */
    private static String ticket = null;
    /**
     * 登录成功后的 userName
     */
    private static String userName = null;
    /**
     * 登录成功后的 passwd
     */
    private static String passwd = null;
    /**
     * result
     */
    public static String loginResult = null;
    public static String batchPostResult = null;
    public static String matchId;

    public static boolean login(String userName, String passwd) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("userName", userName);
            params.put("passwd", passwd);
            params.put("validCode", "");
            loginResult = OkHttpUtil.postForm(Config.loginUrl, params);
            JSONObject resultJSON = JSON.parseObject(loginResult);
            boolean hasError = resultJSON.getBoolean("hasError");
            if( !hasError ){
                //登录成功
                ticket = resultJSON.getString("ticket");
                CxbHttpUtils.userName = userName;
                CxbHttpUtils.passwd = passwd;
                return true;
            } else {
                Log.e(Config.TAG, loginResult);
            }
        } catch (Exception e) {
//            Log.e(Config.TAG,e.getMessage());
            e.printStackTrace();
        }
        ticket = null;
        CxbHttpUtils.userName = null;
        CxbHttpUtils.passwd = null;
        return false;
    }


    /**
     * 加载数据，返回当前 matchId
     * 获取不到抛异常到上层
     */
    private static String loadData() throws Exception {
        String result = OkHttpUtil.get(Config.loadDataUrl);
        JSONObject resultJSON = JSON.parseObject(result);
        String info = resultJSON.getString("info");
        JSONObject infoJSON = JSON.parseObject(info);
        String matchId = infoJSON.getString("matchId");
        return matchId;
    }

    /**
     * 提交订单
     * 提交成功 为true ,否则为false
     * <p>
     * Map<String,String> params = new HashMap<>();
     * params.put("kenoId","1");
     * params.put("matchId",matchId);
     * params.put("cart[0][playId]","1"); //第一注
     * params.put("cart[0][dtype]","1"); //玩法类型
     * params.put("cart[0][content]","");
     * params.put("cart[0][isComplex]","false"); //是否混合
     * params.put("cart[0][pl]","1.99"); //赔率
     * params.put("cart[0][money]","1"); //金额
     * //第二注的参数，第三注以此类推
     * params.put("cart[1][playId]","2"); //第二注
     * params.put("cart[1][dtype]","3");
     * params.put("cart[1][content]","");
     * params.put("cart[1][isComplex]","false");
     * params.put("cart[1][pl]","1.99");
     * params.put("cart[1][money]","6");
     */
    public static Result batchPost(Map<String, String> params) {
        boolean isSuccess = false;
        String message = "";
        try {
            if (ticket != null) {
                matchId = loadData();
                params.put("matchId", matchId);
                String cookie = "ticket=" + ticket;
                batchPostResult = OkHttpUtil.postForm(Config.batchPostUrl, params, cookie);
                JSONObject resultJSON = JSON.parseObject(batchPostResult);
                boolean hasError = resultJSON.getBoolean("hasError");
                if (!hasError) {
                    isSuccess = true;
                    message = "下注成功！";
                } else {
                    isSuccess = false;
                    message = "下注失败：" + batchPostResult;
                }
            }
        } catch (Exception e) {
            isSuccess = false;
            message = "下注失败：" + e.getMessage();
        }
        return new Result(isSuccess, message);
    }

}
