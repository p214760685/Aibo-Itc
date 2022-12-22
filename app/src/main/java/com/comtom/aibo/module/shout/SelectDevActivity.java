package com.comtom.aibo.module.shout;

import android.content.Intent;
import android.graphics.Rect;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comtom.aibo.R;
import com.comtom.aibo.data.TermDb;
import com.comtom.aibo.entity.GroupBean;
import com.comtom.aibo.entity.TermIDBean;
import com.comtom.aibo.entity.TermsDetails;
import com.comtom.aibo.httpset.HttpApi;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SelectDevActivity extends BaseActivity {

    private RecyclerView ms_recyclerView;
    private MySmartRefreshLayout refresh_layout;
    private CommonAdapter commonAdapter;
    private RadioGroup select_check_group;
    private List<GroupBean> groupList;
    private TextView select_type, select_dev_text;
    private CheckBox select_dev_check;
    private int requestType = 1; // 请求类型 1：终端 2： 分组


    @Override
    public int getLayoutId() {
        return R.layout.activity_select_dev;
    }

    @Override
    public void initView() {
        setTitle("选择终端");
        setMenu("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TermsDetails> list = new ArrayList<>();
                if (requestType == 1) {//终端
                    for (int i = 0; i < groupList.size(); i++) {
                        if (groupList.get(i).getTermsDetail() != null && groupList.get(i).getTermsDetail().isFlag()) {
                            list.add(groupList.get(i).getTermsDetail());
                        }
                    }
                    if (list.size() > 0) {
                        Intent intent = new Intent(activity, ShoutActivity.class);
                        intent.putExtra("list", (Serializable) list);
                        intent.putExtra("volume", 20);
                        intent.putExtra("name", "");
                        startActivity(intent);
                        finish();
                    } else {
                        UtilToast.showToast(activity, "请选择终端");
                    }

                } else {//分组转终端
                    UtilLog.showNet("groupList==" + groupList.size());
                    for (int i = 0; i < groupList.size(); i++) {
                        UtilLog.showNet("isFlag==" + groupList.get(i).isFlag());
                        if (groupList.get(i).getTermsDetails() != null && groupList.get(i).getTermsDetails().size() > 0) {
                            if (groupList.get(i).isFlag()) {
                                list.addAll(groupList.get(i).getTermsDetails());
                            }
                        }
                    }
                    if (list.size() > 0) {
                        List<TermsDetails> reportList = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            boolean flag = false;
                            for (int j = 0; j < reportList.size(); j++) {
                                if (list.get(i).getID() == reportList.get(j).getID()) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {
                                reportList.add(list.get(i));
                            }
                        }
                        Intent intent = new Intent(activity, ShoutActivity.class);
                        intent.putExtra("list", (Serializable) reportList);
                        intent.putExtra("volume", 20);
                        intent.putExtra("name", "");
                        startActivity(intent);
                        finish();
                    } else {
                        UtilToast.showToast(activity, "请选择分组");
                    }
                }
            }
        });
        select_check_group = findViewById(R.id.select_check_group);
        select_type = findViewById(R.id.select_type);
        select_dev_text = findViewById(R.id.select_dev_text);
        select_dev_check = findViewById(R.id.select_dev_check);
        groupList = new ArrayList<>();
        ms_recyclerView = findViewById(R.id.ms_recyclerView);
        refresh_layout = findViewById(R.id.refresh_layout);
        refresh_layout.initAddHead(this);
        ms_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ms_recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = UtilDip.getPixel(activity, R.dimen.base_dimen_2);
            }
        });

        commonAdapter = new CommonAdapter<GroupBean>(activity, R.layout.refresh_item, groupList) {
            @Override
            protected void convert(ViewHolder holder, final GroupBean entity, final int position) {
                if (requestType == 1) {
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
                } else {
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
                        select_type.setText("终端列表");
                        refresh_layout.autoRefresh();
                        break;
                    //选择分组
                    case R.id.select_check_radio_2:
                        requestType = 2;
                        select_type.setText("选择分组");
                        refresh_layout.autoRefresh();
                        break;
                }

            }
        });
        //历史记录
        findViewById(R.id.select_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HistoryActivity.class);
                startActivity(intent);
                finish();
//                startActivityForResult(intent,1001);
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
                                GroupBean groupBean = new GroupBean();
                                groupBean.setTermsDetail(data.getTerms().get(i));
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
        } else {//分组
            mRxLife.add(RxHttp.request(HttpApi.api().getGroups(new HashMap<>()))
                    .request(new RxRequest.ResultCallback<GroupBean>() {

                        @Override
                        public void onSuccess(String code, GroupBean data) {
                            groupList.clear();
                            groupList.addAll(data.getGroups());
                            if (groupList.size() > 0) {
                                Set<Integer> set = new TreeSet<>();
                                for (int i = 0; i < groupList.size(); i++) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            int[] ids = data.getIntArrayExtra("result");
            Map<String, Object> map = new HashMap<>();
            map.put("TermIDs", ids);
            map.put("TNumber", 2);
            mRxLife.add(RxHttp.request(HttpApi.api().getTermState(map))
                    .request(new RxRequest.ResultCallback<TermsDetails>() {

                        @Override
                        public void onSuccess(String code, TermsDetails data) {
                            Intent intent = new Intent(activity, ShoutActivity.class);
                            intent.putExtra("list", (Serializable) data.getTerms());
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailed(String code, String msg) {
                            HttpApi.failDeal(code, msg, activity);
                        }
                    }));
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
    }
}
