package com.comtom.aibo.module.shout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comtom.aibo.R;
import com.comtom.aibo.app.AirApp;
import com.comtom.aibo.data.TermDb;
import com.comtom.aibo.entity.TermsDetails;
import com.comtom.aibo.greendao.gen.DaoSession;
import com.comtom.aibo.greendao.gen.TermDbDao;
import com.comtom.aibo.httpset.HttpApi;
import com.comtom.aibo.module.base.BaseActivity;
import com.haozi.dev.smartframe.rxhttp.core.RxHttp;
import com.haozi.dev.smartframe.rxhttp.request.RxRequest;
import com.haozi.dev.smartframe.utils.adapt.CommonAdapter;
import com.haozi.dev.smartframe.utils.adapt.base.ViewHolder;
import com.haozi.dev.smartframe.utils.tool.UtilDip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yhq.dialog.core.DialogBuilder;

public class HistoryActivity extends BaseActivity {
    private RecyclerView his_recyclerView;
    private CommonAdapter commonAdapter;
    private DaoSession daoSession;
    private TermDbDao termDb;
    private List<TermDb> list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    public void initView() {
        setTitle("历史记录");
        daoSession = AirApp.instance().getDaoSession();
        termDb = daoSession.getTermDbDao();
        list = new ArrayList<>();
        list.addAll(termDb.queryRaw("group by NAME"));
        his_recyclerView = findViewById(R.id.his_recyclerView);
        his_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        his_recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = UtilDip.getPixel(activity, R.dimen.base_dimen_2);
            }
        });
        commonAdapter = new CommonAdapter<TermDb>(activity, R.layout.history_item, list) {
            @Override
            protected void convert(ViewHolder holder, final TermDb entity, final int position) {
                holder.setText(R.id.history_item_name, entity.getName());
                holder.setText(R.id.history_item_num, entity.getTermsList().size() + "个终端");
                holder.setOnClickListener(R.id.history_item_line, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<TermDb> listTer = termDb.queryRaw("where NAME=?", entity.getName());
                        TermDb result =  listTer.get(0);
                        int[] ids = new int[result.getTermsList().size()];
                        for (int i = 0; i < result.getTermsList().size(); i++) {
                            ids[i] = result.getTermsList().get(i).getID();
                        }
                        getData(ids,result.getVolume(),entity.getName());
//                        Intent intent = getIntent();
//                        intent.putExtra("result", ids);
//                        setResult(RESULT_OK,intent);
//                        finish();
                    }
                });
                holder.setOnClickListener(R.id.history_item_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogBuilder.alertDialog(activity)
                                .setTitle("删除警告")
                                .setMessage("确认删除此纪录？")
                                .setOnPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        List<TermDb> result = termDb.queryRaw("where NAME=?", entity.getName());
//                                        for (int i = 0; i < result.size(); i++) {
//                                            termDb.deleteByKey(result.get(i).getTmId());
//                                        }
                                        if(result.size() > 0)
                                            termDb.deleteByKey(result.get(0).getTmId());
                                        list.clear();
                                        list.addAll(termDb.loadAll());
                                        commonAdapter.notifyDataSetChanged();
                                    }
                                }).create().show();
                    }
                });
            }
        };
        his_recyclerView.setAdapter(commonAdapter);
    }

    private void getData(int[] ids,int volume,String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("TermIDs", ids);
        map.put("TNumber", 2);
        mRxLife.add(RxHttp.request(HttpApi.api().getTermState(map))
                .request(new RxRequest.ResultCallback<TermsDetails>() {

                    @Override
                    public void onSuccess(String code, TermsDetails data) {
                        Intent intent = new Intent(activity, ShoutActivity.class);
                        intent.putExtra("list", (Serializable) data.getTerms());
                        intent.putExtra("volume", volume);
                        intent.putExtra("name", name);
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