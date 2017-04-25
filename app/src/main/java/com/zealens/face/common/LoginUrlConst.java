package com.zealens.face.common;

/**
 * Created on 2017/3/17
 * in BlaBla by Kyle
 */

public class LoginUrlConst extends UrlConst {
    public static final String WECHAT_APP_ID = "wx3c965a3edb8c225c";
    public static final String WECHAT_APP_SECRET = "f06e94f188d77b0defef4f795f31d754";
    public static final String WECHAT_LOGIN_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=wx3c965a3edb8c225c&redirect_uri=http%3A%2F%2Fbiz.zealens.com&response_type=code&scope=snsapi_login";
    public static final String WECHAT_TOKEN_URL_HEAD_STYLE = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
            "appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public static final String APPID_TAG = "APPID";
    public static final String SECRET_TAG = "SECRET";
    public static final String CODE_TAG = "CODE";

    public static final String WECHAT_GET_USER_INFO_STYLE = "https://api.weixin.qq.com/sns/userinfo?" +
            "access_token=ACCESS_TOKEN&openid=OPENID";
    public static final String ACCESS_TOKEN_TAG = "ACCESS_TOKEN";
    public static final String OPENID_TAG = "OPENID";

    public static final String HEAD_ACCESS_TOKEN = ACCESS_TOKEN_TAG.toLowerCase();
    public static final String HEAD_OPENID = OPENID_TAG.toLowerCase();
    public static final String HEAD_CODE = "code";
    public static final String JSON_NICKNAME = "nickname";
    public static final String JSON_HEAD_IMG_URL = "headimgurl";
    public static final String JSON_HEAD_UNIONID = "unionid";
}
