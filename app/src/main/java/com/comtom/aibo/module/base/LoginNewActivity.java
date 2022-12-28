package com.comtom.aibo.module.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comtom.aibo.R;
import com.comtom.aibo.app.MyActivityManager;
import com.comtom.aibo.data.DataBaseInfo;
import com.comtom.aibo.data.LogonInfo;
import com.comtom.aibo.data.User;
import com.comtom.aibo.entity.LoginBean;
import com.comtom.aibo.httpset.HttpApi;
import com.comtom.aibo.httpset.HttpListen;
import com.comtom.aibo.httpset.RequestSetting;
import com.comtom.aibo.step.killSelfService;
import com.comtom.aibo.utils.IConstant;
import com.comtom.aibo.utils.PublicUtil;
import com.comtom.aibo.utils.UIUtil;
import com.haozi.dev.smartframe.rxhttp.core.RxHttp;
import com.haozi.dev.smartframe.rxhttp.core.RxLife;
import com.haozi.dev.smartframe.rxhttp.request.RxRequest;
import com.haozi.dev.smartframe.utils.tool.ShareDate;
import com.haozi.dev.smartframe.utils.tool.StatusBarUtils;
import com.haozi.dev.smartframe.utils.tool.UtilLog;
import com.haozi.dev.smartframe.utils.tool.UtilOpenWebView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yhq.dialog.core.DialogBuilder;

public class LoginNewActivity extends BaseActivity {

    private LinearLayout llHistoryUser;
    private boolean bInstalled = true;
    private EditText username_et_id, password_et_id;
    private ImageView ivAccountClear, ivAccountMore ,iv_logo;
    private CheckBox chbSave, chbPrivacy;
    private LinearLayout rlAccountInfo; // 账号信息
    private DataBaseInfo mDataBaseInfo;
    private List<User> userList;
    private View  rlShadow;
    boolean bChkPrivacy = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login_zx;
    }

    @Override
    public void initView() {
//        StatusBarUtils.setColor(this, Color.parseColor("#FFFFFF"));
        String type = PublicUtil.getMetaDate(activity);
        iv_logo = findViewById(R.id.iv_logo);
        if(IConstant.ITC.equals(type)){
//            iv_logo.setImageResource(R.mipmap.flash_logo_hs);
//            iv_logo.setImageResource(R.mipmap.logo_itc_new);
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

        Intent intent = getIntent();
        if (null != intent) {
            int nEnternType = intent.getIntExtra(
                    IConstant.strEnternLogonUIType_KEY, 0);
            if (IConstant.nLogOutEnternLogonui == nEnternType) {// 注销进入

            } else if (IConstant.nRelogonEnternLogonui == nEnternType) {// 切换账号进入

            }
        }

        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        UIUtil.setScreenWidthAndHeight(dm.widthPixels, dm.heightPixels);

        SharedPreferences preferences = getSharedPreferences(
                IConstant.strComtomSharedPreferences, 0);

        String strUserName, strUserPassword, strServerIp;
        boolean bChkSavePass = true;

        strUserName = preferences.getString(IConstant.strUserName_KEY,
                IConstant.strdefaultUserName);
        strUserPassword = preferences.getString(IConstant.strUserPassword_KEY,
                IConstant.strdefaultUserPassword);

        TextView tvPrivacy = (TextView) findViewById(R.id.tvPrivacy);
        tvPrivacy.setOnClickListener(mOnClickListener);

        strServerIp = ShareDate.getShare(activity,IConstant.strServerIp_KEY,"10.0.0.1");

        bChkSavePass = preferences.getBoolean(IConstant.strbSavePassDefaul_KEY,
                IConstant.bSavePassDefaul);

        findViewById(R.id.logon_id).setOnClickListener(mOnClickListener);

        ivAccountClear = (ImageView) findViewById(R.id.ivAccountClear);
        ivAccountMore = (ImageView) findViewById(R.id.ivAccountMore);

        ivAccountClear.setOnClickListener(mOnClickListener);
        ivAccountMore.setOnClickListener(mOnClickListener);

        username_et_id = (EditText) findViewById(R.id.username_et_id);
        username_et_id.setText(strUserName);

        password_et_id = (EditText) findViewById(R.id.password_et_id);
        password_et_id.setText(strUserPassword);

        chbSave = (CheckBox) findViewById(R.id.chbSave);
        chbSave.setChecked(bChkSavePass);

        chbPrivacy = (CheckBox) findViewById(R.id.chbPrivacy);
        chbPrivacy.setChecked(bChkPrivacy);
        chbPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bChkPrivacy = isChecked;
            }
        });

        EditText et = (EditText) findViewById(R.id.etServerSite);
        et.setText(strServerIp);

