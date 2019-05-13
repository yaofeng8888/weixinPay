package com.yf.weixinpay.util;

public interface PayConfigUtils {
     String APP_ID = "";//微信公众号的ID
     String MCH_ID = "";//商户ID
     String API_KEY = "";//API密钥
     String UFDOOER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//微信的统一下单地址
     String NOTIFY_URL = "http://192.168.1.55:8080/payment/result";//回调地址
     String CREATE_IP = "192.168.1.55";//发起IP
}
