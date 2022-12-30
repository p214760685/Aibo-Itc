package com.comtom.aibo.module.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.comtom.aibo.R;
import com.comtom.aibo.app.MyActivityManager;
import com.comtom.aibo.entity.RealPlay;
import com.comtom.aibo.httpset.HttpApi;
import com.comtom.aibo.httpset.HttpListen;
import com.comtom.aibo.module.board.BoardActivity;
import com.comtom.aibo.module.file.FileActivity;
import com.comtom.aibo.module.shout.SelectDevActivity;
import com.haozi.dev.smartframe.rxhttp.core.RxHttp;
import com.haozi.dev.smartframe.rxhttp.request.RxRequest;
import com.haozi.dev.smartframe.utils.permissions.OnPermissionCallback;
import com.haozi.dev.smartframe.utils.permissions.Permission;
import com.haozi.dev.smartframe.utils.permissions.XXPermissions;
import com.haozi.dev.smartframe.utils.tool.ShareDate;
import com.haozi.dev.smartframe.utils.tool.UtilToast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
                XXPermissions.with(activity)
                        .permission(new String[]{Permission.MANAGE_EXTERNAL_STORAGE})
                        .request(new OnPermissionCallback() {
                            @Override
                            public void onGranted(List<String> permissions, boolean all) {
                                if (all) {
                                    startActivity(new Intent(activity, FileActivity.class));
                                } else {
                                    Toast.makeText(activity, "请先申请本地文件读写权限", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onDenied(List<String> permissions, boolean never) {
                                if (never) {
                                    Toast.makeText(activity, "权限被禁止，已为您打开系统设置", Toast.LENGTH_SHORT).show();
                                    XXPermissions.startPermissionActivity(activity, permissions);
                                } else {
                                    Toast.makeText(activity, "获取权限失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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