package com.comtom.aibo.module.board;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comtom.aibo.R;
import com.comtom.aibo.app.AirApp;
import com.comtom.aibo.data.BoardFileDb;
import com.comtom.aibo.entity.PlayBean;
import com.comtom.aibo.entity.ProgInfoBean;
import com.comtom.aibo.entity.RealPlay;
import com.comtom.aibo.entity.TermsDetails;
import com.comtom.aibo.greendao.gen.BoardFileDbDao;
import com.comtom.aibo.greendao.gen.DaoSession;
import com.comtom.aibo.httpset.HttpApi;
import com.comtom.aibo.httpset.HttpListen;
import com.comtom.aibo.module.base.BaseActivity;
import com.comtom.aibo.utils.PublicUtil;
import com.comtom.aibo.utils.StatusBarColorCompat;
import com.haozi.dev.smartframe.rxhttp.core.RxHttp;
import com.haozi.dev.smartframe.rxhttp.request.RxRequest;
import com.haozi.dev.smartframe.rxhttp.request.base.BaseBean;
import com.haozi.dev.smartframe.utils.adapt.CommonAdapter;
import com.haozi.dev.smartframe.utils.adapt.base.ViewHolder;
import com.haozi.dev.smartframe.utils.tool.ShareDate;
import com.haozi.dev.smartframe.utils.tool.UtilDip;
import com.haozi.dev.smartframe.utils.tool.UtilLog;
import com.haozi.dev.smartframe.utils.tool.UtilTime;
import com.haozi.dev.smartframe.utils.tool.UtilToast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yhq.dialog.core.DialogBuilder;
import cn.yhq.dialog.core.IDialog;

/**
 * 文件点播
 */
public class PlayActivity extends BaseActivity implements View.OnClickListener {
    private BoardFileDb boardFileDb;
    private TextView play_board_text, play_dev_text, play_music_text, play_max_time, play_type_text, play_text_title;
    private ImageView play_type_img, play_img_up, play_img_pause, play_img_play, play_img_stop, play_img_next;
    private View play_board_view, play_dev_view, play_music_view;
    private RecyclerView dev_recyclerView, music_recyclerView;
    private Chronometer play_chronometer;
    private AppCompatSeekBar vertical_seekbar;
    private CommonAdapter musicAdapter;
    private int type = 1;//播放模式 1：顺序播放，2：循环播放，3：随机播放
    private int status = 0;//0:停止 1：播放 2 :暂停
    private int playIndex = 1;//播放下标
    private RealPlay realPlay;//广播会话
    private int modelTime = 0;//播放时间
    private long rangeTime = 0;//暂停时间
    private int volume = 20;//音量
    private IDialog dialogBuilder = null;//弹框
    private TextView shout_seek_text;
    private SeekBar shout_seek_bar;
    private BoardFileDbDao boardDb ;
    private String name ;


