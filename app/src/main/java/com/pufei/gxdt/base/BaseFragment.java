package com.pufei.gxdt.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment的基类
 * Created by wangwenzhang on 2017/11/9.
 */

public abstract class BaseFragment extends Fragment {
    protected String TAG=getClass().getSimpleName();
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(getLayout(),container,false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView();
        getData();
    }

    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart("MainScreen"); //统计页面("MainScreen"为页面名称，可自定义)
    }
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("MainScreen");
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
     * @return
     */
    public abstract int getLayout();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
