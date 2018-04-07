package com.hx.wx.constant;


import com.hx.wx.utils.PropertyUtil;

public class WxConfig {

    public static final String WX_APPID= PropertyUtil.getString("weixin-oauth_app_id");
    public static final String WX_SECRET= PropertyUtil.getString("weixin-oauth_secret");


    public static final String WX_LOGIN_CALLBACK = PropertyUtil.getString("weixin-login-callback");
    public static final String WX_LOGIN_JUMP_FRONT=PropertyUtil.getString("weixin-login-jump-front");
}
