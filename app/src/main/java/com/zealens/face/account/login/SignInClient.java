package com.zealens.face.account.login;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zealens.face.common.LoginUrlConst;
import com.zealens.face.core.CoreContext;
import com.zealens.face.user.UserInfo;
import com.zealens.face.util.HttpUtils;

import java.io.IOException;

/**
 * browser to process web view
 * Created by KyleCe on 2015/12/23.
 */
public class SignInClient extends WebViewClient {
    private VerifyCallback mVerifyCallback;
    private CoreContext mCoreContext;

    public SignInClient(VerifyCallback verifyCallback, CoreContext coreContext) {
        mVerifyCallback = verifyCallback;
        mCoreContext = coreContext;
    }

    /**
     * optimise the load effect
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        view.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        String codeHead = LoginUrlConst.HEAD_CODE;
        if (url.contains(LoginUrlConst.REDIRECT_URL) && url.contains(codeHead)) {
            String code = url.substring(url.lastIndexOf(codeHead) + codeHead.length() + 1, url.length() - 1);
            removeTailRedundancies(code, LoginUrlConst.URL_DIVIDER);

            String accessTokenUrl = LoginUrlConst.WECHAT_TOKEN_URL_HEAD_STYLE
                    .replace(LoginUrlConst.APPID_TAG, LoginUrlConst.WECHAT_APP_ID)
                    .replace(LoginUrlConst.SECRET_TAG, LoginUrlConst.WECHAT_APP_SECRET)
                    .replace(LoginUrlConst.CODE_TAG, code);
            mCoreContext.postTask(() -> {
                try {
                    String response = new HttpUtils().run(accessTokenUrl);

                    Gson gson = new Gson();
                    JsonElement element = gson.fromJson(response, JsonElement.class);
                    JsonObject jsonObj = element.getAsJsonObject();
                    String accessToken = jsonObj.get(LoginUrlConst.HEAD_ACCESS_TOKEN).getAsString();
                    String openId = jsonObj.get(LoginUrlConst.HEAD_OPENID).getAsString();
                    String userInfoUrl = LoginUrlConst.WECHAT_GET_USER_INFO_STYLE
                            .replace(LoginUrlConst.ACCESS_TOKEN_TAG, accessToken)
                            .replace(LoginUrlConst.OPENID_TAG, openId);

                    String userInfoResponse = new HttpUtils().run(userInfoUrl);
                    element = gson.fromJson(userInfoResponse, JsonElement.class);
                    jsonObj = element.getAsJsonObject();
                    String nickname = jsonObj.get(LoginUrlConst.JSON_NICKNAME).getAsString();
                    String headimgurl = jsonObj.get(LoginUrlConst.JSON_HEAD_IMG_URL).getAsString();
                    String unionid = jsonObj.get(LoginUrlConst.JSON_HEAD_UNIONID).getAsString();
                    mVerifyCallback.bingo(new UserInfo(nickname, headimgurl, unionid));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return false;
    }

    private String removeTailRedundancies(String str, String distinguish) {
        String firstHandle = str.substring(0, str.lastIndexOf(distinguish));
        if (firstHandle.contains(distinguish)) {
            firstHandle = removeTailRedundancies(firstHandle, distinguish);
        }
        return firstHandle;
    }
}

