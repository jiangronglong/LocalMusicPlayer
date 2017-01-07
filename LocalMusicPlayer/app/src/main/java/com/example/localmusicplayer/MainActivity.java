package com.example.localmusicplayer;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.localmusicplayer.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private List<View> views = new ArrayList<>();
    private ViewPager viewPager;
    //底端菜单栏LinearLayout
    private LinearLayout home_linearlayout;
    private LinearLayout song_list_linearlayout;
    private LinearLayout search_linearlayout;
    //底端菜单栏ImageView
    private ImageView home_picture;
    private ImageView song_list_picture;
    private ImageView search_picture;

    private ImageView recent_picture;

    //底端菜单栏TextView
    private TextView home_text;
    private TextView song_list_text;
    private TextView search_text;

    private TextView recent_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //初始化各个控件
        InitView();
        InitData();
    }
    private void InitData(){
        LayoutInflater minflate=LayoutInflater.from(this);
        View mainPage=minflate.inflate(R.layout.home_layout,null);
        View listPage=minflate.inflate(R.layout.list_layout,null);
        View searchPage=minflate.inflate(R.layout.search_layout,null);
        views.add(mainPage);
        views.add(listPage);
        views.add(searchPage);
        MyPagerAdapter adapter=new MyPagerAdapter(views);
        viewPager.setAdapter(adapter);
    }
    private void InitView(){
        viewPager=(ViewPager)findViewById(R.id.viewpager);

        home_linearlayout=(LinearLayout)findViewById(R.id.home);
        song_list_linearlayout=(LinearLayout)findViewById(R.id.song_list);
        search_linearlayout=(LinearLayout)findViewById(R.id.search);

        home_linearlayout.setOnClickListener(this);
        song_list_linearlayout.setOnClickListener(this);
        search_linearlayout.setOnClickListener(this);

        home_picture=(ImageView)findViewById(R.id.home_picture);
        song_list_picture=(ImageView)findViewById(R.id.song_list_picture);
        search_picture=(ImageView)findViewById(R.id.search_picture);

        home_text=(TextView)findViewById(R.id.home_text);
        song_list_text=(TextView)findViewById(R.id.song_list_text);
        search_text=(TextView)findViewById(R.id.search_text);

        home_picture.setSelected(true);
        home_text.setSelected(true);

        recent_picture=home_picture;
        recent_text=home_text;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                changeTab(arg0);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        } );
    }
    private void changeTab(int id){
        recent_picture.setSelected(false);
        recent_text.setSelected(false);
        switch(id){
            case R.id.home:
                viewPager.setCurrentItem(0);
            case 0:
                home_picture.setSelected(true);
                recent_picture=home_picture;
                home_text.setSelected(true);
                recent_text=home_text;
                break;
            case R.id.song_list:
                viewPager.setCurrentItem(1);
            case 1:
               song_list_picture.setSelected(true);
                recent_picture=song_list_picture;
               song_list_text.setSelected(true);
                recent_text=song_list_text;
                break;
            case R.id.search:
                viewPager.setCurrentItem(2);
            case 2:
                search_picture.setSelected(true);
                recent_picture=search_picture;
                search_text.setSelected(true);
                recent_text=search_text;
                break;


        }
    }



    @Override
    public void onClick(View v) {
        changeTab(v.getId());

    }
}
