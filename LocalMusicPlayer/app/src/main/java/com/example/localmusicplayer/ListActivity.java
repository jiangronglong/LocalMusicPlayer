package com.example.localmusicplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localmusicplayer.adapter.MusicListAdapter;
import com.example.localmusicplayer.classes.AppConstant;
import com.example.localmusicplayer.classes.Mp3Info;
import com.example.localmusicplayer.utils.ConstantUtils;
import com.example.localmusicplayer.utils.CustomDialog;
import com.example.localmusicplayer.utils.GetScreenUtils;
import com.example.localmusicplayer.utils.MediaUtils;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

/**
 * Created by jiang rong long on 2017/1/16.
 */

public class ListActivity  extends Activity{
    private ListView mMusiclist; // 音乐列表
    private List<Mp3Info> mp3Infos = null;
    MusicListAdapter listAdapter; // 自定义列表适配器
    private Button previousBtn;//上一首按钮
    private Button repeatBtn;//重复按钮
    private Button playBtn;//播放按钮
    private Button rankBtn;//随机按钮
    private Button nextBtn;//下一首按钮
    private ImageView music_icon;//歌曲的封面
    private TextView music_name;//歌曲名
    private TextView music_during;//歌曲时长
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

    private ListReceiver listReceiver;  //自定义的广播接收器

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
        setContentView(R.layout.list_layout);
        mMusiclist = (ListView) findViewById(R.id.music_list);
        mp3Infos = MediaUtils.getMp3Infos(ListActivity.this); // 获取歌曲对象集合
        listAdapter = new MusicListAdapter(this, mp3Infos);
        mMusiclist.setAdapter(listAdapter);
        mMusiclist.setOnItemClickListener(new MusicListItemClickListener());

        mMusiclist.setOnCreateContextMenuListener(new MusicListItemContextListener());

        initById();
        setViewOnClickListener();
        repeatStatue = noneRepeat; // 初始状态为无重复播放状态



       registerReceiver();
    }

    private class MusicListItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            listPosition = position;
            playMusic(listPosition);
        }

    }
    private void registerReceiver(){
        listReceiver = new ListReceiver();
        // 创建IntentFilter
        IntentFilter filter = new IntentFilter();
        // 指定BroadcastReceiver监听的Action
        filter.addAction(UPDATE_ACTION);
        filter.addAction(MUSIC_CURRENT);
        filter.addAction(MUSIC_DURATION);
        filter.addAction(REPEAT_ACTION);
        filter.addAction(SHUFFLE_ACTION);
        // 注册BroadcastReceiver
        registerReceiver(listReceiver, filter);

    }



    private void setViewOnClickListener(){
        ViewOnClickListener listener=new ViewOnClickListener();
        previousBtn.setOnClickListener(listener);
        playBtn.setOnClickListener(listener);
        repeatBtn.setOnClickListener(listener);
        rankBtn.setOnClickListener(listener);
        nextBtn.setOnClickListener(listener);

    }


    public class MusicListItemContextListener implements
            View.OnCreateContextMenuListener {

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            musicListItemDialog(); // 长按后弹出的对话框
            final AdapterView.AdapterContextMenuInfo menuInfo2 = (AdapterView.AdapterContextMenuInfo) menuInfo;
            listPosition = menuInfo2.position; // 点击列表的位置
        }

    }


    /**
     * 自定义对话框
     */
    public void musicListItemDialog() {
        String[] menuItems = new String[] {  "查看详情" };
        ListView menuList = new ListView(ListActivity.this);
        menuList.setCacheColorHint(Color.TRANSPARENT);
        menuList.setDividerHeight(1);
        menuList.setAdapter(new ArrayAdapter<String>(ListActivity.this,
                R.layout.context_dialog_layout, R.id.dialogText, menuItems));
        menuList.setLayoutParams(new WindowManager.LayoutParams(GetScreenUtils
                .getScreen(ListActivity.this)[0] / 2, WindowManager.LayoutParams.WRAP_CONTENT));

        final CustomDialog customDialog = new CustomDialog.Builder(
               ListActivity.this).setTitle(R.string.show)
                .setView(menuList).create();
        customDialog.show();

        menuList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                customDialog.cancel();
                customDialog.dismiss();
                if (position == 0) {
                    showMusicInfo(listPosition);
                }
            }

        });
    }

    private void showMusicInfo(int position) {
        Mp3Info mp3Info = mp3Infos.get(position);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.music_info_layout, null);
        ((TextView) view.findViewById(R.id.tv_song_title)).setText(mp3Info
                .getTitle());
        ((TextView) view.findViewById(R.id.tv_song_artist)).setText(mp3Info
                .getArtist());
        ((TextView) view.findViewById(R.id.tv_song_album)).setText(mp3Info
                .getAlbum());
        ((TextView) view.findViewById(R.id.tv_song_filepath)).setText(mp3Info
                .getUrl());
        ((TextView) view.findViewById(R.id.tv_song_duration)).setText(MediaUtils
                .formatTime(mp3Info.getDuration()));
        ((TextView) view.findViewById(R.id.tv_song_format)).setText(ConstantUtils
                .getSuffix(mp3Info.getDisplayName()));
        ((TextView) view.findViewById(R.id.tv_song_size)).setText(ConstantUtils
                .formatByteToMB(mp3Info.getSize()) + "MB");
        new CustomDialog.Builder(ListActivity.this).setTitle("歌曲详细信息:")
                .setNeutralButton("确定", null).setView(view).create().show();
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
            Toast.makeText(ListActivity.this, "无下一首", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ListActivity.this, "无上一首", Toast.LENGTH_SHORT).show();
        }

    }



    private void initById(){
        previousBtn=(Button)findViewById(R.id.previous_music);
        repeatBtn=(Button)findViewById(R.id.repeat_music);
        playBtn=(Button)findViewById(R.id.play_music);
        rankBtn=(Button)findViewById(R.id.rank_music);
        nextBtn=(Button)findViewById(R.id.next_music);
        music_icon=(ImageView)findViewById(R.id.music_icon);
        music_name=(TextView)findViewById(R.id.music_name_text);
        music_during=(TextView)findViewById(R.id.music_during_text);

    }

    public void playMusic(int listPosition) {
        if (mp3Infos != null) {
            Mp3Info mp3Info = mp3Infos.get(listPosition);
           music_name.setText(mp3Info.getTitle()); // 这里显示标题
            Bitmap bitmap = MediaUtils.getArtwork(this, mp3Info.getId(),
                    mp3Info.getAlbumId(), true, true);// 获取专辑位图对象，为小图
            music_icon.setImageBitmap(bitmap); // 这里显示专辑图片
            Intent intent = new Intent(ListActivity.this, HomeActivity.class); // 定义Intent对象，跳转到PlayerActivity
            // 添加一系列要传递的数据
            intent.putExtra("title", mp3Info.getTitle());
            intent.putExtra("url", mp3Info.getUrl());
            intent.putExtra("artist", mp3Info.getArtist());
            intent.putExtra("listPosition", listPosition);
            intent.putExtra("currentTime", currentTime);
            intent.putExtra("repeatState", repeatStatue);
            intent.putExtra("shuffleState", isRank);
            intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
            intent.setPackage("com.example.localmusicplayer");
            startActivity(intent);
        }
    }

    public class ListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MUSIC_CURRENT)) {
                // currentTime代表当前播放的时间
                currentTime = intent.getIntExtra("currentTime", -1);
                music_during.setText(MediaUtils.formatTime(currentTime));
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
