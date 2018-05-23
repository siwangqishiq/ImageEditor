package com.pufei.gxdt.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.pufei.gxdt.R;
import com.pufei.gxdt.utils.AppManager;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    protected int total;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 获取数据
     */
    public abstract void getData();

    /**
     * 设置布局文件id
     *
     * @return
     */
    public abstract int getLayout();


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}
