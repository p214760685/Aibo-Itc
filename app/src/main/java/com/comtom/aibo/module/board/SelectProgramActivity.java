package com.comtom.aibo.module.board;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comtom.aibo.R;
import com.comtom.aibo.app.AirApp;
import com.comtom.aibo.data.BoardFileDb;
import com.comtom.aibo.entity.GroupBean;
import com.comtom.aibo.entity.ProgInfoBean;
import com.comtom.aibo.entity.TermIDBean;
import com.comtom.aibo.entity.TermsDetails;
import com.comtom.aibo.greendao.gen.BoardFileDbDao;
import com.comtom.aibo.greendao.gen.DaoSession;
import com.comtom.aibo.httpset.HttpApi;
import com.comtom.aibo.httpset.HttpListen;
import com.comtom.aibo.module.base.BaseActivity;
import com.haozi.dev.smartframe.rxhttp.core.RxHttp;
import com.haozi.dev.smartframe.rxhttp.request.RxRequest;
import com.haozi.dev.smartframe.utils.adapt.CommonAdapter;
import com.haozi.dev.smartframe.utils.adapt.base.ViewHolder;
import com.haozi.dev.smartframe.utils.tool.UtilDip;
import com.haozi.dev.smartframe.utils.tool.UtilLog;
import com.haozi.dev.smartframe.utils.tool.UtilToast;
import com.haozi.dev.smartframe.view.refresh.MySmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cn.yhq.dialog.core.DialogBuilder;

public class SelectProgramActivity extends BaseActivity {

    private RecyclerView ms_recyclerView;
    private RecyclerView.ItemDecoration itemDecoration;
    private MySmartRefreshLayout refresh_layout;
    private TextView select_program_music, select_program_dev;
    private CommonAdapter commonAdapter;
    private RadioGroup select_check_group;
    private List<GroupBean> groupList;
    private List<ProgInfoBean> selectMusicList;
    private List<GroupBean> selectGroupList;
    private List<TermsDetails> selectTermsList;
    private int requestType = 3; // 类型 1：终端 2： 分组 3:曲目
    private TextView select_dev_text;
    private CheckBox select_dev_check;
    private int oldCheck = 1;


    @Override
    public int getLayoutId() {
        return R.layout.activity_select_program;
    }

