package com.comtom.aibo.module.board;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comtom.aibo.R;
import com.comtom.aibo.app.AirApp;
import com.comtom.aibo.data.BoardFileDb;
import com.comtom.aibo.greendao.gen.BoardFileDbDao;
import com.comtom.aibo.greendao.gen.DaoSession;
import com.comtom.aibo.module.base.BaseActivity;
import com.haozi.dev.smartframe.utils.adapt.CommonAdapter;
import com.haozi.dev.smartframe.utils.adapt.base.ViewHolder;
import com.haozi.dev.smartframe.utils.tool.PackageUtils;
import com.haozi.dev.smartframe.utils.tool.UtilDip;
import com.haozi.dev.smartframe.utils.tool.UtilLog;

import java.util.ArrayList;
import java.util.List;

import cn.yhq.dialog.core.DialogBuilder;

public class BoardActivity extends BaseActivity {
    private RecyclerView board_recyclerView;
    private DaoSession daoSession;
    private BoardFileDbDao boardDb;
    private CommonAdapter commonAdapter ;
    private  List<BoardFileDb> list;

    @Override
    public int getLayoutId() {
        return R.layout.activity_board;
    }

    @Override
    public void initView() {
        setTitle("文件点播");
        daoSession = AirApp.instance().getDaoSession();
        boardDb = daoSession.getBoardFileDbDao();
        list = new ArrayList<>();
        board_recyclerView = findViewById(R.id.board_recyclerView);
        board_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        board_recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = UtilDip.getPixel(activity, R.dimen.base_dimen_32);
            }
        });
        commonAdapter = new CommonAdapter<BoardFileDb>(activity, R.layout.board_item, list) {
            @Override
            protected void convert(ViewHolder holder, final BoardFileDb entity, final int position) {
                holder.setText(R.id.board_item_name, entity.getName());
                RelativeLayout layout_content = holder.getView(R.id.layout_content);
                layout_content.getLayoutParams().width = PackageUtils.getScreenWidth(activity) - getResources().getDimensionPixelOffset(R.dimen.base_dimen_64);
                if(entity.getStatus() == 0){
                    holder.setText(R.id.board_item_status,"未播放");
                    holder.setTextColor(R.id.board_item_status, Color.parseColor("#255B7D"));
                }else if(entity.getStatus() == 1){
                    holder.setText(R.id.board_item_status,"播放中");
                    holder.setTextColor(R.id.board_item_status, Color.parseColor("#70A67F"));
                }
                holder.setOnClickListener(R.id.board_item_line, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity,PlayActivity.class);
                        intent.putExtra("name",entity.getName());
                        startActivity(intent);
                    }
                });
                holder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UtilLog.showNet("1111111");
                        DialogBuilder.alertDialog(activity)
                                .setTitle("删除警告")
                                .setMessage("确认删除此点播任务吗？")
                                .setOnPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        boardDb.deleteByKey(entity.getBoardId());
                                        list.remove(position);
                                        commonAdapter.setDatas(list);
                                    }
                                }).create().show();
                    }
                });
            }
        };
        board_recyclerView.setAdapter(commonAdapter);
        findViewById(R.id.board_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity,SelectProgramActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        list.addAll(boardDb.loadAll());
        commonAdapter.setDatas(list);
    }
}