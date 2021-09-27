package cn.dsttl3.dome.weibologin;

import cn.dsttl3.dome.weibologin.slice.WeiBoLoginSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class WeiBoLogin extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(WeiBoLoginSlice.class.getName());
    }
}
