package com.boredream.bga.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.bga.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainFragment extends BaseFragment {

    View view;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_main, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        loadData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {
        rv.setLayoutManager(new GridLayoutManager(activity, 2));
//        adapter = new TvAdapter(activity, tvList);
//        rv.setAdapter(adapter);

        refresh.setEnableRefresh(true);
        refresh.setEnableLoadmore(false);
        refresh.setOnRefreshListener(refresh -> {
            loadData();
        });

        setupRefreshLayout(refresh);
    }

    private void loadData() {
    }

    private void onResponse() {
    }
}