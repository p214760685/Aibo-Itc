package com.comtom.aibo.module.shout;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comtom.aibo.R;
import com.comtom.aibo.app.AirApp;
import com.comtom.aibo.data.BoardFileDb;
import com.comtom.aibo.data.TermDb;
import com.comtom.aibo.entity.RealPlay;
import com.comtom.aibo.entity.TermsDetails;
import com.comtom.aibo.greendao.gen.DaoSession;
import com.comtom.aibo.greendao.gen.TermDbDao;
import com.comtom.aibo.httpset.HttpApi;
import com.comtom.aibo.httpset.HttpListen;
import com.comtom.aibo.module.base.BaseActivity;
import com.comtom.aibo.utils.PublicUtil;
import com.comtom.aibo.utils.StatusBarColorCompat;
import com.comtom.aibo.utils.UIUtil;
import com.comtom.commonlib.lame.LameUtil;
import com.haozi.dev.smartframe.rxhttp.core.RxHttp;
import com.haozi.dev.smartframe.rxhttp.request.RxRequest;
import com.haozi.dev.smartframe.rxhttp.request.base.BaseBean;
import com.haozi.dev.smartframe.utils.adapt.CommonAdapter;
import com.haozi.dev.smartframe.utils.adapt.base.ViewHolder;
import com.haozi.dev.smartframe.utils.permissions.OnPermissionCallback;
import com.haozi.dev.smartframe.utils.permissions.Permission;
import com.haozi.dev.smartframe.utils.permissions.XXPermissions;
import com.haozi.dev.smartframe.utils.tool.ShareDate;
import com.haozi.dev.smartframe.utils.tool.UtilDip;
import com.haozi.dev.smartframe.utils.tool.UtilLog;
import com.haozi.dev.smartframe.utils.tool.UtilToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yhq.dialog.core.DialogBuilder;

public class ShoutActivity extends BaseActivity implements View.OnClickListener {
    private TextView shout_shout_text, shout_tim_text, shout_play_dec, shout_seek_text, shout_menu_save;
    private View shout_shout_view, shout_tim_view;
    private ImageView shout_play_btn;
    private SeekBar shout_seek_bar;
    private List<TermsDetails> termsDetails;//终端详情
    private RecyclerView recyclerView;
    private int[] Tids;
    private RealPlay realPlay;//对讲会话数据
    private int volum = 20;//默认音量
    private String name = "";


    //录音推流
    int mSessionId = -1; // 会话ID
    int mSeriaNo = 0; // 帧序号

    final int frequency = 44100;
    final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    int recBufSize;
    AudioRecord audioRecord;

    LameUtil mLameUtil;
    boolean mbToMp3Format = true;// 转换成MP3编码格式

    String strServerIp;
    int mUdpPort = 15001;

    RecordPlayThread mRecordPlayThread;
    boolean isRecording = false;

    boolean mSaveFile = false;// 保存录音文件,测试用
    String filePath, filePath1;
    FileOutputStream fos, fos1;

