package com.example.localmusicplayer.listBiz;

import com.example.localmusicplayer.classes.Mp3Info;

import java.util.List;

/**
 * Created by jiang rong long on 2017/2/4.
 */

public interface IListBiz {
    public void loadingList(List<Mp3Info> mp3Infos,OnLoadingListListener loadingListListener);
    public void longClickListItem(OnLongClickListItemListener longClickListItemListener);
    public void clickListItem(OnClickListItemListener onClickListItemListener);
    public void clickBtn(OnClickBtnListener clickBtnListener);
}
