package cn.dsttl3.dome.weibologin;

import cn.dsttl3.dome.weibologin.slice.MyAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MyAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MyAbilitySlice.class.getName());
    }
}
