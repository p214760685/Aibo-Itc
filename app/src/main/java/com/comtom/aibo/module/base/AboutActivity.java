package com.comtom.aibo.module.base;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.comtom.aibo.R;
import com.comtom.aibo.utils.IConstant;
import com.comtom.aibo.utils.PublicUtil;
import com.comtom.aibo.utils.UIUtil;
import com.haozi.dev.smartframe.utils.tool.ShareDate;
import com.haozi.dev.smartframe.utils.tool.UtilToast;

public class AboutActivity extends BaseActivity {
    private ImageView iv_logo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        setTitle("关于");
        String type = PublicUtil.getMetaDate(activity);
        TextView about_version = findViewById(R.id.about_version);
        about_version.setText("版本：V" + getAppVersionName());
        iv_logo = findViewById(R.id.iv_logo);
        if(IConstant.ITC.equals(type)){
            iv_logo.setImageResource(R.mipmap.icon_login_logo);
        }else if(IConstant.THREEA.equals(type)){
            iv_logo.setImageResource(R.mipmap.a);
        }else if(IConstant.CRX.equals(type)){
            iv_logo.setImageResource(R.mipmap.logon_logo_crx);
        }else if(IConstant.SL.equals(type)){
            iv_logo.setImageResource(R.mipmap.logon_logo_sl);
        }else if(IConstant.LS.equals(type)){
            iv_logo.setImageResource(R.mipmap.logon_logo_ls);
        }else if(IConstant.ZX.equals(type)){
            iv_logo.setImageResource(R.mipmap.logon_logo_zx);
        }else if(IConstant.DC.equals(type)){
            iv_logo.setImageResource(R.mipmap.icon_dc);
        }else if(IConstant.CEOPA.equals(type)){
            iv_logo.setImageResource(R.mipmap.ceopa);
        }
    }

    /**
     * 返回当前程序版本名
     */
    private  String getAppVersionName() {
        String versionName=null;
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
        }
        return versionName;
    }
}