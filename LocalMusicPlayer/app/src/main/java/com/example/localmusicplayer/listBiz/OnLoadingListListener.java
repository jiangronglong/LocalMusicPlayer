package com.example.localmusicplayer.listBiz;

import com.example.localmusicplayer.classes.Mp3Info;

import java.util.List;

/**
 * Created by jiang rong long on 2017/2/4.
 */

public interface OnLoadingListListener {
    void loadingListSuccess(List<Mp3Info> mp3Infos);
    void loadingListFailed();

}
