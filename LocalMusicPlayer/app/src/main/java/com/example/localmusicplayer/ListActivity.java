package com.example.localmusicplayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.example.localmusicplayer.adapter.MusicListAdapter;
import com.example.localmusicplayer.classes.Mp3Info;
import com.example.localmusicplayer.utils.MediaUtils;

import java.util.List;

/**
 * Created by jiang rong long on 2017/1/16.
 */

public class ListActivity  extends Activity{
    private ListView mMusiclist; // 音乐列表
    private List<Mp3Info> mp3Infos = null;
    MusicListAdapter listAdapter; // 改为自定义列表适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_layout);
        mMusiclist = (ListView) findViewById(R.id.music_list);
        mp3Infos = MediaUtils.getMp3Infos(ListActivity.this); // 获取歌曲对象集合
        listAdapter = new MusicListAdapter(this, mp3Infos);
        mMusiclist.setAdapter(listAdapter);
    }
}
