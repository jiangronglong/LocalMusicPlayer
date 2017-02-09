package com.example.localmusicplayer.homeBiz;

/**
 * Created by jiang rong long on 2017/2/9.
 */

public class HomeBiz implements IHomeBiz {
    @Override
    public void clickBtn( final OnClickBtnListener clickBtnListener) {
        clickBtnListener.clickBtnSuccess();
    }
}
