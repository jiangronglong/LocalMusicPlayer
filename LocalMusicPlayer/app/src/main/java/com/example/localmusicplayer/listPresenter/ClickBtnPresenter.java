package com.example.localmusicplayer.listPresenter;

import com.example.localmusicplayer.listBiz.IListBiz;
import com.example.localmusicplayer.listBiz.ListBiz;
import com.example.localmusicplayer.listBiz.OnClickBtnListener;
import com.example.localmusicplayer.listView.IClickBtnView;

/**
 * Created by jiang rong long on 2017/2/9.
 */

public class ClickBtnPresenter {
    private IListBiz listBiz;
    private IClickBtnView clickBtnView;
    public ClickBtnPresenter(IClickBtnView clickBtnView){
        this.clickBtnView=clickBtnView;
        this.listBiz=new ListBiz();
    }
    public void clickPrevious(){
        listBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toPrevious();
            }
        });
    }
    public void clickRepeat_one(){
        listBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toRepeat_one();
            }
        });
    }
    public void clickRepeat_all(){
        listBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toRepeat_all();
            }
        });
    }
    public void clickRepeat_none(){
        listBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toRepeat_none();
            }
        });
    }
    public void clickPlay(){
        listBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toPlay();
            }
        });
    }
    public void clickRankMusic(){
        listBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toRankMusic();
            }
        });
    }
    public void clickNext(){
        listBiz.clickBtn(new OnClickBtnListener() {
            @Override
            public void clickBtnSuccess() {
                clickBtnView.toNext();
            }
        });
    }
}
