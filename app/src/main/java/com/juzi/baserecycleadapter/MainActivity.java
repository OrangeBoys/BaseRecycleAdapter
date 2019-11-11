package com.juzi.baserecycleadapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.net.NetworkInterface;
import java.net.URL;
import java.util.List;

/**
 * 简单的展示加载中 网络错误 以及加载数据的demo
 */
public class MainActivity extends Activity {
    private FloatingActionButton btn_reset;
    private RecyclerView mRecyclerView;
    private View notDataView;
    private View error_view;
    private View loading_view;
    private QuickAdapter quickAdapter;
    private final String BaseUrl = "https://wanandroid.com/wxarticle/chapters/json";
    private QuickBean quickBean;
    private List<QuickBean.DataBean> quickBeanData;
    private TextView tv_shishi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_reset = findViewById(R.id.btn_reset);
        mRecyclerView = findViewById(R.id.rv_list);
        quickAdapter = new QuickAdapter(R.layout.item, quickBeanData);

        //无数据加载的View
        notDataView = getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) mRecyclerView.getParent(), false);

        //错误加载的View
        error_view = getLayoutInflater().inflate(R.layout.error_view, (ViewGroup) mRecyclerView.getParent(), false);
        tv_shishi = error_view.findViewById(R.id.tv_shishi);


        tv_shishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        //等待的View
        loading_view = getLayoutInflater().inflate(R.layout.loading_view, (ViewGroup) mRecyclerView.getParent(), false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(quickAdapter);
        onRefresh();
    }
    private void getInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkGo.<String>get(BaseUrl).execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String data = response.body();
                        Gson gson = new Gson();
                        quickBean = gson.fromJson(data, QuickBean.class);
//                        quickBeanData = quickBean.getData();
                        if (quickBean.getErrorCode() == 0) {
                            // 成功
                            if (quickBean.getData() != null) {
                                quickAdapter.setNewData(quickBean.getData());
                            } else {
                                quickAdapter.setEmptyView(notDataView);
                                quickAdapter.setNewData(null);
                            }
                        } else {
                            quickAdapter.setEmptyView(error_view);
                            quickAdapter.setNewData(null);
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        quickAdapter.setEmptyView(error_view);
                        quickAdapter.setNewData(null);
                    }
                });
            }
        }).start();
    }

    @SuppressLint("ResourceAsColor")
    private void onRefresh() {
        if (NetworkUtils.isConnected()) {//判断网络是否可用
            quickAdapter.setEmptyView(loading_view);
            getInfo();
        } else {
            ToastUtils.setGravity(Gravity.CENTER, 20, 20);
            ToastUtils.showLong("网络不可用,请打开网络连接!!!");
            ToastUtils.setMsgTextSize(20);
            quickAdapter.setEmptyView(error_view);
        }
    }
}