    @Override
    public void initView() {
        setTitle("新建");
        setMenu("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectMusicList.size() <= 0) {
                    UtilToast.showToast(activity, "请选择曲目");
                } else {
                    List<TermsDetails> list = new ArrayList<>();
                    if (selectTermsList.size() <= 0 && selectGroupList.size() <= 0) {
                        UtilToast.showToast(activity, "请选择终端或者分组");
                    } else {
                        list.addAll(selectTermsList);
                        if (selectGroupList.size() > 0) {
                            for (int i = 0; i < selectGroupList.size(); i++) {
                                list.addAll(selectGroupList.get(i).getTermsDetails());
                            }
                        }
                        //去重
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < list.size(); ) {
                                if (i != j && list.get(i).getID() == list.get(j).getID()) {
                                    list.remove(j);
                                } else {
                                    j++;
                                }
                            }
                        }
                        //弹框
                        DialogBuilder.editTextDialog(activity)
                                .setTitle("填写任务名称")
                                .setOnEditTextDialogListener(new DialogBuilder.OnEditTextDialogListener() {
                                    @Override
                                    public void onEditTextCreated(EditText editText) {
                                        editText.setHint("请填写任务名称");
                                        editText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                    }

                                    @Override
                                    public boolean onEditTextSelected(EditText editText, String text) {
                                        if (text != null && text.length() > 0) {
                                            DaoSession daoSession = AirApp.instance().getDaoSession();
                                            BoardFileDbDao boardDb = daoSession.getBoardFileDbDao();
                                            List<BoardFileDb> boardList = boardDb.queryRaw("where NAME=?", text);
                                            if (boardList != null && boardList.size() > 0) {
                                                UtilToast.showToast(activity, "任务名称已存在，请更换");
                                            } else {
                                                boardDb.insert(new BoardFileDb(null,text,-1,0,20,1,list,selectMusicList));
                                                Intent intent = new Intent(activity,PlayActivity.class);
                                                intent.putExtra("name",text);
                                                startActivity(intent);
                                                finish();
                                            }
                                        } else {
                                            UtilToast.showToast(activity, "请填写任务名称");
                                        }
                                        return false;
                                    }
                                }).show();
                    }
                }
            }
        });
        select_check_group = findViewById(R.id.select_check_group);
        select_program_music = findViewById(R.id.select_program_music);
        select_program_dev = findViewById(R.id.select_program_dev);
        groupList = new ArrayList<>();
        selectMusicList = new ArrayList<>();
        selectGroupList = new ArrayList<>();
        selectTermsList = new ArrayList<>();
        ms_recyclerView = findViewById(R.id.ms_recyclerView);
        refresh_layout = findViewById(R.id.refresh_layout);
        select_dev_text = findViewById(R.id.select_dev_text);
        select_dev_check = findViewById(R.id.select_dev_check);
        refresh_layout.initAddHead(this);
        ms_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ms_recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = UtilDip.getPixel(activity, R.dimen.base_dimen_2);
            }
        });

        itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = UtilDip.getPixel(activity, R.dimen.base_dimen_1);
            }
        };

        commonAdapter = new CommonAdapter<GroupBean>(activity, R.layout.refresh_item, groupList) {
            @Override
            protected void convert(ViewHolder holder, final GroupBean entity, final int position) {
                if (requestType == 1) {
                    holder.setVisible(R.id.select_item_dev_line, true);
                    holder.setVisible(R.id.select_item_program_line, false);
                    holder.setVisible(R.id.select_item_status, true);
                    holder.setVisible(R.id.select_item_round, false);
                    holder.setText(R.id.select_item_name, entity.getTermsDetail().getName());
                    if (entity.getTermsDetail() != null && entity.getTermsDetail().getStatus() == -1) {
                        holder.setText(R.id.select_item_status, "离线");
                        holder.setBackgroundRes(R.id.select_item_status, R.drawable.bg_select_offline);
                    } else if (entity.getTermsDetail() != null && entity.getTermsDetail().getStatus() == 0) {
                        holder.setText(R.id.select_item_status, "空闲");
                        holder.setBackgroundRes(R.id.select_item_status, R.drawable.bg_select_free);
                    } else if (entity.getTermsDetail() != null && entity.getTermsDetail().getStatus() > 0) {
                        holder.setText(R.id.select_item_status, "工作中");
                        holder.setBackgroundRes(R.id.select_item_status, R.drawable.bg_select_work);
                    }
                    CheckBox select_item_check = holder.getView(R.id.select_item_check);
                    select_item_check.setChecked(entity.getTermsDetail().isFlag());
                    select_item_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (groupList.size() > 0 && position >= 0 && groupList.get(position).getTermsDetail() != null) {
                                groupList.get(position).getTermsDetail().setFlag(isChecked);
                                loadCheck();
                            }
                        }
                    });
                } else if (requestType == 2) {
                    holder.setVisible(R.id.select_item_dev_line, true);
                    holder.setVisible(R.id.select_item_program_line, false);
                    holder.setVisible(R.id.select_item_status, false);
                    holder.setVisible(R.id.select_item_round, true);
                    holder.setText(R.id.select_item_name, entity.getName());
                    CheckBox select_item_check = holder.getView(R.id.select_item_check);
                    select_item_check.setChecked(entity.isFlag());
                    select_item_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (groupList.size() > 0 && position >= 0) {
                                groupList.get(position).setFlag(isChecked);
                                loadCheck();
                            }
                        }
                    });
                } else if (requestType == 3) {
                    holder.setVisible(R.id.select_item_dev_line, false);
                    holder.setVisible(R.id.select_item_program_line, true);
                    holder.setText(R.id.select_item_program_name, entity.getName());
                    RecyclerView select_item_program_recyclerView = holder.getView(R.id.select_item_program_recyclerView);
                    select_item_program_recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    select_item_program_recyclerView.removeItemDecoration(itemDecoration);
                    select_item_program_recyclerView.addItemDecoration(itemDecoration);
                    select_item_program_recyclerView.setAdapter(new CommonAdapter<ProgInfoBean>(activity, R.layout.program_item, entity.getItems()) {
                        @Override
                        protected void convert(ViewHolder holder, final ProgInfoBean program, final int childPosition) {
                            holder.setText(R.id.music_item_name, program.getName());
                            CheckBox music_item_check = holder.getView(R.id.music_item_check);
                            music_item_check.setChecked(program.isFlag());
                            music_item_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (entity.getItems() != null && entity.getItems().size() > 0 && childPosition >= 0) {
                                        groupList.get(position).getItems().get(childPosition).setFlag(isChecked);
                                        loadCheck();
                                    }
                                }
                            });
                        }
                    });
                    if (entity.isOpen()) {
                        holder.setVisible(R.id.select_item_program_recyclerView, true);
                        holder.setImageResource(R.id.select_item_program_arraw, R.mipmap.icon_board_select_open);
                    } else {
                        holder.setVisible(R.id.select_item_program_recyclerView, false);
                        holder.setImageResource(R.id.select_item_program_arraw, R.mipmap.icon_board_select_close);
                    }
                    holder.setOnClickListener(R.id.select_item_program_line, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!entity.getID().equals("-1")) {
                                if (entity.isOpen()) {
                                    groupList.get(position).setOpen(false);
                                    commonAdapter.setDatas(groupList);
                                } else {
                                    if (entity.getItems() != null && entity.getItems().size() > 0) {
                                        groupList.get(position).setOpen(true);
                                        commonAdapter.setDatas(groupList);
                                    } else {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("DirId", Integer.parseInt(entity.getID()));
                                        mRxLife.add(RxHttp.request(HttpApi.api().MLListDir(map))
                                                .listener(new HttpListen(activity))
                                                .request(new RxRequest.ResultCallback<ProgInfoBean>() {

                                                    @Override
                                                    public void onSuccess(String code, ProgInfoBean data) {
                                                        if (data.getItems().size() > 0) {
                                                            List<ProgInfoBean> child = new ArrayList<>();
                                                            for (int i = 0; i < data.getItems().size(); i++) {
                                                                if (data.getItems().get(i).getType() == 1) {
                                                                    for (int j = 0; j < selectMusicList.size(); j++) {
                                                                        if (data.getItems().get(i).getID().equals(selectMusicList.get(j).getID())) {
                                                                            data.getItems().get(i).setFlag(true);
                                                                            break;
                                                                        }
                                                                    }
                                                                    child.add(data.getItems().get(i));
                                                                }
                                                            }
                                                            groupList.get(position).setItems(child);
                                                            groupList.get(position).setOpen(true);
                                                            commonAdapter.setDatas(groupList);
                                                        } else {
                                                            UtilToast.showToast(activity, "该文件夹下暂无曲目");
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailed(String code, String msg) {
                                                        HttpApi.failDeal(code, msg, activity);
                                                    }
                                                }));
                                    }
                                }
                            }
                        }
                    });

                }
            }
        };
        ms_recyclerView.setAdapter(commonAdapter);

        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
        select_check_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                groupList.clear();
                commonAdapter.setDatas(groupList);
                switch (checkedId) {
                    //选择终端
                    case R.id.select_check_radio_1:
                        requestType = 1;
                        refresh_layout.autoRefresh();
                        break;
                    //选择分组
                    case R.id.select_check_radio_2:
                        requestType = 2;
                        refresh_layout.autoRefresh();
                        break;
                }

            }
        });
        select_program_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requestType != 3)
                    oldCheck = requestType;
                requestType = 3;
                refresh_layout.autoRefresh();
                findViewById(R.id.select_program_bottom).setVisibility(View.GONE);
                select_program_music.setBackgroundColor(Color.parseColor("#D7674E"));
                select_program_dev.setBackgroundColor(Color.parseColor("#EEEEEE"));
                select_program_music.setTextColor(Color.parseColor("#FFFFFF"));
                select_program_dev.setTextColor(Color.parseColor("#666666"));
            }
        });
        select_program_dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestType = oldCheck;
                refresh_layout.autoRefresh();
                findViewById(R.id.select_program_bottom).setVisibility(View.VISIBLE);
                select_program_music.setBackgroundColor(Color.parseColor("#EEEEEE"));
                select_program_dev.setBackgroundColor(Color.parseColor("#D7674E"));
                select_program_music.setTextColor(Color.parseColor("#666666"));
                select_program_dev.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });
        //全选、全不选
        findViewById(R.id.select_dev_line).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestType == 1) {//终端
                    if (groupList.size() > 0) {
                        boolean flag = true;
                        for (int i = 0; i < groupList.size(); i++) {
                            if (!groupList.get(i).getTermsDetail().isFlag()) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            for (int i = 0; i < groupList.size(); i++) {
                                groupList.get(i).getTermsDetail().setFlag(false);
                            }
                        } else {
                            for (int i = 0; i < groupList.size(); i++) {
                                groupList.get(i).getTermsDetail().setFlag(true);
                            }
                        }
                        commonAdapter.setDatas(groupList);
                        loadCheck();
                    }

                } else if (requestType == 2) {//分组
                    if (groupList.size() > 0) {
                        boolean flag = true;
                        for (int i = 0; i < groupList.size(); i++) {
                            if (!groupList.get(i).isFlag()) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            for (int i = 0; i < groupList.size(); i++) {
                                groupList.get(i).setFlag(false);
                            }
                        } else {
                            for (int i = 0; i < groupList.size(); i++) {
                                groupList.get(i).setFlag(true);
                            }
                        }
                        commonAdapter.setDatas(groupList);
                        loadCheck();
                    }
                }

            }
        });
        refresh_layout.autoRefresh();
    }

    /**
     * 获取终端状态详情
     *
     * @param termIDs
     */
    private void getData(int[] termIDs) {
        Map<String, Object> map = new HashMap<>();
        map.put("TermIDs", termIDs);
        map.put("TNumber", 2);
        mRxLife.add(RxHttp.request(HttpApi.api().getTermState(map))
                .request(new RxRequest.ResultCallback<TermsDetails>() {

                    @Override
                    public void onSuccess(String code, TermsDetails data) {
                        if (requestType == 1) {//终端数据
                            groupList.clear();
                            for (int i = 0; i < data.getTerms().size(); i++) {
                                TermsDetails termsDetails = data.getTerms().get(i);
                                GroupBean groupBean = new GroupBean();
                                for (int j = 0; j < selectTermsList.size(); j++) {
                                    if (termsDetails.getID() == selectTermsList.get(j).getID()) {
                                        termsDetails.setFlag(true);
                                        break;
                                    }
                                }
                                groupBean.setTermsDetail(termsDetails);
                                groupList.add(groupBean);
                            }
                        } else {//分组绑定终端详情
                            for (int i = 0; i < groupList.size(); i++) {
                                if (groupList.get(i).getTids() != null && groupList.get(i).getTids().length > 0) {
                                    groupList.get(i).setTermsDetails(getChildTerms(groupList.get(i).getTids(), data.getTerms()));
                                }
                            }
                        }
                        commonAdapter.setDatas(groupList);
                        loadCheck();
                        refresh_layout.finishRefresh();
                    }

                    @Override
                    public void onFailed(String code, String msg) {
                        HttpApi.failDeal(code, msg, activity);
                    }
                }));
    }

    /**
     * 下拉刷新初始化数据1：终端 2：分组
     */
    private void initData() {
        groupList.clear();
        commonAdapter.setDatas(groupList);
        if (requestType == 1) {//终端
            Map<String, Object> map = new HashMap<>();
            map.put("TNumber", 2);
            mRxLife.add(RxHttp.request(HttpApi.api().getTermIds(map))
                    .request(new RxRequest.ResultCallback<TermIDBean>() {
                        @Override
                        public void onSuccess(String code, TermIDBean data) {
                            UtilLog.showNet(data.getTermIds().length + "");
                            if (data.getTermIds() != null && data.getTermIds().length > 0) {
                                getData(data.getTermIds());
                            } else {
                                UtilToast.showToast(activity, "暂无终端数据");
                                refresh_layout.finishRefresh();
                            }
                        }

                        @Override
                        public void onFailed(String code, String msg) {
                            HttpApi.failDeal(code, msg, activity);
                            refresh_layout.finishRefresh();
                        }
                    }));
        } else if (requestType == 2) {//分组
            mRxLife.add(RxHttp.request(HttpApi.api().getGroups(new HashMap<>()))
                    .request(new RxRequest.ResultCallback<GroupBean>() {

                        @Override
                        public void onSuccess(String code, GroupBean data) {
                            groupList.clear();

                            groupList.addAll(data.getGroups());
                            if (groupList.size() > 0) {
                                Set<Integer> set = new TreeSet<>();
                                for (int i = 0; i < groupList.size(); i++) {
                                    for (int j = 0; j < selectGroupList.size(); j++) {
                                        if (groupList.get(i).getID().equals(selectGroupList.get(j).getID())) {
                                            groupList.get(i).setFlag(true);
                                            break;
                                        }
                                    }
                                    if (groupList.get(i).getTids().length > 0) {
                                        for (int j = 0; j < groupList.get(i).getTids().length; j++) {
                                            set.add(groupList.get(i).getTids()[j]);
                                        }
                                    }
                                }
                                if (set.size() > 0) {
                                    int[] needArr = new int[set.size()];
                                    int index = 0;
                                    for (Integer integer : set) {
                                        needArr[index] = integer;
                                        ++index;
                                    }
                                    getData(needArr);
                                }
                            } else {
                                UtilToast.showToast(activity, "暂无分组数据");
                                refresh_layout.finishRefresh();
                            }
                        }

                        @Override
                        public void onFailed(String code, String msg) {
                            HttpApi.failDeal(code, msg, activity);
                            refresh_layout.finishRefresh();
                        }
                    }));
        } else if (requestType == 3) {//曲目
            Map<String, Object> map = new HashMap<>();
            map.put("DirId", 0);
            mRxLife.add(RxHttp.request(HttpApi.api().MLListDir(map))
                    .request(new RxRequest.ResultCallback<ProgInfoBean>() {

                        @Override
                        public void onSuccess(String code, ProgInfoBean data) {
                            groupList.clear();
                            if (data.getItems().size() > 0) {
                                List<ProgInfoBean> child = new ArrayList<>();
                                for (int i = 0; i < data.getItems().size(); i++) {
                                    GroupBean groupBean = new GroupBean();
                                    if (data.getItems().get(i).getType() == 2) {
                                        groupBean.setID(data.getItems().get(i).getID());
                                        groupBean.setName(data.getItems().get(i).getName());
                                        groupList.add(groupBean);
                                    } else if (data.getItems().get(i).getType() == 1) {
                                        for (int j = 0; j < selectMusicList.size(); j++) {
                                            if (data.getItems().get(i).getID().equals(selectMusicList.get(j).getID())) {
                                                data.getItems().get(i).setFlag(true);
                                            }
                                        }
                                        child.add(data.getItems().get(i));
                                    }
                                }
                                if (child.size() > 0) {
                                    GroupBean groupBean = new GroupBean();
                                    groupBean.setID("-1");
                                    groupBean.setName("未分组媒体信息");
                                    groupBean.setItems(child);
                                    groupBean.setOpen(true);
                                    groupList.add(groupBean);
                                }
                                commonAdapter.setDatas(groupList);
                                refresh_layout.finishRefresh();
                            } else {
                                UtilToast.showToast(activity, "暂无曲目数据");
                                refresh_layout.finishRefresh();
                            }
                        }

                        @Override
                        public void onFailed(String code, String msg) {
                            HttpApi.failDeal(code, msg, activity);
                            refresh_layout.finishRefresh();
                        }
                    }));
        }

    }

    /**
     * 筛选分组下终端详情
     *
     * @param all
     * @return
     */
    private List<TermsDetails> getChildTerms(int[] Tid, List<TermsDetails> all) {
        List<TermsDetails> list = new ArrayList<>();
        for (int i = 0; i < Tid.length; i++) {
            for (int j = 0; j < all.size(); j++) {
                if (Tid[i] == all.get(j).getID()) {
                    list.add(all.get(j));
                }
            }
        }
        return list;
    }

    /**
     * 设置选中的数据存储
     */
    private void setSelect() {
        if (requestType == 1) {
            selectTermsList.clear();
            for (int i = 0; i < groupList.size(); i++) {
                if (groupList.get(i).getTermsDetail().isFlag()) {
                    selectTermsList.add(groupList.get(i).getTermsDetail());
                }
            }
        } else if (requestType == 2) {
            selectGroupList.clear();
            for (int i = 0; i < groupList.size(); i++) {
                if (groupList.get(i).isFlag()) {
                    selectGroupList.add(groupList.get(i));
                }
            }
        } else if (requestType == 3) {
            selectMusicList.clear();
            for (int i = 0; i < groupList.size(); i++) {
                for (int j = 0; j < groupList.get(i).getItems().size(); j++) {
                    if (groupList.get(i).getItems().get(j).isFlag()) {
                        selectMusicList.add(groupList.get(i).getItems().get(j));
                    }
                }

            }
        }
    }


    /**
     * 判断是否全选或者不选
     */
    private void loadCheck() {
        if (requestType == 1) {//终端
            if (groupList.size() > 0) {
                boolean flag = true;
                for (int i = 0; i < groupList.size(); i++) {
                    if (!groupList.get(i).getTermsDetail().isFlag()) {
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
        } else if (requestType == 2) {//分组
            if (groupList.size() > 0) {
                boolean flag = true;
                for (int i = 0; i < groupList.size(); i++) {
                    if (!groupList.get(i).isFlag()) {
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
        setSelect();
    }
}
