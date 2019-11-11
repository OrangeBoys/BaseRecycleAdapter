package com.juzi.baserecycleadapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class QuickAdapter extends BaseQuickAdapter<QuickBean.DataBean, BaseViewHolder> {

    public QuickAdapter(int layoutResId, @Nullable List<QuickBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuickBean.DataBean item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_older, item.getOrder()+"");

    }
}
