package com.example.localmusicplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localmusicplayer.classes.AppConstant;
import com.example.localmusicplayer.classes.Mp3Info;
import com.example.localmusicplayer.services.MusicService;
import com.example.localmusicplayer.utils.MediaUtils;

import java.util.List;

/**
 * Created by jiang rong long on 2017/1/16.
 */

public class HomeActivity extends Activity{

    private List<Mp3Info> mp3Infos;
    private Button previousBtn;//上一首按钮
    private Button repeatBtn;//重复按钮
    private Button playBtn;//播放按钮
    private Button rankBtn;//随机按钮
    private Button nextBtn;//下一首按钮

    private TextView music_name;//歌曲名
    private TextView music_artist;

    private String title; // 歌曲标题
    private String artist; // 歌曲艺术家
    private int flag; // 播放标识

    private  int repeatStatue;//重复标识
    private final int singleRepeat=1;//单曲循环
    private final int allRepeat=2;//全部循环
    private final int noneRepeat=3;//不循环
    private int listPosition = 0; // 标识列表位置

    private boolean isFirstTime = true;//
    private boolean isPlaying; // 正在播放
    private boolean isPause; // 暂停
    private boolean isNoneRank = true; // 顺序播放
    private boolean isRank = false; // 随机播放

    private HomeReceiver HomeReceiver;  //自定义的广播接收器

    private int currentTime;
    private int durationTime;

