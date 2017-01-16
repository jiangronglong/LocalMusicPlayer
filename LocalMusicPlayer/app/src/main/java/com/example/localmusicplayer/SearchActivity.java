package com.example.localmusicplayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by jiang rong long on 2017/1/16.
 */

public class SearchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_layout);

    }
}
