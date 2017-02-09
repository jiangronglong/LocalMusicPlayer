package com.example.localmusicplayer.listView;

import android.view.View;

/**
 * Created by jiang rong long on 2017/2/9.
 */

public interface IClickBtnView {
     void toPrevious();
     void toRepeat_one();
     void toRepeat_all();
     void toRepeat_none();
     void toPlay();
     void toRankMusic();
     void toNext();

}
