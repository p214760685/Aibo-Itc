package com.comtom.aibo.module.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.comtom.aibo.R;
import com.comtom.aibo.app.MyActivityManager;
import com.comtom.aibo.module.board.BoardActivity;
import com.comtom.aibo.module.file.FileActivity;
import com.comtom.aibo.module.shout.SelectDevActivity;
import com.haozi.dev.smartframe.utils.tool.UtilToast;


public class HomeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        findViewById(R.id.home_menu_layout_1).setOnClickListener(this);
        findViewById(R.id.home_menu_layout_2).setOnClickListener(this);
        findViewById(R.id.home_menu_layout_3).setOnClickListener(this);
        findViewById(R.id.home_menu_layout_4).setOnClickListener(this);
        findViewById(R.id.home_menu_layout_5).setOnClickListener(this);
        findViewById(R.id.home_menu_layout_6).setOnClickListener(this);
        findViewById(R.id.home_menu_layout_7).setOnClickListener(this);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            UtilToast.showToast(activity,"再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            MyActivityManager.init(getApplication()).exitApp();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_menu_layout_1://寻呼喊话
                startActivity(new Intent(activity, SelectDevActivity.class));
                break;
            case R.id.home_menu_layout_2://文件点播
                startActivity(new Intent(activity, BoardActivity.class));
                break;
            case R.id.home_menu_layout_3://文件上传
                startActivity(new Intent(activity, FileActivity.class));
                break;
            case R.id.home_menu_layout_4://设置优先级
                startActivity(new Intent(activity, SetPriorActivity.class));
                break;
            case R.id.home_menu_layout_5://注销
                startActivity(new Intent(activity, LoginNewActivity.class));
                finish();
                break;
            case R.id.home_menu_layout_6://退出
//                System.exit(0);
                MyActivityManager.init(getApplication()).exitApp();
                break;
            case R.id.home_menu_layout_7://关于
                startActivity(new Intent(activity, AboutActivity.class));
                break;
        }
    }
}