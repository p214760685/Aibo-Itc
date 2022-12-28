package com.comtom.aibo.module.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.comtom.aibo.R;
import com.comtom.aibo.utils.IConstant;
import com.comtom.aibo.utils.PublicUtil;
import com.comtom.aibo.utils.StatusBarColorCompat;
import com.haozi.dev.smartframe.utils.permissions.OnPermissionCallback;
import com.haozi.dev.smartframe.utils.permissions.Permission;
import com.haozi.dev.smartframe.utils.permissions.XXPermissions;

import java.util.List;

public class SpalishActivity extends BaseActivity {
    private RelativeLayout rl_flash_bg ;
    private TextView flash_copyright ;
    private ImageView flash_logo ,flash_small_logo,flash_bottom_logo,flash_bottom_option,flash_right;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarColorCompat.hideStatus(activity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_spalish_sb;
    }

    @Override
    public void initView() {
//        rl_flash_bg = findViewById(R.id.rl_flash_bg);
//        flash_logo = findViewById(R.id.flash_logo);
//        flash_bottom_logo = findViewById(R.id.flash_bottom_logo);
//        flash_small_logo = findViewById(R.id.flash_small_logo);
//        flash_bottom_option = findViewById(R.id.flash_bottom_option);
//        flash_right = findViewById(R.id.flash_right);
//        flash_copyright =  findViewById(R.id.flash_copyright);
//        String type = PublicUtil.getMetaDate(activity);
////        UtilLog.showLog(type);
//        if(IConstant.ITC.equals(type)){
////            rl_flash_bg.setBackgroundResource(R.mipmap.flash_bg_itc);
//        }else if(IConstant.THREEA.equals(type)){
//            rl_flash_bg.setBackgroundResource(R.mipmap.welcome);
//            flash_logo.setVisibility(View.INVISIBLE);
//            flash_small_logo.setVisibility(View.GONE);
//            flash_bottom_logo.setVisibility(View.GONE);
//            flash_bottom_option.setVisibility(View.GONE);
//            flash_right.setVisibility(View.GONE);
//        }else if(IConstant.CRX.equals(type)){
//            rl_flash_bg.setBackgroundResource(R.mipmap.welcome_crx);
//            flash_logo.setVisibility(View.INVISIBLE);
//            flash_small_logo.setVisibility(View.GONE);
//            flash_bottom_logo.setVisibility(View.GONE);
//            flash_bottom_option.setVisibility(View.GONE);
//            flash_right.setVisibility(View.GONE);
//        }else if(IConstant.SL.equals(type)){
//            rl_flash_bg.setBackgroundResource(R.mipmap.welcome_sl);
//            flash_logo.setVisibility(View.INVISIBLE);
//            flash_small_logo.setVisibility(View.GONE);
//            flash_bottom_logo.setVisibility(View.GONE);
//            flash_bottom_option.setVisibility(View.GONE);
//            flash_right.setVisibility(View.GONE);
//        }else if(IConstant.LS.equals(type)){
//            rl_flash_bg.setBackgroundResource(R.mipmap.welcome_ls);
//            flash_logo.setVisibility(View.INVISIBLE);
//            flash_small_logo.setVisibility(View.GONE);
//            flash_bottom_logo.setVisibility(View.GONE);
//            flash_bottom_option.setVisibility(View.GONE);
//            flash_right.setVisibility(View.GONE);
//        }else if(IConstant.ZX.equals(type)){
//            rl_flash_bg.setBackgroundResource(R.mipmap.welcome_zx);
//            flash_logo.setVisibility(View.INVISIBLE);
//            flash_small_logo.setVisibility(View.GONE);
//            flash_bottom_logo.setVisibility(View.GONE);
//            flash_bottom_option.setVisibility(View.GONE);
//            flash_right.setVisibility(View.GONE);
//        }else if(IConstant.DC.equals(type)){
//            rl_flash_bg.setBackgroundResource(R.mipmap.flash_bg_zx);
//            flash_logo.setVisibility(View.INVISIBLE);
//            flash_small_logo.setVisibility(View.VISIBLE);
//            flash_bottom_logo.setVisibility(View.VISIBLE);
//            flash_bottom_option.setVisibility(View.VISIBLE);
//            flash_right.setVisibility(View.GONE);
//            flash_small_logo.setImageResource(R.mipmap.icon_dc);
//        }else if(IConstant.CEOPA.equals(type)){
//            rl_flash_bg.setBackgroundResource(R.mipmap.flash_bg);
//            flash_logo.setVisibility(View.INVISIBLE);
//            flash_small_logo.setVisibility(View.VISIBLE);
//            flash_bottom_logo.setVisibility(View.GONE);
//            flash_bottom_option.setVisibility(View.GONE);
//            flash_right.setVisibility(View.VISIBLE);
//            flash_small_logo.setImageResource(R.mipmap.ceopa);
//            flash_copyright.setVisibility(View.VISIBLE);
//            flash_copyright.setText("版权所有: 广州市西派电子科技有限公司\\nCopyright © 2013-2020, All Rights Reserved");
//        }else {
//        }
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mHandler.sendMessageDelayed(new Message(), 1000);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            XXPermissions.with(activity)
                    .permission(new String[]{Permission.MANAGE_EXTERNAL_STORAGE})
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            if(all){
                                goLoginPage();
                            }else{
                                Toast.makeText(activity,"未获取全部权限",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onDenied(List<String> permissions, boolean never) {
                            if(never){
                                Toast.makeText(activity,"权限被禁止，已为您打开系统设置",Toast.LENGTH_SHORT).show();
                                XXPermissions.startPermissionActivity(activity, permissions);
                            }else{
                                Toast.makeText(activity,"获取权限失败",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });

        }
    };

    private void goLoginPage() {
        Intent intent = new Intent(this, LoginNewActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XXPermissions.REQUEST_CODE) {
            if (XXPermissions.isGrantedPermission(activity, Permission.MANAGE_EXTERNAL_STORAGE) ) {
                Toast.makeText(activity,"权限设置成功",Toast.LENGTH_SHORT).show();
                goLoginPage();
            } else {
                Toast.makeText(activity,"获取权限失败",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    
}