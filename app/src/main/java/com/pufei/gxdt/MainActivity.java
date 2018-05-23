package com.pufei.gxdt;

import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.base.TabVpAdapter;
import com.pufei.gxdt.module.home.fragment.HomeFragment;
import com.pufei.gxdt.module.maker.fragment.MakerFragment;
import com.pufei.gxdt.utils.AppManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.home_vp)
    ViewPager homeVp;
    @BindView(R.id.home_tab)
    TabLayout homeTab;
    private List<Fragment> fragmentList;
    private TabVpAdapter tabAdapter;
    private long mExitTime;

    @Override
    public void initView() {
        addFragment();
        tabAdapter = new TabVpAdapter(this, getSupportFragmentManager(), fragmentList);
        homeVp.setAdapter(tabAdapter);
        homeTab.setupWithViewPager(homeVp);
        for (int i = 0; i < fragmentList.size(); i++) {
            TabLayout.Tab tab = homeTab.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabAdapter.getCustomView(i));
                if (i == 0) {
                    ((ImageView) tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(true);
                    ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setSelected(true);
                }
            }
        }

        homeTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(true);
                ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setSelected(true);
                homeVp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(false);
                ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void getData() {
    }



    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    private void addFragment() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new HomeFragment());
        fragmentList.add(new MakerFragment());
        fragmentList.add(new HomeFragment());

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().finishAllActivity();
                AppManager.getAppManager().AppExit(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
