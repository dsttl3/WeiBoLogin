package cn.dsttl3.dome.weibologin.slice;

import cn.dsttl3.dome.weibologin.ResourceTable;
import cn.dsttl3.dome.weibologin.bean.WeiBoTokenJson;
import com.google.gson.Gson;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Text;
import ohos.agp.components.webengine.CookieStore;
import ohos.agp.components.webengine.ResourceRequest;
import ohos.agp.components.webengine.WebAgent;
import ohos.agp.components.webengine.WebView;
import ohos.media.image.PixelMap;
import ohos.utils.net.Uri;
import okhttp3.*;

import java.io.IOException;

public class WeiBoLoginSlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_wei_bo_login);
        Text text = (Text) findComponentById(ResourceTable.Id_Text_weibologin);
        WebView myWebView = (WebView) findComponentById(ResourceTable.Id_WebView_weibologin);
        myWebView.getWebConfig().setJavaScriptPermit(true);
        myWebView.setWebAgent(new WebAgent(){

            @Override
            public boolean isNeedLoadUrl(WebView webView, ResourceRequest request) {

                if (request.getRequestUrl().toString().startsWith("sinaweibo")){
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setUri(Uri.parse(request.getRequestUrl().toString()));
                    intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION);
                    startAbility(intent);
                    return false;
                }
                if (request.getRequestUrl().toString().startsWith("https://api.dsttl3.cn/?code=")){
                    String code = request.getRequestUrl().toString().substring(28);
                    Intent intent = new Intent();
                    intent.setParam("code",code);
                    Operation operation = new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName("cn.dsttl3.dome.weibologin")
                            .withAbilityName("cn.dsttl3.dome.weibologin.MyAbility")
                            .build();
                    intent.setOperation(operation);
                    startAbility(intent);
                    terminateAbility();
                }
                return true;
            }

            @Override
            public void onLoadingPage(WebView webView, String url, PixelMap icon) {
                super.onLoadingPage(webView, url, icon);
                text.setText("正在访问：" + url);
            }

            @Override
            public void onPageLoaded(WebView webView, String url) {
                super.onPageLoaded(webView, url);
                String cookie = CookieStore.getInstance().getCookie(url);
                text.setText(url);
            }
        });
        //授权连接
        myWebView.load("https://api.weibo.com/oauth2/authorize?client_id=2593566539&response_type=code&forcelogin=false&scope=all&redirect_uri=https%3A%2F%2Fapi.dsttl3.cn");
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
