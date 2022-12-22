package com.comtom.aibo.module.base;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.comtom.aibo.R;
import com.haozi.dev.smartframe.rxhttp.core.RxLife;

public abstract class BaseActivity extends AppCompatActivity {

    protected BaseActivity activity;
    protected RxLife mRxLife;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getLayoutId());
        activity = this;
        mRxLife = RxLife.create();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxLife.destroy();
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化视图
     */
    public abstract void initView();


    /**
     * 设置标题
      * @param title
     */
    protected void setTitle(String title) {
        TextView title_name = findViewById(R.id.title_name);
        ImageView title_back = findViewById(R.id.title_back);
        title_name.setText(title);
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(v -> finish());
    }

    /**
     * 设置文字menu
     * @param menu
     */
    protected void setMenu(String menu , View.OnClickListener clickListener) {
        TextView title_menu_text = findViewById(R.id.title_menu_text);
        title_menu_text.setVisibility(View.VISIBLE);
        title_menu_text.setText(menu);
        if(clickListener != null)
            title_menu_text.setOnClickListener(clickListener);
    }

    /**
     * 隐藏返回键
     */
    protected void hideBack() {
        ImageView title_back = findViewById(R.id.title_back);
        title_back.setVisibility(View.GONE);
    }


}
