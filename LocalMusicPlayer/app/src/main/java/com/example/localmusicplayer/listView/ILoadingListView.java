package com.example.localmusicplayer.listView;

import com.example.localmusicplayer.classes.Mp3Info;

import java.util.List;

/**
 * Created by jiang rong long on 2017/2/4.
 */

public interface ILoadingListView {
    public List<Mp3Info> getMusicMessage();
    public void showListView(List<Mp3Info> mp3Infos);
    public void showNoMusic();
}
