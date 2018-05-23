package com.pufei.gxdt.base;



/**
 * Created by wangwenzhang on 2017/11/9.
 * 所有的普通View基类
 */

public interface BaseView<P extends BasePresenter> {


    /**
     * 设置 presenter
     */
    void setPresenter(P presenter);

}
