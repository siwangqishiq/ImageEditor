package com.pufei.gxdt.base;

import android.app.Activity;
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

public abstract class BaseMvpFragment<P extends BasePresenter> extends Fragment implements BaseView<P>{
    protected  P presenter;
    protected Activity activity;
    protected String TAG=getClass().getSimpleName();
    Unbinder unbinder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(getLayout(),container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView();
        setPresenter(presenter);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
       // MobclickAgent.onPageStart(getClass().getSimpleName()); //统计页面("MainScreen"为页面名称，可自定义)

    }

    public void onPause() {
        super.onPause();
     //   MobclickAgent.onPageEnd(getClass().getSimpleName());
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
    /**
     * 布局销毁 调用presenter置空view，防止内存溢出
     */

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter!=null){
            presenter.detachView();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
