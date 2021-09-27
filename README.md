# WeiBoLogin
 
> 前期准备
在微博开放平台注册一个网站应用 
[微博开放平台](https://open.weibo.com/connect)

进入正题
创建一个登录页面的Ability
因为使用的是网站接入的方式，所以登录使用WebView来实现
登录页布局文件
```html/xml
<?xml version="1.0" encoding="utf-8"?>
<DirectionalLayout
    xmlns:ohos="http://schemas.huawei.com/res/ohos"
    ohos:height="match_parent"
    ohos:width="match_parent"
    ohos:orientation="vertical">

    <ohos.agp.components.webengine.WebView
        ohos:id="$+id:WebView_weibologin"
        ohos:height="match_parent"
        ohos:width="match_parent"/>

</DirectionalLayout>
```

在登录页AbilitySlice中对WebView进行设置
```java
public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_wei_bo_login);

        WebView myWebView = (WebView) findComponentById(ResourceTable.Id_WebView_weibologin);
        myWebView.getWebConfig().setJavaScriptPermit(true);

	//自定义WebAgent 用于登录的相关操作
        myWebView.setWebAgent(new WebAgent(){

	// isNeedLoadUrl 当WebView即将打开一个链接时会调用此方法 
            @Override
            public boolean isNeedLoadUrl(WebView webView, ResourceRequest request) {
		// request.getRequestUrl().toString().startsWith("sinaweibo") 
		// 当请求链接为sinaweibo开头（点击网页上一键登录会唤起微博客户端）时，使用下面的方法唤起微博客户端。
                if (request.getRequestUrl().toString().startsWith("sinaweibo")){
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setUri(Uri.parse(request.getRequestUrl().toString()));
                    intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION);
                    startAbility(intent);
                    return false;
                }

		// 当在微博客户端授权后，会重定向至定义的网址，示例中重定向至https://api.dsttl3.cn/?code=【code的值】 ，这时候就可以从链接中获取到code进行下一步了。这里把code传入下个页面
                if (request.getRequestUrl().toString().startsWith("https://api.dsttl3.cn)){
                    String code = request.getRequestUrl().toString().substring(28);
                    Intent intent = new Intent();
		//  在intent中带上code
                    intent.setParam("code",code);
                    Operation operation = new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName("cn.dsttl3.dome.weibologin")
                            .withAbilityName("cn.dsttl3.dome.weibologin.MyAbility")
                            .build();
                    intent.setOperation(operation);
                    startAbility(intent);
		// 结束当前Ability
                    terminateAbility();
                }
                return true;
            }
        });
        //授权连接，需要自己修改
        myWebView.load("https://api.weibo.com/oauth2/authorize?client_id=2593566539&response_type=code&forcelogin=false&scope=all&redirect_uri=https%3A%2F%2Fapi.dsttl3.cn");
    }
```

获取到code后，在MyAbility中获取微博token
```java
public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_my);
        Text text = (Text) findComponentById(ResourceTable.Id_text_helloworld);
        String code = intent.getStringParam("code");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String YOUR_CLIENT_ID = "2593566539";
                    String YOUR_CLIENT_SECRET = "383fc6262e954e18f5b7efe3c9899284";
                    String YOUR_REGISTERED_REDIRECT_URI = "https://api.dsttl3.cn";
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
```

获取微博Token完成。

效果演示
![SVID_20210928_022040_1.gif](https://harmonyos.oss-cn-beijing.aliyuncs.com/images/202109/928db7e5236cc247ef4620042c4ee82a74cd8e.gif)


了解更多请下载源代码

[GitHub](https://github.com/dsttl3/WeiBoLogin)   -  [Gitee](https://gitee.com/dsttl3/WeiBoLogin)
