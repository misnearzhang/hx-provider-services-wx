package com.hx.wx.service;

import com.hx.wx.constant.WxConfig;
import com.hx.wx.utils.HttpUtils;
import com.google.gson.Gson;
import com.hx.wx.utils.XMLUtils;
import io.swagger.annotations.Api;
import org.jdom.JDOMException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import java.util.*;

@RestController
@RequestMapping("/wx/service/")
@Api(value = "微信相关控制器", description = "支付 登录 签名", position = 100, protocols = "http")
public class WxController {




    @RequestMapping(value = "index", method = RequestMethod.GET)
    public Object index(HttpServletResponse response, HttpServletRequest request, String signature, String timestamp, String nonce, String echostr) {
        return echostr;
    }


    /**
     * 收取 微信位置上报信息
     *
     * @param response
     * @param request
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "token", method = {RequestMethod.GET, RequestMethod.POST})
    public Object token(HttpServletResponse response, HttpServletRequest request, String signature, String timestamp, String nonce, String echostr) throws IOException {
        String xml = HttpUtils.inputStream2String(request.getInputStream());
        try {
            if (xml != null && !"".equalsIgnoreCase(xml)) {
                Map map = XMLUtils.doXMLParse(xml);

            }
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return echostr;
    }

    /**
     * 微信调用该地址 拿到code 然后再获取openId
     */
    @RequestMapping(value = "weixinAutoLoginJump", method = RequestMethod.GET)
    public void weixinAutoLoginJump(HttpServletRequest request, HttpServletResponse response) {
        try {
            Gson gson = new Gson();
            String backUrl = request.getParameter("backUrl");
            if (backUrl == null || "".equalsIgnoreCase(backUrl) || "null".equalsIgnoreCase(backUrl)) {
                System.out.println("未加连接");
                backUrl = WxConfig.WX_LOGIN_JUMP_FRONT;
            }
            System.out.println("backUrl:" + backUrl);
            String code = request.getParameter("code");
            StringBuffer tokenUrl = new StringBuffer("https://api.weixin.qq.com/sns/oauth2/access_token");
            tokenUrl.append("?").append("appid=");
            tokenUrl.append(WxConfig.WX_APPID);
            tokenUrl.append("&").append("secret=");
            tokenUrl.append(WxConfig.WX_SECRET);
            tokenUrl.append("&").append("code=").append(code);
            tokenUrl.append("&").append("grant_type=authorization_code");
            String tokenRes = HttpUtils.getRequest(new URL(tokenUrl.toString()));
            //收到openId accesstoken     有效期两个小时
            //将数据 保存到session
            Map resultMap = gson.fromJson(tokenRes, Map.class);
            String openid = (String) resultMap.get("openid");
            String access_token = (String) resultMap.get("access_token");

            request.getSession().setAttribute("accessToken", access_token);
            request.getSession().setAttribute("openId", openid);

            System.out.println("取到 openid" + openid);
            //判断用户是否已经绑定账号

            //response.sendRedirect(/*WxConfig.WX_LOGIN_JUMP_FRONT*/backUrl + "?openId=" + openid + "&loginStatus=false");
            response.getWriter().write(openid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信公众号里面就是配置这个链接  用户点击菜单访问这个跟链接
     * jumpUrl 这个参数是传给微信回调的,回调是将带回一些参数
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "weixinAutoLogin", method = RequestMethod.GET)
    public void weixinAutoLogin(HttpServletRequest request, HttpServletResponse response, String backUrl) {
        try {

            //WxConfig.WX_LOGIN_CALLBACK 该链接为  weixinAutoLoginJump
            String back = getOpenIdUrl(WxConfig.WX_LOGIN_CALLBACK, backUrl);
            //链接重定向去微信服务器
            response.sendRedirect(back);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 组装 获取微信openId的url
     * @param jumpUrl
     * @param backUrl
     * @return
     * @throws Exception
     */
    private String getOpenIdUrl(String jumpUrl, String backUrl) throws Exception {
        StringBuffer url = new StringBuffer("https://open.weixin.qq.com/connect/oauth2/authorize");
        url.append("?").append("appId=");
        url.append(WxConfig.WX_APPID);
        url.append("&").append("redirect_uri=");
        url.append(URLEncoder.encode(jumpUrl + "?backUrl=" + backUrl, "utf-8"));
        url.append("&").append("response_type=code");
        url.append("&").append("scope=snsapi_base");
        url.append("&").append("state=").append(1);
        url.append("wechat_redirect");
        return url.toString();
    }
}
