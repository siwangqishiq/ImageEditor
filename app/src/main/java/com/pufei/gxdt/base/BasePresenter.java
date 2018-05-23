package com.pufei.gxdt.base;

import android.content.Context;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Presenter的基类
 * Created by wangwenzhang on 2017/11/9.
 */

public class BasePresenter <V extends BaseView>{
    protected WeakReference<V> mViewReference;
    protected V baseview;//泛型 view  所有的view都要继承BaseView
    protected CompositeDisposable mCompositeSubscription;//rxjava 存放观察者
    private Context context;
    protected BasePresenter(){}
    protected BasePresenter(Context context){
        this.context=context;
    }
    protected void addSubscription(Disposable disposable){//添加观察者
        if (mCompositeSubscription == null){
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(disposable);
    }
    public void unSubscription(){//解除订阅
        if (mCompositeSubscription != null){
            mCompositeSubscription.dispose();
        }
    }
    public void attachView(V view){//绑定view
        this.baseview=view;
        mViewReference = new WeakReference<V>(view);
    }

    public void detachView(){//销毁view
        if(mViewReference != null){
            mViewReference.clear();
            mViewReference = null;
        }
        this.baseview=null;
        unSubscription();
    }
}
