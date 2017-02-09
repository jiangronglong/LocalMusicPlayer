package com.example.localmusicplayer.listPresenter;

import com.example.localmusicplayer.listBiz.IListBiz;
import com.example.localmusicplayer.listBiz.ListBiz;
import com.example.localmusicplayer.listBiz.OnClickListItemListener;
import com.example.localmusicplayer.listView.IClickListItemView;

/**
 * Created by jiang rong long on 2017/2/9.
 */

public class ClickListItemPresenter {
    private IListBiz listBiz;
    private IClickListItemView clickListItemView;
    public ClickListItemPresenter(IClickListItemView clickListItemView){
        this.clickListItemView=clickListItemView;
        this.listBiz=new ListBiz();
    }
    public  void toHomeActivity(){
        listBiz.clickListItem(new OnClickListItemListener() {
            @Override
            public void clickListItemSuccess() {
                clickListItemView.toHomeActivity();
            }
        });
    }
}
