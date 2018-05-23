package com.pufei.gxdt.module.maker.fragment;

import android.content.Intent;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseFragment;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;

public class MakerFragment extends BaseFragment {
    @Override
    public void initView() {
        Intent intent = new Intent(getActivity(),EditImageActivity.class);
        startActivity(intent);
    }

    @Override
    public void getData() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_maker;
    }
}
