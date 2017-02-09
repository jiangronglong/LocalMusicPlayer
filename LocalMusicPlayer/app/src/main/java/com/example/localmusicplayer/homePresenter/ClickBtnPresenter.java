package com.example.localmusicplayer.homePresenter;

import com.example.localmusicplayer.homeBiz.HomeBiz;
import com.example.localmusicplayer.homeBiz.IHomeBiz;
import com.example.localmusicplayer.homeBiz.OnClickBtnListener;
import com.example.localmusicplayer.homeView.IClickBtnView;


/**
 * Created by jiang rong long on 2017/2/9.
 */

public class ClickBtnPresenter {
    private IHomeBiz homeBiz;
    private IClickBtnView clickBtnView;
    public ClickBtnPresenter(IClickBtnView clickBtnView){
        this.clickBtnView=clickBtnView;
        this.homeBiz=new HomeBiz();
    }
    public void clickPrevious(){
        homeBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toPrevious();
            }
        });
    }
    public void clickRepeat_one(){
        homeBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toRepeat_one();
            }
        });
    }
    public void clickRepeat_all(){
        homeBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toRepeat_all();
            }
        });
    }
    public void clickRepeat_none(){
        homeBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toRepeat_none();
            }
        });
    }
    public void clickPlay(){
        homeBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toPlay();
            }
        });
    }
    public void clickRankMusic(){
        homeBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toRankMusic();
            }
        });
    }
    public void clickNext(){
        homeBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toNext();
            }
        });
    }
}
