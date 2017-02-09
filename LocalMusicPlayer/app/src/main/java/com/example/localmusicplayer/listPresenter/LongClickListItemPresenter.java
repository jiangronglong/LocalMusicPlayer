package com.example.localmusicplayer.listPresenter;

import com.example.localmusicplayer.listBiz.IListBiz;
import com.example.localmusicplayer.listBiz.ListBiz;
import com.example.localmusicplayer.listBiz.OnLongClickListItemListener;
import com.example.localmusicplayer.listView.ILongClickListItemView;

/**
 * Created by jiang rong long on 2017/2/8.
 */

public class LongClickListItemPresenter {
    private IListBiz listBiz;
    private ILongClickListItemView longClickListItemView;
    public LongClickListItemPresenter(ILongClickListItemView longClickListItemView){
        this.longClickListItemView=longClickListItemView;
        this.listBiz=new ListBiz();
    }
    public void LongClickListItem(){
        listBiz.longClickListItem(new OnLongClickListItemListener() {
            @Override
            public void longClickListItemSuccess() {
                longClickListItemView.longClickListItem();

            }
        });
    }
}
