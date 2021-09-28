package cn.dsttl3.dome.weibologin.slice;

import cn.dsttl3.dome.weibologin.ResourceTable;
import cn.dsttl3.dome.weibologin.bean.WeiBoTokenJson;
import com.google.gson.Gson;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;
import okhttp3.*;

import java.io.IOException;

public class MyAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_my);
        Text text = (Text) findComponentById(ResourceTable.Id_text_helloworld);
        String code = intent.getStringParam("code");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 微博应用信息
                    // App Key
                    String YOUR_CLIENT_ID = "2593566539";
                    // App Secret
                    String YOUR_CLIENT_SECRET = "383fc6262e954e18f5b7efe3c9899284";
                    // 授权回调页
                    String YOUR_REGISTERED_REDIRECT_URI = "https://api.dsttl3.cn";
                    // 微博API地址，固定参数，不需要修改
                    String ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
                    OkHttpClient client = new OkHttpClient();
                    FormBody body = new FormBody.Builder()
                            .add("client_id", YOUR_CLIENT_ID)
                            .add("client_secret", YOUR_CLIENT_SECRET)
                            .add("grant_type", "authorization_code")
                            .add("redirect_uri", YOUR_REGISTERED_REDIRECT_URI)
                            .add("code", code).build();
                    Request okRequest = new Request.Builder().url(ACCESS_TOKEN_URL).header("referer",YOUR_REGISTERED_REDIRECT_URI).post(body).build();
                    Call call = client.newCall(okRequest);
                    Response re = call.execute();
                    String s = re.body().string();
                    Gson gson = new Gson();
                    WeiBoTokenJson w = gson.fromJson(s, WeiBoTokenJson.class);
                    getUITaskDispatcher().asyncDispatch(new Runnable() {
                        @Override
                        public void run() {
                            text.setText("登录成功：token=" + w.getAccess_token());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