    private DaoSession daoSession;
    private TermDbDao termDb ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shout;
    }

    @Override
    public void initView() {
        StatusBarColorCompat.setStatusBarColor(activity, Color.parseColor("#F8FFFF"));
        termsDetails = (List<TermsDetails>) getIntent().getSerializableExtra("list");
        volum = getIntent().getIntExtra("volume",20);
        name = getIntent().getStringExtra("name");
        daoSession = AirApp.instance().getDaoSession();
        termDb = daoSession.getTermDbDao();
        Tids = new int[termsDetails.size()];
        for (int i = 0; i < termsDetails.size(); i++) {
            Tids[i] = termsDetails.get(i).getID();
        }
        shout_shout_text = findViewById(R.id.shout_shout_text);
        shout_tim_text = findViewById(R.id.shout_tim_text);
        shout_shout_view = findViewById(R.id.shout_shout_view);
        shout_tim_view = findViewById(R.id.shout_tim_view);
        recyclerView = findViewById(R.id.recyclerView);
        shout_play_btn = findViewById(R.id.shout_play_btn);
        shout_play_dec = findViewById(R.id.shout_play_dec);
        shout_seek_bar = findViewById(R.id.shout_seek_bar);
        shout_seek_text = findViewById(R.id.shout_seek_text);
        shout_menu_save = findViewById(R.id.shout_menu_save);
        Drawable drawable = PublicUtil.getNewDrawable(activity, R.mipmap.icon_seek_btn, getResources().getDimensionPixelOffset(R.dimen.base_dimen_160), getResources().getDimensionPixelOffset(R.dimen.base_dimen_120));
        shout_seek_bar.setThumb(drawable);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = UtilDip.getPixel(activity, R.dimen.base_dimen_2);
            }
        });
        recyclerView.setAdapter(new CommonAdapter<TermsDetails>(activity, R.layout.refresh_item, termsDetails) {
            @Override
            protected void convert(ViewHolder holder, TermsDetails entity, final int position) {
                holder.setVisible(R.id.select_item_status, true);
                holder.setVisible(R.id.select_item_round, false);
                holder.setVisible(R.id.select_item_check, false);
                holder.setText(R.id.select_item_name, entity.getName());
                if (entity.getStatus() == -1) {
                    holder.setText(R.id.select_item_status, "离线");
                    holder.setBackgroundRes(R.id.select_item_status, R.drawable.bg_select_offline);
                } else if (entity.getStatus() == 0) {
                    holder.setText(R.id.select_item_status, "空闲");
                    holder.setBackgroundRes(R.id.select_item_status, R.drawable.bg_select_free);
                } else if (entity.getStatus() > 0) {
                    holder.setText(R.id.select_item_status, "工作中");
                    holder.setBackgroundRes(R.id.select_item_status, R.drawable.bg_select_work);
                }
            }
        });
        shout_seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                volum = seekBar.getProgress();
                shout_seek_text.setText(volum + "");
                setVolue(volum);
            }
        });
        shout_menu_save.setOnClickListener(this);
        shout_seek_text.setText(volum + "");
        shout_seek_bar.setProgress(volum);
        findViewById(R.id.shout_line_shout).setOnClickListener(this);
        findViewById(R.id.shout_line_tim).setOnClickListener(this);
        findViewById(R.id.shout_close).setOnClickListener(this);
        findViewById(R.id.shout_play_btn).setOnClickListener(this);
    }

    /**
     * 设置终端音量
     */
    private void setVolue(int volue) {
        if (realPlay != null && realPlay.getSid() != -1) {
            Map<String, Object> map = new HashMap<>();
            map.put("Sid", realPlay.getSid());
            map.put("Vol", volue);
            mRxLife.add(RxHttp.request(HttpApi.api().SessionVolSet(map))
                    .request(new RxRequest.ResultCallback<BaseBean>() {
                        @Override
                        public void onSuccess(String code, BaseBean data) {
                        }

                        @Override
                        public void onFailed(String code, String msg) {
                            HttpApi.failDeal(code, msg, activity);
                        }
                    }));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shout_line_shout://喊话
                shout_menu_save.setVisibility(View.GONE);
                findViewById(R.id.shout_frag_shout).setVisibility(View.VISIBLE);
                findViewById(R.id.shout_frag_tim).setVisibility(View.GONE);
                shout_shout_text.setTextColor(Color.parseColor("#255B7D"));
                shout_tim_text.setTextColor(Color.parseColor("#666666"));
                shout_shout_view.setBackgroundResource(R.drawable.bg_shout_line_on);
                shout_tim_view.setBackgroundResource(R.drawable.bg_shout_line_un);
                break;
            case R.id.shout_line_tim://终端
                if(name != null && !name.equals("")){
                    shout_menu_save.setVisibility(View.GONE);
                }else{
                    shout_menu_save.setVisibility(View.VISIBLE);
                }
                findViewById(R.id.shout_frag_shout).setVisibility(View.GONE);
                findViewById(R.id.shout_frag_tim).setVisibility(View.VISIBLE);
                shout_shout_text.setTextColor(Color.parseColor("#666666"));
                shout_tim_text.setTextColor(Color.parseColor("#255B7D"));
                shout_shout_view.setBackgroundResource(R.drawable.bg_shout_line_un);
                shout_tim_view.setBackgroundResource(R.drawable.bg_shout_line_on);
                break;
            case R.id.shout_close://关闭
                onBackPressed();
                break;
            case R.id.shout_menu_save://保存记录
                DialogBuilder.editTextDialog(activity)
                        .setTitle("填写记录名称")
                        .setOnEditTextDialogListener(new DialogBuilder.OnEditTextDialogListener() {
                            @Override
                            public void onEditTextCreated(EditText editText) {
                                editText.setHint("请输入记录名称");
                            }

                            @Override
                            public boolean onEditTextSelected(EditText editText, String text) {
                                if (text != null && text.length() > 0) {
                                    List<TermDb> termList = termDb.queryRaw("where NAME=?", text);
                                    if (termList != null && termList.size() > 0) {
                                        UtilToast.showToast(activity, "此纪录名称已存在，请更换");
                                    } else {
                                        TermDb term = new TermDb(null, text, volum , termsDetails);
                                        termDb.insert(term);
                                        name = text;
                                        shout_menu_save.setVisibility(View.GONE);
                                        UtilToast.showToast(activity, "保存成功");
                                    }
                                } else {
                                    UtilToast.showToast(activity, "请输入记录名称");
                                }
                                return false;
                            }
                        }).show();
                break;
            case R.id.shout_play_btn://启动、停用喊话
                if (!isRecording) {
                    XXPermissions.with(activity)
                            .permission(new String[]{Permission.RECORD_AUDIO})
                            .request(new OnPermissionCallback() {
                                @Override
                                public void onGranted(List<String> permissions, boolean all) {
                                    if (all) {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("Tids", Tids);
                                        map.put("Time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                        map.put("PlayPrior", ShareDate.getShareInt(activity,"shoutPrior",100));
                                        map.put("AudioFormat", 2);
                                        map.put("TNumber", 2);
                                        mRxLife.add(RxHttp.request(HttpApi.api().RealPlayStart(map))
                                                .listener(new HttpListen(activity))
                                                .request(new RxRequest.ResultCallback<RealPlay>() {
                                                    @Override
                                                    public void onSuccess(String code, RealPlay data) {
                                                        realPlay = data;
                                                        if (realPlay.getSid() == -1) {
                                                            UtilToast.showToast(activity, "创建会话失败");
                                                        } else {
                                                            mSessionId = realPlay.getSid();
                                                            strServerIp = realPlay.getDataIP();
//                                                            mUdpPort = realPlay.getDataPort();
                                                            setVolue(volum);
                                                            startRecordServer();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailed(String code, String msg) {
                                                        HttpApi.failDeal(code, msg, activity);
                                                    }
                                                }));
                                    } else {
                                        Toast.makeText(activity, "请先申请录音权限", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }

                                @Override
                                public void onDenied(List<String> permissions, boolean never) {
                                    if (never) {
                                        Toast.makeText(activity, "权限被禁止，已为您打开系统设置", Toast.LENGTH_SHORT).show();
                                        XXPermissions.startPermissionActivity(activity, permissions);
                                    } else {
                                        Toast.makeText(activity, "获取权限失败", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                } else {//停止
                    stopRecordServer();
                }
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if(volum > 0){
                volum = volum -1 ;
                shout_seek_text.setText(volum + "");
                setVolue(volum);
            }else{
                volum = 0 ;
                UtilToast.showToast(activity,"已经是最低音量");
            }
            shout_seek_bar.setProgress(volum);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if(volum < 56 ){
                volum = volum  + 1 ;
                shout_seek_text.setText(volum + "");
                setVolue(volum);
            }else{
                volum = 56 ;
                UtilToast.showToast(activity,"已经是最大音量");
            }
            shout_seek_bar.setProgress(volum);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    // 下面是话筒通话部分
    void initRecord() {
        if (null == mLameUtil) {
            mLameUtil = new LameUtil();
        }
        recBufSize = AudioRecord.getMinBufferSize(frequency,
                channelConfiguration, audioEncoding);
        if (mbToMp3Format) {
            int n1 = recBufSize % mMp3LenPcm;
            int n2 = recBufSize / mMp3LenPcm;
            if (0 != n1) {
                recBufSize = (n2 + 1) * mMp3LenPcm;
            }
        }

        // 实例化AudioRecord(声音来源，采样率，声道设置，采样声音编码，缓存大小）
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
                channelConfiguration, audioEncoding, recBufSize);

    }

    void startRecordServer() {
        isRecording = true;
        shout_play_dec.setText("点击停止喊话");
        shout_play_btn.setImageResource(R.mipmap.icon_shout_btn_stop);
        mRecordPlayThread = new RecordPlayThread();
        mRecordPlayThread.start();
    }

    void stopRecordServer() {
        Map<String, Object> map = new HashMap<>();
        map.put("Sid", realPlay.getSid());
        mRxLife.add(RxHttp.request(HttpApi.api().RealPlayStop(map))
                .listener(new HttpListen(activity))
                .request(new RxRequest.ResultCallback<BaseBean>() {
                    @Override
                    public void onSuccess(String code, BaseBean data) {
                        isRecording = false;
                        realPlay = null;
                        shout_play_dec.setText("点击开始喊话");
                        shout_play_btn.setImageResource(R.mipmap.icon_shout_btn_play);
                        try {
                            if (null != mRecordPlayThread) {
                                mRecordPlayThread.join(1000);
                                mRecordPlayThread = null;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(String code, String msg) {
                        HttpApi.failDeal(code, msg, activity);
                    }
                }));
    }

    int mMp3LenPcm = (94 * 2);

    class RecordPlayThread extends Thread {
        @Override
        public void run() {
            try {// 提升线程的等级
                android.os.Process
                        .setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                initRecord();

                if (mSaveFile) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        String fileDir = Environment.getExternalStorageDirectory().getPath()
                                + "/12";
                        File file = new File(fileDir);
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        filePath = fileDir + "/" + "record.pcm";
                        filePath1 = fileDir + "/" + "record.mp3";
                    }
                    fos = new FileOutputStream(filePath);
                    fos1 = new FileOutputStream(filePath1);
                }

                boolean bIsMono = true;
                int MP3_SIZE = 8192;
                byte[] mp3_buffer = new byte[MP3_SIZE];
                int write;
                if (mbToMp3Format) {
                    mLameUtil.initlame(bIsMono);
                }

                int nmp3bufflen = mMp3LenPcm;// 2304;
                // byte 文件来存储声音
                byte[] buffer = new byte[nmp3bufflen];// new byte[recBufSize];

                // 开始采集声音
                audioRecord.startRecording();

                mSeriaNo = 1;

                int pcmsize = recBufSize;
//                UtilToast.showToast(activity,"话筒成功开启,可以讲话");

                while (isRecording) {
                    int bufferReadResult = 0;

                    if (mbToMp3Format) {
                        bufferReadResult = audioRecord.read(buffer, 0,
                                nmp3bufflen);// 94 * 2);
                    } else {
                        bufferReadResult = audioRecord.read(buffer, 0, pcmsize);
                    }
                    if (mbToMp3Format) {
                        write = mLameUtil
                                .encodebuffer(buffer, bufferReadResult,
                                        mp3_buffer, MP3_SIZE, bIsMono);

                        FullDataSendto(mSessionId, mSeriaNo, mp3_buffer, write);

                        if (mSaveFile) {
                            try {
                                if (fos != null) {
                                    fos.write(buffer, 0, bufferReadResult);
                                }
                                if (fos1 != null) {
                                    fos1.write(mp3_buffer, 0, write);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        FullDataSendto(mSessionId, mSeriaNo, buffer,
                                bufferReadResult);
                    }
                }

                audioRecord.stop();
                audioRecord.release();

                if (mbToMp3Format) {
                    write = mLameUtil.encodeflush(mp3_buffer, MP3_SIZE);
                    FullDataSendto(mSessionId, mSeriaNo++, mp3_buffer, write);
                    mLameUtil.closelame();
                }
            } catch (Exception e) {
                e.printStackTrace();
                UtilLog.showNet("error===" + e.getMessage());
                try {
                    audioRecord.stop();
                    audioRecord.release();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                mRecordPlayThread = null;
                isRecording = false;

            }

//            Resources rs = getActivity().getResources();待开启
//            String str = rs.getString(R.string.wait_start_speak_str);
//            sendDisplayInfo(str, 3);

            if (mSaveFile && fos != null && fos1 != null) {
                try {
                    fos.close();
                    fos1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ;

    boolean bUdpFull = true; // 是不是包括 会话ID和帧序

    void FullDataSendto(int nSessiontId, int nSeariaNo, byte[] mp3_buffer,
                        int mp3bufferlen) {

        if (mp3bufferlen <= 0) {
            return;
        }

        byte[] byteSessiontId = UIUtil.toHH(nSessiontId);
        byte[] byteSeriaNo = UIUtil.toHH(nSeariaNo);

        int nFullDataLen = byteSessiontId.length + byteSeriaNo.length
                + mp3bufferlen;

        byte[] newFullData = new byte[nFullDataLen];

        int ndstPos = 0;
        // 会话ID
        System.arraycopy(byteSessiontId, 0, newFullData, ndstPos,
                byteSessiontId.length);
        ndstPos += byteSessiontId.length;
        // 帧序号
        System.arraycopy(byteSeriaNo, 0, newFullData, ndstPos,
                byteSeriaNo.length);
        ndstPos += byteSeriaNo.length;
        // 语音数据
        System.arraycopy(mp3_buffer, 0, newFullData, ndstPos, mp3bufferlen);

        if (bUdpFull) {
            UIUtil.Udp_sendto(newFullData, nFullDataLen, strServerIp, mUdpPort);
        } else {
            UIUtil.Udp_sendto(mp3_buffer, mp3bufferlen, strServerIp, mUdpPort);
        }
    }

//    @Override
//    public boolean clearAll() {
//        boolean bresult = true;
//        if (null != mRecordPlayThread) {
//            bresult = false;
//        }
//        StopAllServer();
//        return bresult;
//    }


    @Override
    public void onBackPressed() {
            if(name != null && !name.equals("")){
                List<TermDb> termList = termDb.queryRaw("where NAME=?", name);
                TermDb termDbEntity = termList.get(0);
                termDbEntity.setVolume(volum);
                termDb.update(termDbEntity);
            }

        if (isRecording) {
            Map<String, Object> map = new HashMap<>();
            map.put("Sid", realPlay.getSid());
            mRxLife.add(RxHttp.request(HttpApi.api().RealPlayStop(map))
                    .listener(new HttpListen(activity))
                    .request(new RxRequest.ResultCallback<BaseBean>() {
                        @Override
                        public void onSuccess(String code, BaseBean data) {
                            isRecording = false;
                            onBackPressed();
                        }

                        @Override
                        public void onFailed(String code, String msg) {
                            HttpApi.failDeal(code, msg, activity);
                        }
                    }));
        } else {
            super.onBackPressed();
        }

    }
}