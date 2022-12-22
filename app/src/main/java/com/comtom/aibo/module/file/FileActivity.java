package com.comtom.aibo.module.file;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comtom.aibo.R;
import com.comtom.aibo.data.SongData;
import com.comtom.aibo.entity.FileBean;
import com.comtom.aibo.entity.HandleBean;
import com.comtom.aibo.httpset.HttpApi;
import com.comtom.aibo.module.base.BaseActivity;
import com.comtom.aibo.utils.FTPToolkit;
import com.haozi.dev.smartframe.rxhttp.core.RxHttp;
import com.haozi.dev.smartframe.rxhttp.request.RxRequest;
import com.haozi.dev.smartframe.rxhttp.request.base.BaseBean;
import com.haozi.dev.smartframe.utils.adapt.CommonAdapter;
import com.haozi.dev.smartframe.utils.adapt.base.ViewHolder;
import com.haozi.dev.smartframe.utils.tool.UtilDip;
import com.haozi.dev.smartframe.utils.tool.UtilLog;
import com.haozi.dev.smartframe.utils.tool.UtilToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yhq.dialog.core.DialogBuilder;
import cn.yhq.dialog.core.IDialog;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class FileActivity extends BaseActivity {
    private List<SongData> songList;
    private RecyclerView file_recyclerView;
    private TextView select_dev_text;
    private CheckBox select_dev_check ;
    private CommonAdapter commonAdapter;
    private CommonAdapter listAdapter;
    private IDialog iDialog;
    private IDialog dialogBuilder;
    private List<SongData> list;
    private int index = 0 ;//上传下标

    @Override
    public int getLayoutId() {
        return R.layout.activity_file;
    }

    @Override
    public void initView() {
        setTitle("文件上传");
        setMenu("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                index = 0 ;
                for (int i = 0; i < songList.size(); i++) {
                    if (songList.get(i).isFlag()) {
                        list.add(songList.get(i));
                    }
                }

                if (list.size() > 0) {
                    listAdapter.setDatas(list);
                    dialogBuilder.show();
                    uploadFile();
                } else {
                    UtilToast.showToast(activity, "请先选择要上传的文件");
                }
            }
        });
        songList = new ArrayList<>();
        list = new ArrayList<>();
        iDialog = DialogBuilder.loadingDialog0(activity).create();
        select_dev_text = findViewById(R.id.select_dev_text);
        select_dev_check = findViewById(R.id.select_dev_check);
        file_recyclerView = findViewById(R.id.file_recyclerView);
        file_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        file_recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = UtilDip.getPixel(activity, R.dimen.base_dimen_2);
            }
        });

        commonAdapter = new CommonAdapter<SongData>(activity, R.layout.file_music_item, songList) {
            @Override
            protected void convert(ViewHolder holder, final SongData entity, final int position) {
                holder.setText(R.id.file_item_name, entity.getName());
                CheckBox file_item_check = holder.getView(R.id.file_item_check);
                file_item_check.setChecked(entity.isFlag());
                file_item_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        songList.get(position).setFlag(isChecked);
                        loadCheck();
                    }
                });
            }
        };
        file_recyclerView.setAdapter(commonAdapter);
        getData();
        initDialog();
        findViewById(R.id.select_dev_line).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                if (songList.size() > 0) {
                    for (int i = 0; i < songList.size(); i++) {
                        if(!songList.get(i).isFlag()){
                            flag = false;
                            break;
                        }
                    }

                    if(flag){
                        for (int i = 0; i < songList.size(); i++) {
                            songList.get(i).setFlag(false);
                        }
                    }else{
                        for (int i = 0; i < songList.size(); i++) {
                            songList.get(i).setFlag(true);
                        }
                    }
                    commonAdapter.setDatas(songList);
                    loadCheck();
                }
            }
        });
    }

    private void initDialog(){
        View customView = View.inflate(activity,R.layout.file_upload_item, null);
        RecyclerView upload_recyclerView = customView.findViewById(R.id.upload_recyclerView);
        upload_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        upload_recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = UtilDip.getPixel(activity, R.dimen.base_dimen_2);
            }
        });

        listAdapter = new CommonAdapter<SongData>(activity, R.layout.upload_music_item, list) {
            @Override
            protected void convert(ViewHolder holder, final SongData entity, final int position) {
                holder.setText(R.id.file_item_name, entity.getName());
                if( position < index){
                    holder.setTextColor(R.id.text_status, Color.parseColor("#2aa515"));
                    holder.setText(R.id.text_status,"已完成");
                }else if(index == position){
                    holder.setTextColor(R.id.text_status, Color.parseColor("#D7674E"));
                    holder.setText(R.id.text_status,"上传中...");
                }else{
                    holder.setTextColor(R.id.text_status, Color.parseColor("#bfbfbf"));
                    holder.setText(R.id.text_status,"待上传");
                }
            }
        };
        upload_recyclerView.setAdapter(listAdapter);
        DialogBuilder build = DialogBuilder.otherDialog(activity).setContentView(customView);
        build.setTitle("正在上传...");
        dialogBuilder = build.create();
        dialogBuilder.getInnerDialog().setCanceledOnTouchOutside(false);
    }

    private void getData() {
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                SongData song = new SongData();
                song.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                song.id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                song.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                song.albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                if(song.name.contains(".mp3")){
                    songList.add(song);
                }
            }
            commonAdapter.setDatas(songList);
        }
    }

    private void uploadFile() {
        if(index < list.size()){
            uploadOne(list.get(index));
        }else {
            dialogBuilder.dismiss();
            UtilToast.showToast(activity,"上传成功");
        }
    }

    private void uploadOne(SongData songData) {
        Map<String, Object> map = new HashMap<>();
        map.put("Type", 1);
//        iDialog.show();
        mRxLife.add(RxHttp.request(HttpApi.api().FileUpload(map))
                .request(new RxRequest.ResultCallback<FileBean>() {
                    @Override
                    public void onSuccess(String code, FileBean data) {
                        uploadTask(songData, data);
                    }

                    @Override
                    public void onFailed(String code, String msg) {
                        HttpApi.failDeal(code, msg, activity);
                    }
                }));
    }

    /**
     * 上传方法
     *
     * @param songData
     * @param data
     */
    private void uploadTask(SongData songData, FileBean data) {
        String url = data.getFtpUrl();
        String[] str = url.split("//");
        String[] details = str[1].split(":");
        String host = details[0];
        int port = Integer.parseInt(details[1]);
        UtilLog.showNet("host==" + host + "===port==" + port + "===path==="
                + songData.path + "===remote==" + str[2]);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FTPClient client = FTPToolkit.makeFtpConnection(host, port, data.getFtpUsr(), data.getFtpPwd());
                    FTPToolkit.upload(client, songData.path, str[2], new FTPDataTransferListener() {

                        @Override
                        public void started() {
//                            iDialog.show();
                        }

                        @Override
                        public void transferred(int i) {

                        }

                        @Override
                        public void completed() {
//                            iDialog.dismiss();
                            Map<String, Object> map = new HashMap<>();
                            map.put("ParentId", 0);
                            map.put("Type", 1);
                            map.put("Name", songData.getName());
                            map.put("FileId", data.getFileId());
                            mRxLife.add(RxHttp.request(HttpApi.api().MLCreateNode(map))
                                    .request(new RxRequest.ResultCallback<HandleBean>() {
                                        @Override
                                        public void onSuccess(String code, HandleBean data) {
                                            if(data.getHandle() != 0){
                                                workHandle(data.getHandle());
                                            }else{
                                                if(index < list.size()){
                                                    index++;
                                                    uploadFile();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailed(String code, String msg) {
                                            HttpApi.failDeal(code, msg, activity);
                                        }
                                    }));
                        }

                        @Override
                        public void aborted() {

                        }

                        @Override
                        public void failed() {
                            Message message = new Message();
                            message.what = 1002;
                            handler.sendMessage(message);
                        }
                    });
                } catch (Exception e) {
                    UtilLog.showNet("e==" + e.getMessage());
                    Message message = new Message();
                    message.what = 1001;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
//            iDialog.dismiss();
            if (msg.what == 1001) {
                dialogBuilder.dismiss();
                UtilToast.showToast(activity, "上传失败,请重试");
            } else if (msg.what == 1002) {
                dialogBuilder.dismiss();
                UtilToast.showToast(activity, "连接上传服务器失败");
            }
        }
    };


   private void loadCheck(){
       boolean flag = true;
       if (songList.size() > 0) {
           for (int i = 0; i < songList.size(); i++) {
               if(!songList.get(i).isFlag()){
                   flag = false;
                   break;
               }
           }
           if (flag) {
               select_dev_text.setText("取消全选");
               select_dev_check.setChecked(true);
           } else {
               select_dev_text.setText("全选");
               select_dev_check.setChecked(false);
           }
       }
   }

    /**
     * 获取媒体库节点的进度
     */
   private void workHandle(int progress){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<>();
                map.put("Handle", progress);
                mRxLife.add(RxHttp.request(HttpApi.api().MLCreateNodeProcess(map))
                        .request(new RxRequest.ResultCallback<HandleBean>() {
                            @Override
                            public void onSuccess(String code, HandleBean data) {
                                if(data.getStatus() == 1){
                                    index++;
                                    uploadFile();
                                }else{
                                    workHandle(progress);
                                }
                            }

                            @Override
                            public void onFailed(String code, String msg) {
                                HttpApi.failDeal(code, msg, activity);
                            }
                        }));
            }
        },1000);

   }


}