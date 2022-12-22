package com.comtom.aibo.module.base;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.comtom.aibo.R;
import com.comtom.aibo.utils.IConstant;
import com.comtom.aibo.utils.PublicUtil;
import com.haozi.dev.smartframe.utils.tool.ShareDate;
import com.haozi.dev.smartframe.utils.tool.UtilToast;

public class SetPriorActivity extends BaseActivity {
    private ImageView iv_logo;
    private EditText shout_prior,play_prior;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public void initView() {
        setTitle("设置优先级");
        String type = PublicUtil.getMetaDate(activity);
        TextView about_version = findViewById(R.id.about_version);
        about_version.setText("版本：V" + getAppVersionName());
        shout_prior = findViewById(R.id.shout_prior);
        play_prior = findViewById(R.id.play_prior);
        shout_prior.setText(ShareDate.getShareInt(activity,"shoutPrior",100) + "");
        play_prior.setText(ShareDate.getShareInt(activity,"playPrior",100) + "");
        iv_logo = findViewById(R.id.iv_logo);
        if(IConstant.ITC.equals(type)){
            iv_logo.setImageResource(R.mipmap.flash_logo_hs);
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

        findViewById(R.id.set_prior).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shout_prior.getText().toString().equals("")){
                    UtilToast.showToast(activity,"请输入寻呼喊话优先级");
                }else if(play_prior.getText().toString().equals("")){
                    UtilToast.showToast(activity,"请输入文件点播优先级");
                }else{
                    ShareDate.setShareInt(activity,"shoutPrior", Integer.parseInt(shout_prior.getText().toString()) );
                    ShareDate.setShareInt(activity,"playPrior",Integer.parseInt(play_prior.getText().toString()));
                    UtilToast.showToast(activity,"设置成功");
                }
            }
        });
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