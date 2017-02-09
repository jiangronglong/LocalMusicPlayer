package com.example.localmusicplayer.listBiz;

import com.example.localmusicplayer.classes.Mp3Info;

import java.util.List;

/**
 * Created by jiang rong long on 2017/2/4.
 */

public class ListBiz implements IListBiz {
    @Override
    public void loadingList( final List<Mp3Info> mp3Infos,final OnLoadingListListener loadingListListener) {
        if(mp3Infos!=null){
            loadingListListener.loadingListSuccess(mp3Infos);
        }else{
            loadingListListener.loadingListFailed();
        }

    }

    @Override
    public void longClickListItem(final OnLongClickListItemListener longClickListItemListener) {
        longClickListItemListener.longClickListItemSuccess();

    }

    @Override
    public void clickListItem(final OnClickListItemListener onClickListItemListener) {
        onClickListItemListener.clickListItemSuccess();

    }

    @Override
    public void clickBtn(final OnClickBtnListener clickBtnListener) {
        clickBtnListener.clickBtnSuccess();
    }
}