    /**
     * 状态控制图标改变
     */
    private void statusChange() {
        if (status == 0) {//停止----只能点击播放
            play_img_up.setImageResource(R.mipmap.icon_play_up_un);
            play_img_next.setImageResource(R.mipmap.icon_play_next_un);
            play_img_play.setImageResource(R.mipmap.icon_play_play);
            play_img_stop.setImageResource(R.mipmap.icon_play_stop_un);
            play_img_pause.setImageResource(R.mipmap.icon_play_pause_un);
        } else if (status == 1) {//播放 ----播放按钮不能点
            play_img_play.setImageResource(R.mipmap.icon_play_play_un);
            play_img_stop.setImageResource(R.mipmap.icon_play_stop);
            play_img_pause.setImageResource(R.mipmap.icon_play_pause);
            switch (type) {//播放模式
                case 1://顺序播放
                    play_img_up.setImageResource(R.mipmap.icon_play_up);
                    play_img_next.setImageResource(R.mipmap.icon_play_next);
                    if (playIndex >= boardFileDb.getMusicList().size()) {
                        play_img_next.setImageResource(R.mipmap.icon_play_next_un);
                    }
                    if (playIndex <= 1) {
                        play_img_up.setImageResource(R.mipmap.icon_play_up_un);
                    }
                    break;
                case 2://循环播放
                    play_img_up.setImageResource(R.mipmap.icon_play_up);
                    play_img_next.setImageResource(R.mipmap.icon_play_next);
                    break;
                case 3://随机播放
                    play_img_up.setImageResource(R.mipmap.icon_play_up);
                    play_img_next.setImageResource(R.mipmap.icon_play_next);
                    if (boardFileDb.getMusicList().size() <= 1) {
                        play_img_up.setImageResource(R.mipmap.icon_play_up_un);
                        play_img_next.setImageResource(R.mipmap.icon_play_next_un);
                    }
                    break;
            }
        } else if (status == 2) {//暂停 ----暂停按钮不能点
            play_img_pause.setImageResource(R.mipmap.icon_play_pause_un);
            play_img_play.setImageResource(R.mipmap.icon_play_play);
            play_img_stop.setImageResource(R.mipmap.icon_play_stop);
            switch (type) {//播放模式
                case 1://顺序播放
                    play_img_up.setImageResource(R.mipmap.icon_play_up);
                    play_img_next.setImageResource(R.mipmap.icon_play_next);
                    if (playIndex >= boardFileDb.getMusicList().size()) {
                        play_img_next.setImageResource(R.mipmap.icon_play_next_un);
                    }
                    if (playIndex <= 1) {
                        play_img_up.setImageResource(R.mipmap.icon_play_up_un);
                    }
                    break;
                case 2://循环播放
                    play_img_up.setImageResource(R.mipmap.icon_play_up);
                    play_img_next.setImageResource(R.mipmap.icon_play_next);
                    break;
                case 3://随机播放
                    play_img_up.setImageResource(R.mipmap.icon_play_up);
                    play_img_next.setImageResource(R.mipmap.icon_play_next);
                    if (boardFileDb.getMusicList().size() <= 1) {
                        play_img_up.setImageResource(R.mipmap.icon_play_up_un);
                        play_img_next.setImageResource(R.mipmap.icon_play_next_un);
                    }
                    break;
            }
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    public void initView() {
        StatusBarColorCompat.setStatusBarColor(activity, Color.parseColor("#F8FFFF"));
        name = getIntent().getStringExtra("name");
        DaoSession daoSession = AirApp.instance().getDaoSession();
        boardDb = daoSession.getBoardFileDbDao();
        List<BoardFileDb> boardList = boardDb.queryRaw("where NAME=?", name);
        boardFileDb = boardList.get(0);
        type = boardFileDb.getType();
        volume = boardFileDb.getVolume();
        dev_recyclerView = findViewById(R.id.dev_recyclerView);
        music_recyclerView = findViewById(R.id.music_recyclerView);
        play_board_text = findViewById(R.id.play_board_text);
        play_dev_text = findViewById(R.id.play_dev_text);
        play_music_text = findViewById(R.id.play_music_text);
        play_board_view = findViewById(R.id.play_board_view);
        play_dev_view = findViewById(R.id.play_dev_view);
        play_music_view = findViewById(R.id.play_music_view);
        vertical_seekbar = findViewById(R.id.vertical_seekbar);
        play_chronometer = findViewById(R.id.play_chronometer);
        play_max_time = findViewById(R.id.play_max_time);
        play_type_text = findViewById(R.id.play_type_text);
        play_type_img = findViewById(R.id.play_type_img);
        play_text_title = findViewById(R.id.play_text_title);
        play_img_up = findViewById(R.id.play_img_up);
        play_img_pause = findViewById(R.id.play_img_pause);
        play_img_play = findViewById(R.id.play_img_play);
        play_img_stop = findViewById(R.id.play_img_stop);
        play_img_next = findViewById(R.id.play_img_next);
        vertical_seekbar.setEnabled(false);
        dev_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dev_recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = UtilDip.getPixel(activity, R.dimen.base_dimen_2);
            }
        });
        music_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        music_recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = UtilDip.getPixel(activity, R.dimen.base_dimen_2);
            }
        });
        dev_recyclerView.setAdapter(new CommonAdapter<TermsDetails>(activity, R.layout.refresh_item, boardFileDb.getTermsList()) {
            @Override
            protected void convert(ViewHolder holder, final TermsDetails entity, final int position) {
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

        musicAdapter = new CommonAdapter<ProgInfoBean>(activity, R.layout.play_music_item, boardFileDb.getMusicList()) {
            @Override
            protected void convert(ViewHolder holder, final ProgInfoBean entity, final int position) {
                holder.setText(R.id.music_item_name, entity.getName());
                if ((playIndex - 1) == position && status > 0) {
                    holder.setTextColor(R.id.music_item_name, Color.parseColor("#255B7D"));
                } else {
                    holder.setTextColor(R.id.music_item_name, Color.parseColor("#333333"));
                }
                holder.setOnClickListener(R.id.music_item_line, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (realPlay == null) {
                            int[] ids = new int[boardFileDb.getTermsList().size()];
                            for (int i = 0; i < boardFileDb.getTermsList().size(); i++) {
                                ids[i] = boardFileDb.getTermsList().get(i).getID();
                            }
                            Map<String, Object> map = new HashMap<>();
                            map.put("Tids", ids);
                            map.put("PlayPrior", ShareDate.getShareInt(activity,"playPrior",100));
                            map.put("Name", boardFileDb.getName());
                            map.put("Vol", volume);
                            map.put("TNumber", 2);
                            mRxLife.add(RxHttp.request(HttpApi.api().FileSessionCreate(map))
                                    .request(new RxRequest.ResultCallback<RealPlay>() {
                                        @Override
                                        public void onSuccess(String code, RealPlay data) {
                                            realPlay = data;
                                            playIndex = position + 1;
                                            playMusic();
                                        }

                                        @Override
                                        public void onFailed(String code, String msg) {
                                            HttpApi.failDeal(code, msg, activity);
                                        }
                                    }));
                        } else {
                            playIndex = position + 1;
                            playMusic();
                        }
                    }
                });
            }
        };
        music_recyclerView.setAdapter(musicAdapter);
        //计时器监听
        play_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                vertical_seekbar.setProgress((int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000));
                if (SystemClock.elapsedRealtime() - chronometer.getBase() > modelTime * 1000) {
                    play_chronometer.stop();
                    vertical_seekbar.setProgress(0);
                    play_max_time.setText("00:00");
                    rangeTime = 0;
                    play_chronometer.setBase(SystemClock.elapsedRealtime());
                    if (type == 1) {//顺序播放
                        if (playIndex < boardFileDb.getMusicList().size()) {
                            playIndex++;
                            playMusic();
                        } else {
                            status = 0;
                            vertical_seekbar.setProgress(0);
                            rangeTime = 0;
                            play_max_time.setText("00:00");
                            findViewById(R.id.play_dec_line).setVisibility(View.GONE);
                            play_chronometer.setBase(SystemClock.elapsedRealtime());
                            statusChange();
                            destoryTask(false);
                        }
                    } else if (type == 2) {//循环播放
                        if (playIndex < boardFileDb.getMusicList().size()) {
                            playIndex++;
                            playMusic();
                        } else {
                            playIndex = 1;
                            playMusic();
                        }
                    } else {//随机播放
                        playIndex = (int) (1 + Math.random() * boardFileDb.getMusicList().size());
                        playMusic();
                    }
                }
            }
        });
        statusChange();
        if (type == 1) {
            play_type_text.setText("顺序播放");
            play_type_img.setImageResource(R.mipmap.icon_play_mode_dq);
        } else if (type == 2) {
            play_type_text.setText("循环播放");
            play_type_img.setImageResource(R.mipmap.icon_play_mode_sx);
        } else {
            play_type_text.setText("随机播放");
            play_type_img.setImageResource(R.mipmap.icon_play_mode_sj);
        }
        findViewById(R.id.shout_close).setOnClickListener(this);
        findViewById(R.id.play_board_line).setOnClickListener(this);
        findViewById(R.id.play_dev_line).setOnClickListener(this);
        findViewById(R.id.play_music_line).setOnClickListener(this);
        findViewById(R.id.play_control_up).setOnClickListener(this);
        findViewById(R.id.play_control_pause).setOnClickListener(this);
        findViewById(R.id.play_control_play).setOnClickListener(this);
        findViewById(R.id.play_control_stop).setOnClickListener(this);
        findViewById(R.id.play_control_next).setOnClickListener(this);
        findViewById(R.id.play_type_line).setOnClickListener(this);
        findViewById(R.id.play_set_vole).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shout_close://关闭
                onBackPressed();
                break;
            case R.id.play_set_vole://设置音量
                if (dialogBuilder == null) {
                    View customView = View.inflate(activity, R.layout.dialog_set_volum, null);
                    shout_seek_bar = customView.findViewById(R.id.shout_seek_bar);
                    shout_seek_text = customView.findViewById(R.id.shout_seek_text);
                    ImageView shout_close = customView.findViewById(R.id.shout_close);
                    shout_seek_text.setText("" + volume);
                    shout_seek_bar.setProgress(volume);
                    Drawable drawable = PublicUtil.getNewDrawable(activity, R.mipmap.icon_seek_btn, getResources().getDimensionPixelOffset(R.dimen.base_dimen_160), getResources().getDimensionPixelOffset(R.dimen.base_dimen_120));
                    shout_seek_bar.setThumb(drawable);
                    shout_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
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
                            volume = seekBar.getProgress();
                            shout_seek_text.setText(volume + "");
                            setVolue(volume);
                        }
                    });
                    dialogBuilder = DialogBuilder.bottomSheetDialog(activity).setContentView(customView).create();
                    dialogBuilder.getInnerDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (event.getAction() == KeyEvent.ACTION_DOWN &&  keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                                if (volume > 0) {
                                    volume = volume - 1;
                                    if (shout_seek_text != null){
                                        shout_seek_text.setText(volume + "");
                                    }

                                    setVolue(volume);
                                } else {
                                    volume = 0;
                                    UtilToast.showToast(activity, "已经是最低音量");
                                }
                                if (shout_seek_bar != null){}
                                    shout_seek_bar.setProgress(volume);
                                return true;
                            } else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                                if (volume < 56) {
                                    volume = volume + 1;
                                    if (shout_seek_text != null)
                                        shout_seek_text.setText(volume + "");
                                    setVolue(volume);
                                } else {
                                    volume = 56;
                                    UtilToast.showToast(activity, "已经是最大音量");
                                }
                                if (shout_seek_bar != null)
                                    shout_seek_bar.setProgress(volume);
                                return true;
                            }
                            return false;
                        }
                    });

                    dialogBuilder.show();
                } else {
                    dialogBuilder.show();
                }
                break;
            case R.id.play_control_up://上一曲
                if (realPlay != null && status != 0) {
                    if (type == 1) {//顺序播放
                        if (playIndex > 1) {
                            playIndex--;
                            playMusic();
                        }
                    } else if (type == 2) {//循环播放
                        if (playIndex > 1) {
                            playIndex--;
                            playMusic();
                        } else {
                            playIndex = boardFileDb.getMusicList().size();
                            playMusic();
                        }
                    } else {//随机播放
                        playIndex = (int) (1 + Math.random() * boardFileDb.getMusicList().size());
                        playMusic();
                    }
                }
                break;
            case R.id.play_control_pause://暂停
                if (status == 1)
                    controlMusic(2);
                break;
            case R.id.play_control_play://播放
                if (realPlay == null) {
                    int[] ids = new int[boardFileDb.getTermsList().size()];
                    for (int i = 0; i < boardFileDb.getTermsList().size(); i++) {
                        ids[i] = boardFileDb.getTermsList().get(i).getID();
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("Tids", ids);
                    map.put("PlayPrior", ShareDate.getShareInt(activity,"playPrior",100));
                    map.put("Name", boardFileDb.getName());
                    map.put("Vol", volume);
                    map.put("TNumber", 2);
                    mRxLife.add(RxHttp.request(HttpApi.api().FileSessionCreate(map))
                            .request(new RxRequest.ResultCallback<RealPlay>() {
                                @Override
                                public void onSuccess(String code, RealPlay data) {
                                    realPlay = data;
                                    playMusic();
                                }

                                @Override
                                public void onFailed(String code, String msg) {
                                    HttpApi.failDeal(code, msg, activity);
                                }
                            }));
                } else {
                    if (status == 2) {//暂停
                        controlMusic(1);
                    } else if (status == 0) {//停止后启动播放
                        playMusic();
                    }
                }
                break;
            case R.id.play_control_stop://停止
                if (realPlay != null && status != 0)
                    controlMusic(0);
                break;
            case R.id.play_control_next://下一曲
                if (realPlay != null && status != 0) {
                    if (type == 1) {//顺序播放
                        if (playIndex < boardFileDb.getMusicList().size()) {
                            playIndex++;
                            playMusic();
                        }
                    } else if (type == 2) {//循环播放
                        if (playIndex < boardFileDb.getMusicList().size()) {
                            playIndex++;
                        } else {
                            playIndex = 1;
                        }
                        playMusic();
                    } else {//随机播放
                        playIndex = (int) (1 + Math.random() * boardFileDb.getMusicList().size());
                        playMusic();
                    }
                }
                break;
            case R.id.play_type_line://播放模式
                if (type == 1) {
                    type = 2;
                    play_type_text.setText("循环播放");
                    play_type_img.setImageResource(R.mipmap.icon_play_mode_sx);
                } else if (type == 2) {
                    type = 3;
                    play_type_text.setText("随机播放");
                    play_type_img.setImageResource(R.mipmap.icon_play_mode_sj);
                } else {
                    type = 1;
                    play_type_text.setText("顺序播放");
                    play_type_img.setImageResource(R.mipmap.icon_play_mode_dq);
                }
                statusChange();
                break;
            case R.id.play_board_line://点播
                music_recyclerView.setVisibility(View.GONE);
                dev_recyclerView.setVisibility(View.GONE);
                findViewById(R.id.play_content_board).setVisibility(View.VISIBLE);
                play_board_text.setTextColor(Color.parseColor("#255B7D"));
                play_dev_text.setTextColor(Color.parseColor("#666666"));
                play_music_text.setTextColor(Color.parseColor("#666666"));
                play_board_view.setBackgroundResource(R.drawable.bg_shout_line_on);
                play_dev_view.setBackgroundResource(R.drawable.bg_shout_line_un);
                play_music_view.setBackgroundResource(R.drawable.bg_shout_line_un);
                break;
            case R.id.play_dev_line://终端
                music_recyclerView.setVisibility(View.GONE);
                dev_recyclerView.setVisibility(View.VISIBLE);
                findViewById(R.id.play_content_board).setVisibility(View.GONE);
                play_board_text.setTextColor(Color.parseColor("#666666"));
                play_dev_text.setTextColor(Color.parseColor("#255B7D"));
                play_music_text.setTextColor(Color.parseColor("#666666"));
                play_board_view.setBackgroundResource(R.drawable.bg_shout_line_un);
                play_dev_view.setBackgroundResource(R.drawable.bg_shout_line_on);
                play_music_view.setBackgroundResource(R.drawable.bg_shout_line_un);
                break;
            case R.id.play_music_line://内容
                music_recyclerView.setVisibility(View.VISIBLE);
                dev_recyclerView.setVisibility(View.GONE);
                findViewById(R.id.play_content_board).setVisibility(View.GONE);
                play_board_text.setTextColor(Color.parseColor("#666666"));
                play_dev_text.setTextColor(Color.parseColor("#666666"));
                play_music_text.setTextColor(Color.parseColor("#255B7D"));
                play_board_view.setBackgroundResource(R.drawable.bg_shout_line_un);
                play_dev_view.setBackgroundResource(R.drawable.bg_shout_line_un);
                play_music_view.setBackgroundResource(R.drawable.bg_shout_line_on);
                break;

        }
    }

    /**
     * 启动播放
     */
    private void playMusic() {
        Map<String, Object> map = new HashMap<>();
        map.put("Sid", realPlay.getSid());
        map.put("ProgId", Integer.parseInt(boardFileDb.getMusicList().get(playIndex - 1).getID()));
        map.put("Name", boardFileDb.getName());
        mRxLife.add(RxHttp.request(HttpApi.api().FileSessionSetProg(map))
                .listener(new HttpListen(activity))
                .request(new RxRequest.ResultCallback<PlayBean>() {
                    @Override
                    public void onSuccess(String code, PlayBean data) {
                        modelTime = data.getTotalTime();
                        status = 1;
                        findViewById(R.id.play_dec_line).setVisibility(View.VISIBLE);
                        play_text_title.setText("正在播放：" + boardFileDb.getMusicList().get(playIndex - 1).getName());
                        play_max_time.setText(UtilTime.formatTime(modelTime * 1000));
                        play_chronometer.setBase(SystemClock.elapsedRealtime());
                        play_chronometer.start();
                        vertical_seekbar.setMax(modelTime);
                        musicAdapter.notifyDataSetChanged();
                        statusChange();
                    }

                    @Override
                    public void onFailed(String code, String msg) {
                        HttpApi.failDeal(code, msg, activity);
                    }
                }));
    }

    /**
     * 控制播放
     */
    private void controlMusic(int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("Sid", realPlay.getSid());
        map.put("Status", type);
        mRxLife.add(RxHttp.request(HttpApi.api().FileSessionSetStat(map))
                .request(new RxRequest.ResultCallback<BaseBean>() {
                    @Override
                    public void onSuccess(String code, BaseBean data) {
                        status = type;
                        musicAdapter.notifyDataSetChanged();
                        switch (status) {
                            case 0://停止
                                play_chronometer.stop();
                                vertical_seekbar.setProgress(0);
                                rangeTime = 0;
                                play_max_time.setText("00:00");
                                findViewById(R.id.play_dec_line).setVisibility(View.GONE);
                                play_chronometer.setBase(SystemClock.elapsedRealtime());
                                destoryTask(false);
                                break;
                            case 1://播放
                                play_chronometer.setBase(SystemClock.elapsedRealtime() - rangeTime);
                                play_chronometer.start();
                                break;
                            case 2://暂停
                                rangeTime = SystemClock.elapsedRealtime() - play_chronometer.getBase();
                                play_chronometer.stop();
                                break;
                        }
                        statusChange();
                    }

                    @Override
                    public void onFailed(String code, String msg) {
                        HttpApi.failDeal(code, msg, activity);
                    }
                }));
    }

    private void destoryTask(boolean flag){
        if (realPlay != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("Sid", realPlay.getSid());
            mRxLife.add(RxHttp.request(HttpApi.api().FileSessionDestory(map))
                    .request(new RxRequest.ResultCallback<BaseBean>() {
                        @Override
                        public void onSuccess(String code, BaseBean data) {
                            realPlay = null;
                            if(flag)
                                onBackPressed();
                        }

                        @Override
                        public void onFailed(String code, String msg) {
                            HttpApi.failDeal(code, msg, activity);
                        }
                    }));
        }
    };

    @Override
    public void onBackPressed() {
        if (realPlay != null) {
            destoryTask(true);
        } else {
            boardFileDb.setVolume(volume);
            boardFileDb.setType(type);
            boardDb.update(boardFileDb);
            super.onBackPressed();
        }
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (volume > 0) {
                volume = volume - 1;
                if (shout_seek_text != null)
                    shout_seek_text.setText(volume + "");
                setVolue(volume);
            } else {
                volume = 0;
                UtilToast.showToast(activity, "已经是最低音量");
            }
            if (shout_seek_bar != null)
                shout_seek_bar.setProgress(volume);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (volume < 56) {
                volume = volume + 1;
                if (shout_seek_text != null)
                    shout_seek_text.setText(volume + "");
                setVolue(volume);
            } else {
                volume = 56;
                UtilToast.showToast(activity, "已经是最大音量");
            }
            if (shout_seek_bar != null)
                shout_seek_bar.setProgress(volume);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}