    //服务要发送的一些Action
    public static final String UPDATE_ACTION = "com.example.action.UPDATE_ACTION";  //更新动作
    public static final String CTL_ACTION = "com.example.action.CTL_ACTION";        //控制动作
    public static final String MUSIC_CURRENT = "com.example.action.MUSIC_CURRENT";  //当前音乐播放时间更新动作
    public static final String MUSIC_DURATION = "com.example.action.MUSIC_DURATION";//新音乐长度更新动作
    public static final String REPEAT_ACTION = "com.example.action.REPEAT_ACTION"; // 音乐重复改变动作
    public static final String SHUFFLE_ACTION = "com.example.action.SHUFFLE_ACTION"; // 音乐随机播放动作


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_layout);
        initById();

        setViewOnClickListener();
        mp3Infos = MediaUtils.getMp3Infos(HomeActivity.this); // 获取歌曲对象集合
        registerReceiver();
        getDataFromBundle();
        initView();

    }


    private void initView(){
        isPlaying = true;
        isPause = false;
        music_name.setText(title);
        music_artist.setText(artist);
        Mp3Info mp3Info = mp3Infos.get(listPosition);
        switch (repeatStatue) {
            case singleRepeat: // 单曲循环
                rankBtn.setClickable(false);
                repeatBtn.setBackgroundResource(R.drawable.repeat_current);
                break;
            case allRepeat: // 全部循环
                rankBtn.setClickable(false);
                repeatBtn.setBackgroundResource(R.drawable.repeat_all);
                break;
            case noneRepeat: // 无重复
                rankBtn.setClickable(true);
                repeatBtn.setBackgroundResource(R.drawable.repeat_none);
                break;
        }
        if (isRank) {
            isNoneRank = false;
            rankBtn.setBackgroundResource(R.drawable.rank_music);
            repeatBtn.setClickable(false);
        } else {
            isNoneRank = true;
            rankBtn.setBackgroundResource(R.drawable.rank_music_none);
            repeatBtn.setClickable(true);
        }
        if (flag == AppConstant.PlayerMsg.PLAYING_MSG) { // 如果播放信息是正在播放
            Intent intent = new Intent();
            intent.putExtra("listPosition", listPosition);
            sendBroadcast(intent);
        } else if (flag == AppConstant.PlayerMsg.PLAY_MSG) { // 如果是点击列表播放歌曲的话
            playBtn.setBackgroundResource(R.drawable.play_music);
            play();
        } else if (flag == AppConstant.PlayerMsg.CONTINUE_MSG) {
            Intent intent = new Intent(HomeActivity.this, MusicService.class);
            playBtn.setBackgroundResource(R.drawable.play_music);
            intent.setAction("com.example.MUSIC_SERVICE");
            intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);	//继续播放音乐
            intent.setPackage("com.example.localmusicplayer");
            startService(intent);

        }

    }



    private void registerReceiver(){
        HomeReceiver = new HomeReceiver();
        // 创建IntentFilter
        IntentFilter filter = new IntentFilter();
        // 指定BroadcastReceiver监听的Action
        filter.addAction(UPDATE_ACTION);
        filter.addAction(MUSIC_CURRENT);
        filter.addAction(MUSIC_DURATION);
        filter.addAction(REPEAT_ACTION);
        filter.addAction(SHUFFLE_ACTION);
        // 注册BroadcastReceiver
        registerReceiver(HomeReceiver, filter);

    }


    private void initById(){
        previousBtn=(Button)findViewById(R.id.previous_music);
        repeatBtn=(Button)findViewById(R.id.repeat_music);
        playBtn=(Button)findViewById(R.id.play_music);
        rankBtn=(Button)findViewById(R.id.rank_music);
        nextBtn=(Button)findViewById(R.id.next_music);
        music_name=(TextView)findViewById(R.id.home_music_name_text);
        music_artist=(TextView)findViewById(R.id.home_music_artist);

    }

    private void getDataFromBundle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle==null){
            title="一眼万年";
            artist="小江";
            listPosition=0;
            repeatStatue=1;
            isRank=false;
            flag=AppConstant.PlayerMsg.PLAY_MSG;
            currentTime=0;
            durationTime=0;

        }else {
            title = bundle.getString("title");
            artist = bundle.getString("artist");
            listPosition = bundle.getInt("listPosition");
            repeatStatue = bundle.getInt("repeatState");
            isRank = bundle.getBoolean("shuffleState");
            flag = bundle.getInt("MSG");
            currentTime = bundle.getInt("currentTime");
            durationTime = bundle.getInt("duration");
        }
    }

    private void setViewOnClickListener(){
        ViewOnClickListener listener=new ViewOnClickListener();
        previousBtn.setOnClickListener(listener);
        playBtn.setOnClickListener(listener);
        repeatBtn.setOnClickListener(listener);
        rankBtn.setOnClickListener(listener);
        nextBtn.setOnClickListener(listener);

    }

    private class ViewOnClickListener implements View.OnClickListener{
        Intent intent=new Intent();

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.previous_music:
                    playBtn.setBackgroundResource(R.drawable.play_music);
                    isFirstTime=false;
                    isPlaying=true;
                    isPause=false;
                    previous();
                    break;
                case R.id.repeat_music:
                    if (repeatStatue == noneRepeat) {
                        repeat_one();
                        rankBtn.setClickable(false);
                        repeatStatue = singleRepeat;
                    } else if (repeatStatue == singleRepeat) {
                        repeat_all();
                        rankBtn.setClickable(false);
                        repeatStatue = allRepeat;
                    } else if (repeatStatue == allRepeat) {
                        repeat_none();
                        rankBtn.setClickable(true);
                        repeatStatue = singleRepeat;
                    }
                    switch (repeatStatue) {
                        case singleRepeat: // 单曲循环
                            repeatBtn
                                    .setBackgroundResource(R.drawable.repeat_current);
                            break;
                        case allRepeat: // 全部循环
                            repeatBtn
                                    .setBackgroundResource(R.drawable.repeat_all);
                            break;
                        case noneRepeat: // 无重复
                            repeatBtn
                                    .setBackgroundResource(R.drawable.repeat_none);
                            break;
                    }

                    break;
                case R.id.play_music:
                    if (isFirstTime) {
                        play();
                        isFirstTime = false;
                        isPlaying = true;
                        isPause = false;
                    } else {
                        if (isPlaying) {
                            playBtn.setBackgroundResource(R.drawable.stop_music);
                            intent.setAction("com.example.MUSIC_SERVICE");
                            intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
                            intent.setPackage("com.example.localmusicplayer");
                            startService(intent);
                            isPlaying = false;
                            isPause = true;

                        } else if (isPause) {
                            playBtn.setBackgroundResource(R.drawable.play_music);
                            intent.setAction("com.example.MUSIC_SERVICE");
                            intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
                            intent.setPackage("com.example.localmusicplayer");
                            startService(intent);
                            isPause = false;
                            isPlaying = true;
                        }
                    }
                    break;
                case R.id.rank_music:
                    if (isNoneRank) {
                        rankBtn.setBackgroundResource(R.drawable.rank_music);
                        isNoneRank = false;
                        isRank = true;
                        RankMusic();
                        repeatBtn.setClickable(false);
                    } else if (isRank) {
                        rankBtn.setBackgroundResource(R.drawable.rank_music_none);
                        isRank = false;
                        isNoneRank = true;
                        repeatBtn.setClickable(true);
                    }
                    break;
                case R.id.next_music:
                    playBtn.setBackgroundResource(R.drawable.play_music);
                    isFirstTime = false;
                    isPlaying = true;
                    isPause = false;
                    next();
                    break;

            }

        }
    }

    public void next() {
        listPosition=listPosition+1;
        if(listPosition<= mp3Infos.size() - 1){
            Mp3Info mp3Info=mp3Infos.get(listPosition);
            Intent intent = new Intent();
            intent.setAction("com.example.MUSIC_SERVICE");
            intent.putExtra("listPosition", listPosition);
            intent.putExtra("url", mp3Info.getUrl());
            intent.putExtra("MSG", AppConstant.PlayerMsg.PRIVIOUS_MSG);
            intent.setPackage("com.example.localmusicplayer");
            startService(intent);
        }else {
            listPosition = mp3Infos.size() - 1;
            Toast.makeText(HomeActivity.this, "无下一首", Toast.LENGTH_SHORT).show();
        }

    }

    public void RankMusic(){
        Intent intent = new Intent(CTL_ACTION);
        intent.putExtra("control", 4);
        sendBroadcast(intent);
    }

    public void play() {
        playBtn.setBackgroundResource(R.drawable.play_music);
        Mp3Info mp3Info = mp3Infos.get(listPosition);
        music_name.setText(mp3Info.getTitle());
        Intent intent = new Intent();
        intent.setAction("com.example.MUSIC_SERVICE");
        intent.putExtra("listPosition", 0);
        intent.putExtra("url", mp3Info.getUrl());
        intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
        intent.setPackage("com.example.localmusicplayer");
        startService(intent);
    }

    public void repeat_one() {
        Intent intent = new Intent(CTL_ACTION);
        intent.putExtra("control", 1);
        sendBroadcast(intent);
    }
    public void repeat_all() {
        Intent intent = new Intent(CTL_ACTION);
        intent.putExtra("control", 2);
        sendBroadcast(intent);
    }
    public void repeat_none() {
        Intent intent = new Intent(CTL_ACTION);
        intent.putExtra("control", 3);
        sendBroadcast(intent);
    }



    public void previous(){
        listPosition=listPosition-1;
        if(listPosition>=0){
            Mp3Info mp3Info=mp3Infos.get(listPosition);
            Intent intent = new Intent();
            intent.setAction("com.example.MUSIC_SERVICE");
            intent.putExtra("listPosition", listPosition);
            intent.putExtra("url", mp3Info.getUrl());
            intent.putExtra("MSG", AppConstant.PlayerMsg.PRIVIOUS_MSG);
            intent.setPackage("com.example.localmusicplayer");
            startService(intent);
        }else {
            Toast.makeText(HomeActivity.this, "无上一首", Toast.LENGTH_SHORT).show();
        }

    }

    public class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MUSIC_CURRENT)) {
                // currentTime代表当前播放的时间
                currentTime = intent.getIntExtra("currentTime", -1);
            } else if (action.equals(MUSIC_DURATION)) {
                durationTime = intent.getIntExtra("duration", -1);
            } else if (action.equals(UPDATE_ACTION)) {
                // 获取Intent中的current消息，current代表当前正在播放的歌曲
                listPosition = intent.getIntExtra("current", -1);
                if (listPosition >= 0) {
                    music_name.setText(mp3Infos.get(listPosition).getTitle());
                }
            } else if (action.equals(REPEAT_ACTION)) {
                repeatStatue = intent.getIntExtra("repeatState", -1);
                switch (repeatStatue) {
                    case singleRepeat: // 单曲循环
                        repeatBtn.setBackgroundResource(R.drawable.repeat_current);
                        rankBtn.setClickable(false);
                        break;
                    case allRepeat: // 全部循环
                        repeatBtn.setBackgroundResource(R.drawable.repeat_all);
                        rankBtn.setClickable(false);
                        break;
                    case noneRepeat: // 无重复
                        repeatBtn.setBackgroundResource(R.drawable.repeat_none);
                        rankBtn.setClickable(true);
                        break;
                }
            } else if (action.equals(SHUFFLE_ACTION)) {
                isRank = intent.getBooleanExtra("shuffleState", false);
                if (isRank) {
                    isNoneRank = false;
                    rankBtn.setBackgroundResource(R.drawable.rank_music);
                    repeatBtn.setClickable(false);
                } else {
                    isNoneRank = true;
                    rankBtn.setBackgroundResource(R.drawable.rank_music_none);
                    repeatBtn.setClickable(true);
                }
            }
        }

    }


}