//        mHandler = new HandleITcpCallback(this);

        findViewById(R.id.login_ip_set).setOnClickListener(mOnClickListener);
        findViewById(R.id.login_ip_calse).setOnClickListener(mOnClickListener);
        findViewById(R.id.login_ip_btn).setOnClickListener(mOnClickListener);

        llHistoryUser = (LinearLayout) findViewById(R.id.llHistoryUser);

        username_et_id.setOnFocusChangeListener(mOnFocusChangeListener);
        password_et_id.setOnFocusChangeListener(mOnFocusChangeListener);

        username_et_id.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (username_et_id.hasFocus()) {
                    if (s.length() > 0) {
                        if (View.INVISIBLE == ivAccountClear.getVisibility()) {
                            ivAccountClear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (View.VISIBLE == ivAccountClear.getVisibility()) {
                            ivAccountClear.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });

        String strPath = UIUtil.getAppDir(this);
        File file = new File(strPath);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            File file2 = new File(strPath, IConstant.mReplayFileName);
            if (file2.exists()) {
                mDataBaseInfo = (DataBaseInfo) UIUtil
                        .file2Object(new FileInputStream(file2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == mDataBaseInfo) {
            mDataBaseInfo = new DataBaseInfo();
        }

        userList = mDataBaseInfo.getUserList();
        if (null != userList && userList.size() > 0) {
            // 有历史记录
            ivAccountMore.setVisibility(View.VISIBLE);
            rlAccountInfo =  findViewById(R.id.rlAccountInfo);
        } else {
            // 没有历史记录
            ivAccountMore.setVisibility(View.GONE);
            ivAccountClear.setVisibility(View.GONE);
//            RelativeLayout.LayoutParams par = (RelativeLayout.LayoutParams) ivAccountClear
//                    .getLayoutParams();
//            par.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
        }

        // 蒙层
        rlShadow = findViewById(R.id.rlShadow);
        rlShadow.setOnClickListener(mOnClickListener);

        boolean bFirstStartUp = preferences.getBoolean(
                IConstant.FirstStartUp_KEY, true);
        if (!bFirstStartUp) {
            rlShadow.setVisibility(View.GONE);
        } else {
            rlShadow.setVisibility(View.VISIBLE);
        }

    }

    View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            switch (v.getId()) {
                case R.id.username_et_id: {
                    if (hasFocus) {
                        String data = username_et_id.getEditableText().toString();
                        if (TextUtils.isEmpty(data)) {
                            ivAccountClear.setVisibility(View.INVISIBLE);
                        } else {
                            ivAccountClear.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ivAccountClear.setVisibility(View.INVISIBLE);
                    }
                }
                break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            String strPath = UIUtil.getAppDir(this);
            UIUtil.object2File(mDataBaseInfo, new FileOutputStream(new File(
                    strPath + "/" + IConstant.mReplayFileName)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


//    private void hideMoresettingView(int duration) {
//
//        Rect rc = new Rect();
//
//        Rect rctotal = new Rect();
//        llmoresettingView.getGlobalVisibleRect(rctotal);
//
//        final int eh = rctotal.bottom - rc.bottom;
//
//        Animation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0, eh);
//
//        translateAnimation
//                .setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        int left = llmoresettingView.getLeft();
//                        int top = llmoresettingView.getTop() + eh;
//                        int width = llmoresettingView.getWidth();
//                        int height = llmoresettingView.getHeight();
//                        llmoresettingView.clearAnimation();
//                        llmoresettingView.layout(left, top, left + width, top
//                                + height);
//                    }
//                });
//
//        translateAnimation.setDuration(duration);
//        translateAnimation.setFillAfter(true);
//
//        llmoresettingView.startAnimation(translateAnimation);
//
//        bHideMore = true;
//
//        setDirctorIcon();
//    }

//    private void showMoresettingView() {
//
//        Rect rc = new Rect();
//
//        Rect rctotal = new Rect();
//        llmoresettingView.getGlobalVisibleRect(rctotal);
//
//        final int eh = rctotal.bottom - rc.bottom;
//
//        Animation translateAnimation = new TranslateAnimation(0.0f, 0.0f, eh, 0);
//        translateAnimation
//                .setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        int left = llmoresettingView.getLeft();
//                        int top = llmoresettingView.getTop() - eh;
//                        int width = llmoresettingView.getWidth();
//                        int height = llmoresettingView.getHeight();
//                        llmoresettingView.clearAnimation();
//                        llmoresettingView.layout(left, top, left + width, top
//                                + height);
//                    }
//                });
//
//        translateAnimation.setDuration(mDuration);
//        translateAnimation.setFillAfter(true);
//        llmoresettingView.startAnimation(translateAnimation);
//
//        bHideMore = false;
//
//        setDirctorIcon();
//    }

//    private void setDirctorIcon() {
//
//        if (bHideMore) {
//            ivDirector.setBackgroundResource(R.drawable.icon_up_arrow);
//        } else {
//            Bitmap bit = BitmapFactory.decodeResource(this.getResources(),
//                    R.drawable.icon_up_arrow);
//
//            // 将图片设置为宽100，高200，在这儿就可以实现图片的大小缩放
//            Bitmap resize = Bitmap.createScaledBitmap(bit, 100, 200, true);
//
//            Matrix m = new Matrix();
//            int width = resize.getWidth();
//            int height = resize.getHeight();
//            m.setRotate(-180);
//
//            // 做好旋转与大小之后，重新创建位图，0-width宽度上显示的区域，高度类似
//            Bitmap b = Bitmap
//                    .createBitmap(resize, 0, 0, width, height, m, true);
//            BitmapDrawable bd = new BitmapDrawable(getResources(), b);
//            ivDirector.setBackgroundDrawable(bd);
//        }
//    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_ip_btn: {
                    EditText et = (EditText) findViewById(R.id.etServerSite);
                    UIUtil.HideSoftInput(activity, et);
                    savaConfig();
                    findViewById(R.id.login_ip_liner).setVisibility(View.GONE);
                    findViewById(R.id.login_ip_set).setVisibility(View.VISIBLE);
//                    hideMoresettingView(mDuration);
                }
                         break;
                case R.id.login_ip_set :
                    findViewById(R.id.login_ip_liner).setVisibility(View.VISIBLE);
                    findViewById(R.id.login_ip_set).setVisibility(View.GONE);
                        break;
                case R.id.login_ip_calse :
                    EditText et = (EditText) findViewById(R.id.etServerSite);
                    UIUtil.HideSoftInput(activity, et);
                    findViewById(R.id.login_ip_liner).setVisibility(View.GONE);
                    findViewById(R.id.login_ip_set).setVisibility(View.VISIBLE);
                    break;
//                case R.id.rlLoginMain: {
//                    if (UIUtil.HideSoftInput(activity,
//                            findViewById(R.id.etServerSite))) {
//                        return;
//                    }
//
//                    if (llHistoryUser.getVisibility() == View.VISIBLE) {
//                        llHistoryUser.setVisibility(View.GONE);
//                        ivAccountMore.setImageResource(R.drawable.logon_more_bt_bg);
//                        return;
//                    }

//                    Rect rc = new Rect();
//                    llIpHeadTip.getGlobalVisibleRect(rc);
//
//                    int screenH = UIUtil.getScreenHeight();
//                    if (rc.bottom + 10 > screenH) {
//                        bHideMore = true;
//                    } else {
//                        bHideMore = false;
//                    }
//                    if (bHideMore) {
//                        showMoresettingView();
//                    } else {
//                        hideMoresettingView(mDuration);
//                    }
//                }
//                break;
                case R.id.ivAccountClear: {
                    username_et_id.getEditableText().clear();
                    ivAccountClear.setVisibility(View.INVISIBLE);
                }
                break;
                case R.id.ivAccountMore: {
                    if (llHistoryUser.getVisibility() != View.VISIBLE) {
                        llHistoryUser.removeAllViews();
                        View parent = findViewById(R.id.rlLoginMain);
                        Rect prc = new Rect();
                        parent.getWindowVisibleDisplayFrame(prc);
                        Rect rc = new Rect();
                        rlAccountInfo.getGlobalVisibleRect(rc);

                        RelativeLayout.LayoutParams par = (RelativeLayout.LayoutParams) llHistoryUser
                                .getLayoutParams();
                        par.topMargin = rc.bottom - prc.top;
                        par.leftMargin = rc.left;
                        par.width = rc.width();
                        llHistoryUser.setLayoutParams(par);
                        llHistoryUser.setVisibility(View.VISIBLE);
//                        ivAccountMore.setImageResource(R.drawable.logon_more_up_bt_bg);

                        int size = userList.size();
                        for (int i = 0; i < size; i++) {
                            User user = userList.get(i);
                            View chichView = LayoutInflater.from(
                                    activity).inflate(
                                    R.layout.layout_item_history_user, null);
                            TextView tv = (TextView) chichView
                                    .findViewById(R.id.tvHistoryUser);
                            String admin = "管理员";
                            String normal_user = "普通用户";
                            String info = String
                                    .format("%s(%s)",
                                            user.strUserName,
                                            ((0 == user.mUserType || 10 == user.mUserType) ? admin
                                                    : normal_user));
                            tv.setText(info);
                            int nitemh = (int) getResources().getDimension(
                                    R.dimen.logon_input_height);
                            if (i + 1 >= size) {
                                nitemh += 2;
                            }

                            LinearLayout.LayoutParams childPar = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, nitemh);

                            llHistoryUser.addView(chichView, childPar);

                            ImageView iv = (ImageView) chichView
                                    .findViewById(R.id.ivHistoryClear);
                            iv.setTag(i);
                            iv.setOnClickListener(mOnClickListener);
                            chichView.setTag(i);
                            chichView.setOnClickListener(mOnClickListener);
                        }
                    } else {
                        llHistoryUser.setVisibility(View.GONE);
//                        ivAccountMore.setImageResource(R.drawable.logon_more_bt_bg);
                    }
                }
                break;
                case R.id.rlHistoryUser: {
                    llHistoryUser.setVisibility(View.GONE);
//                    ivAccountMore.setImageResource(R.drawable.logon_more_bt_bg);
                    TextView tv = (TextView) v.findViewById(R.id.tvHistoryUser);
                    String data = tv.getText().toString();
                    if (!TextUtils.isEmpty(data)) {
                        int npos = data.lastIndexOf("(");
                        if (-1 != npos) {
                            data = data.substring(0, npos);
                        }

                        username_et_id.setText(data);
                    }

                    int pos = Integer.parseInt(v.getTag().toString());
                    User user = userList.get(pos);
                    password_et_id.setText(user.strUserPassword);
                }
                break;
                case R.id.ivHistoryClear: {
                    llHistoryUser.setVisibility(View.GONE);
//                    ivAccountMore.setImageResource(R.drawable.logon_more_bt_bg);
                    int pos = Integer.parseInt(v.getTag().toString());
                    userList.remove(pos);
                    if (userList.size() <= 0) {
                        ivAccountMore.setVisibility(View.GONE);
                        ivAccountClear.setVisibility(View.GONE);
//                        LinearLayout.LayoutParams par = (LinearLayout.LayoutParams) ivAccountClear
//                                .getLayoutParams();
//                        par.addRule(LinearLayout.ALIGN_PARENT_RIGHT, 1);
                    }
                }
                break;
                case R.id.rlShadow: {
                    SharedPreferences preferences = getSharedPreferences(
                            IConstant.strComtomSharedPreferences, 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(IConstant.FirstStartUp_KEY, false);
                    editor.commit();
                    rlShadow.setVisibility(View.GONE);
                }
                break;
                case R.id.logon_id: {
                    if (bChkPrivacy) {
                        Logon();
                    } else {
                        UIUtil.showToast("未同意用户协议", Toast.LENGTH_SHORT);
                    }
                }
                break;
                case R.id.tvPrivacy: {
                    UtilOpenWebView.openUrl(activity,"file:///android_asset/privacy.html","用户协议");
//                    Intent intent = new Intent(activity, PrivacyActivity.class);
//                    startActivity(intent);
                }
                break;
                default:
                    break;
            }
        }
    };

    void savaConfig() {
        String strServerIp;

        EditText et = (EditText) findViewById(R.id.etServerSite);
        strServerIp = et.getText().toString();

        if (!UIUtil.isIpAddress(strServerIp)) {
            UIUtil.showToast("服务器ip有误,请正确配置",
                    Toast.LENGTH_SHORT);
            return;
        }
        ShareDate.setShare(activity,IConstant.strServerIp_KEY,strServerIp);

//        DialogBuilder.messageDialog(activity).setMessage("重新设置IP需要重启应用")
//                .setTitle("重启应用")
//                .setOnPositiveButtonClickListener(new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        Intent intent1=new Intent(activity, killSelfService.class);
////                        intent1.putExtra("PackageName",getPackageName());
////                        intent1.putExtra("Delayed",2000);
////                        startService(intent1);
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                    }
//                }).create().show();
    }

    public void Logon() {
        UIUtil.HideSoftInput(this, findViewById(R.id.etServerSite));

        if (IConstant.mTryUse) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(UIUtil.getCurTime1());
                Date date2 = sdf.parse(IConstant.mExpireData);

                if (date.getTime() > date2.getTime()) {
                    UIUtil.showToast("非正式版本,试用已经到期",
                            Toast.LENGTH_SHORT);
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
                UIUtil.showToast("非正式版本,试用已经到期",
                        Toast.LENGTH_SHORT);
                return;
            }
        }

        String strUserName, strPassword, strServerIp;

        EditText et = (EditText) findViewById(R.id.username_et_id);

        strUserName = et.getEditableText().toString();

        et = (EditText) findViewById(R.id.password_et_id);
        strPassword = et.getEditableText().toString();

        strServerIp = ShareDate.getShare(activity,IConstant.strServerIp_KEY,"10.0.0.1");
//        UtilLog.showNet("strServerIp==" + strServerIp);
        if (!UIUtil.isIpAddress(strServerIp)) {
            UIUtil.showToast("服务器ip有误,请正确配置",
                    Toast.LENGTH_SHORT);

            return;
        }

        if (TextUtils.isEmpty(strUserName) || TextUtils.isEmpty(strPassword)) {
            UIUtil.showToast("用户名称或密码不能为空",
                    Toast.LENGTH_SHORT);
            return;
        }
        ShareDate.setShare(activity,"token","");
        Map<String, Object> map = new HashMap<>();
        map.put("User", strUserName);
        map.put("Passwd", strPassword);
        mRxLife.add(RxHttp.request(HttpApi.api().login(map))
                .listener(new HttpListen(activity))
                .request(new RxRequest.ResultCallback<LoginBean>() {

                    @Override
                    public void onSuccess(String code, LoginBean data) {
                        UtilLog.showNet("onSuccess===" + data.getJSessionID());
                        ShareDate.setShare(activity,"token",data.getJSessionID());
//                        PublicUtil.token = data.getJSessionID();
                        LogonInfo logonInfo = LogonInfo.getInstance();
                        logonInfo.setUserName(strUserName);
                        logonInfo.setUserPassword(strPassword);
                        saveUserInfo();
                        gotoMain();
                        finish();
                    }

                    @Override
                    public void onFailed(String code, String msg) {
                        HttpApi.failDeal(code,msg,activity);
                    }
                }));


    }

    void saveUserInfo() {
        String strUserName, strPassword;

        EditText et = (EditText) findViewById(R.id.username_et_id);

        strUserName = et.getEditableText().toString();

        et = (EditText) findViewById(R.id.password_et_id);
        strPassword = et.getEditableText().toString();

        SharedPreferences preferences = getSharedPreferences(
                IConstant.strComtomSharedPreferences, 0);

        LogonInfo logonInfo = LogonInfo.getInstance();
        int nUserLevel = logonInfo.getUserLevel();

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(IConstant.strUserName_KEY, strUserName);
        if (chbSave.isChecked()) {
            editor.putString(IConstant.strUserPassword_KEY, strPassword);
        } else {
            editor.putString(IConstant.strUserPassword_KEY, "");
        }
        editor.putInt(IConstant.strUserLevel_KEY, nUserLevel);
        editor.putBoolean(IConstant.strbSavePassDefaul_KEY, chbSave.isChecked());

        editor.commit();

        if (!mDataBaseInfo.Exist(strUserName, strPassword, nUserLevel,
                chbSave.isChecked())) {
            User user = new User();
            user.strUserName = strUserName;
            if (chbSave.isChecked()) {
                user.strUserPassword = strPassword;
            } else {
                user.strUserPassword = "";
            }
            user.mUserType = nUserLevel;
            mDataBaseInfo.addUser(user);
        }
    }

    void gotoMain() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyActivityManager.init(getApplication()).exitApp();
//        android.os.Process.killProcess(android.os.Process.myPid());
    }
}