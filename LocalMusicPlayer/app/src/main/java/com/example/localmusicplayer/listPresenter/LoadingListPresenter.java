package com.example.localmusicplayer.listPresenter;

import com.example.localmusicplayer.classes.Mp3Info;
import com.example.localmusicplayer.listBiz.IListBiz;
import com.example.localmusicplayer.listBiz.ListBiz;
import com.example.localmusicplayer.listBiz.OnLoadingListListener;
import com.example.localmusicplayer.listView.ILoadingListView;

import java.util.List;

/**
 * Created by jiang rong long on 2017/2/8.
 */

public class LoadingListPresenter {
    private IListBiz listBiz;
    private ILoadingListView loadingListView;
    public LoadingListPresenter(ILoadingListView loadingListView){
       this.loadingListView=loadingListView;
        this.listBiz=new ListBiz();
    }
     public void LoadingList(){
         listBiz.loadingList(loadingListView.getMusicMessage(), new OnLoadingListListener() {
             @Override
             public void loadingListSuccess(List<Mp3Info> mp3Infos) {
                 mp3Infos=loadingListView.getMusicMessage();
                 loadingListView.showListView(mp3Infos);
             }

             @Override
             public void loadingListFailed() {
                 loadingListView.showNoMusic();

             }
         });
     }
}
